package db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.dp.hammer.superhome.db.AppDatabase
import ua.dp.hammer.superhome.db.dao.EnvSensorSettingsDao
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRow
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.db.entities.EnvSensorSettings

@RunWith(AndroidJUnit4::class)
class EnvSensorSettingsTest {
    private lateinit var database: AppDatabase
    private lateinit var envSensorSettingsDao: EnvSensorSettingsDao

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        envSensorSettingsDao = database.getEnvSensorSettingsDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testIt() = runBlocking {
        val envSensorSettings: EnvSensorSettings =
            EnvSensorSettings("name", "displayed name", true)
        envSensorSettingsDao.insertEnvSensorSettings(envSensorSettings)

        assertEquals(envSensorSettings.name, envSensorSettingsDao.getEnvSensorSettings(envSensorSettings.name).name)

        var settingsAndDisplayedRows: EnvSensorSettingAndDisplayedRows =
            envSensorSettingsDao.getEnvSensorSettingsAndDisplayedRows(envSensorSettings.name)

        assertEquals(envSensorSettings.name, settingsAndDisplayedRows.envSensorSettings?.name)
        assertEquals(0, settingsAndDisplayedRows.displayedRows.size)

        val envSensorDisplayedRow1: EnvSensorDisplayedRow =
            EnvSensorDisplayedRow(null, "row1", envSensorSettings.name)
        val envSensorDisplayedRow2: EnvSensorDisplayedRow =
            EnvSensorDisplayedRow(null, "row2", envSensorSettings.name)
        envSensorSettingsDao.insertEnvSensorDisplayedRow(envSensorDisplayedRow1)
        envSensorSettingsDao.insertEnvSensorDisplayedRow(envSensorDisplayedRow2)
        settingsAndDisplayedRows = envSensorSettingsDao.getEnvSensorSettingsAndDisplayedRows(envSensorSettings.name)

        assertEquals(envSensorSettings.name, settingsAndDisplayedRows.envSensorSettings?.name)
        assertEquals(2, settingsAndDisplayedRows.displayedRows.size)
        assertNotNull(settingsAndDisplayedRows.displayedRows.find { it.rowName == "row1" })
        assertNotNull(settingsAndDisplayedRows.displayedRows.find { it.rowName == "row2" })
        assertNull(settingsAndDisplayedRows.displayedRows.find { it.rowName == "row3" })
    }
}