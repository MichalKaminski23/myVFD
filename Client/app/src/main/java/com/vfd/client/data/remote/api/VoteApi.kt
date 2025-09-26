package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.VoteDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VoteApi {

    @POST("api/votes/my")
    suspend fun createVote(
        @Body voteDto: VoteDtos.VoteCreate
    ): VoteDtos.VoteResponse

    @GET("api/votes/my")
    suspend fun getVotes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "voteId,asc"
    ): PageResponse<VoteDtos.VoteResponse>

    @PATCH("api/votes/my/{voteId}")
    suspend fun updateVote(
        @Path("voteId") voteId: Int,
        @Body voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse
}