package com.vfd.client.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.repositories.AssetRepository
import com.vfd.client.data.repositories.EventRepository
import com.vfd.client.data.repositories.FirefighterRepository
import com.vfd.client.data.repositories.InvestmentProposalRepository
import com.vfd.client.data.repositories.OperationRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.daysUntilSomething
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

data class BadgesState(
    val pendingFirefighters: Int = 0,
    val activeFirefighters: Int = 0,
    val totalAssets: Int = 0,
    val upcomingEvents: Int = 0,
    val totalOperations: Int = 0,
    val pendingInvestments: Int = 0
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firefighterRepository: FirefighterRepository,
    private val assetRepository: AssetRepository,
    private val eventRepository: EventRepository,
    private val operationRepository: OperationRepository,
    private val investmentProposalRepository: InvestmentProposalRepository
) : ViewModel() {

    private val _badgesState = MutableStateFlow(BadgesState())
    val badgesState: StateFlow<BadgesState> = _badgesState.asStateFlow()

    private val _canCreateThings = MutableStateFlow(false)
    val canCreateThings: StateFlow<Boolean> = _canCreateThings.asStateFlow()

    private val _hasRole = MutableStateFlow(false)
    val hasRole: StateFlow<Boolean> = _hasRole.asStateFlow()

    private val _role = MutableStateFlow("")
    val role: StateFlow<String> = _role.asStateFlow()

    fun setUserRole(role: String?) {
        _canCreateThings.value = role?.uppercase() == "PRESIDENT"
        _hasRole.value = !role.isNullOrBlank()
        _role.value = role?.uppercase() ?: ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshBadges() {
        if (_role.value == "USER") {
            return
        }
        viewModelScope.launch {
            try {
                supervisorScope {
                    // uruchamiamy zapytania równolegle i zabezpieczamy każde osobno
                    val pendingDeferred = async { safeGetPendingFirefighters() }
                    val activeDeferred = async { safeGetActiveFirefighters() }
                    val assetsDeferred = async { safeGetAssets() }
                    val eventsDeferred = async { safeGetUpcomingEventsCount() }
                    val operationsDeferred = async { safeGetOperations() }
                    val investmentsDeferred = async { safeGetPendingInvestments() }

                    val newState = BadgesState(
                        pendingFirefighters = pendingDeferred.await(),
                        activeFirefighters = activeDeferred.await(),
                        totalAssets = assetsDeferred.await(),
                        upcomingEvents = eventsDeferred.await(),
                        totalOperations = operationsDeferred.await(),
                        pendingInvestments = investmentsDeferred.await()
                    )
                    _badgesState.value = newState
                }
            } catch (t: Throwable) {
                Log.e("MainViewModel", "refreshBadges failed", t)
            }
        }
    }

    private suspend fun safeGetPendingFirefighters(): Int {
        return try {
            when (val r = firefighterRepository.getPendingFirefighters(page = 0, size = 1)) {
                is ApiResult.Success -> r.data?.totalElements ?: r.data?.items?.size ?: 0
                else -> 0
            }
        } catch (_: Throwable) {
            0
        }
    }

    private suspend fun safeGetActiveFirefighters(): Int {
        return try {
            when (val r = firefighterRepository.getFirefighters(page = 0, size = 1)) {
                is ApiResult.Success -> r.data?.totalElements ?: r.data?.items?.size ?: 0
                else -> 0
            }
        } catch (_: Throwable) {
            0
        }
    }

    private suspend fun safeGetAssets(): Int {
        return try {
            when (val r = assetRepository.getAssets(page = 0, size = 1)) {
                is ApiResult.Success -> r.data?.totalElements ?: r.data?.items?.size ?: 0
                else -> 0
            }
        } catch (_: Throwable) {
            0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun safeGetUpcomingEventsCount(): Int {
        return try {
            when (val r = eventRepository.getEvents(page = 0, size = 20)) {
                is ApiResult.Success -> r.data?.items?.count { dto ->
                    val daysLeft = daysUntilSomething(dto.eventDate)
                    daysLeft in 0..30
                } ?: 0

                else -> 0
            }
        } catch (_: Throwable) {
            0
        }
    }

    private suspend fun safeGetOperations(): Int {
        return try {
            when (val r = operationRepository.getOperations(page = 0, size = 1)) {
                is ApiResult.Success -> r.data?.totalElements ?: r.data?.items?.size ?: 0
                else -> 0
            }
        } catch (_: Throwable) {
            0
        }
    }

    private suspend fun safeGetPendingInvestments(): Int {
        return try {
            when (val r =
                investmentProposalRepository.getInvestmentProposals(page = 0, size = 20)) {
                is ApiResult.Success -> r.data?.items?.count { dto ->
                    dto.status.equals(
                        "PENDING",
                        ignoreCase = true
                    )
                } ?: 0

                else -> 0
            }
        } catch (_: Throwable) {
            0
        }
    }
}