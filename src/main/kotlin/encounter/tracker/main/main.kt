package encounter.tracker.main
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.db.Database
import encounter.tracker.views.Root
import encounter.tracker.views.Style
import encountertrackerdb.MainQueries
import tornadofx.App
import tornadofx.launch

class EncounterTracker: App(Root::class, Style::class)

fun main(args: Array<String>) {
    // Getting database connection.

    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    Database.Schema.create(driver)
    val database = Database(driver)
    val mainQueries: MainQueries = database.mainQueries
    mainQueries.insertCharacter("Johnny Two Fingers", 12, 1, 20, 20)
    mainQueries.updateCharacterByID("Johnny Three Fingers", 12, 1, 20, 20, 3)
    println(mainQueries.selectCharacterByID(1).executeAsList())
    println(mainQueries.selectAllCharacters().executeAsList())
    // Creating Schema
    // Launching App (TornadoFX)
    launch<EncounterTracker>(args)
}