package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.data.repositories.UserRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurrentUserUiState(
    val currentUser: UserDtos.UserResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _currentUserUiState = MutableStateFlow(CurrentUserUiState())
    val currentUserUiState = _currentUserUiState.asStateFlow()

    fun getUserByEmailAddress() {
        viewModelScope.launch {
            _currentUserUiState.value =
                _currentUserUiState.value.copy(
                    currentUser = null,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = userRepository.getUserByEmailAddress()) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _currentUserUiState.value = _currentUserUiState.value.copy(
                        currentUser = response,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _currentUserUiState.value = _currentUserUiState.value.copy(
                        currentUser = null,
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load current user"
                    )
                }

                is ApiResult.Loading -> {
                    _currentUserUiState.value = _currentUserUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}