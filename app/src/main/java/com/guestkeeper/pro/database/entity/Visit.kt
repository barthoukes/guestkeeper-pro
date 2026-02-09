package com.guestkeeper.pro.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.guestkeeper.pro.model.VisitStatus
import java.util.*

@Entity(
    tableName = "visit",
    foreignKeys = [
        ForeignKey(
            entity = Visitor::class,
            parentColumns = ["id"],
            childColumns = ["visitor_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["visitor_id"]),
        Index(value = ["tag_id"]),
        Index(value = ["arrival_time"]),
        Index(value = ["status"])
    ]
)
data class Visit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "visitor_id")
    val visitorId: Long,

    @ColumnInfo(name = "tag_id")
    val tagId: Long? = null,

    @ColumnInfo(name = "purpose")
    val purpose: String? = null,

    @ColumnInfo(name = "host_employee")
    val hostEmployee: String? = null,

    @ColumnInfo(name = "arrival_time")
    val arrivalTime: Date,

    @ColumnInfo(name = "estimated_departure")
    val estimatedDeparture: Date,

    @ColumnInfo(name = "actual_departure")
    val actualDeparture: Date? = null,

    @ColumnInfo(name = "status")
    val status: VisitStatus = VisitStatus.ACTIVE,

    @ColumnInfo(name = "checkout_notes")
    val checkoutNotes: String? = null,

    @ColumnInfo(name = "created_by")
    val createdBy: Long, // User ID who created this visit

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)

