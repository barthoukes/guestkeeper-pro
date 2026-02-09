package com.guestkeeper.pro.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.guestkeeper.pro.R

/**
 * Enum representing the status of a physical visitor tag
 */
enum class TagStatus {
   AVAILABLE {
      override fun toString(): String = "Available"
      override fun getDescription(): String = "Tag is available for assignment"
      @ColorRes override fun getColorResId(): Int = R.color.tag_available
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_available
      override fun canAssign(): Boolean = true
      override fun isProblematic(): Boolean = false
      override fun getActionLabel(): String = "Assign"
   },

   IN_USE {
      override fun toString(): String = "In Use"
      override fun getDescription(): String = "Tag is currently assigned to a visitor"
      @ColorRes override fun getColorResId(): Int = R.color.tag_in_use
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_in_use
      override fun canAssign(): Boolean = false
      override fun isProblematic(): Boolean = false
      override fun getActionLabel(): String = "Check In"
   },

   LOST {
      override fun toString(): String = "Lost"
      override fun getDescription(): String = "Tag has been reported as lost"
      @ColorRes override fun getColorResId(): Int = R.color.tag_lost
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_lost
      override fun canAssign(): Boolean = false
      override fun isProblematic(): Boolean = true
      override fun getActionLabel(): String = "Mark Found"
   },

   DAMAGED {
      override fun toString(): String = "Damaged"
      override fun getDescription(): String = "Tag is damaged and cannot be used"
      @ColorRes override fun getColorResId(): Int = R.color.tag_damaged
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_damaged
      override fun canAssign(): Boolean = false
      override fun isProblematic(): Boolean = true
      override fun getActionLabel(): String = "Repair"
   },

   MAINTENANCE {
      override fun toString(): String = "Maintenance"
      override fun getDescription(): String = "Tag is under maintenance"
      @ColorRes override fun getColorResId(): Int = R.color.tag_maintenance
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_maintenance
      override fun canAssign(): Boolean = false
      override fun isProblematic(): Boolean = true
      override fun getActionLabel(): String = "Complete Maintenance"
   },

   RESERVED {
      override fun toString(): String = "Reserved"
      override fun getDescription(): String = "Tag is reserved for a specific visitor"
      @ColorRes override fun getColorResId(): Int = R.color.tag_reserved
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_reserved
      override fun canAssign(): Boolean = true
      override fun isProblematic(): Boolean = false
      override fun getActionLabel(): String = "Assign Now"
   },

   RETIRED {
      override fun toString(): String = "Retired"
      override fun getDescription(): String = "Tag has been permanently retired"
      @ColorRes override fun getColorResId(): Int = R.color.tag_retired
      @DrawableRes override fun getIconResId(): Int = R.drawable.ic_tag_retired
      override fun canAssign(): Boolean = false
      override fun isProblematic(): Boolean = true
      override fun getActionLabel(): String = "Reactivate"
   };

   /**
    * Get user-friendly description of the status
    */
   abstract fun getDescription(): String

   /**
    * Get color resource for this status
    */
   @ColorRes
   abstract fun getColorResId(): Int

   /**
    * Get icon resource for this status
    */
   @DrawableRes
   abstract fun getIconResId(): Int

   /**
    * Check if tag can be assigned to a visitor
    */
   abstract fun canAssign(): Boolean

   /**
    * Check if tag has a problem (lost, damaged, etc.)
    */
   abstract fun isProblematic(): Boolean

   /**
    * Get appropriate action label for this status
    */
   abstract fun getActionLabel(): String

   /**
    * Get next logical status when action is taken
    */
   fun getNextStatusOnAction(): TagStatus {
      return when (this) {
         AVAILABLE -> IN_USE
         IN_USE -> AVAILABLE
         LOST -> AVAILABLE
         DAMAGED -> MAINTENANCE
         MAINTENANCE -> AVAILABLE
         RESERVED -> IN_USE
         RETIRED -> AVAILABLE
      }
   }

