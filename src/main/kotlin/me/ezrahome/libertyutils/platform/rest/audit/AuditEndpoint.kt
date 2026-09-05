package me.ezrahome.libertyutils.platform.rest.audit

import jakarta.websocket.server.PathParam
import me.ezrahome.libertyutils.dailysnapshot.business.DailySnapshotService
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionService
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("secured/audit")
class AuditEndpoint(
    private val dailySnapshotService: DailySnapshotService,
    private val transactionService: TransactionService
) {

    @GetMapping("snapshots")
    fun getDailySnapshotAuditRecords(@PathParam("recordId") recordId: String): Collection<MasterAuditDto> {
        return dailySnapshotService.getAuditRecords(recordId)
    }

    @GetMapping("transactions")
    fun getTransactionAuditRecords(@PathParam("recordId") recordId: String): Collection<MasterAuditDto> {
        return transactionService.getAuditRecords(recordId)
    }
}
