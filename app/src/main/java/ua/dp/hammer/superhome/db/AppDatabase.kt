package ua.dp.hammer.superhome.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.dp.hammer.superhome.db.dao.EnvSensorSettingsDao
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRow
import ua.dp.hammer.superhome.db.entities.EnvSensorSettings

@Database(entities = [EnvSensorSettings::class, EnvSensorDisplayedRow::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getEnvSensorSettingsDao(): EnvSensorSettingsDao

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
            return Room.databaseBuilder(context, AppDatabase::class.java, "super_home_db").build()
        }
    }
}