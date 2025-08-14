package com.vfd.server.repositories

import com.vfd.server.entities.Vote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VoteRepository : JpaRepository<Vote, Int> {

    fun existsByProposal_InvestmentProposalIdAndFirefighter_FirefighterId(
        investmentProposalId: Int,
        firefighterId: Int
    ): Boolean
}