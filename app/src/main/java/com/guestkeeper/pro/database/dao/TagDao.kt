package com.guestkeeper.pro.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guestkeeper.pro.database.entity.Tag
import com.guestkeeper.pro.model.TagStatus
import java.util.*

/**
 * Data Access Object (DAO) for Tag operations
 * Handles all database operations for physical tag management
 */
@Dao
interface TagDao {

   // ==================== BASIC CRUD OPERATIONS ====================

   /**
    * Insert a new tag into the database
    * @param tag The tag to insert
    * @return The inserted tag's ID
    */
   @Insert(onConflict = OnConflictStrategy.ABORT)
   suspend fun insert(tag: Tag): Long

   /**
    * Insert multiple tags at once
    * @param tags List of tags to insert
    */
   @Insert(onConflict = OnConflictStrategy.ABORT)
   suspend fun insertAll(tags: List<Tag>)

   /**
    * Update an existing tag
    * @param tag The tag to update
    */
   @Update
   suspend fun update(tag: Tag)

   /**
    * Delete a tag from the database
    * @param tag The tag to delete
    */
   @Delete
   suspend fun delete(tag: Tag)

   /**
    * Delete a tag by its ID
    * @param tagId The ID of the tag to delete
    */
   @Query("DELETE FROM tag WHERE id = :tagId")
   suspend fun deleteById(tagId: Long)

   /**
    * Delete all tags (use with caution)
    */
   @Query("DELETE FROM tag")
   suspend fun deleteAll()

   // ==================== SINGLE TAG QUERIES ====================

   /**
    * Get a tag by its ID
    * @param tagId The tag ID
    * @return The tag or null if not found
    */
   @Query("SELECT * FROM tag WHERE id = :tagId")
   suspend fun getById(tagId: Long): Tag?

   /**
    * Get a tag by its tag number
    * @param tagNumber The unique tag number
    * @return The tag or null if not found
    */
   @Query("SELECT * FROM tag WHERE tag_number = :tagNumber")
   suspend fun getByTagNumber(tagNumber: String): Tag?

   /**
    * Get a tag by the current visit ID
    * @param visitId The current visit ID
    * @return The tag or null if not found
    */
   @Query("SELECT * FROM tag WHERE current_visit_id = :visitId")
   suspend fun getByCurrentVisitId(visitId: Long): Tag?

   // ==================== LIST QUERIES ====================

   /**
    * Get all tags
    * @return LiveData list of all tags
    */
   @Query("SELECT * FROM tag ORDER BY tag_number ASC")
   fun getAll(): LiveData<List<Tag>>

   /**
    * Get all active tags
    * @return LiveData list of active tags
    */
   @Query("SELECT * FROM tag WHERE is_active = 1 ORDER BY tag_number ASC")
   fun getAllActive(): LiveData<List<Tag>>

   /**
    * Get tags by status
    * @param status The tag status to filter by
    * @return LiveData list of tags with the specified status
    */
   @Query("SELECT * FROM tag WHERE status = :status AND is_active = 1 ORDER BY tag_number ASC")
   fun getByStatus(status: TagStatus): LiveData<List<Tag>>

   /**
    * Get tags by multiple statuses
    * @param statuses List of tag statuses to filter by
    * @return List of tags with any of the specified statuses
    */
   @Query("SELECT * FROM tag WHERE status IN (:statuses) AND is_active = 1 ORDER BY tag_number ASC")
   suspend fun getByStatuses(statuses: List<TagStatus>): List<Tag>

   /**
    * Get all available tags (not in use and active)
    * @return LiveData list of available tags
    */
   @Query("SELECT * FROM tag WHERE status = 'AVAILABLE' AND is_active = 1 ORDER BY tag_number ASC")
   fun getAvailableTags(): LiveData<List<Tag>>

   /**
    * Get all tags currently in use
    * @return LiveData list of tags in use
    */
   @Query("SELECT * FROM tag WHERE status = 'IN_USE' AND is_active = 1 ORDER BY tag_number ASC")
   fun getInUseTags(): LiveData<List<Tag>>

