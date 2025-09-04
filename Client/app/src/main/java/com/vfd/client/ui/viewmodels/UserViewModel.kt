package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.data.repositories.UserRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val json: Json
) : ViewModel() {

    private val _users =
        MutableStateFlow<ApiResult<PageResponse<UserDtos.UserResponse>>>(ApiResult.Loading())
    val users: StateFlow<ApiResult<PageResponse<UserDtos.UserResponse>>> = _users

    private val _user = MutableStateFlow<UserDtos.UserResponse?>(null)
    val user: StateFlow<UserDtos.UserResponse?> = _user

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun loadAllUsers(page: Int = 0, size: Int = 20, sort: String = "createdAt,asc") {
        viewModelScope.launch {
            _users.value = ApiResult.Loading()
            _users.value = repository.getAllUsers(page, size, sort)
        }
    }

    fun getUser() {
        viewModelScope.launch {
            when (val result = repository.getCurrentUser()) {
                is ApiResult.Success -> {
                    _user.value = result.data
                }

                is ApiResult.Error -> {
                }

                else -> {
                }
            }
        }
    }

    fun updateUserName(userId: Int) {
        viewModelScope.launch {
            when (val result =
                repository.updateUser(userId, UserDtos.UserPatch(firstName = name.value))) {
                is ApiResult.Success -> {
                    _errorMessage.value = null
                }

                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                }

                else -> {}
            }
        }
    }
}