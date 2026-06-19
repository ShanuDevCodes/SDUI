package `in`.shanudevcodes.sdui.feature.screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.shanudevcodes.sdui.feature.screen.domain.repository.ScreenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel managing SDUI screen state and side-effects.
 */
class SduiScreenViewModel(
    private val screenId: String,
    private val repository: ScreenRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _uiState = MutableStateFlow<SduiScreenUiState>(SduiScreenUiState.Loading)
    val uiState: StateFlow<SduiScreenUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SduiScreenUiEvent>()
    val uiEvent: SharedFlow<SduiScreenUiEvent> = _uiEvent.asSharedFlow()

    init {
        loadScreen()
    }

    fun handleEvent(event: SduiScreenEvent) {
        when (event) {
            is SduiScreenEvent.LoadScreen -> loadScreen()
            is SduiScreenEvent.OnAction -> {
                // Action routing is handled by ActionDispatcher in SduiScreenComposable
            }
            is SduiScreenEvent.OnStateChange -> {
                // Handled at the screen stateHolder level or VM level if needed
            }
        }
    }

    private fun loadScreen() {
        _uiState.value = SduiScreenUiState.Loading
        viewModelScope.launch(dispatcher) {
            repository.getScreen(screenId)
                .onSuccess { definition ->
                    _uiState.value = SduiScreenUiState.Success(definition)
                }
                .onFailure { exception ->
                    _uiState.value = SduiScreenUiState.Error(exception.message ?: "Failed to load screen")
                }
        }
    }
}
