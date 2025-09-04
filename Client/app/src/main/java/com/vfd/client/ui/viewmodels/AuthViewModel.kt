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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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

    init {
        _uiState.value = AuthUiState(
            firstName = "Arek",
            lastName = "Niemusialski",
            country = "Poland",
            voivodeship = "Silesian",
            city = "Strzyzowice",
            postalCode = "69-420",
            street = "Belna",
            houseNumber = "1",
            apartNumber = "7",
            email = "arek@test.com",
            phone = "123123123",
            password = "Dupa12345!"
        )
    }

    val tokenFlow = authRepository.getToken()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(100000000000000), null)

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
                    val authResponse = result.data
                    val jwt = authResponse?.token
                    if (jwt != null) {
                        authRepository.saveToken(jwt)
                    }

                    _uiState.value = state.copy(loading = false, success = true)
                }

                is ApiResult.Error -> {
                    _uiState.value = state.copy(
                        loading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

                is ApiResult.Loading -> {
                    _uiState.value = state.copy(loading = true)
                }
            }
        }
    }

    fun login() {
        val state = _uiState.value
        val user = UserDtos.UserLogin(
            emailAddress = state.email,
            password = state.password
        )

        viewModelScope.launch {
            _uiState.value = state.copy(loading = true, error = null, success = false)

            when (val result = authRepository.login(user)) {
                is ApiResult.Success<AuthResponseDto> -> {
                    val authResponse = result.data
                    val jwt = authResponse?.token
                    if (jwt != null) {
                        authRepository.saveToken(jwt)
                    }

                    _uiState.value = state.copy(loading = false, success = true)
                }

                is ApiResult.Error -> {
                    _uiState.value = state.copy(
                        loading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

                is ApiResult.Loading -> {
                    _uiState.value = state.copy(loading = true)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
            _uiState.value = _uiState.value.copy(success = false)
        }
    }
}