package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.data.remote.dtos.InvestmentProposalStatus
import com.vfd.client.data.remote.dtos.VoteDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppInvestmentProposalCard
import com.vfd.client.ui.components.elements.AppStringDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.components.texts.AppAmountTextField
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.InvestmentProposalViewModel
import com.vfd.client.ui.viewmodels.VoteViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun InvestmentProposalScreen(
    investmentProposalViewModel: InvestmentProposalViewModel,
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    voteViewModel: VoteViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val investmentProposalUiState by investmentProposalViewModel.investmentProposalUiState.collectAsState()
    val investmentProposalUpdatelUiState by investmentProposalViewModel.investmentProposalUpdateUiState.collectAsState()
    var editingInvestmentProposalId by remember { mutableStateOf<Int?>(null) }

    val voteUiState by voteViewModel.voteUiState.collectAsState()
    val voteCreateUiState by voteViewModel.voteCreateUiState.collectAsState()
    val voteUpdateUiState by voteViewModel.voteUpdateUiState.collectAsState()

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    val hasMore = investmentProposalUiState.page + 1 < investmentProposalUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(investmentProposalViewModel.uiEvents, snackbarHostState)
    AppUiEvents(voteViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(investmentProposalUpdatelUiState.success) {
        if (investmentProposalUpdatelUiState.success) {
            editingInvestmentProposalId = null
            investmentProposalViewModel.onInvestmentProposalUpdateValueChange { it.copy(success = false) }
        }
    }
    LaunchedEffect(voteCreateUiState.success) {
        if (voteCreateUiState.success) {
            investmentProposalViewModel.getInvestmentProposals(page = 0, refresh = true)
            voteViewModel.onVoteCreateValueChange { it.copy(success = false) }
        }
    }
    LaunchedEffect(voteUpdateUiState.success) {
        if (voteUpdateUiState.success) {
            investmentProposalViewModel.getInvestmentProposals(page = 0, refresh = true)
            voteViewModel.onVoteUpdateValueChange { it.copy(success = false) }
        }
    }

    val hasPermission =
        currentFirefighterUiState.currentFirefighter?.role.toString() != FirefighterRole.USER.toString()

    if (hasPermission) {
        LaunchedEffect(Unit) {
            investmentProposalViewModel.getInvestmentProposals(page = 0, refresh = true)
            firefighterViewModel.getFirefighterByEmailAddress()

            RefreshManager.events.collect { event ->
                when (event) {
                    is RefreshEvent.InvestmentProposalScreen -> {
                        investmentProposalViewModel.getInvestmentProposals(page = 0, refresh = true)
                    }

                    else -> {}
                }
            }
        }
    }

    var pendingVote by remember { mutableStateOf<Pair<Int, Boolean>?>(null) }

    val statusPairs = InvestmentProposalStatus.entries.map { status ->
        status.name to when (status) {
            InvestmentProposalStatus.REJECTED -> stringResource(id = R.string.rejected)
            InvestmentProposalStatus.PENDING -> stringResource(id = R.string.pending)
            InvestmentProposalStatus.APPROVED -> stringResource(id = R.string.approved)
            InvestmentProposalStatus.CANCELLED -> stringResource(id = R.string.cancelled)
        }
    }

    val statusItems = statusPairs.map { it.second }
    val selectedStatusLabel =
        statusPairs.firstOrNull { it.first == investmentProposalUpdatelUiState.status }?.second
            ?: ""

    LaunchedEffect(
        voteUiState.isLoading,
        pendingVote,
        currentFirefighterUiState.currentFirefighter
    ) {
        val toHandle = pendingVote
        val myId = currentFirefighterUiState.currentFirefighter?.firefighterId

        if (toHandle != null && !voteUiState.isLoading && myId != null) {
            val (proposalId, desired) = toHandle
            val myVote = voteUiState.votes.firstOrNull {
                it.investmentProposalId == proposalId && it.firefighterId == myId
            }
            when {
                myVote == null -> {
                    voteViewModel.createVote(
                        VoteDtos.VoteCreate(
                            investmentProposalId = proposalId,
                            voteValue = desired
                        )
                    )
                }

                myVote.voteValue != desired -> {
                    voteViewModel.updateVote(
                        myVote.voteId,
                        VoteDtos.VotePatch(voteValue = desired)
                    )
                }

                else -> {
                    investmentProposalViewModel.getInvestmentProposals(page = 0, refresh = true)
                }
            }
            pendingVote = null
        }
    }

    AppListScreen(
        data = investmentProposalUiState.investmentProposals,
        isLoading = investmentProposalUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        filter = { investmentProposal, query ->
            query.isBlank() ||
                    investmentProposal.description.contains(
                        query,
                        ignoreCase = true
                    ) || investmentProposal.status.contains(query, ignoreCase = true)
        },
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !investmentProposalUiState.isLoading)
                investmentProposalViewModel.getInvestmentProposals(page = investmentProposalUiState.page + 1)
        },
        errorMessage = investmentProposalUiState.errorMessage,
        itemKey = { it.investmentProposalId }
    ) { investmentProposal ->
        if (editingInvestmentProposalId == investmentProposal.investmentProposalId) {
            if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                AppInvestmentProposalCard(
                    investmentProposal,
                    actions = {
                        AppTextField(
                            value = investmentProposalUpdatelUiState.description,
                            onValueChange = { new ->
                                investmentProposalViewModel.onInvestmentProposalUpdateValueChange() {
                                    it.copy(description = new, descriptionTouched = true)
                                }
                            },
                            label = stringResource(id = R.string.item_description),
                            errorMessage = investmentProposalUpdatelUiState.errorMessage,
                            singleLine = false
                        )
                        AppAmountTextField(
                            amount = investmentProposalUpdatelUiState.amount,
                            onAmountChange = { new ->
                                investmentProposalViewModel.onInvestmentProposalUpdateValueChange {
                                    it.copy(amount = new, amountTouched = true)
                                }
                            },
                            label = stringResource(id = R.string.amount),
                        )
                        AppStringDropdown(
                            label = stringResource(id = R.string.item_status),
                            items = statusItems,
                            selected = selectedStatusLabel,
                            onSelected = { newLabel ->
                                val code = statusPairs.firstOrNull { it.second == newLabel }?.first
                                    ?: newLabel
                                investmentProposalViewModel.onInvestmentProposalUpdateValueChange {
                                    it.copy(status = code, statusTouched = true)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Default.ShoppingCart,
                            enabled = !investmentProposalUpdatelUiState.isLoading
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppButton(
                                icon = Icons.Default.Check,
                                label = stringResource(id = R.string.save),
                                onClick = {
                                    investmentProposal.investmentProposalId.let { id ->
                                        val investmentProposalDto =
                                            InvestmentProposalDtos.InvestmentProposalPatch(
                                                description = if (investmentProposalUpdatelUiState.descriptionTouched)
                                                    investmentProposalUpdatelUiState.description
                                                else null,

                                                amount = if (investmentProposalUpdatelUiState.amountTouched)
                                                    investmentProposalUpdatelUiState.amount
                                                else null,

                                                status = investmentProposalUpdatelUiState.status

                                            )
                                        investmentProposalViewModel.updateInvestmentProposal(
                                            id,
                                            investmentProposalDto
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = investmentProposalUpdatelUiState.description.isNotBlank() &&
                                        investmentProposalUpdatelUiState.amount != null &&
                                        investmentProposalUpdatelUiState.status.isNotBlank(),
                                loading = investmentProposalUpdatelUiState.isLoading
                            )
                            AppButton(
                                icon = Icons.Default.Close,
                                label = stringResource(id = R.string.cancel),
                                onClick = { editingInvestmentProposalId = null },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                )
            } else {
                AppInvestmentProposalCard(investmentProposal)
            }
        } else {
            AppInvestmentProposalCard(
                investmentProposal,
                actions = {
                    if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                        AppButton(
                            icon = Icons.Default.Edit,
                            label = stringResource(id = R.string.edit),
                            onClick = {
                                editingInvestmentProposalId =
                                    investmentProposal.investmentProposalId
                                investmentProposalViewModel.onInvestmentProposalUpdateValueChange {
                                    it.copy(
                                        description = investmentProposal.description,
                                        amount = investmentProposal.amount,
                                        status = investmentProposal.status,
                                        descriptionTouched = false,
                                        amountTouched = false,
                                        statusTouched = false,
                                    )
                                }

                            }
                        )
                    }
                    if (investmentProposal.status == InvestmentProposalStatus.PENDING.name) {
                        val myVote = investmentProposal.myVote
                        val isThisItemLoading =
                            voteCreateUiState.isLoading ||
                                    voteUpdateUiState.isLoading ||
                                    (pendingVote?.first == investmentProposal.investmentProposalId && voteUiState.isLoading)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppButton(
                                icon = Icons.Default.Check,
                                label = stringResource(id = R.string.yes),
                                onClick = {
                                    val id = investmentProposal.investmentProposalId
                                    if (myVote == null) {
                                        voteViewModel.createVote(
                                            VoteDtos.VoteCreate(
                                                investmentProposalId = id,
                                                voteValue = true
                                            )
                                        )
                                    } else {
                                        pendingVote = id to true
                                        voteViewModel.getVotes(
                                            page = 0,
                                            size = 50,
                                            investmentProposalId = id,
                                            refresh = true
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isThisItemLoading && myVote != true,
                                loading = isThisItemLoading
                            )
                            AppButton(
                                icon = Icons.Default.Close,
                                label = stringResource(id = R.string.no),
                                onClick = {
                                    val id = investmentProposal.investmentProposalId
                                    if (myVote == null) {
                                        voteViewModel.createVote(
                                            VoteDtos.VoteCreate(
                                                investmentProposalId = id,
                                                voteValue = false
                                            )
                                        )
                                    } else {
                                        pendingVote = id to false
                                        voteViewModel.getVotes(
                                            page = 0,
                                            size = 50,
                                            investmentProposalId = id,
                                            refresh = true
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isThisItemLoading && myVote != false,
                                loading = isThisItemLoading
                            )
                        }
                    }
                }
            )
        }
    }
}