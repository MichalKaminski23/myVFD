package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.data.repositories.InvestmentProposalRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

data class InvestmentProposalUiState(
    val investmentProposals: List<InvestmentProposalDtos.InvestmentProposalResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class InvestmentProposalUpdateUiState(
    val description: String = "",
    val amount: LocalDateTime? = null,
    val status: String = "",
    val descriptionTouched: Boolean = false,
    val amountTouched: Boolean = false,
    val statusTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

data class InvestmentProposalCreateUiState(
    val description: String = "",
    val amount: LocalDateTime? = null,
    val descriptionTouched: Boolean = false,
    val amountTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class InvestmentProposalViewModel @Inject constructor(
    private val investmentProposalRepository: InvestmentProposalRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _investmentProposalUiState = MutableStateFlow(InvestmentProposalUiState())
    val investmentProposalUiState = _investmentProposalUiState.asStateFlow()

    private val _investmentProposalUpdateUiState =
        MutableStateFlow(InvestmentProposalUpdateUiState())
    val investmentProposalUpdateUiState = _investmentProposalUpdateUiState.asStateFlow()

    private val _investmentProposalCreateUiState =
        MutableStateFlow(InvestmentProposalCreateUiState())
    val investmentProposalCreateUiState = _investmentProposalCreateUiState.asStateFlow()

    fun onInvestmentProposalUpdateValueChange(field: (InvestmentProposalUpdateUiState) -> InvestmentProposalUpdateUiState) {
        _investmentProposalUpdateUiState.value = field(_investmentProposalUpdateUiState.value)
    }

    fun onInvestmentProposalCreateValueChange(field: (InvestmentProposalCreateUiState) -> InvestmentProposalCreateUiState) {
        _investmentProposalCreateUiState.value = field(_investmentProposalCreateUiState.value)
    }

    fun createInvestmentProposal(investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate) {
        viewModelScope.launch {
            _investmentProposalCreateUiState.value = _investmentProposalCreateUiState.value.copy(
                isLoading = true,
                errorMessage = null,
                success = false
            )

            when (val result =
                investmentProposalRepository.createInvestmentProposal(investmentProposalDto)) {

                is ApiResult.Success -> {
                    _investmentProposalCreateUiState.value =
                        _investmentProposalCreateUiState.value.copy(
                            description = "",
                            amount = null,
                            isLoading = false,
                            success = true
                        )

                    _investmentProposalUiState.value = _investmentProposalUiState.value.copy(
                        investmentProposals = listOf(result.data!!) + _investmentProposalUiState.value.investmentProposals
                    )
                    _uiEvent.send(UiEvent.Success("Investment proposal created successfully"))
                }

                is ApiResult.Error -> {
                    _investmentProposalCreateUiState.value =
                        _investmentProposalCreateUiState.value.copy(
                            isLoading = false,
                            success = false,
                            errorMessage = result.message ?: "Failed to create investment proposal"
                        )
                    _uiEvent.send(UiEvent.Error("Failed to create investment proposa"))
                }

                is ApiResult.Loading -> {
                    _investmentProposalCreateUiState.value =
                        _investmentProposalCreateUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getInvestmentProposals(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _investmentProposalUiState.value = _investmentProposalUiState.value.copy(
                investmentProposals = if (refresh || page == 0) emptyList() else _investmentProposalUiState.value.investmentProposals,
                isLoading = true,
                errorMessage = null
            )

            when (val result = investmentProposalRepository.getInvestmentProposals(page, size)) {
                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _investmentProposalUiState.value = _investmentProposalUiState.value.copy(
                        investmentProposals = if (refresh || page == 0) {
                            response.items
                        } else {
                            _investmentProposalUiState.value.investmentProposals + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _investmentProposalUiState.value = _investmentProposalUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load investment proposals"
                    )
                }

                is ApiResult.Loading -> {
                    _investmentProposalUiState.value =
                        _investmentProposalUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateInvestmentProposal(
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch,
    ) {
        viewModelScope.launch {
            _investmentProposalUpdateUiState.value =
                _investmentProposalUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = investmentProposalRepository.updateInvestmentProposal(
                investmentProposalId,
                investmentProposalDto
            )) {

                is ApiResult.Success -> {
                    _investmentProposalUpdateUiState.value =
                        _investmentProposalUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null
                        )

                    val updatedInvestmentProposals =
                        _investmentProposalUiState.value.investmentProposals.map { investmentProposal ->
                            if (investmentProposal.investmentProposalId == investmentProposalId) investmentProposal.copy(
                                description = result.data?.description
                                    ?: investmentProposal.description,
                                amount = result.data?.amount ?: investmentProposal.amount,
                                status = result.data?.status ?: investmentProposal.status,
                            ) else investmentProposal
                        }

                    _investmentProposalUiState.value =
                        _investmentProposalUiState.value.copy(investmentProposals = updatedInvestmentProposals)
                    _uiEvent.send(UiEvent.Success("Investment proposal updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    _investmentProposalUpdateUiState.value =
                        _investmentProposalUpdateUiState.value.copy(
                            isLoading = false,
                            success = false,
                            errorMessage = message
                        )
                    _uiEvent.send(UiEvent.Error("Failed to update investment proposal"))
                }

                is ApiResult.Loading -> {
                    _investmentProposalUpdateUiState.value =
                        _investmentProposalUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}