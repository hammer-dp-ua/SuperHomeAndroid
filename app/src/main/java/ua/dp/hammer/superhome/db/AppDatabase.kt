package ua.dp.hammer.superhome.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ua.dp.hammer.superhome.db.dao.EnvSensorSettingsDao
import ua.dp.hammer.superhome.db.dao.LocalSettingsDao
import ua.dp.hammer.superhome.db.dao.ManagerSettingsDao
import ua.dp.hammer.superhome.db.entities.CameraSettingsEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingsEntity
import ua.dp.hammer.superhome.db.entities.LocalSettingsEntity

@Database(entities = [EnvSensorSettingsEntity::class, EnvSensorDisplayedRowEntity::class, CameraSettingsEntity::class,
    LocalSettingsEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getEnvSensorSettingsDao(): EnvSensorSettingsDao
    abstract fun getManagerSettingsDao(): ManagerSettingsDao
    abstract fun getLocalSettingsDao(): LocalSettingsDao

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
                .addMigrations(MIGRATION_1_2)
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
    }
}