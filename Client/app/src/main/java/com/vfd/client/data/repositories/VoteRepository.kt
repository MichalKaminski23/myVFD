package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.VoteApi
import com.vfd.client.data.remote.dtos.VoteDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class VoteRepository @Inject constructor(
    private val voteApi: VoteApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

    suspend fun createVote(
        voteDto: VoteDtos.VoteCreate
    ): ApiResult<VoteDtos.VoteResponse> =
        safeApiCall { voteApi.createVote(voteDto) }

    suspend fun getVotes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "voteId,asc",
        investmentProposalId: Int
    ): ApiResult<PageResponse<VoteDtos.VoteResponse>> =
        safeApiCall { voteApi.getVotes(page, size, sort, investmentProposalId) }

    suspend fun updateVote(
        voteId: Int,
        voteDto: VoteDtos.VotePatch
    ): ApiResult<VoteDtos.VoteResponse> =
        safeApiCall { voteApi.updateVote(voteId, voteDto) }
}