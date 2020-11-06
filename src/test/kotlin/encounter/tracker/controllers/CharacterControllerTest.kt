package encounter.tracker.controllers

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import encounter.tracker.controllers.CharacterController
import encounter.tracker.models.CharacterModel
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CharacterControllerTest {
    /**
     * Single database in memory connection for testing.
     */
    private lateinit var driver: JdbcSqliteDriver

    private lateinit var controller: CharacterController

    /**
     * Setting up connection and inserting test data.
     */
    @Before
    fun setup() {
        driver = Database.getTestConnection()
        controller = CharacterController()
        Database.query(driver).insertCharacter("TestChar1", 10, 2, 10, 6, null, null)
        Database.query(driver).insertCharacter("TestChar2", 5, -2, 10, 4, null, null)
    }

    @Test
    fun testGetCharacterList() {
        val characters = controller.getCharacterList(driver)
        assertEquals("TestChar1", characters[0].name)
        assertEquals("TestChar2", characters[1].name)
    }

    @Test
    fun testGetFilteredCharacterList() {
        val characterModel = CharacterModel(0, "TestChar2", 5, -2, 10, 4, null)
        val characters = controller.getFilteredCharacterList(characterModel, driver)
        assertEquals("TestChar2", characters[0].name)
    }

    @Test
    fun testDeleteCharacter() {
        controller.deleteCharacter(1, driver)
        val characters = controller.getCharacterList(driver)
        assertEquals(1, characters.size)
    }

    @Test
    fun testAddCharacter() {
        val characterModel = CharacterModel(0, "InsertCharTest", 10, -2, 20, 10, null)
        controller.addCharacter(characterModel, driver)
        val characters = controller.getCharacterList(driver)
        assertEquals("InsertCharTest", characters[2].name)
    }

    @Test
    fun testUpdateCharacter() {
        val characterModel = CharacterModel(2, "UpdateCharTest", 10, -2, 20, 10, null)
        controller.updateCharacter(characterModel, driver)
        val characters = controller.getCharacterList(driver)
        assertEquals("UpdateCharTest", characters[1].name)
    }
}