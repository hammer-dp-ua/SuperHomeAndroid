package ua.dp.hammer.superhome.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.dp.hammer.superhome.db.dao.EnvSensorSettingsDao
import ua.dp.hammer.superhome.db.dao.ManagerSettingsDao
import ua.dp.hammer.superhome.db.entities.CameraSettingsEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingsEntity

@Database(entities = [EnvSensorSettingsEntity::class, EnvSensorDisplayedRowEntity::class, CameraSettingsEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getEnvSensorSettingsDao(): EnvSensorSettingsDao
    abstract fun getManagerSettingsDao(): ManagerSettingsDao

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