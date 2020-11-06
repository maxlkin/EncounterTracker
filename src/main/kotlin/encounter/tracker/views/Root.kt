package encounter.tracker.views

import tornadofx.*

class Root: View() {
    override val root = borderpane {
        minHeight = 400.0
        minWidth = 800.0
        title = "Mobile App Development: Assignment 1"
        left<Sidebar>()
    }

    fun setCharacterView() {
        root.center<CharacterView>()
    }

    fun setNPCView() {
        root.center<NPCharacterView>()
    }

    fun setTemplateView() {
        root.center<TemplateView>()
    }

    fun setEncounterView() {
        root.center<EncounterView>()
    }
}