   /**
    * Get all problematic tags (lost, damaged, maintenance)
    * @return LiveData list of problematic tags
    */
   @Query("SELECT * FROM tag WHERE status IN ('LOST', 'DAMAGED', 'MAINTENANCE') AND is_active = 1 ORDER BY tag_number ASC")
   fun getProblematicTags(): LiveData<List<Tag>>

   /**
    * Get recently used tags
    * @param limit Maximum number of tags to return
    * @return List of recently used tags
    */
   @Query("SELECT * FROM tag WHERE last_used IS NOT NULL AND is_active = 1 ORDER BY last_used DESC LIMIT :limit")
   suspend fun getRecentlyUsed(limit: Int = 20): List<Tag>

   /**
    * Get most frequently used tags
    * @param limit Maximum number of tags to return
    * @return List of most frequently used tags
    */
   @Query("SELECT * FROM tag WHERE total_uses > 0 AND is_active = 1 ORDER BY total_uses DESC LIMIT :limit")
   suspend fun getMostFrequentlyUsed(limit: Int = 20): List<Tag>

   // ==================== SEARCH QUERIES ====================

   /**
    * Search tags by tag number
    * @param query Search query (partial tag number)
    * @return List of matching tags
    */
   @Query("SELECT * FROM tag WHERE tag_number LIKE '%' || :query || '%' AND is_active = 1 ORDER BY tag_number ASC")
   suspend fun searchByTagNumber(query: String): List<Tag>

   /**
    * Search tags by notes
    * @param query Search query (partial note text)
    * @return List of matching tags
    */
   @Query("SELECT * FROM tag WHERE notes LIKE '%' || :query || '%' AND is_active = 1 ORDER BY tag_number ASC")
   suspend fun searchByNotes(query: String): List<Tag>

   /**
    * Search tags with multiple criteria
    * @param tagNumberQuery Partial tag number (optional)
    * @param status Tag status (optional)
    * @param isActive Active status (optional)
    * @return List of matching tags
    */
   @Query("""
        SELECT * FROM tag 
        WHERE (:tagNumberQuery IS NULL OR tag_number LIKE '%' || :tagNumberQuery || '%')
        AND (:status IS NULL OR status = :status)
        AND (:isActive IS NULL OR is_active = :isActive)
        ORDER BY tag_number ASC
    """)
   suspend fun searchTags(
      tagNumberQuery: String? = null,
      status: TagStatus? = null,
      isActive: Boolean? = true
   ): List<Tag>

   // ==================== STATUS MANAGEMENT ====================

   /**
    * Update a tag's status
    * @param tagId The tag ID
    * @param status The new status
    * @param currentVisitId The current visit ID (null if not in use)
    * @param updateTime The update timestamp
    */
   @Query("""
        UPDATE tag 
        SET status = :status, 
            current_visit_id = :currentVisitId,
            updated_at = :updateTime
        WHERE id = :tagId
    """)
   suspend fun updateTagStatus(
      tagId: Long,
      status: TagStatus,
      currentVisitId: Long?,
      updateTime: Date = Date()
   )

   /**
    * Mark a tag as in use (assign to a visit)
    * @param tagId The tag ID
    * @param visitId The visit ID
    */
   @Transaction
   suspend fun assignTagToVisit(tagId: Long, visitId: Long) {
      updateTagStatus(tagId, TagStatus.IN_USE, visitId)
      incrementTagUsage(tagId)
   }

   /**
    * Mark a tag as available (return from visit)
    * @param tagId The tag ID
    */
   @Transaction
   suspend fun returnTag(tagId: Long) {
      updateTagStatus(tagId, TagStatus.AVAILABLE, null)
   }

   /**
    * Mark a tag as lost
    * @param tagId The tag ID
    */
   suspend fun markTagAsLost(tagId: Long) {
      updateTagStatus(tagId, TagStatus.LOST, null)
   }

   /**
    * Mark a tag as damaged
    * @param tagId The tag ID
    */
   suspend fun markTagAsDamaged(tagId: Long) {
      updateTagStatus(tagId, TagStatus.DAMAGED, null)
   }

   /**
    * Mark a tag as under maintenance
    * @param tagId The tag ID
    */
   suspend fun markTagForMaintenance(tagId: Long) {
      updateTagStatus(tagId, TagStatus.MAINTENANCE, null)
   }

