package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.data.remote.dtos.AuthResponseDto
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.data.repositories.AuthRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val firstName: String = "",
    val lastName: String = "",
    val country: String = "",
    val voivodeship: String = "",
    val city: String = "",
    val postalCode: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val apartNumber: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onValueChange(field: (AuthUiState) -> AuthUiState) {
        _uiState.value = field(_uiState.value)
    }

    fun register() {
        val state = _uiState.value
        val user = UserDtos.UserCreate(
            firstName = state.firstName,
            lastName = state.lastName,
            address = AddressDtos.AddressCreate(
                country = state.country,
                voivodeship = state.voivodeship,
                city = state.city,
                postalCode = state.postalCode,
                street = state.street,
                houseNumber = state.houseNumber,
                apartNumber = state.apartNumber.ifBlank { null }
            ),
            emailAddress = state.email,
            phoneNumber = state.phone,
            password = state.password
        )

        viewModelScope.launch {
            _uiState.value = state.copy(loading = true, error = null, success = false)
            when (val result = authRepository.register(user)) {
                is ApiResult.Success<AuthResponseDto> -> {
                    _uiState.value = state.copy(loading = false, success = true)
                }

                is ApiResult.Error -> {
                    _uiState.value = state.copy(
                        loading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

                is ApiResult.Loading<*> -> TODO()
            }
        }
    }
}
