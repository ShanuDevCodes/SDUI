package `in`.shanudevcodes.sdui.core.action

import `in`.shanudevcodes.sdui.core.components.ConditionEvaluator
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Dispatcher to route and execute SduiActionDto actions.
 */
internal class ActionDispatcher(
    private val stateHolder: SduiStateHolder,
    private val onNavigate: ((route: String, params: Map<String, String>) -> Unit)? = null,
    private val onGoBack: (() -> Unit)? = null,
    private val onReplace: ((route: String, params: Map<String, String>) -> Unit)? = null,
    private val onPopToRoot: (() -> Unit)? = null,
    private val onDeepLink: ((url: String) -> Unit)? = null,
    private val onShowSnackbar: ((message: String, actionLabel: String?, onAction: SduiActionDto?) -> Unit)? = null,
    private val onShowDialog: ((title: String, message: String, confirmText: String?, dismissText: String?, onConfirm: SduiActionDto?, onDismiss: SduiActionDto?) -> Unit)? = null,
    private val onCustomAction: ((name: String, payload: Map<String, String>) -> Boolean)? = null,
    private val onApiCall: (suspend (action: SduiActionDto) -> Unit)? = null,
    private val onTrack: ((eventName: String, params: Map<String, String>) -> Unit)? = null
) {

    private val defaultApiCallHandler = ApiCallHandler(
        stateProvider = { stateHolder.state.value },
        dispatchAction = { dispatch(it) }
    )

    /**
     * Executes the given action.
     */
    suspend fun dispatch(action: SduiActionDto) {
        // 1. Intercept custom action via engine if configured
        val isIntercepted = try {
            SduiEngine.getConfig().actionInterceptor?.invoke(action) ?: false
        } catch (e: IllegalStateException) {
            false
        }
        if (isIntercepted) return

        when (action.type) {
            "Navigate" -> {
                val route = action.route
                if (route != null) {
                    onNavigate?.invoke(route, action.params)
                }
            }
            "GoBack" -> {
                onGoBack?.invoke()
            }
            "Replace" -> {
                val route = action.route
                if (route != null) {
                    onReplace?.invoke(route, action.params)
                }
            }
            "PopToRoot" -> {
                onPopToRoot?.invoke()
            }
            "DeepLink" -> {
                val url = action.url
                if (url != null) {
                    onDeepLink?.invoke(url)
                }
            }
            "UpdateState" -> {
                val key = action.stateKey
                val value = action.value
                if (key != null && value != null) {
                    stateHolder.setValue(key, value)
                }
            }
            "ApiCall" -> {
                if (onApiCall != null) {
                    onApiCall.invoke(action)
                } else {
                    defaultApiCallHandler.handle(action)
                }
            }
            "ShowSnackbar" -> {
                val message = action.message
                if (message != null) {
                    onShowSnackbar?.invoke(message, action.actionLabel, action.onAction)
                }
            }
            "ShowDialog" -> {
                val title = action.title
                val message = action.message
                if (title != null && message != null) {
                    onShowDialog?.invoke(
                        title,
                        message,
                        action.confirmText,
                        action.dismissText,
                        action.onConfirm,
                        action.onDismiss
                    )
                }
            }
            "Custom" -> {
                val name = action.name
                if (name != null) {
                    onCustomAction?.invoke(name, action.payload)
                }
            }
            "Sequence" -> {
                for (subAction in action.actions) {
                    dispatch(subAction)
                }
            }
            "Conditional" -> {
                val key = action.stateKey
                val operator = action.operator ?: ""
                val compareValue = action.compareValue ?: action.value
                val stateMap = stateHolder.state.value
                val stateValue = if (!key.isNullOrEmpty()) stateMap[key] else null

                val isTrue = ConditionEvaluator.evaluate(stateValue, operator, compareValue)
                if (isTrue) {
                    val runAction = action.thenAction ?: action.onConfirm
                    if (runAction != null) {
                        dispatch(runAction)
                    }
                } else {
                    val runAction = action.elseAction ?: action.onDismiss
                    if (runAction != null) {
                        dispatch(runAction)
                    }
                }
            }
            "Track" -> {
                val eventName = action.eventName
                if (eventName != null) {
                    onTrack?.invoke(eventName, action.payload)
                }
            }
        }
    }
}
