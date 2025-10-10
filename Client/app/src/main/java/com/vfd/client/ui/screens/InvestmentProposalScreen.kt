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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.data.remote.dtos.InvestmentProposalStatus
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

    val voteUiState = voteViewModel.voteUiState.collectAsState().value
    val voteUpdateUiState = voteViewModel.voteUpdateUiState.collectAsState().value

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    val hasMore = investmentProposalUiState.page + 1 < investmentProposalUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(investmentProposalViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(investmentProposalUpdatelUiState.success) {
        if (investmentProposalUpdatelUiState.success) {
            editingInvestmentProposalId = null
            investmentProposalViewModel.onInvestmentProposalUpdateValueChange { it.copy(success = false) }
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

    AppListScreen(
        data = investmentProposalUiState.investmentProposals,
        isLoading = investmentProposalUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        searchPlaceholder = "Search investments...",
        filter = { investmentProposal, query ->
            query.isBlank() ||
                    investmentProposal.description.contains(
                        query,
                        ignoreCase = true
                    )
        },
        emptyText = "There aren't any investments in your VFD or the investments are still loading",
        emptyFilteredText = "No investments match your search",
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
                            label = "Description",
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
                            label = "Amount"
                        )
                        AppStringDropdown(
                            label = "Status",
                            items = InvestmentProposalStatus.entries.map { it.name },
                            selected = investmentProposalUpdatelUiState.status,
                            onSelected = { selected ->
                                investmentProposalViewModel.onInvestmentProposalUpdateValueChange {
                                    it.copy(status = selected, statusTouched = true)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Default.ShoppingCart,
                            enabled = !investmentProposalUpdatelUiState.isLoading
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppButton(
                                icon = Icons.Default.Check,
                                label = "Save",
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
                                label = "Cancel",
                                onClick = { editingInvestmentProposalId = null },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                )
            } else {
                AppInvestmentProposalCard(
                    investmentProposal
                )
            }
        } else {
            AppInvestmentProposalCard(
                investmentProposal,
                actions = {
                    if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                        AppButton(
                            icon = Icons.Default.Edit,
                            label = "Edit",
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
//                    AppButton(
//                        icon = Icons.Default.Close,
//                        label = "Vote yes",
//                        onClick = {
//                            voteViewModel.onVoteUpdateValueChange {
//                                it.copy(
//                                    voteValue = true
//                                )
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    )
                }
            )
        }
    }
}