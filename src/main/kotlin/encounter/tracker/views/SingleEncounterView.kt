package encounter.tracker.views

import encounter.tracker.controllers.EncounterController
import encountertrackerdb.Encounter
import encountertrackerdb.SelectCharactersIn
import encountertrackerdb.SelectCharactersNotIn
import encountertrackerdb.SelectNPCNotIn
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TextField
import tornadofx.*

class SingleEncounterView :  View() {
    private val controller: EncounterController by inject()

    private var encounterName : SimpleStringProperty = SimpleStringProperty("Unset")
    private var id : SimpleLongProperty = SimpleLongProperty(0)
    var idField : TextField by singleAssign()
    var nameField : TextField by singleAssign()


    fun setEncounter(encounterID: Long) {
        val query = controller.getEncounterByID(encounterID)
        id.set(encounterID)
        encounterName.set(query.name)
        println(controller.getEncounterCharacters(encounterID))
        tableData.setAll(controller.getEncounterCharacters(encounterID))
        characterData.setAll(controller.getCharacterList(encounterID))
        npcData.setAll(controller.getNpcList(encounterID))
    }

    val tableData = controller.getEncounterCharacters(this.id.value)
    val characterData = controller.getCharacterList(this.id.value)
    val npcData = controller.getNpcList(this.id.value)

    override val root = vbox(10) {
        addClass(Style.view)

        // Heading
        label(encounterName) {
            style {
                fontSize = 18.px
            }
        }

        // Encounter tracker
        tableview(tableData) {
            readonlyColumn("ID", SelectCharactersIn::id)
            readonlyColumn("Name", SelectCharactersIn::name)
        }

        // Character adder
        hbox(20) {
            vbox(10){
                label("Add Player Characters")
                tableview(characterData) {
                    readonlyColumn("ID", SelectCharactersNotIn::id)
                    readonlyColumn("Name", SelectCharactersNotIn::name)
                    readonlyColumn("Action", SelectCharactersNotIn::id).cellFormat {
                        graphic = hbox(spacing = 5) {
                            button("Add") {
                                action {
                                    println("Adding character $it to encounter ${this@SingleEncounterView.id.value}")
                                    controller.addCharacterToEncounter(it, this@SingleEncounterView.id.value)
                                    refreshEncounterCharacters()
                                    refreshCharacters()
                                }
                            }
                        }
                    }
                }
            }
            vbox(10){
                label("Add Non Player Characters")
                tableview(npcData) {
                    readonlyColumn("ID", SelectNPCNotIn::id)
                    readonlyColumn("Name", SelectNPCNotIn::name)
                    readonlyColumn("Action", SelectNPCNotIn::id).cellFormat {
                        graphic = hbox(spacing = 5) {
                            button("Add") {
                                action {
                                    println("Adding character $it to encounter ${this@SingleEncounterView.id.value}")
                                    controller.addCharacterToEncounter(it, this@SingleEncounterView.id.value)
                                    refreshEncounterCharacters()
                                    refreshNPCharacters()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun refreshEncounterCharacters() {
        println("Refreshing characters on encounter ${this.id.value}")
        tableData.setAll(controller.getEncounterCharacters(this.id.value))
    }

    private fun refreshCharacters() {
        characterData.setAll(controller.getCharacterList(this.id.value))
    }

    private fun refreshNPCharacters() {
        npcData.setAll(controller.getNpcList(this.id.value))
    }
}