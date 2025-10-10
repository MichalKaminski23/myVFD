package com.vfd.client.ui.components.dialogs

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppAmountTextField
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.InvestmentProposalViewModel

@Composable
fun InvestmentProposalCreateDialog(
    investmentProposalViewModel: InvestmentProposalViewModel,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val investmentProposalCreateUiState =
        investmentProposalViewModel.investmentProposalCreateUiState.collectAsState().value

    AppUiEvents(investmentProposalViewModel.uiEvents, snackbarHostState)

    AppFormDialog(
        show = showDialog,
        onDismiss = onDismiss,
        title = "Create new investment",
        confirmEnabled = investmentProposalCreateUiState.description.isNotBlank()
                && investmentProposalCreateUiState.amount != null
                && !investmentProposalCreateUiState.isLoading,
        confirmLoading = investmentProposalCreateUiState.isLoading,
        errorMessage = investmentProposalCreateUiState.errorMessage,
        onConfirm = {
            investmentProposalViewModel.createInvestmentProposal(
                InvestmentProposalDtos.InvestmentProposalCreate(
                    description = investmentProposalCreateUiState.description,
                    amount = investmentProposalCreateUiState.amount!!
                )
            )
            onDismiss()
        }
    ) {
        AppTextField(
            value = investmentProposalCreateUiState.description,
            onValueChange = { new ->
                investmentProposalViewModel.onInvestmentProposalCreateValueChange {
                    it.copy(
                        description = new
                    )
                }
            },
            label = "Description",
            errorMessage = investmentProposalCreateUiState.errorMessage
        )

        AppAmountTextField(
            amount = investmentProposalCreateUiState.amount,
            onAmountChange = { new ->
                investmentProposalViewModel.onInvestmentProposalCreateValueChange {
                    it.copy(amount = new)
                }
            },
            label = "Amount"
        )
    }
}