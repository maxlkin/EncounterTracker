package encounter.tracker.sql

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class QueryTest {
    /**
     * Single database in memory connection for testing.
     */
    private lateinit var driver: JdbcSqliteDriver

    /**
     * Setting up connection and inserting test data.
     */
    @Before
    fun setup() {
        driver = Database.getTestConnection()
        Database.query(driver).insertCharacter("TestChar1", 10, 2, 10, 6, null)
        Database.query(driver).insertCharacterTemplate("Goblin", "A testing goblin template.")
        Database.query(driver).insertCharacter("TestChar2", 5, -2, 10, 4, 1)
    }

    /**
     * Testing selecting all characters.
     */
    @Test
    fun testSelectCharacters() {
        val characters = Database.query(driver).selectAllCharacters().executeAsList()
        assertEquals(2, characters.size)
        assertEquals("TestChar1", characters[0].name)
        assertEquals("TestChar2", characters[1].name)
    }

    /**
     * Testing selecting characters by ID.
     */
    @Test
    fun testSelectCharacterByID() {
        val character = Database.query(driver).selectCharacterByID(1).executeAsOne()
        assertEquals("TestChar1", character.name)
    }

    /**
     * Test inserting characters.
     */
    @Test
    fun testInsertCharacter() {
        Database.query(driver).insertCharacter("InsertCharTest", 0, 0, 0, 0, null)
        val character = Database.query(driver).selectCharacterByID(3).executeAsOne()
        assertEquals("InsertCharTest", character.name)
    }

    /**
     * Test updating characters.
     */
    @Test
    fun updateCharacterByID() {
        Database.query(driver).updateCharacterByID("UpdateCharTest", 10, -1, 9, 8, 2)
        val character = Database.query(driver).selectCharacterByID(2).executeAsOne()
        assertEquals("UpdateCharTest", character.name)
        assertEquals(10, character.armor_class)
        assertEquals(-1, character.initiative_modifier)
        assertEquals(9, character.max_health)
        assertEquals(8, character.current_health)
    }

    /**
     * Test deleting characters.
     */
    @Test
    fun deleteCharacterByID() {
        Database.query(driver).deleteCharacterByID(2)
        val character = Database.query(driver).selectCharacterByID(2).executeAsOneOrNull()
        assertEquals(null, character)
    }

    /**
     * Test selecting only player characters.
     */
    @Test
    fun selectAllPlayerCharacters() {
        val characters = Database.query(driver).selectAllPlayerCharacters().executeAsList()
        assertEquals(1, characters.size)
    }

    /**
     * Test selecting filtered player characters.
     */
    @Test
    fun selectPlayerCharacters() {
        val characters = Database.query(driver).selectPlayerCharacters(
            "%TestChar%",
            "%%",
            "%%",
            "%%",
            "%%").executeAsList()
        assertEquals(1, characters.size)
    }

    /**
     * Test selecting all non player characters.
     */
    @Test
    fun selectAllNPCharacters() {
        val characters = Database.query(driver).selectAllCharactersWithTemplate().executeAsList()
        assertEquals(1, characters.size)
    }
}