   /**
    * Mark a tag as reserved
    * @param tagId The tag ID
    */
   suspend fun reserveTag(tagId: Long) {
      updateTagStatus(tagId, TagStatus.RESERVED, null)
   }

   /**
    * Reactivate a tag (mark as available after being problematic)
    * @param tagId The tag ID
    */
   suspend fun reactivateTag(tagId: Long) {
      updateTagStatus(tagId, TagStatus.AVAILABLE, null)
   }

   // ==================== USAGE STATISTICS ====================

   /**
    * Increment the usage count for a tag
    * @param tagId The tag ID
    */
   @Query("""
        UPDATE tag 
        SET total_uses = total_uses + 1,
            last_used = :useTime,
            updated_at = :updateTime
        WHERE id = :tagId
    """)
   suspend fun incrementTagUsage(
      tagId: Long,
      useTime: Date = Date(),
      updateTime: Date = Date()
   )

   /**
    * Get the total number of tags
    * @return Total tag count
    */
   @Query("SELECT COUNT(*) FROM tag")
   suspend fun getTotalTagCount(): Int

   /**
    * Get the number of available tags
    * @return Available tag count
    */
   @Query("SELECT COUNT(*) FROM tag WHERE status = 'AVAILABLE' AND is_active = 1")
   suspend fun getAvailableTagCount(): Int

   /**
    * Get the number of tags in use
    * @return In-use tag count
    */
   @Query("SELECT COUNT(*) FROM tag WHERE status = 'IN_USE' AND is_active = 1")
   suspend fun getInUseTagCount(): Int

   /**
    * Get the number of problematic tags
    * @return Problematic tag count
    */
   @Query("SELECT COUNT(*) FROM tag WHERE status IN ('LOST', 'DAMAGED', 'MAINTENANCE') AND is_active = 1")
   suspend fun getProblematicTagCount(): Int

   /**
    * Get tag usage statistics by status
    * @return Map of status to count
    */
   @Query("SELECT status, COUNT(*) as count FROM tag WHERE is_active = 1 GROUP BY status")
   suspend fun getTagStatistics(): Map<TagStatus, Int>

   /**
    * Get the most used tag
    * @return The tag with highest total_uses
    */
   @Query("SELECT * FROM tag WHERE is_active = 1 ORDER BY total_uses DESC LIMIT 1")
   suspend fun getMostUsedTag(): Tag?

   /**
    * Get the least used tag (available)
    * @return The available tag with lowest total_uses
    */
   @Query("SELECT * FROM tag WHERE status = 'AVAILABLE' AND is_active = 1 ORDER BY total_uses ASC LIMIT 1")
   suspend fun getLeastUsedAvailableTag(): Tag?

   /**
    * Get tags that haven't been used in X days
    * @param days Number of days
    * @return List of tags not used in the specified days
    */
   @Query("""
        SELECT * FROM tag 
        WHERE last_used < :cutoffDate 
        AND is_active = 1 
        ORDER BY last_used ASC
    """)
   suspend fun getUnusedTagsSince(cutoffDate: Date): List<Tag>

   // ==================== BULK OPERATIONS ====================

   /**
    * Generate a batch of new tags
    * @param startNumber Starting tag number
    * @param count Number of tags to generate
    * @param notes Optional notes for all tags
    * @return List of generated tag IDs
    */
   suspend fun generateTags(
      startNumber: String,
      count: Int,
      notes: String? = null
   ): List<Long> {
      val tags = mutableListOf<Tag>()
      val baseNumber = startNumber.filter { it.isDigit() }.toIntOrNull() ?: 0

      for (i in 0 until count) {
         val tagNumber = (baseNumber + i).toString().padStart(4, '0')
         val tag = Tag(
            tagNumber = tagNumber,
            notes = notes,
            createdAt = Date(),
            updatedAt = Date()
         )
         tags.add(tag)
      }

      return insertAllAndReturnIds(tags)
   }

   /**
    * Insert multiple tags and return their IDs
    * @param tags List of tags to insert
    * @return List of inserted tag IDs
    */
   @Transaction
   suspend fun insertAllAndReturnIds(tags: List<Tag>): List<Long> {
      val ids = mutableListOf<Long>()
      tags.forEach { tag ->
         ids.add(insert(tag))
      }
      return ids
   }

