package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.data.repositories.UserRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserUiState(
    val user: UserDtos.UserResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow(UserUiState())
    val currentUser = _currentUser.asStateFlow()

    fun getUserByEmailAddress() {
        viewModelScope.launch {
            _currentUser.value = _currentUser.value.copy(isLoading = true, errorMessage = null)

            when (val result = userRepository.getUserByEmailAddress()) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    _currentUser.value = _currentUser.value.copy(
                        user = response,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _currentUser.value = _currentUser.value.copy(
                        user = null,
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load current user"
                    )
                }

                is ApiResult.Loading -> {
                    _currentUser.value = _currentUser.value.copy(isLoading = true)
                }
            }
        }
    }
}