package com.vfd.client.ui.vievmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.data.repositories.UserRepository
import com.vfd.client.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val json: Json
) : ViewModel() {

    private val _users = MutableStateFlow<Resource<List<UserDtos.UserResponse>>>(Resource.Loading())
    val users: StateFlow<Resource<List<UserDtos.UserResponse>>> = _users

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            try {
                val response = repository.getAllUsers()
                Log.d(
                    "API_UserViewModel",
                    "Odebrani użytkownicy:\n${json.encodeToString(response)}"
                )
                _users.value =
                    Resource.Success(response)
            } catch (e: Exception) {
                _users.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateUserName(userId: Int) {
        viewModelScope.launch {
            try {
                val patchDto = UserDtos.UserPatch(firstName = _name.value)
                val updatedUser = repository.updateUser(userId, patchDto)

                Log.d(
                    "API_UserViewModel",
                    "Zaktualizowany użytkownik:\n${json.encodeToString(updatedUser)}"
                )
            } catch (e: Exception) {
                Log.e("UserViewModel", "Błąd aktualizacji użytkownika", e)
            }
        }
    }
}