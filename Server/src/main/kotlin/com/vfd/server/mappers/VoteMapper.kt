package com.vfd.server.mappers

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.entities.Vote
import org.mapstruct.*

@Mapper(componentModel = "spring", uses = [InvestmentProposalMapper::class, FirefighterMapper::class])
interface VoteMapper {

    fun toVoteDto(vote: Vote): VoteDtos.VoteResponse

    @Mapping(target = "voteId", ignore = true)
    @Mapping(target = "investmentProposal", ignore = true)
    @Mapping(target = "firefighter", ignore = true)
    @Mapping(target = "voteDate", ignore = true)
    fun toVoteEntity(voteDto: VoteDtos.VoteCreate): Vote

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "investmentProposal", ignore = true)
    @Mapping(target = "firefighter", ignore = true)
    @Mapping(target = "voteDate", ignore = true)
    fun patchVote(voteDto: VoteDtos.VotePatch, @MappingTarget vote: Vote)
}