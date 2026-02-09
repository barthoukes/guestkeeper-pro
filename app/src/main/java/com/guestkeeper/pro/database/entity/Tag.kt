package com.guestkeeper.pro.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guestkeeper.pro.model.TagStatus
import java.util.*

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "tag_number", unique = true, index = true)
    val tagNumber: String,

    @ColumnInfo(name = "status")
    val status: TagStatus = TagStatus.AVAILABLE,

    @ColumnInfo(name = "current_visit_id")
    val currentVisitId: Long? = null,

    @ColumnInfo(name = "last_used")
    val lastUsed: Date? = null,

    @ColumnInfo(name = "total_uses")
    val totalUses: Int = 0,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)

