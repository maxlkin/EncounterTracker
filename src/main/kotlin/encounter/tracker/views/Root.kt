package encounter.tracker.views

import tornadofx.*

class Root: View() {
    override val root = borderpane {
        minHeight = 400.0
        minWidth = 800.0
        top<Header>()
        left<Sidebar>()
        bottom<Footer>()
    }

    fun setCharacterView() {
        root.center<CharacterView>()
    }

    fun setNPCView() {
        root.center<NPCView>()
    }

    fun setEncounterView() {
        root.center<EncounterView>()
    }
}