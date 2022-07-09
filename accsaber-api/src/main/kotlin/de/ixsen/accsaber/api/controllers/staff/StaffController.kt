package de.ixsen.accsaber.api.controllers.staff

import de.ixsen.accsaber.api.dtos.staff.StaffUserDto
import de.ixsen.accsaber.business.staff.StaffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("staff")
class StaffController(private val staffService: StaffService) {
    @PostMapping
    fun createNewUser(@RequestBody staffUserDto: StaffUserDto): ResponseEntity<*> {
        staffService.saveNewUser(staffUserDto.username, staffUserDto.password)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping
    fun checkLogin(): ResponseEntity<*> {
        return ResponseEntity.noContent().build<Any>()
    }
}