   /**
    * Update multiple tags' status
    * @param tagIds List of tag IDs
    * @param newStatus New status for all tags
    */
   @Transaction
   suspend fun updateTagsStatus(tagIds: List<Long>, newStatus: TagStatus) {
      val updateTime = Date()
      tagIds.forEach { tagId ->
         updateTagStatus(tagId, newStatus, null, updateTime)
      }
   }

   /**
    * Deactivate multiple tags
    * @param tagIds List of tag IDs to deactivate
    */
   @Query("""
        UPDATE tag 
        SET is_active = 0, 
            updated_at = :updateTime
        WHERE id IN (:tagIds)
    """)
   suspend fun deactivateTags(tagIds: List<Long>, updateTime: Date = Date())

   /**
    * Reactivate multiple tags
    * @param tagIds List of tag IDs to reactivate
    */
   @Query("""
        UPDATE tag 
        SET is_active = 1, 
            status = 'AVAILABLE',
            updated_at = :updateTime
        WHERE id IN (:tagIds)
    """)
   suspend fun reactivateTags(tagIds: List<Long>, updateTime: Date = Date())

   // ==================== VALIDATION AND CHECKS ====================

   /**
    * Check if a tag number already exists
    * @param tagNumber The tag number to check
    * @return True if the tag number exists
    */
   @Query("SELECT COUNT(*) FROM tag WHERE tag_number = :tagNumber")
   suspend fun tagNumberExists(tagNumber: String): Boolean

   /**
    * Check if a tag is available for assignment
    * @param tagId The tag ID to check
    * @return True if the tag is available
    */
   suspend fun isTagAvailable(tagId: Long): Boolean {
      val tag = getById(tagId)
      return tag != null && tag.status == TagStatus.AVAILABLE && tag.isActive
   }

   /**
    * Validate if a tag can be assigned to a visit
    * @param tagId The tag ID
    * @return Validation result with error message if invalid
    */
   suspend fun validateTagForAssignment(tagId: Long): Pair<Boolean, String?> {
      val tag = getById(tagId) ?: return Pair(false, "Tag not found")

      if (!tag.isActive) return Pair(false, "Tag is deactivated")

      return when (tag.status) {
         TagStatus.AVAILABLE -> Pair(true, null)
         TagStatus.IN_USE -> Pair(false, "Tag is already in use")
         TagStatus.LOST -> Pair(false, "Tag is marked as lost")
         TagStatus.DAMAGED -> Pair(false, "Tag is damaged")
         TagStatus.MAINTENANCE -> Pair(false, "Tag is under maintenance")
         TagStatus.RESERVED -> Pair(true, null) // Reserved tags can be assigned
      }
   }

   /**
    * Find the next available tag number
    * @param prefix Optional prefix for tag numbers
    * @return The next available tag number
    */
   @Query("""
        SELECT MAX(CAST(tag_number AS INTEGER)) 
        FROM tag 
        WHERE tag_number GLOB '[0-9]*'
    """)
   suspend fun getMaxTagNumber(): Int?

   suspend fun getNextAvailableTagNumber(prefix: String = ""): String {
      val maxNumber = getMaxTagNumber() ?: 0
      var nextNumber = maxNumber + 1
      var nextTagNumber = "$prefix${nextNumber.toString().padStart(4, '0')}"

      // Ensure the tag number doesn't exist
      while (tagNumberExists(nextTagNumber)) {
         nextNumber++
         nextTagNumber = "$prefix${nextNumber.toString().padStart(4, '0')}"
      }

      return nextTagNumber
   }

   // ==================== AUDIT AND HISTORY ====================

   /**
    * Get all tags with their associated visit history
    * Note: This requires a ViewModel or additional query to join with visits
    */
   @Query("""
        SELECT t.*, 
               COUNT(v.id) as total_visits,
               MAX(v.arrival_time) as last_visit_date
        FROM tag t
        LEFT JOIN visit v ON t.id = v.tag_id
        WHERE t.is_active = 1
        GROUP BY t.id
        ORDER BY t.tag_number ASC
    """)
   suspend fun getTagsWithVisitStats(): List<TagWithStats>

