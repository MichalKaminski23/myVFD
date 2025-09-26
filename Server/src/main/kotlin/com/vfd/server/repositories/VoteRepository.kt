package com.vfd.server.repositories

import com.vfd.server.entities.Vote
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VoteRepository : JpaRepository<Vote, Int> {

    fun findAllByInvestmentProposalInvestmentProposalId(
        investmentProposalId: Int,
        pageable: Pageable
    ): Page<Vote>

    fun findByInvestmentProposalInvestmentProposalIdAndFirefighterFirefighterId(
        investmentProposalId: Int,
        firefighterId: Int
    ): Vote?

    fun existsByInvestmentProposalInvestmentProposalIdAndFirefighterFirefighterId(
        investmentProposalId: Int,
        firefighterId: Int
    ): Boolean

    fun countByInvestmentProposalInvestmentProposalId(investmentProposalId: Int): Int

    fun countByInvestmentProposalInvestmentProposalIdAndVoteValue(
        investmentProposalId: Int,
        voteValue: Boolean
    ): Int
}