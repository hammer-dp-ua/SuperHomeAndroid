package ua.dp.hammer.superhome.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ua.dp.hammer.superhome.db.dao.*
import ua.dp.hammer.superhome.db.entities.*

@Database(entities = [EnvSensorSettingsEntity::class, EnvSensorDisplayedRowEntity::class, CameraSettingsEntity::class,
    LocalSettingsEntity::class, DeviceDisplayedTypeEntity::class, AlarmSourceImageEntity::class], version = 6, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getEnvSensorSettingsDao(): EnvSensorSettingsDao
    abstract fun getManagerSettingsDao(): ManagerSettingsDao
    abstract fun getLocalSettingsDao(): LocalSettingsDao
    abstract fun getDevicesDisplayedTypesDao(): DevicesDisplayedTypesDao
    abstract fun getAlarmSourcesImagesDao(): AlarmSourcesImagesDao

    companion object {
        // For Singleton instantiation
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "super_home_db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                .build()
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `LocalSettingsEntity` " +
                        "(" +
                        "`id` INTEGER," +
                        "`localServerAddress` TEXT," +
                        "`globalServerAddress` TEXT," +
                        "PRIMARY KEY(`id`)" +
                        ")")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE `LocalSettingsEntity` " +
                        "ADD COLUMN `localWiFiSsid` TEXT"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE `LocalSettingsEntity` " +
                        "RENAME TO `LocalSettingsEntity_old`"
                )
                database.execSQL(
                    "CREATE TABLE `LocalSettingsEntity` " +
                        "(" +
                        "`id` INTEGER," +
                        "`globalServerAddress` TEXT," +
                        "`localWiFiSsid` TEXT," +
                        "PRIMARY KEY(`id`)" +
                        ")")
                database.execSQL(
                    "INSERT INTO `LocalSettingsEntity` (`id`, `globalServerAddress`, `localWiFiSsid`) " +
                        "SELECT `id`, `globalServerAddress`, `localWiFiSsid` FROM `LocalSettingsEntity_old`"
                )
                database.execSQL(
                    "DROP TABLE `LocalSettingsEntity_old`"
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `DeviceDisplayedTypeEntity` " +
                        "(" +
                        "`type` TEXT NOT NULL," +
                        "`displayedType` TEXT," +
                        "PRIMARY KEY(`type`)" +
                        ")")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `AlarmSourceImageEntity` " +
                        "(" +
                        "`deviceName` TEXT NOT NULL," +
                        "`alarmSource` TEXT NOT NULL," +
                        "`resourceName` TEXT," +
                        "`filePath` TEXT," +
                        "PRIMARY KEY(`deviceName`, `alarmSource`)" +
                        ")")
            }
        }
    }
}