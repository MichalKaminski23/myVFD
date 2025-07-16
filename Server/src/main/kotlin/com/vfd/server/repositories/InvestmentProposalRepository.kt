package com.vfd.server.repositories

import com.vfd.server.entities.InvestmentProposal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvestmentProposalRepository : JpaRepository<InvestmentProposal, Integer>