   /**
    * Get tag usage history (requires a separate history table or join)
    * This is a simplified version
    */
   @Query("""
        SELECT t.tag_number, 
               COUNT(v.id) as usage_count,
               MAX(v.arrival_time) as last_used_time
        FROM tag t
        LEFT JOIN visit v ON t.id = v.tag_id
        WHERE t.is_active = 1
        GROUP BY t.id
        ORDER BY usage_count DESC
    """)
   suspend fun getTagUsageHistory(): List<TagUsageRecord>

   /**
    * Clean up old tags (deactivate tags not used for a long time)
    * @param cutoffDate Cutoff date for last used
    */
   @Query("""
        UPDATE tag 
        SET is_active = 0,
            updated_at = :updateTime,
            notes = COALESCE(notes || '; ', '') || 'Deactivated due to inactivity'
        WHERE last_used < :cutoffDate 
        AND status = 'AVAILABLE'
        AND is_active = 1
    """)
   suspend fun cleanupInactiveTags(cutoffDate: Date, updateTime: Date = Date()): Int

   // ==================== BACKUP AND RESTORE ====================

   /**
    * Export all tags to a list (for backup)
    * @return List of all tags
    */
   @Query("SELECT * FROM tag ORDER BY id ASC")
   suspend fun exportAllTags(): List<Tag>

   /**
    * Import tags from a list (for restore)
    * @param tags List of tags to import
    */
   @Transaction
   suspend fun importTags(tags: List<Tag>) {
      deleteAll() // Clear existing tags
      insertAll(tags)
   }

   // ==================== DATA CLASSES FOR COMPLEX QUERIES ====================

   /**
    * Data class for tags with statistics
    */
   data class TagWithStats(
      val tag: Tag,
      val totalVisits: Int,
      val lastVisitDate: Date?
   )

   /**
    * Data class for tag usage records
    */
   data class TagUsageRecord(
      val tagNumber: String,
      val usageCount: Int,
      val lastUsedTime: Date?
   )

   // ==================== TRANSACTIONAL OPERATIONS ====================

   /**
    * Assign a tag to a visit with validation
    * @param tagId The tag ID
    * @param visitId The visit ID
    * @return Pair of success boolean and error message
    */
   @Transaction
   suspend fun safeAssignTagToVisit(tagId: Long, visitId: Long): Pair<Boolean, String> {
      val (isValid, errorMessage) = validateTagForAssignment(tagId)

      if (!isValid) {
         return Pair(false, errorMessage ?: "Invalid tag for assignment")
      }

      try {
         assignTagToVisit(tagId, visitId)
         return Pair(true, "Tag assigned successfully")
      } catch (e: Exception) {
         return Pair(false, "Failed to assign tag: ${e.message}")
      }
   }

   /**
    * Return a tag from a visit
    * @param tagId The tag ID
    * @param visitId The visit ID (for verification)
    * @return Pair of success boolean and error message
    */
   @Transaction
   suspend fun safeReturnTag(tagId: Long, visitId: Long): Pair<Boolean, String> {
      val tag = getById(tagId) ?: return Pair(false, "Tag not found")

      if (tag.currentVisitId != visitId) {
         return Pair(false, "Tag is not assigned to this visit")
      }

      if (tag.status != TagStatus.IN_USE) {
         return Pair(false, "Tag is not currently in use")
      }

      try {
         returnTag(tagId)
         return Pair(true, "Tag returned successfully")
      } catch (e: Exception) {
         return Pair(false, "Failed to return tag: ${e.message}")
      }
   }

   /**
    * Bulk assign tags to visits
    * @param assignments List of tagId to visitId assignments
    * @return List of results for each assignment
    */
   @Transaction
   suspend fun bulkAssignTags(assignments: Map<Long, Long>): List<TagAssignmentResult> {
      val results = mutableListOf<TagAssignmentResult>()

      assignments.forEach { (tagId, visitId) ->
         val (success, message) = safeAssignTagToVisit(tagId, visitId)
         results.add(TagAssignmentResult(tagId, visitId, success, message))
      }

      return results
   }

   /**
    * Data class for tag assignment results
    */
   data class TagAssignmentResult(
      val tagId: Long,
      val visitId: Long,
      val success: Boolean,
      val message: String
   )
}
