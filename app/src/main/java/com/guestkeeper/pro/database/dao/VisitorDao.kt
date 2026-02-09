package com.guestkeeper.pro.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guestkeeper.pro.database.entity.Visitor

@Dao
interface VisitorDao {

    @Insert
    suspend fun insert(visitor: Visitor): Long

    @Update
    suspend fun update(visitor: Visitor)

    @Delete
    suspend fun delete(visitor: Visitor)

    @Query("SELECT * FROM visitor WHERE id = :id")
    suspend fun getById(id: Long): Visitor?

    @Query("SELECT * FROM visitor WHERE email = :email")
    suspend fun getByEmail(email: String): Visitor?

    @Query("SELECT * FROM visitor WHERE username = :username")
    suspend fun getByUsername(username: String): Visitor?

    @Query("""
        SELECT * FROM visitor 
        WHERE is_active = 1
        ORDER BY full_name ASC
    """)
    fun getAllActive(): LiveData<List<Visitor>>

    @Query("""
        SELECT * FROM visitor 
        WHERE (:searchQuery IS NULL OR 
               full_name LIKE '%' || :searchQuery || '%' OR
               email LIKE '%' || :searchQuery || '%' OR
               company LIKE '%' || :searchQuery || '%')
        AND (:userType IS NULL OR user_type = :userType)
        ORDER BY full_name ASC
    """)
    suspend fun searchVisitors(
        searchQuery: String? = null,
        userType: String? = null
    ): List<Visitor>

    @Query("""
        SELECT * FROM visitor 
        WHERE email = :email OR phone = :phone
        LIMIT 1
    """)
    suspend fun findDuplicate(email: String, phone: String): Visitor?

    @Query("""
        UPDATE visitor 
        SET total_visits = total_visits + 1,
            updated_at = :updateTime
        WHERE id = :visitorId
    """)
    suspend fun incrementVisitCount(visitorId: Long, updateTime: Long)

    @Query("""
        UPDATE visitor 
        SET photo_path = :photoPath,
            updated_at = :updateTime
        WHERE id = :visitorId
    """)
    suspend fun updatePhoto(visitorId: Long, photoPath: String, updateTime: Long)

    @Query("""
        SELECT COUNT(*) FROM visitor 
        WHERE is_active = 1
    """)
    suspend fun getActiveVisitorCount(): Int

    @Query("""
        SELECT COUNT(*) FROM visitor 
        WHERE DATE(created_at/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')
    """)
    suspend fun getVisitorCountByDate(date: Long): Int
}

