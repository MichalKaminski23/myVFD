package com.vfd.server.controllers

import com.vfd.server.dtos.FirefighterDto
import com.vfd.server.services.FirefighterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/firefighters")
class FirefighterController(private val firefighterService: FirefighterService) {

    @PostMapping
    fun createFirefighter(
        @RequestParam userId: Int,
        @RequestParam firedepartmentId: Int,
        @RequestParam roleName: String
    ): ResponseEntity<FirefighterDto> {
        val createdFirefighter = firefighterService.createFirefighter(userId, firedepartmentId, roleName)
        return ResponseEntity.ok(createdFirefighter)
    }

    @GetMapping("/{id}")
    fun getFirefighterById(@PathVariable id: Int): ResponseEntity<FirefighterDto> {
        val firefighter = firefighterService.getFirefighterById(id)
        return ResponseEntity.ok(firefighter)
    }

    // Przypisywanie roli strażakowi
    @PatchMapping("/{id}/role/{roleName}")
    fun updateFirefighterRole(
        @PathVariable id: Int,
        @PathVariable roleName: String
    ): ResponseEntity<FirefighterDto> {
        val updatedFirefighter = firefighterService.updateFirefighterRole(id, roleName)
        return ResponseEntity.ok(updatedFirefighter)
    }

    // Usuwanie roli strażakowi
    @DeleteMapping("/{id}/role/{roleName}")
    fun removeRoleFromFirefighter(
        @PathVariable id: Int,
    ): ResponseEntity<FirefighterDto> {
        val updatedFirefighter = firefighterService.removeRoleFromFirefighter(id)
        return ResponseEntity.ok(updatedFirefighter)
    }
}
