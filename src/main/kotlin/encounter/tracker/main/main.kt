package encounter.tracker.main
import encounter.tracker.database.Database
import encounter.tracker.views.Root
import encounter.tracker.views.Style
import tornadofx.App
import tornadofx.launch

class EncounterTracker: App(Root::class, Style::class)

fun main(args: Array<String>) {
    // Launching App (TornadoFX)
    launch<EncounterTracker>(args)
}