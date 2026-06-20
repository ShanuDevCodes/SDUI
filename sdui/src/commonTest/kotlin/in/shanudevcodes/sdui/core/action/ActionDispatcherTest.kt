package `in`.shanudevcodes.sdui.core.action

import `in`.shanudevcodes.sdui.core.engine.SduiConfig
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonPrimitive

class ActionDispatcherTest {

    private lateinit var stateHolder: SduiStateHolder

    @BeforeTest
    fun setUp() {
        stateHolder = SduiStateHolder()
        SduiEngine.reset()
    }

    @AfterTest
    fun tearDown() {
        SduiEngine.reset()
    }

    @Test
    fun testNavigate() = runTest {
        var navigatedRoute: String? = null
        var navigatedParams: Map<String, String>? = null

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onNavigate = { route, params ->
                navigatedRoute = route
                navigatedParams = params
            }
        )

        val action = SduiActionDto(
            type = "Navigate",
            route = "details_screen",
            params = mapOf("id" to "123")
        )

        dispatcher.dispatch(action)

        assertEquals("details_screen", navigatedRoute)
        assertEquals(mapOf("id" to "123"), navigatedParams)
    }

    @Test
    fun testGoBack() = runTest {
        var backCalled = false
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onGoBack = { backCalled = true }
        )

        dispatcher.dispatch(SduiActionDto(type = "GoBack"))
        assertTrue(backCalled)
    }

    @Test
    fun testReplace() = runTest {
        var replacedRoute: String? = null
        var replacedParams: Map<String, String>? = null

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onReplace = { route, params ->
                replacedRoute = route
                replacedParams = params
            }
        )

        dispatcher.dispatch(
            SduiActionDto(
                type = "Replace",
                route = "home",
                params = mapOf("tab" to "profile")
            )
        )

        assertEquals("home", replacedRoute)
        assertEquals(mapOf("tab" to "profile"), replacedParams)
    }

    @Test
    fun testPopToRoot() = runTest {
        var popToRootCalled = false
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onPopToRoot = { popToRootCalled = true }
        )

        dispatcher.dispatch(SduiActionDto(type = "PopToRoot"))
        assertTrue(popToRootCalled)
    }

    @Test
    fun testDeepLink() = runTest {
        var deepLinkUrl: String? = null
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onDeepLink = { url -> deepLinkUrl = url }
        )

        dispatcher.dispatch(SduiActionDto(type = "DeepLink", url = "app://settings"))
        assertEquals("app://settings", deepLinkUrl)
    }

    @Test
    fun testUpdateState() = runTest {
        val dispatcher = ActionDispatcher(stateHolder = stateHolder)

        assertFalse(stateHolder.state.value.containsKey("count"))

        dispatcher.dispatch(
            SduiActionDto(
                type = "UpdateState",
                stateKey = "count",
                value = JsonPrimitive(10)
            )
        )

        assertEquals(JsonPrimitive(10), stateHolder.getValue("count"))
    }

    @Test
    fun testShowSnackbar() = runTest {
        var snackbarMessage: String? = null
        var label: String? = null
        var actionDto: SduiActionDto? = null

        val testAction = SduiActionDto(type = "GoBack")
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onShowSnackbar = { msg, lbl, act ->
                snackbarMessage = msg
                label = lbl
                actionDto = act
            }
        )

        dispatcher.dispatch(
            SduiActionDto(
                type = "ShowSnackbar",
                message = "Task completed",
                actionLabel = "Undo",
                onAction = testAction
            )
        )

        assertEquals("Task completed", snackbarMessage)
        assertEquals("Undo", label)
        assertEquals(testAction, actionDto)
    }

    @Test
    fun testShowDialog() = runTest {
        var dialogTitle: String? = null
        var dialogMsg: String? = null
        var cText: String? = null
        var dText: String? = null
        var confirmAct: SduiActionDto? = null
        var dismissAct: SduiActionDto? = null

        val actConfirm = SduiActionDto(type = "Navigate", route = "success")
        val actDismiss = SduiActionDto(type = "GoBack")

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onShowDialog = { t, m, ct, dt, ca, da ->
                dialogTitle = t
                dialogMsg = m
                cText = ct
                dText = dt
                confirmAct = ca
                dismissAct = da
            }
        )

        dispatcher.dispatch(
            SduiActionDto(
                type = "ShowDialog",
                title = "Alert",
                message = "Are you sure?",
                confirmText = "Yes",
                dismissText = "No",
                onConfirm = actConfirm,
                onDismiss = actDismiss
            )
        )

        assertEquals("Alert", dialogTitle)
        assertEquals("Are you sure?", dialogMsg)
        assertEquals("Yes", cText)
        assertEquals("No", dText)
        assertEquals(actConfirm, confirmAct)
        assertEquals(actDismiss, dismissAct)
    }

    @Test
    fun testCustom() = runTest {
        var customName: String? = null
        var customPayload: Map<String, String>? = null

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onCustomAction = { name, payload ->
                customName = name
                customPayload = payload
                true
            }
        )

        dispatcher.dispatch(
            SduiActionDto(
                type = "Custom",
                name = "refresh_feed",
                payload = mapOf("silent" to "true")
            )
        )

        assertEquals("refresh_feed", customName)
        assertEquals(mapOf("silent" to "true"), customPayload)
    }

    @Test
    fun testTrack() = runTest {
        var trackName: String? = null
        var trackPayload: Map<String, String>? = null

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onTrack = { name, payload ->
                trackName = name
                trackPayload = payload
            }
        )

        dispatcher.dispatch(
            SduiActionDto(
                type = "Track",
                eventName = "click_banner",
                payload = mapOf("banner_id" to "456")
            )
        )

        assertEquals("click_banner", trackName)
        assertEquals(mapOf("banner_id" to "456"), trackPayload)
    }

    @Test
    fun testSequence() = runTest {
        var count = 0
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onCustomAction = { name, _ ->
                if (name == "increment") {
                    count++
                }
                true
            }
        )

        val action = SduiActionDto(
            type = "Sequence",
            actions = listOf(
                SduiActionDto(type = "Custom", name = "increment"),
                SduiActionDto(type = "Custom", name = "increment")
            )
        )

        dispatcher.dispatch(action)
        assertEquals(2, count)
    }

    @Test
    fun testSequence_withNavigate() = runTest {
        var navigatedRoute: String? = null
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onNavigate = { route, _ -> navigatedRoute = route }
        )

        val action = SduiActionDto(
            type = "Sequence",
            actions = listOf(
                SduiActionDto(type = "Navigate", route = "target_screen")
            )
        )

        dispatcher.dispatch(action)
        assertEquals("target_screen", navigatedRoute)
    }


    @Test
    fun testConditional_trueCondition() = runTest {
        var customTriggered = false
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onCustomAction = { name, _ ->
                if (name == "then") customTriggered = true
                true
            }
        )

        stateHolder.setValue("isLoggedIn", JsonPrimitive(true))

        val action = SduiActionDto(
            type = "Conditional",
            stateKey = "isLoggedIn",
            operator = "eq",
            compareValue = JsonPrimitive(true),
            thenAction = SduiActionDto(type = "Custom", name = "then"),
            elseAction = SduiActionDto(type = "Custom", name = "else")
        )

        dispatcher.dispatch(action)
        assertTrue(customTriggered)
    }

    @Test
    fun testConditional_falseCondition() = runTest {
        var customTriggered = false
        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onCustomAction = { name, _ ->
                if (name == "else") customTriggered = true
                true
            }
        )

        stateHolder.setValue("isLoggedIn", JsonPrimitive(false))

        val action = SduiActionDto(
            type = "Conditional",
            stateKey = "isLoggedIn",
            operator = "eq",
            compareValue = JsonPrimitive(true),
            thenAction = SduiActionDto(type = "Custom", name = "then"),
            elseAction = SduiActionDto(type = "Custom", name = "else")
        )

        dispatcher.dispatch(action)
        assertTrue(customTriggered)
    }

    @Test
    fun testActionInterceptor_blocksExecution() = runTest {
        var configInterceptorCalled = false
        var defaultActionHandlerCalled = false

        SduiEngine.initialize(
            SduiConfig(
                baseUrl = "https://example.com",
                actionInterceptor = {
                    configInterceptorCalled = true
                    true // Consume action
                }
            )
        )

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onNavigate = { _, _ -> defaultActionHandlerCalled = true }
        )

        val action = SduiActionDto(type = "Navigate", route = "test")
        dispatcher.dispatch(action)

        assertTrue(configInterceptorCalled)
        assertFalse(defaultActionHandlerCalled)
    }

    @Test
    fun testActionInterceptor_doesNotBlockWhenReturnsFalse() = runTest {
        var configInterceptorCalled = false
        var defaultActionHandlerCalled = false

        SduiEngine.initialize(
            SduiConfig(
                baseUrl = "https://example.com",
                actionInterceptor = {
                    configInterceptorCalled = true
                    false // Do not consume action
                }
            )
        )

        val dispatcher = ActionDispatcher(
            stateHolder = stateHolder,
            onNavigate = { _, _ -> defaultActionHandlerCalled = true }
        )

        val action = SduiActionDto(type = "Navigate", route = "test")
        dispatcher.dispatch(action)

        assertTrue(configInterceptorCalled)
        assertTrue(defaultActionHandlerCalled)
    }
}
