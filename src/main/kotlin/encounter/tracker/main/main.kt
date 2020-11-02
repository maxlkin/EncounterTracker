package encounter.tracker.main
import encounter.tracker.database.Database
import encounter.tracker.views.Root
import encounter.tracker.views.Style
import tornadofx.App
import tornadofx.launch

class EncounterTracker: App(Root::class, Style::class)

fun main(args: Array<String>) {
    //
    Database.query().insertCharacter("Johnny Two Fingers", 12, 1, 20, 20)
    Database.query().updateCharacterByID("Johnny Three Fingers", 12, 1, 20, 20, 3)
    println(Database.query().selectCharacterByID(1).executeAsList())
    println(Database.query().selectAllCharacters().executeAsList())
    // Launching App (TornadoFX)
    launch<EncounterTracker>(args)
}