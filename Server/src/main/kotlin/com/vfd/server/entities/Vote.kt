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

    @ManyToOne
    @JoinColumn(name = "proposal_id")
    var proposal: InvestmentProposal? = null

    @ManyToOne
    @JoinColumn(name = "firefighter_id")
    var firefighter: Firefighter? = null

    @Column(name = "vote_value")
    var voteValue: Boolean? = null

    @Column(name = "vote_date")
    var voteDate: LocalDateTime? = null
}