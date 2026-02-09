package com.guestkeeper.pro.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guestkeeper.pro.model.UserType
import java.util.*

@Entity(tableName = "visitor")
data class Visitor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "full_name", index = true)
    val fullName: String,

    @ColumnInfo(name = "email", unique = true, index = true)
    val email: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "company")
    val company: String? = null,

    @ColumnInfo(name = "photo_path")
    val photoPath: String? = null,

    @ColumnInfo(name = "user_type")
    val userType: UserType = UserType.GUEST,

    @ColumnInfo(name = "username")
    val username: String, // Auto-generated from email

    @ColumnInfo(name = "password_hash")
    val passwordHash: String, // Hashed auto-generated password

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),

    @ColumnInfo(name = "total_visits")
    val totalVisits: Int = 0
)

