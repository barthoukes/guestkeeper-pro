package com.guestkeeper.pro.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guestkeeper.pro.database.entity.Visit
import com.guestkeeper.pro.model.VisitStatus
import java.util.*

@Dao
interface VisitDao {

    @Insert
    suspend fun insert(visit: Visit): Long

    @Update
    suspend fun update(visit: Visit)

    @Delete
    suspend fun delete(visit: Visit)

    @Query("SELECT * FROM visit WHERE id = :id")
    suspend fun getById(id: Long): Visit?

    @Query("""
        SELECT v.* FROM visit v
        JOIN visitor vi ON v.visitor_id = vi.id
        WHERE v.status = 'ACTIVE'
        ORDER BY v.arrival_time DESC
    """)
    fun getActiveVisits(): LiveData<List<Visit>>

    @Query("""
        SELECT v.* FROM visit v
        JOIN visitor vi ON v.visitor_id = vi.id
        WHERE v.status = 'ACTIVE' AND vi.user_type = :userType
        ORDER BY v.arrival_time DESC
    """)
    fun getActiveVisitsByType(userType: String): LiveData<List<Visit>>

    @Query("""
        SELECT v.* FROM visit v
        JOIN visitor vi ON v.visitor_id = vi.id
        WHERE v.status = 'ACTIVE' 
        AND v.estimated_departure <= :currentTime
        ORDER BY v.estimated_departure ASC
    """)
    suspend fun getOverdueVisits(currentTime: Date): List<Visit>

    @Query("""
        SELECT v.* FROM visit v
        WHERE v.visitor_id = :visitorId
        ORDER BY v.arrival_time DESC
        LIMIT :limit
    """)
    suspend fun getVisitsByVisitor(visitorId: Long, limit: Int = 50): List<Visit>

    @Query("""
        SELECT v.* FROM visit v
        JOIN visitor vi ON v.visitor_id = vi.id
        WHERE (:searchQuery IS NULL OR 
               vi.full_name LIKE '%' || :searchQuery || '%' OR
               vi.email LIKE '%' || :searchQuery || '%' OR
               vi.company LIKE '%' || :searchQuery || '%')
        AND (:startDate IS NULL OR v.arrival_time >= :startDate)
        AND (:endDate IS NULL OR v.arrival_time <= :endDate)
        AND (:userType IS NULL OR vi.user_type = :userType)
        ORDER BY v.arrival_time DESC
    """)
    suspend fun searchVisits(
        searchQuery: String? = null,
        startDate: Date? = null,
        endDate: Date? = null,
        userType: String? = null
    ): List<Visit>

    @Query("""
        UPDATE visit 
        SET status = 'COMPLETED', 
            actual_departure = :departureTime,
            checkout_notes = :notes,
            updated_at = :updateTime
        WHERE id = :visitId
    """)
    suspend fun checkoutVisit(
        visitId: Long,
        departureTime: Date,
        notes: String?,
        updateTime: Date
    )

    @Query("""
        UPDATE visit 
        SET estimated_departure = :newDepartureTime,
            updated_at = :updateTime
        WHERE id = :visitId
    """)
    suspend fun extendVisit(
        visitId: Long,
        newDepartureTime: Date,
        updateTime: Date
    )

    @Query("""
        SELECT COUNT(*) FROM visit 
        WHERE status = 'ACTIVE'
    """)
    suspend fun getActiveVisitCount(): Int

    @Query("""
        SELECT v.* FROM visit v
        JOIN visitor vi ON v.visitor_id = vi.id
        WHERE v.tag_id = :tagId
        AND v.status = 'ACTIVE'
        LIMIT 1
    """)
    suspend fun getActiveVisitByTag(tagId: Long): Visit?

    @Query("""
        SELECT COUNT(*) FROM visit 
        WHERE DATE(arrival_time/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')
    """)
    suspend fun getVisitCountByDate(date: Date): Int

    @Transaction
    suspend fun checkoutMultipleVisits(visitIds: List<Long>) {
        val currentTime = Date()
        visitIds.forEach { id ->
            checkoutVisit(id, currentTime, "Bulk checkout", currentTime)
        }
    }
}

