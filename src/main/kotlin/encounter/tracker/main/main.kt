package encounter.tracker.main
import encounter.tracker.database.Database
import encounter.tracker.views.Root
import encounter.tracker.views.Style
import tornadofx.App
import tornadofx.launch
import mu.KotlinLogging
class EncounterTracker: App(Root::class, Style::class)
private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    // Checking database setup
    //val driver = Database.getConnection()
    try {
        Database.setup()
    } catch (e: Exception) {
        logger.info("Skipping database setup. May already exist.")
    }
    // Launching App (TornadoFX)
    launch<EncounterTracker>(args)
}