package com.vfd.client.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class RefreshEvent {
    object MeScreen : RefreshEvent()
    object ModeratorScreen : RefreshEvent()
    object NewFirefighterScreen : RefreshEvent()
    object FirefighterScreen : RefreshEvent()
    object AssetScreen : RefreshEvent()
    object EventScreen : RefreshEvent()
    object OperationScreen : RefreshEvent()
    object InvestmentProposalScreen : RefreshEvent()
}

object RefreshManager {
    private val _events = MutableSharedFlow<RefreshEvent>()
    val events = _events.asSharedFlow()

    suspend fun refresh(event: RefreshEvent) {
        _events.emit(event)
    }
}
