package encounter.tracker.controllers

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class EncounterControllerTest {
    /**
     * Single database in memory connection for testing.
     */
    private lateinit var driver: JdbcSqliteDriver

    private lateinit var controller: EncounterController

    /**
     * Setting up connection and inserting test data.
     */
    @Before
    fun setup() {
        driver = Database.getTestConnection()
        controller = EncounterController()
        // Add encounter
        Database.query(driver).insertEncounter("TestEncounter1" )
        // Add players
        Database.query(driver).insertCharacter("TestChar1", 10, 2, 10, 6, null, null)
        Database.query(driver).insertCharacter("TestChar2", 10, 2, 10, 6, null, null)
        // Add player to encounter
        Database.query(driver).addCharacterToEncounter(1, 1 )
        // Add NPC
        Database.query(driver).insertTemplate("TestTemplate1", "Test Template")
        Database.query(driver).insertCharacter("TestNPC1", 5, -2, 10, 4, 1, null)
    }

    @Test
    fun testGetCharacterList() {
        val characters = controller.getCharacterList(1, driver)
        assertEquals(1, characters.size)
        assertEquals("TestChar2", characters[0].name)
    }

    @Test
    fun testGetNPCList() {
        val characters = controller.getNpcList(driver)
        assertEquals(1, characters.size)
        assertEquals("TestNPC1", characters[0].name)
    }

    @Test
    fun testGetEncounterCharacters() {
        val characters = controller.getEncounterCharacters(1, driver)
        assertEquals(1, characters.size)
        assertEquals("TestChar1", characters[0].name)
    }
}