package encounter.tracker.views

import tornadofx.View
import tornadofx.label
import tornadofx.vbox

class EncounterView :  View() {
    override val root = vbox {
        label("Encounter view")
    }
}