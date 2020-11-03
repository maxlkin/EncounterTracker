package encounter.tracker.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.db.Database
import encountertrackerdb.MainQueries

object Database {
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:database/encounter_tracker.db")
        Database.Schema.create(driver)
    }

    fun getConnection(): JdbcSqliteDriver {
        return JdbcSqliteDriver("jdbc:sqlite:database/encounter_tracker.db")
    }

    fun query(): MainQueries {
        //val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:database/encounter_tracker.db")
        val database = Database(this.getConnection())
        return database.mainQueries
    }

    fun getTestConnection(): JdbcSqliteDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Database.Schema.create(driver)
        return driver
    }

    fun testQuery(driver: SqlDriver): MainQueries {
        val database = Database(driver)
        return database.mainQueries
    }
}