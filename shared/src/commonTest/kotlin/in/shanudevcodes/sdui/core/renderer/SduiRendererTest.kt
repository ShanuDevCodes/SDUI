package `in`.shanudevcodes.sdui.core.renderer

import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SduiRendererTest {

    private class MockApplier : Applier<Unit> {
        override val current: Unit = Unit
        override fun down(node: Unit) {}
        override fun up() {}
        override fun insertTopDown(index: Int, instance: Unit) {}
        override fun insertBottomUp(index: Int, instance: Unit) {}
        override fun remove(index: Int, count: Int) {}
        override fun move(from: Int, to: Int, count: Int) {}
        override fun clear() {}
    }

    private class TestFrameClock : MonotonicFrameClock {
        override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
            return onFrame(0L)
        }
    }

    private fun runComposableTest(content: @Composable () -> Unit) = runBlocking {
        val clock = TestFrameClock()
        val recomposer = Recomposer(coroutineContext + clock)
        val composition = Composition(MockApplier(), recomposer)
        val job = launch(clock) {
            recomposer.runRecomposeAndApplyChanges()
        }
        composition.setContent(content)
        recomposer.close()
        job.join()
    }

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testRendererResolvesAndCallsRegisteredComponent() {
        var renderCalled = false
        var passedComponent: SduiComponentDto? = null

        ComponentRegistry.register("CustomType") { component, _, _ ->
            renderCalled = true
            passedComponent = component
        }

        val component = SduiComponentDto(type = "CustomType")
        val stateHolder = SduiStateHolder()

        runComposableTest {
            SduiRenderer(component = component, stateHolder = stateHolder)
        }

        assertTrue(renderCalled, "Component renderer should be called for registered type")
        assertEquals("CustomType", passedComponent?.type)
    }

    @Test
    fun testRendererPropagatesLocalActionHandler() {
        var actionDispatched = false
        ComponentRegistry.register("TriggerType") { _, _, _ ->
            val handler = LocalSduiActionHandler.current
            handler( `in`.shanudevcodes.sdui.core.schema.SduiActionDto(type = "MockAction") )
        }

        val component = SduiComponentDto(type = "TriggerType")
        val stateHolder = SduiStateHolder()

        runComposableTest {
            CompositionLocalProvider(
                LocalSduiActionHandler provides { action ->
                    if (action.type == "MockAction") {
                        actionDispatched = true
                    }
                }
            ) {
                SduiRenderer(component = component, stateHolder = stateHolder)
            }
        }

        assertTrue(actionDispatched, "Action handler should be propagated and successfully invoked")
    }

    @Test
    fun testRendererDepthLimitAbortsAtMaxDepth() {
        var leafRendered = false
        ComponentRegistry.register("Node") { component, _, stateHolder ->
            component.children.forEach { child ->
                SduiRenderer(child, stateHolder)
            }
        }
        ComponentRegistry.register("Leaf") { _, _, _ ->
            leafRendered = true
        }

        // Create a tree of nodes.
        // SduiRenderer depth check is: if (currentDepth > 50) return
        fun buildChain(depth: Int): SduiComponentDto {
            return if (depth == 0) {
                SduiComponentDto(type = "Leaf")
            } else {
                SduiComponentDto(type = "Node", children = listOf(buildChain(depth - 1)))
            }
        }

        val stateHolder = SduiStateHolder()

        // 1. Safe depth: chain of 45 (depth 45 <= 50)
        runComposableTest {
            SduiRenderer(component = buildChain(45), stateHolder = stateHolder)
        }
        assertTrue(leafRendered, "Leaf should render within safe recursion depth limit (< 50)")

        // Reset state
        leafRendered = false

        // 2. Dangerous depth: chain of 52 (depth 52 > 50)
        runComposableTest {
            SduiRenderer(component = buildChain(52), stateHolder = stateHolder)
        }
        assertFalse(leafRendered, "Leaf should NOT render when recursion depth exceeds 50")
    }
}
