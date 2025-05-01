package me.ezra_home.daily_snapshot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DailySnapshotApplication

fun main(args: Array<String>) {
	runApplication<DailySnapshotApplication>(*args)
}
