package me.ezrahome.libertyutils.dailysnapshot.rest

import jakarta.websocket.server.PathParam
import me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core.DailySnapshotInsertDto
import me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core.DailySnapshotResponseDto
import me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core.DailySnapshotService
import me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core.DailySnapshotUpdateDto
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("secured/snapshot")
class DailySnapshotEndpoint(private val dailySnapshotService: DailySnapshotService) {

    @GetMapping
    fun getSnapshotsBetweenDates(
        @PathParam("startDate") startDate: String,
        @PathParam("endDate") endDate: String
    ): Collection<DailySnapshotResponseDto> {

        return dailySnapshotService.getSnapshotsForDates(startDate, endDate)
    }

    @GetMapping("audit")
    fun getAuditRecords(@PathParam("snapshotId") snapshotId: String): Collection<MasterAuditDto> {
        return dailySnapshotService.getAuditRecords(snapshotId)
    }

    @PostMapping
    fun createDailySnapshot(@RequestBody dailySnapshotInsertDto: DailySnapshotInsertDto): DailySnapshotResponseDto {
        return dailySnapshotService.createDailySnapshot(dailySnapshotInsertDto)
    }

    @PutMapping
    fun updateDailySnapshot(@RequestBody dailySnapshotUpdateDto: DailySnapshotUpdateDto): DailySnapshotResponseDto {
        return dailySnapshotService.updateDailySnapshot(dailySnapshotUpdateDto)
    }

    @DeleteMapping("{id}")
    fun deleteSnapshot(@PathVariable("id") id: UUID?) {
        dailySnapshotService.deleteDailySnapshot(id!!)
    }
}
