package encounter.tracker.views

import encounter.tracker.controllers.EncounterController
import encountertrackerdb.*
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import tornadofx.*

class SingleEncounterView :  View() {
    private val controller: EncounterController by inject()

    private var encounterName : SimpleStringProperty = SimpleStringProperty("Unset")
    private var id : SimpleLongProperty = SimpleLongProperty(0)

    private val tableData = controller.getEncounterCharacters(this.id.value)
    private val characterData = controller.getCharacterList(this.id.value)
    private val npcData = controller.getNpcList()

    override val root = vbox(10) {
        addClass(Style.view)

        // Heading
        label(encounterName) {
            style {
                fontSize = 18.px
            }
        }

        // Encounter tracker
        // Controls
        hbox(20) {
            button("Roll Initiative") {
                action {
                    controller.rollInitiative(this@SingleEncounterView.id.value)
                    refreshEncounterCharacters()
                }
            }
            button("Roll Initiative Where Empty") {
                action {
                    controller.rollNulledInitiative(this@SingleEncounterView.id.value)
                    refreshEncounterCharacters()
                }
            }
        }
        tableview(tableData) {
            isEditable = true
            readonlyColumn("Initiative", SelectCharactersIn::initiative)
            readonlyColumn("Name", SelectCharactersIn::name)
            readonlyColumn("Health", SelectCharactersIn::id).cellFormat {
                graphic = hbox(spacing = 5) {
                    textfield {
                        var health = controller.getCharacterHealth(it)
                        text = health.toString()
                        action {
                            try {
                                health = this.text.toLong()
                                controller.setCharacterHealth(health, it)
                                refreshEncounterCharacters()
                            } catch (e: NumberFormatException) {
                                alert(Alert.AlertType.ERROR, "Error updating health", "Character health not formatted correctly. Is it blank or contains letters?")
                            }

                        }
                    }
                }
            }

            readonlyColumn("Max Health", SelectCharactersIn::max_health)
            readonlyColumn("Template", SelectCharactersIn::type)
            readonlyColumn("Action", SelectCharactersIn::id_).cellFormat {
                graphic = hbox(spacing = 5) {
                    button("Delete") {
                        action {
                            controller.removeCharacterFromEncounter(it)
                            refreshEncounterCharacters()
                            refreshCharacters()
                        }
                    }
                }
            }
        }

        hbox(20) {
            vbox(10){
                // Character adder
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
                // NPC adder
                label("Add Non Player Characters")
                tableview(npcData) {
                    readonlyColumn("ID", SelectAllCharactersWithTemplate::id)
                    readonlyColumn("Name", SelectAllCharactersWithTemplate::name)
                    readonlyColumn("Action", SelectAllCharactersWithTemplate::id).cellFormat {
                        graphic = hbox(spacing = 5) {
                            button("Add") {
                                action {
                                    println("Adding character $it to encounter ${this@SingleEncounterView.id.value}")
                                    controller.addCharacterToEncounter(it, this@SingleEncounterView.id.value, true)
                                    refreshEncounterCharacters()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setEncounter(encounterID: Long) {
        val encounter = controller.getEncounterByID(encounterID)
        id.set(encounterID)
        encounterName.set(encounter.name)
        tableData.setAll(controller.getEncounterCharacters(encounterID))
        characterData.setAll(controller.getCharacterList(encounterID))
        npcData.setAll(controller.getNpcList())
    }

    private fun refreshEncounterCharacters() {
        tableData.setAll(controller.getEncounterCharacters(this.id.value))
    }

    private fun refreshCharacters() {
        characterData.setAll(controller.getCharacterList(this.id.value))
    }
}