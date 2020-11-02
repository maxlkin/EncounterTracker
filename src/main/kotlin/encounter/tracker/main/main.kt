package encounter.tracker.main
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.db.Database
import encounter.tracker.views.Root
import tornadofx.App
import tornadofx.launch

class EncounterTracker: App(Root::class)

fun main(args: Array<String>) {
    // Getting database connection.
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    // Creating Schema
    Database.Schema.create(driver)
    // Launching App (TornadoFX)
    launch<EncounterTracker>(args)
}