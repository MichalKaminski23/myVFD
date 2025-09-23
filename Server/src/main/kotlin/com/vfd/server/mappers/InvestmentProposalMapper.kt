package com.vfd.server.mappers

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.entities.InvestmentProposal
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface InvestmentProposalMapper {

    @Mapping(target = "firedepartmentId", source = "firedepartment.firedepartmentId")
    fun toInvestmentProposalDto(investmentProposal: InvestmentProposal): InvestmentProposalDtos.InvestmentProposalResponse

    @Mapping(target = "investmentProposalId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "votes", ignore = true)
    fun toInvestmentProposalEntity(investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate): InvestmentProposal

    @Mapping(target = "investmentProposalId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "votes", ignore = true)
    fun toInvestmentProposalEntityDev(investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreateDev): InvestmentProposal

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "votes", ignore = true)
    fun patchInvestmentProposal(
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch,
        @MappingTarget investmentProposal: InvestmentProposal
    )
}