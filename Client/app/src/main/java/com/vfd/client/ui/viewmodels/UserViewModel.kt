package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AddressDtos
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

data class UserUpdateUiState(
    val firstName: String = "",
    val lastName: String = "",
    val address: AddressDtos.AddressCreate
    = AddressDtos.AddressCreate(
        country = "",
        voivodeship = "",
        city = "",
        postalCode = "",
        street = "",
        houseNumber = "",
        apartNumber = null
    ),
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val firstNameTouched: Boolean = false,
    val lastNameTouched: Boolean = false,
    val addressTouched: Boolean = false,
    val emailAddressTouched: Boolean = false,
    val phoneNumberTouched: Boolean = false,
    val passwordTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _currentUserUiState = MutableStateFlow(CurrentUserUiState())
    val currentUserUiState = _currentUserUiState.asStateFlow()

    private val _userUpdateUiState = MutableStateFlow(UserUpdateUiState())
    val userUpdateUiState = _userUpdateUiState.asStateFlow()

    fun onUserUpdateValueChange(field: (UserUpdateUiState) -> UserUpdateUiState) {
        _userUpdateUiState.value = field(_userUpdateUiState.value)
    }

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

    fun updateUser(userDto: UserDtos.UserPatch) {
        viewModelScope.launch {
            _userUpdateUiState.value =
                _userUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = userRepository.updateUser(userDto)) {

                is ApiResult.Success -> {
                    _userUpdateUiState.value = _userUpdateUiState.value.copy(
                        isLoading = false,
                        success = true,
                        errorMessage = null,
                        fieldErrors = emptyMap()
                    )

                    val updatedUser = _currentUserUiState.value.currentUser
                    if (updatedUser != null) {
                        {
                            _currentUserUiState.value.currentUser?.copy(
                                firstName = userDto.firstName
                                    ?: _currentUserUiState.value.currentUser!!.firstName,
                                lastName = userDto.lastName
                                    ?: _currentUserUiState.value.currentUser!!.lastName,
                                address = AddressDtos.AddressResponse(
                                    addressId = result.data?.address?.addressId
                                        ?: _currentUserUiState.value.currentUser!!.address.addressId,
                                    country = result.data?.address?.country
                                        ?: _currentUserUiState.value.currentUser!!.address.country,
                                    voivodeship = result.data?.address?.voivodeship
                                        ?: _currentUserUiState.value.currentUser!!.address.voivodeship,
                                    city = result.data?.address?.city
                                        ?: _currentUserUiState.value.currentUser!!.address.city,
                                    postalCode = result.data?.address?.postalCode
                                        ?: _currentUserUiState.value.currentUser!!.address.postalCode,
                                    street = result.data?.address?.street
                                        ?: _currentUserUiState.value.currentUser!!.address.street,
                                    houseNumber = result.data?.address?.houseNumber
                                        ?: _currentUserUiState.value.currentUser!!.address.houseNumber,
                                    apartNumber = result.data?.address?.apartNumber
                                        ?: _currentUserUiState.value.currentUser!!.address.apartNumber
                                ),
                                emailAddress = userDto.emailAddress
                                    ?: _currentUserUiState.value.currentUser!!.emailAddress,
                                phoneNumber = userDto.phoneNumber
                                    ?: _currentUserUiState.value.currentUser!!.phoneNumber
                            )
                        }

                    } else
                        null
                    _currentUserUiState.value =
                        _currentUserUiState.value.copy(currentUser = updatedUser)
                    _uiEvent.send(UiEvent.Success("User updated successfully"))
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

                    _userUpdateUiState.value = _userUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error("Failed to update user"))
                }

                is ApiResult.Loading -> {
                    _userUpdateUiState.value = _userUpdateUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}