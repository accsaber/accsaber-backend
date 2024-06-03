package de.ixsen.accsaber.api.controllers

import de.ixsen.accsaber.business.jobs.JobService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneId

@RestController
@RequestMapping("status")
class StatusController(
    private val jobService: JobService
) {
    @GetMapping("last-update")
    fun lastUpdate(): String {
        val lastUpdate = this.jobService.getLastUpdate()
        return lastUpdate.toInstant(ZoneId.systemDefault().rules.getOffset(lastUpdate)).toString()
    }
}
