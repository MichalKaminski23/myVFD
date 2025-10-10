package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.VoteDtos
import com.vfd.client.data.repositories.VoteRepository
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

data class VoteUiState(
    val votes: List<VoteDtos.VoteResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class VoteUpdateUiState(
    val voteValue: Boolean = false,
    val voteValueTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

data class VoteCreateUiState(
    val investmentProposalId: Int = -1,
    val voteValue: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val voteRepository: VoteRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _voteUiState = MutableStateFlow(VoteUiState())
    val voteUiState = _voteUiState.asStateFlow()

    private val _voteUpdateUiState = MutableStateFlow(VoteUpdateUiState())
    val voteUpdateUiState = _voteUpdateUiState.asStateFlow()

    private val _voteCreateUiState = MutableStateFlow(VoteCreateUiState())
    val voteCreateUiState = _voteCreateUiState.asStateFlow()

    fun onVoteUpdateValueChange(field: (VoteUpdateUiState) -> VoteUpdateUiState) {
        _voteUpdateUiState.value = field(_voteUpdateUiState.value)
    }

    fun onVoteCreateValueChange(field: (VoteCreateUiState) -> VoteCreateUiState) {
        _voteCreateUiState.value = field(_voteCreateUiState.value)
    }

    fun createVote(voteDto: VoteDtos.VoteCreate) {
        viewModelScope.launch {
            _voteCreateUiState.value = _voteCreateUiState.value.copy(
                isLoading = true,
                errorMessage = null,
                success = false
            )

            when (val result = voteRepository.createVote(voteDto)) {
                is ApiResult.Success -> {
                    _voteCreateUiState.value = _voteCreateUiState.value.copy(
                        isLoading = false,
                        success = true
                    )

                    _voteUiState.value = _voteUiState.value.copy(
                        votes = listOf(result.data!!) + _voteUiState.value.votes
                    )
                    _uiEvent.send(UiEvent.Success("Vote created successfully"))
                }

                is ApiResult.Error -> {
                    _voteCreateUiState.value = _voteCreateUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to create vote"
                    )
                    _uiEvent.send(UiEvent.Error("Failed to create vote"))
                }

                is ApiResult.Loading -> {
                    _voteCreateUiState.value = _voteCreateUiState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun getVotes(
        page: Int = 0,
        size: Int = 20,
        investmentProposalId: Int,
        refresh: Boolean = false
    ) {
        viewModelScope.launch {
            _voteUiState.value = _voteUiState.value.copy(
                votes = if (refresh || page == 0) emptyList() else _voteUiState.value.votes,
                isLoading = true,
                errorMessage = null
            )

            when (val result =
                voteRepository.getVotes(page, size, "voteId,asc", investmentProposalId)) {
                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _voteUiState.value = _voteUiState.value.copy(
                        votes = if (refresh || page == 0) {
                            response.items
                        } else {
                            _voteUiState.value.votes + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _voteUiState.value = _voteUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load votes"
                    )
                }

                is ApiResult.Loading -> {
                    _voteUiState.value =
                        _voteUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateVote(
        voteId: Int,
        voteDto: VoteDtos.VotePatch,
    ) {
        viewModelScope.launch {
            _voteUpdateUiState.value =
                _voteUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = voteRepository.updateVote(voteId, voteDto)) {

                is ApiResult.Success -> {
                    _voteUpdateUiState.value =
                        _voteUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null
                        )

                    val updatedVotes = _voteUiState.value.votes.map { vote ->
                        if (vote.voteId == voteId) vote.copy(
                            voteValue = result.data?.voteValue ?: vote.voteValue
                        ) else vote
                    }

                    _voteUiState.value = _voteUiState.value.copy(votes = updatedVotes)
                    _uiEvent.send(UiEvent.Success("Vote updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    _voteUpdateUiState.value = _voteUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = message
                    )
                    _uiEvent.send(UiEvent.Error("Failed to update vote"))
                }

                is ApiResult.Loading -> {
                    _voteUpdateUiState.value =
                        _voteUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}