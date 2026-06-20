package `in`.shanudevcodes.sdui

import androidx.compose.foundation.layout.fillMaxSize
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.engine.SduiConfig
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.feature.screen.data.ScreenRepositoryImpl
import androidx.navigation3.runtime.NavBackStack
import `in`.shanudevcodes.sdui.core.presentation.SduiRoute
import `in`.shanudevcodes.sdui.core.presentation.SduiNavDisplay
import kotlinx.coroutines.launch

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
                add(SvgDecoder.Factory())
            }
            .build()
    }

    // Initialize the SDUI Engine pointing to our local server
    remember {
        SduiEngine.initialize(
            config = SduiConfig(
                baseUrl = "http://10.107.4.227:8085/",
                defaultHeaders = emptyMap()
            )
        )
        // Register custom host-app components
        SduiEngine.registerComponent("SduiBannerCard") { component, modifier, stateHolder ->
            SduiBannerCard(component, modifier, stateHolder)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val repository = remember { ScreenRepositoryImpl() }
    val backStack = remember { NavBackStack<SduiRoute>(SduiRoute.Screen("complex")) }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) {innerPadding->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SduiNavDisplay(
                    backStack = backStack,
                    repository = repository,
                    onNavigate = { route, params ->
                        scope.launch {
                            if (route == "back") {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.lastIndex)
                                } else {
                                    snackbarHostState.showSnackbar("Cannot go back: Root screen reached")
                                }
                            } else {
                                backStack.add(SduiRoute.Screen(route, params))
                            }
                        }
                    },
                    onShowSnackbar = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}