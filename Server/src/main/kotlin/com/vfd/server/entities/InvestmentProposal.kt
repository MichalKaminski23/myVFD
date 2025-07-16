package com.vfd.server.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "InvestmentProposals")
class InvestmentProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
    var proposalId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "firedepartment_id", nullable = false)
    lateinit var firedepartment: Firedepartment

    @Column(name = "description", length = 512, nullable = true)
    lateinit var description: String

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    lateinit var amount: BigDecimal

    @Column(name = "submission_date", nullable = false)
    lateinit var submissionDate: LocalDateTime

    @Column(name = "status", length = 16, nullable = false)
    lateinit var status: String

    @OneToMany(mappedBy = "proposal", cascade = [CascadeType.ALL], orphanRemoval = true)
    var votes: MutableList<Vote> = mutableListOf()
}