   /**
    * Get list of valid status transitions from this status
    */
   fun getValidTransitions(): List<TagStatus> {
      return when (this) {
         AVAILABLE -> listOf(IN_USE, RESERVED, LOST, DAMAGED, RETIRED)
         IN_USE -> listOf(AVAILABLE, LOST, DAMAGED)
         LOST -> listOf(AVAILABLE, RETIRED)
         DAMAGED -> listOf(MAINTENANCE, RETIRED)
         MAINTENANCE -> listOf(AVAILABLE, DAMAGED, RETIRED)
         RESERVED -> listOf(IN_USE, AVAILABLE, LOST)
         RETIRED -> listOf(AVAILABLE)
      }
   }

   /**
    * Check if transition to another status is valid
    */
   fun canTransitionTo(newStatus: TagStatus): Boolean {
      return newStatus in getValidTransitions()
   }

   /**
    * Get status from string value (case-insensitive)
    */
   companion object {
      fun fromString(value: String?): TagStatus {
         return when (value?.uppercase()) {
            "AVAILABLE" -> AVAILABLE
            "IN_USE", "IN USE", "ASSIGNED" -> IN_USE
            "LOST" -> LOST
            "DAMAGED" -> DAMAGED
            "MAINTENANCE" -> MAINTENANCE
            "RESERVED" -> RESERVED
            "RETIRED" -> RETIRED
            else -> AVAILABLE // Default
         }
      }

      /**
       * Get all statuses that can be assigned to a visitor
       */
      fun getAssignableStatuses(): List<TagStatus> {
         return entries.filter { it.canAssign() }
      }

      /**
       * Get all problematic statuses
       */
      fun getProblematicStatuses(): List<TagStatus> {
         return entries.filter { it.isProblematic() }
      }

      /**
       * Get all active statuses (tags that are in circulation)
       */
      fun getActiveStatuses(): List<TagStatus> {
         return listOf(AVAILABLE, IN_USE, RESERVED, MAINTENANCE)
      }

      /**
       * Get all inactive statuses (tags not in circulation)
       */
      fun getInactiveStatuses(): List<TagStatus> {
         return listOf(LOST, DAMAGED, RETIRED)
      }

      /**
       * Get status display strings for UI
       */
      fun getDisplayStrings(): List<String> {
         return entries.map { it.toString() }
      }

      /**
       * Get status by ordinal with safety check
       */
      fun fromOrdinal(ordinal: Int): TagStatus {
         return entries.getOrElse(ordinal) { AVAILABLE }
      }

      /**
       * Get status color by ordinal
       */
      @ColorRes
      fun getColorByOrdinal(ordinal: Int): Int {
         return fromOrdinal(ordinal).getColorResId()
      }

      /**
       * Get status description by ordinal
       */
      fun getDescriptionByOrdinal(ordinal: Int): String {
         return fromOrdinal(ordinal).getDescription()
      }
   }
}

/**
 * Extension functions for TagStatus
 */
fun TagStatus.isAvailable(): Boolean = this == TagStatus.AVAILABLE
fun TagStatus.isInUse(): Boolean = this == TagStatus.IN_USE
fun TagStatus.isReserved(): Boolean = this == TagStatus.RESERVED
fun TagStatus.needsAttention(): Boolean = this.isProblematic()

/**
 * Data class for tag status statistics
 */
data class TagStatusStats(
   val available: Int = 0,
   val inUse: Int = 0,
   val lost: Int = 0,
   val damaged: Int = 0,
   val maintenance: Int = 0,
   val reserved: Int = 0,
   val retired: Int = 0
) {
   val total: Int get() = available + inUse + lost + damaged + maintenance + reserved + retired
   val active: Int get() = available + inUse + reserved + maintenance
   val problematic: Int get() = lost + damaged + retired
   val assignable: Int get() = available + reserved
}
