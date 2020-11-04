package encounter.tracker.views

import tornadofx.*

class Sidebar : View() {
    private val parentClass: Root by inject()

    override val root = vbox {
        minWidth = 200.0
        this.addClass(Style.sidebar)
        button("Characters") {
            action {
                parentClass.setCharacterView()
            }
        }
        button("Templates") {
            action {
                parentClass.setTemplateView()
            }
        }
        button("NPC's") {
            action {
                parentClass.setNPCView()
            }
        }
        button("Encounters") {
            action {
                parentClass.setEncounterView()
            }
        }
    }
}
