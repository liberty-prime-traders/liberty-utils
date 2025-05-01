package me.ezra_home.daily_snapshot

import jakarta.websocket.server.PathParam
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@CrossOrigin
@RestController
@RequestMapping("secured/snapshot")
class DailySnapshotEndpoint(private val dailySnapshotService: DailySnapshotService) {

    @GetMapping
    fun getSnapshotsBetweenDates(
        @PathParam("startDate") startDate: String,
        @PathParam("endDate") endDate: String
    ): List<DailySnapshotResponseDto> {

        return dailySnapshotService.getSnapshotsForDates(startDate, endDate)
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
