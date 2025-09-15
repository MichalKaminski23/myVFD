package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.data.repositories.UserRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserUiState(
    val users: List<UserDtos.UserResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState = _userUiState.asStateFlow()

    private val _user = MutableStateFlow<UserDtos.UserResponse?>(null)
    val user: StateFlow<UserDtos.UserResponse?> = _user

    fun loadAllUsers(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _userUiState.value = _userUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = userRepository.getAllUsers(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    _userUiState.value = _userUiState.value.copy(
                        users = response.items,
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _userUiState.value = _userUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load users"
                    )
                }

                is ApiResult.Loading -> {
                    _userUiState.value = _userUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _userUiState.value = _userUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = userRepository.getCurrentUser()) {

                is ApiResult.Success -> {
                    _user.value = result.data
                    _userUiState.value = _userUiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _user.value = null
                    _userUiState.value = _userUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load current user"
                    )
                }

                is ApiResult.Loading -> {
                    _userUiState.value = _userUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}