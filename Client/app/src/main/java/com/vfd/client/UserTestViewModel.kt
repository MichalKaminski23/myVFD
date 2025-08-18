package com.vfd.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserTestViewModel(
    private val repo: UserRepoTest = UserRepoTest()
) : ViewModel() {

    private val _state = MutableStateFlow<UiTestState<List<UserTestDto>>>(UiTestState.Loading)
    val state: StateFlow<UiTestState<List<UserTestDto>>> = _state

    init {
        refresh()
    }

    fun refresh() {
        _state.value = UiTestState.Loading
        viewModelScope.launch {
            try {
                val data = repo.fetchAllUsers()
                _state.value = UiTestState.Success(data)
            } catch (e: Exception) {
                _state.value = UiTestState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
