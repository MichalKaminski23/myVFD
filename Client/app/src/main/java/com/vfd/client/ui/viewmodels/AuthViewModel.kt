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

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val country: String = "",
    val voivodeship: String = "",
    val city: String = "",
    val postalCode: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val apartNumber: String = "",
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

data class LoginUiState(
    val emailAddress: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun onRegisterValueChange(field: (RegisterUiState) -> RegisterUiState) {
        _registerUiState.value = field(_registerUiState.value)
    }

    fun onLoginValueChange(field: (LoginUiState) -> LoginUiState) {
        _loginUiState.value = field(_loginUiState.value)
    }

    init {
        _registerUiState.value = RegisterUiState(
            firstName = "Arek",
            lastName = "Niemusialski",
            country = "Poland",
            voivodeship = "Silesian",
            city = "Strzyzowice",
            postalCode = "69-420",
            street = "Belna",
            houseNumber = "1",
            apartNumber = "7",
            emailAddress = "arek@test.com",
            phoneNumber = "123123123",
            password = "Dupa12345!"
        )

        _loginUiState.value = LoginUiState(
            emailAddress = "jan.kowalski@test.com",
            password = "Dupa12345!"
        )
    }

    val token = authRepository.getToken()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(100000000), null)

    fun register() {
        val newUser = UserDtos.UserCreate(
            firstName = _registerUiState.value.firstName,
            lastName = _registerUiState.value.lastName,
            address = AddressDtos.AddressCreate(
                country = _registerUiState.value.country,
                voivodeship = _registerUiState.value.voivodeship,
                city = _registerUiState.value.city,
                postalCode = _registerUiState.value.postalCode,
                street = _registerUiState.value.street,
                houseNumber = _registerUiState.value.houseNumber,
                apartNumber = _registerUiState.value.apartNumber.ifBlank { null }
            ),
            emailAddress = _registerUiState.value.emailAddress,
            phoneNumber = _registerUiState.value.phoneNumber,
            password = _registerUiState.value.password
        )

        viewModelScope.launch {
            _registerUiState.value =
                _registerUiState.value.copy(isLoading = true, errorMessage = null, success = false)

            when (val result = authRepository.register(newUser)) {
                is ApiResult.Success<AuthResponseDto> -> {
                    val authResponse = result.data
                    val jwt = authResponse?.token
                    if (jwt != null) {
                        authRepository.saveToken(jwt)
                    }

                    _registerUiState.value =
                        _registerUiState.value.copy(isLoading = false, success = true)
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = when {
                        message.contains("phone number", ignoreCase = true) ->
                            mapOf("phoneNumber" to message)

                        message.contains("email", ignoreCase = true) ->
                            mapOf("emailAddress" to message)

                        else -> result.fieldErrors
                    }

                    _registerUiState.value = _registerUiState.value.copy(
                        isLoading = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                }

                is ApiResult.Loading -> {
                    _registerUiState.value = _registerUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun login() {
        val user = UserDtos.UserLogin(
            emailAddress = _loginUiState.value.emailAddress,
            password = _loginUiState.value.password
        )

        viewModelScope.launch {
            _loginUiState.value =
                _loginUiState.value.copy(isLoading = true, errorMessage = null, success = false)

            when (val result = authRepository.login(user)) {
                is ApiResult.Success<AuthResponseDto> -> {
                    val authResponse = result.data
                    val jwt = authResponse?.token
                    if (jwt != null) {
                        authRepository.saveToken(jwt)
                    }

                    _loginUiState.value =
                        _loginUiState.value.copy(isLoading = false, success = true)
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = when {
                        message.contains("phone number", ignoreCase = true) ->
                            mapOf("phoneNumber" to message)

                        message.contains("email", ignoreCase = true) ->
                            mapOf("emailAddress" to message)

                        else -> result.fieldErrors
                    }

                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                }

                is ApiResult.Loading -> {
                    _loginUiState.value = _loginUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
            _loginUiState.value = _loginUiState.value.copy(success = false)
            _registerUiState.value = _registerUiState.value.copy(success = false)
        }
    }
}