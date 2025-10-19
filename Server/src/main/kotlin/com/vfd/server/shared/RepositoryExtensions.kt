package com.vfd.server.shared

import com.vfd.server.entities.*
import com.vfd.server.exceptions.ForbiddenException
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.repositories.*

fun AddressRepository.findByIdOrThrow(addressId: Int): Address =
    findById(addressId).orElseThrow {
        ResourceNotFoundException("Address", "id", addressId)
    }

fun UserRepository.findByEmailOrThrow(emailAddress: String): User =
    findByEmailAddressIgnoreCase(emailAddress)
        ?: throw ResourceNotFoundException("User", "email", emailAddress)

fun UserRepository.assertNotExistsByEmail(emailAddress: String) {
    if (findByEmailAddressIgnoreCase(emailAddress) != null) {
        throw ResourceConflictException("User", "email address", emailAddress)
    }
}

fun UserRepository.assertNotExistsByPhone(phoneNumber: String) {
    if (findByPhoneNumber(phoneNumber) != null) {
        throw ResourceConflictException("User", "phone number", phoneNumber)
    }
}

fun UserRepository.findByIdOrThrow(userId: Int): User =
    findById(userId)
        .orElseThrow { ResourceNotFoundException("User", "id", userId) }

fun FirefighterRepository.findByIdOrThrow(firefighterId: Int): Firefighter =
    findById(firefighterId).orElseThrow {
        ResourceNotFoundException("Firefighter", "id", firefighterId)
    }

fun FirefighterRepository.assertNotExistsByUserId(firefighterId: Int) {
    if (existsById(firefighterId)) {
        throw ResourceConflictException("Firefighter", "id", firefighterId)
    }
}

fun Firefighter.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot modify firefighters of another firedepartment.")
    }
}

fun Firefighter.requireFiredepartment(): Firedepartment =
    this.firedepartment ?: throw ResourceNotFoundException(
        "Firefighter", "firedepartment", firedepartment!!.firedepartmentId!!
    )

fun Firefighter.requireFiredepartmentId(): Int =
    this.firedepartment?.firedepartmentId
        ?: throw ResourceNotFoundException("Firefighter", "firedepartment", firedepartment!!.firedepartmentId!!)

fun FiredepartmentRepository.findByIdOrThrow(firedepartmentId: Int): Firedepartment =
    findById(firedepartmentId).orElseThrow {
        ResourceNotFoundException("Firedepartment", "id", firedepartmentId)
    }

fun FiredepartmentRepository.assertNotExistsByName(name: String) {
    if (findByNameIgnoreCase(name) != null) {
        throw ResourceConflictException("Firedepartment", "name", name)
    }
}

fun FiredepartmentRepository.assertNameNotUsedByOther(name: String, firedepartmentId: Int) {
    val existing = findByNameIgnoreCase(name)
    if (existing != null && existing.firedepartmentId != firedepartmentId) {
        throw ResourceConflictException("Firedepartment", "name", name)
    }
}

fun AssetRepository.findByIdOrThrow(assetId: Int): Asset =
    findById(assetId).orElseThrow {
        ResourceNotFoundException("Asset", "id", assetId)
    }

fun Asset.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot modify assets of another firedepartment.")
    }
}

fun AssetTypeRepository.findByIdOrThrow(assetType: String): AssetType =
    findById(assetType).orElseThrow {
        ResourceNotFoundException("Asset's type", "code", assetType)
    }

fun AssetTypeRepository.assertNotExistsByAssetType(assetType: String) {
    if (existsByAssetType(assetType)) {
        throw ResourceConflictException("Asset's type", "code", assetType)
    }
}

fun EventRepository.findByIdOrThrow(eventId: Int): Event =
    findById(eventId).orElseThrow {
        ResourceNotFoundException("Event", "id", eventId)
    }

fun Event.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot modify assets of another firedepartment.")
    }
}

fun FirefighterActivityRepository.findByIdOrThrow(firefighterActivityId: Int): FirefighterActivity =
    findById(firefighterActivityId).orElseThrow {
        ResourceNotFoundException("Firefighter's activity", "id", firefighterActivityId)
    }

fun FirefighterActivity.requireSameFirefighter(firefighterId: Int) {
    if (this.firefighter?.firefighterId != firefighterId) {
        throw ForbiddenException("You cannot modify assets of another firefighters.")
    }
}

fun FirefighterActivityTypeRepository.findByIdOrThrow(firefighterActivityType: String): FirefighterActivityType =
    findById(firefighterActivityType).orElseThrow {
        ResourceNotFoundException("Firefighter's activity type", "code", firefighterActivityType)
    }

fun FirefighterActivityTypeRepository.assertNotExistsByActivityType(firefighterActivityType: String) {
    if (existsByFirefighterActivityType(firefighterActivityType)) {
        throw ResourceConflictException("Firefighter's activity type", "code", firefighterActivityType)
    }
}

fun InspectionRepository.findByIdOrThrow(inspectionId: Int): Inspection =
    findById(inspectionId).orElseThrow {
        ResourceNotFoundException("Inspection", "id", inspectionId)
    }

fun Inspection.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.asset?.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot add/modify inspections of another firedepartment.")
    }
}

fun InspectionTypeRepository.findByIdOrThrow(inspectionType: String): InspectionType =
    findById(inspectionType).orElseThrow {
        ResourceNotFoundException("Inspection's type", "code", inspectionType)
    }

fun InspectionTypeRepository.assertNotExistsByInspectionType(inspectionType: String) {
    if (existsByInspectionType(inspectionType)) {
        throw ResourceConflictException("Inspection's type", "code", inspectionType)
    }
}

fun InvestmentProposalRepository.findByIdOrThrow(investmentProposalId: Int): InvestmentProposal =
    findById(investmentProposalId).orElseThrow {
        ResourceNotFoundException("Investment proposal", "id", investmentProposalId)
    }

fun InvestmentProposal.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot add/view/modify investment's proposal of another firedepartment.")
    }
}

fun OperationRepository.findByIdOrThrow(operationId: Int): Operation =
    findById(operationId).orElseThrow {
        ResourceNotFoundException("Operation", "id", operationId)
    }

fun Operation.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot modify operations of another firedepartment.")
    }
}

fun OperationTypeRepository.findByIdOrThrow(operationType: String): OperationType =
    findById(operationType).orElseThrow {
        ResourceNotFoundException("Operation's type", "code", operationType)
    }

fun OperationTypeRepository.assertNotExistsByOperationType(operationType: String) {
    if (existsByOperationType(operationType)) {
        throw ResourceConflictException("Operation's type", "code", operationType)
    }
}

fun VoteRepository.findByIdOrThrow(voteId: Int): Vote =
    findById(voteId).orElseThrow {
        ResourceNotFoundException("Vote", "id", voteId)
    }

fun Vote.requireSameFiredepartment(firedepartmentId: Int) {
    if (investmentProposal?.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("You cannot view/modify votes for proposals from another firedepartment.")
    }
}

fun Vote.requireSameFirefighter(firefighterId: Int) {
    if (firefighter?.firefighterId != firefighterId) {
        throw ForbiddenException("You cannot view/modify votes for proposals from another firefighters.")
    }
}

fun VoteRepository.assertNotExistsByFirefighter(
    investmentProposalId: Int,
    firefighterId: Int
) {
    if (existsByInvestmentProposalInvestmentProposalIdAndFirefighterFirefighterId(
            investmentProposalId,
            firefighterId
        )
    ) {
        throw ResourceConflictException(
            "Vote",
            "investment proposal's id and firefighter's id",
            "$investmentProposalId and $firefighterId"
        )
    }
}