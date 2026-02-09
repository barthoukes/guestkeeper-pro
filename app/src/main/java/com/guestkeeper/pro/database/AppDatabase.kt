package com.guestkeeper.pro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.guestkeeper.pro.database.converter.*
import com.guestkeeper.pro.database.dao.*
import com.guestkeeper.pro.database.entity.*

@Database(
    entities = [
        User::class,
        Visitor::class,
        Visit::class,
        Tag::class,
        CompanySettings::class,
        VisitHistory::class,
        Backup::class,
        Notification::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateConverter::class,
    TimeConverter::class,
    UserTypeConverter::class,
    TagStatusConverter::class,
    UserRoleConverter::class,
    VisitStatusConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun visitorDao(): VisitorDao
    abstract fun visitDao(): VisitDao
    abstract fun tagDao(): TagDao
    abstract fun companySettingsDao(): CompanySettingsDao
    abstract fun visitHistoryDao(): VisitHistoryDao
    abstract fun backupDao(): BackupDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guestkeeper.db"
                )
                    .addCallback(DatabaseCallback())
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Future migrations will be added here
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Initialize with default admin user
                db.execSQL(
                    """
                    INSERT INTO user (email, password_hash, role, created_at) 
                    VALUES ('admin@guestkeeper.com', 
                    '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 
                    'ADMIN', ${System.currentTimeMillis()})
                    """.trimIndent()
                )
            }
        }
    }
}

