package com.vfd.server.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Votes")
class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    var voteId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "proposal_id", nullable = false)
    lateinit var proposal: InvestmentProposal

    @ManyToOne(optional = false)
    @JoinColumn(name = "firefighter_id", nullable = false)
    lateinit var firefighter: Firefighter

    @Column(name = "value", nullable = false)
    var value: Boolean? = null

    @Column(name = "vote_date", nullable = false)
    lateinit var voteDate: LocalDateTime
}