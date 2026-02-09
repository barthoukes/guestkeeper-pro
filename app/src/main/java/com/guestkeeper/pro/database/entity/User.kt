package com.guestkeeper.pro.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guestkeeper.pro.model.UserRole
import java.util.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "email", unique = true)
    val email: String,

    @ColumnInfo(name = "password_hash")
    val passwordHash: String,

    @ColumnInfo(name = "full_name")
    val fullName: String? = null,

    @ColumnInfo(name = "phone")
    val phone: String? = null,

    @ColumnInfo(name = "role")
    val role: UserRole = UserRole.RECEPTIONIST,

    @ColumnInfo(name = "last_login")
    val lastLogin: Date? = null,

    @ColumnInfo(name = "session_timeout")
    val sessionTimeout: Int = 30, // minutes

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)

