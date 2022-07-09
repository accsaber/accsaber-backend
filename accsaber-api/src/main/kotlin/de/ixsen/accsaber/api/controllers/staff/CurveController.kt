package de.ixsen.accsaber.api.controllers.staff

import de.ixsen.accsaber.api.dtos.staff.CurveDto
import de.ixsen.accsaber.business.staff.CurveService
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Disabled for prod environment, needs to be enabled via config
 */
@RestController
@RequestMapping("curve")
@ConditionalOnExpression("\${accsaber.enable-curve-query}")
class CurveController(
    private val curveService: CurveService
) {


    @PostMapping
    fun saveNewCurve(@RequestBody curveDto: CurveDto): ResponseEntity<Any> {
        this.curveService.saveNewCurve(curveDto.curve)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun resetCurve(@RequestBody curveDto: CurveDto): ResponseEntity<Any> {
        this.curveService.resetCurve()
        return ResponseEntity.noContent().build()
    }
}