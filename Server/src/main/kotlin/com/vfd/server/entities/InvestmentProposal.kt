package com.vfd.server.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "InvestmentProposals")
class InvestmentProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_proposal_id")
    var investmentProposalId: Int? = null

    @ManyToOne
    @JoinColumn(name = "firedepartment_id")
    var firedepartment: Firedepartment? = null

    @Column(name = "description", length = 512)
    var description: String? = null

    @Column(name = "amount", precision = 10, scale = 2)
    var amount: BigDecimal? = null

    @Column(name = "submission_date")
    var submissionDate: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16)
    var status: InvestmentProposalStatus? = null

    @OneToMany(mappedBy = "investmentProposal", cascade = [CascadeType.ALL], orphanRemoval = true)
    var votes: MutableList<Vote> = mutableListOf()
}