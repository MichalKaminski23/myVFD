package com.vfd.server.shared

import com.vfd.server.entities.*
import com.vfd.server.exceptions.ForbiddenException
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.repositories.*

fun AddressRepository.findByIdOrThrow(addressId: Int): Address =
    findById(addressId).orElseThrow {
        ResourceNotFoundException("address.address", "id", addressId)
    }

fun UserRepository.findByEmailOrThrow(emailAddress: String): User =
    findByEmailAddressIgnoreCase(emailAddress)
        ?: throw ResourceNotFoundException("user.user", "user.emailAddress", emailAddress)

fun UserRepository.assertNotExistsByEmail(emailAddress: String) {
    if (findByEmailAddressIgnoreCase(emailAddress) != null) {
        throw ResourceConflictException("user.user", "user.emailAddress", emailAddress)
    }
}

fun UserRepository.assertNotExistsByPhone(phoneNumber: String) {
    if (findByPhoneNumber(phoneNumber) != null) {
        throw ResourceConflictException("user.user", "user.phoneNumber", phoneNumber)
    }
}

fun UserRepository.findByIdOrThrow(userId: Int): User =
    findById(userId)
        .orElseThrow { ResourceNotFoundException("user.user", "id", userId) }

fun FirefighterRepository.findByIdOrThrow(firefighterId: Int): Firefighter =
    findById(firefighterId).orElseThrow {
        ResourceNotFoundException("firefighter.firefighter", "id", firefighterId)
    }

fun FirefighterRepository.assertNotExistsByUserId(firefighterId: Int) {
    if (existsById(firefighterId)) {
        throw ResourceConflictException("firefighter.firefighter", "id", firefighterId)
    }
}

fun Firefighter.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun Firefighter.requireFiredepartment(): Firedepartment =
    this.firedepartment ?: throw ResourceNotFoundException(
        "firefighter.firefighter", "firedepartment.firedepartment", firedepartment!!.firedepartmentId!!
    )

fun Firefighter.requireFiredepartmentId(): Int =
    this.firedepartment?.firedepartmentId
        ?: throw ResourceNotFoundException(
            "firefighter.firefighter",
            "firedepartment.firedepartment",
            firedepartment!!.firedepartmentId!!
        )

fun FiredepartmentRepository.findByIdOrThrow(firedepartmentId: Int): Firedepartment =
    findById(firedepartmentId).orElseThrow {
        ResourceNotFoundException("firedepartment.firedepartment", "id", firedepartmentId)
    }

fun FiredepartmentRepository.assertNotExistsByName(name: String) {
    if (findByNameIgnoreCase(name) != null) {
        throw ResourceConflictException("firedepartment.firedepartment", "item.name", name)
    }
}

fun FiredepartmentRepository.assertNameNotUsedByOther(name: String, firedepartmentId: Int) {
    val existing = findByNameIgnoreCase(name)
    if (existing != null && existing.firedepartmentId != firedepartmentId) {
        throw ResourceConflictException("firedepartment.firedepartment", "item.name", name)
    }
}

fun AssetRepository.findByIdOrThrow(assetId: Int): Asset =
    findById(assetId).orElseThrow {
        ResourceNotFoundException("asset.asset", "id", assetId)
    }

fun Asset.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun AssetTypeRepository.findByIdOrThrow(assetType: String): AssetType =
    findById(assetType).orElseThrow {
        ResourceNotFoundException("asset.type", "item.code", assetType)
    }

fun AssetTypeRepository.assertNotExistsByAssetType(assetType: String) {
    if (existsByAssetType(assetType)) {
        throw ResourceConflictException("asset.type", "item.code", assetType)
    }
}

fun EventRepository.findByIdOrThrow(eventId: Int): Event =
    findById(eventId).orElseThrow {
        ResourceNotFoundException("event.event", "id", eventId)
    }

fun Event.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun FirefighterActivityRepository.findByIdOrThrow(firefighterActivityId: Int): FirefighterActivity =
    findById(firefighterActivityId).orElseThrow {
        ResourceNotFoundException("firefighter.activity", "id", firefighterActivityId)
    }

//fun FirefighterActivity.requireSameFirefighter(firefighterId: Int) {
//    if (this.firefighter?.firefighterId != firefighterId) {
//        throw ForbiddenException("forbidden.text")
//    }
//}

fun FirefighterActivityTypeRepository.findByIdOrThrow(firefighterActivityType: String): FirefighterActivityType =
    findById(firefighterActivityType).orElseThrow {
        ResourceNotFoundException("firefighter.activity.type", "item.code", firefighterActivityType)
    }

fun FirefighterActivityTypeRepository.assertNotExistsByActivityType(firefighterActivityType: String) {
    if (existsByFirefighterActivityType(firefighterActivityType)) {
        throw ResourceConflictException("firefighter.activity.type", "item.code", firefighterActivityType)
    }
}

fun InspectionRepository.findByIdOrThrow(inspectionId: Int): Inspection =
    findById(inspectionId).orElseThrow {
        ResourceNotFoundException("inspection.inspection", "id", inspectionId)
    }

fun Inspection.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.asset?.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun InspectionTypeRepository.findByIdOrThrow(inspectionType: String): InspectionType =
    findById(inspectionType).orElseThrow {
        ResourceNotFoundException("inspection.inspection.type", "item.code", inspectionType)
    }

fun InspectionTypeRepository.assertNotExistsByInspectionType(inspectionType: String) {
    if (existsByInspectionType(inspectionType)) {
        throw ResourceConflictException("inspection.inspection.type", "item.code", inspectionType)
    }
}

fun InvestmentProposalRepository.findByIdOrThrow(investmentProposalId: Int): InvestmentProposal =
    findById(investmentProposalId).orElseThrow {
        ResourceNotFoundException("investment.investment", "id", investmentProposalId)
    }

fun InvestmentProposal.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun OperationRepository.findByIdOrThrow(operationId: Int): Operation =
    findById(operationId).orElseThrow {
        ResourceNotFoundException("operation.operation", "id", operationId)
    }

fun Operation.requireSameFiredepartment(firedepartmentId: Int) {
    if (this.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun OperationTypeRepository.findByIdOrThrow(operationType: String): OperationType =
    findById(operationType).orElseThrow {
        ResourceNotFoundException("operation.operation.type", "item.code", operationType)
    }

fun OperationTypeRepository.assertNotExistsByOperationType(operationType: String) {
    if (existsByOperationType(operationType)) {
        throw ResourceConflictException("operation.operation.type", "item.code", operationType)
    }
}

fun VoteRepository.findByIdOrThrow(voteId: Int): Vote =
    findById(voteId).orElseThrow {
        ResourceNotFoundException("vote.vote", "id", voteId)
    }

fun Vote.requireSameFiredepartment(firedepartmentId: Int) {
    if (investmentProposal?.firedepartment?.firedepartmentId != firedepartmentId) {
        throw ForbiddenException("forbidden.text")
    }
}

fun Vote.requireSameFirefighter(firefighterId: Int) {
    if (firefighter?.firefighterId != firefighterId) {
        throw ForbiddenException("forbidden.text")
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
            "vote.vote",
            "investment.firefighter",
            "$investmentProposalId and $firefighterId"
        )
    }
}