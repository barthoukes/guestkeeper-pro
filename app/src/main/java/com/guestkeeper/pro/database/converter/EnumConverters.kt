package com.guestkeeper.pro.database.converter

import androidx.room.TypeConverter
import com.guestkeeper.pro.model.*

/**
 * Room Database Type Converters for Enum classes
 *
 * These converters handle the conversion between:
 * - Enum <-> String (database storage)
 * - Enum <-> Int (for ordering or legacy systems)
 * - Enum <-> Ordinal (backup method)
 */

object EnumConverters {

   // ==================== USER TYPE CONVERTERS ====================

   /**
    * Convert UserType to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun userTypeToString(userType: UserType?): String? {
      return userType?.name
   }

   /**
    * Convert String from database to UserType
    */
   @TypeConverter
   @JvmStatic
   fun stringToUserType(value: String?): UserType? {
      return value?.let {
         try {
            UserType.valueOf(it)
         } catch (e: IllegalArgumentException) {
            // Fallback to default or handle error
            UserType.GUEST
         }
      }
   }

   /**
    * Convert UserType to display-friendly string
    */
   @TypeConverter
   @JvmStatic
   fun userTypeToDisplayString(userType: UserType?): String? {
      return userType?.toString()
   }

   // ==================== USER ROLE CONVERTERS ====================

   /**
    * Convert UserRole to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun userRoleToString(userRole: UserRole?): String? {
      return userRole?.name
   }

   /**
    * Convert String from database to UserRole
    */
   @TypeConverter
   @JvmStatic
   fun stringToUserRole(value: String?): UserRole? {
      return value?.let {
         try {
            UserRole.valueOf(it)
         } catch (e: IllegalArgumentException) {
            UserRole.RECEPTIONIST
         }
      }
   }

   /**
    * Convert UserRole to integer (for legacy systems or ordering)
    */
   @TypeConverter
   @JvmStatic
   fun userRoleToInt(userRole: UserRole?): Int? {
      return userRole?.ordinal
   }

   /**
    * Convert integer to UserRole
    */
   @TypeConverter
   @JvmStatic
   fun intToUserRole(value: Int?): UserRole? {
      return value?.let {
         UserRole.entries.getOrNull(it) ?: UserRole.RECEPTIONIST
      }
   }

   // ==================== VISIT STATUS CONVERTERS ====================

   /**
    * Convert VisitStatus to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun visitStatusToString(visitStatus: VisitStatus?): String? {
      return visitStatus?.name
   }

   /**
    * Convert String from database to VisitStatus
    */
   @TypeConverter
   @JvmStatic
   fun stringToVisitStatus(value: String?): VisitStatus? {
      return value?.let {
         try {
            VisitStatus.valueOf(it)
         } catch (e: IllegalArgumentException) {
            VisitStatus.ACTIVE
         }
      }
   }

   /**
    * Convert VisitStatus to color code integer
    */
   @TypeConverter
   @JvmStatic
   fun visitStatusToColor(visitStatus: VisitStatus?): Int? {
      return visitStatus?.getColorCode()
   }

   /**
    * Convert VisitStatus to display-friendly string
    */
   @TypeConverter
   @JvmStatic
   fun visitStatusToDisplayString(visitStatus: VisitStatus?): String? {
      return visitStatus?.toString()
   }

   // ==================== TAG STATUS CONVERTERS ====================

   /**
    * Convert TagStatus to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun tagStatusToString(tagStatus: TagStatus?): String? {
      return tagStatus?.name
   }

   /**
    * Convert String from database to TagStatus
    */
   @TypeConverter
   @JvmStatic
   fun stringToTagStatus(value: String?): TagStatus? {
      return value?.let {
         try {
            TagStatus.valueOf(it)
         } catch (e: IllegalArgumentException) {
            TagStatus.AVAILABLE
         }
      }
   }

   /**
    * Convert TagStatus to integer code
    */
   @TypeConverter
   @JvmStatic
   fun tagStatusToInt(tagStatus: TagStatus?): Int? {
      return tagStatus?.ordinal
   }

   /**
    * Convert integer to TagStatus
    */
   @TypeConverter
   @JvmStatic
   fun intToTagStatus(value: Int?): TagStatus? {
      return value?.let {
         TagStatus.entries.getOrNull(it) ?: TagStatus.AVAILABLE
      }
   }

   // ==================== NOTIFICATION TYPE CONVERTERS ====================

   /**
    * Convert NotificationType to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun notificationTypeToString(notificationType: NotificationType?): String? {
      return notificationType?.name
   }

   /**
    * Convert String from database to NotificationType
    */
   @TypeConverter
   @JvmStatic
   fun stringToNotificationType(value: String?): NotificationType? {
      return value?.let {
         try {
            NotificationType.valueOf(it)
         } catch (e: IllegalArgumentException) {
            NotificationType.DEPARTURE_REMINDER
         }
      }
   }

   /**
    * Convert NotificationType to priority integer
    */
   @TypeConverter
   @JvmStatic
   fun notificationTypeToPriority(notificationType: NotificationType?): Int? {
      return notificationType?.getPriority()
   }

   // ==================== BACKUP STATUS CONVERTERS ====================

   /**
    * Convert BackupStatus to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun backupStatusToString(backupStatus: BackupStatus?): String? {
      return backupStatus?.name
   }

   /**
    * Convert String from database to BackupStatus
    */
   @TypeConverter
   @JvmStatic
   fun stringToBackupStatus(value: String?): BackupStatus? {
      return value?.let {
         try {
            BackupStatus.valueOf(it)
         } catch (e: IllegalArgumentException) {
            BackupStatus.PENDING
         }
      }
   }

   // ==================== REPORT FORMAT CONVERTERS ====================

   /**
    * Convert ReportFormat to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun reportFormatToString(reportFormat: ReportFormat?): String? {
      return reportFormat?.name
   }

   /**
    * Convert String from database to ReportFormat
    */
   @TypeConverter
   @JvmStatic
   fun stringToReportFormat(value: String?): ReportFormat? {
      return value?.let {
         try {
            ReportFormat.valueOf(it)
         } catch (e: IllegalArgumentException) {
            ReportFormat.CSV
         }
      }
   }

   /**
    * Convert ReportFormat to file extension
    */
   @TypeConverter
   @JvmStatic
   fun reportFormatToExtension(reportFormat: ReportFormat?): String? {
      return reportFormat?.getFileExtension()
   }

   // ==================== PHOTO QUALITY CONVERTERS ====================

   /**
    * Convert PhotoQuality to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun photoQualityToString(photoQuality: PhotoQuality?): String? {
      return photoQuality?.name
   }

   /**
    * Convert String from database to PhotoQuality
    */
   @TypeConverter
   @JvmStatic
   fun stringToPhotoQuality(value: String?): PhotoQuality? {
      return value?.let {
         try {
            PhotoQuality.valueOf(it)
         } catch (e: IllegalArgumentException) {
            PhotoQuality.MEDIUM
         }
      }
   }

   /**
    * Convert PhotoQuality to integer quality value (0-100)
    */
   @TypeConverter
   @JvmStatic
   fun photoQualityToInt(photoQuality: PhotoQuality?): Int? {
      return photoQuality?.getQualityValue()
   }

   // ==================== DATA RETENTION POLICY CONVERTERS ====================

   /**
    * Convert RetentionPolicy to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun retentionPolicyToString(retentionPolicy: RetentionPolicy?): String? {
      return retentionPolicy?.name
   }

   /**
    * Convert String from database to RetentionPolicy
    */
   @TypeConverter
   @JvmStatic
   fun stringToRetentionPolicy(value: String?): RetentionPolicy? {
      return value?.let {
         try {
            RetentionPolicy.valueOf(it)
         } catch (e: IllegalArgumentException) {
            RetentionPolicy.ONE_YEAR
         }
      }
   }

   /**
    * Convert RetentionPolicy to days integer
    */
   @TypeConverter
   @JvmStatic
   fun retentionPolicyToDays(retentionPolicy: RetentionPolicy?): Int? {
      return retentionPolicy?.getDays()
   }

   // ==================== PAYMENT STATUS CONVERTERS ====================

   /**
    * Convert PaymentStatus to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun paymentStatusToString(paymentStatus: PaymentStatus?): String? {
      return paymentStatus?.name
   }

   /**
    * Convert String from database to PaymentStatus
    */
   @TypeConverter
   @JvmStatic
   fun stringToPaymentStatus(value: String?): PaymentStatus? {
      return value?.let {
         try {
            PaymentStatus.valueOf(it)
         } catch (e: IllegalArgumentException) {
            PaymentStatus.PENDING
         }
      }
   }

   // ==================== VISITOR GENDER CONVERTERS ====================

   /**
    * Convert Gender to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun genderToString(gender: Gender?): String? {
      return gender?.name
   }

   /**
    * Convert String from database to Gender
    */
   @TypeConverter
   @JvmStatic
   fun stringToGender(value: String?): Gender? {
      return value?.let {
         try {
            Gender.valueOf(it)
         } catch (e: IllegalArgumentException) {
            Gender.UNSPECIFIED
         }
      }
   }

   // ==================== ID TYPE CONVERTERS ====================

   /**
    * Convert IdType to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun idTypeToString(idType: IdType?): String? {
      return idType?.name
   }

   /**
    * Convert String from database to IdType
    */
   @TypeConverter
   @JvmStatic
   fun stringToIdType(value: String?): IdType? {
      return value?.let {
         try {
            IdType.valueOf(it)
         } catch (e: IllegalArgumentException) {
            IdType.OTHER
         }
      }
   }

   // ==================== VISIT PURPOSE CONVERTERS ====================

   /**
    * Convert VisitPurpose to String for database storage
    */
   @TypeConverter
   @JvmStatic
   fun visitPurposeToString(visitPurpose: VisitPurpose?): String? {
      return visitPurpose?.name
   }

   /**
    * Convert String from database to VisitPurpose
    */
   @TypeConverter
   @JvmStatic
   fun stringToVisitPurpose(value: String?): VisitPurpose? {
      return value?.let {
         try {
            VisitPurpose.valueOf(it)
         } catch (e: IllegalArgumentException) {
            VisitPurpose.BUSINESS_MEETING
         }
      }
   }

   // ==================== ARRAY/LIST CONVERTERS ====================

   /**
    * Convert List of UserTypes to comma-separated String
    */
   @TypeConverter
   @JvmStatic
   fun userTypeListToString(userTypes: List<UserType>?): String? {
      return userTypes?.joinToString(",") { it.name }
   }

   /**
    * Convert comma-separated String to List of UserTypes
    */
   @TypeConverter
   @JvmStatic
   fun stringToUserTypeList(value: String?): List<UserType>? {
      return value?.split(",")?.mapNotNull {
         try {
            UserType.valueOf(it.trim())
         } catch (e: IllegalArgumentException) {
            null
         }
      }
   }

   /**
    * Convert List of VisitStatus to comma-separated String
    */
   @TypeConverter
   @JvmStatic
   fun visitStatusListToString(statusList: List<VisitStatus>?): String? {
      return statusList?.joinToString(",") { it.name }
   }

   /**
    * Convert comma-separated String to List of VisitStatus
    */
   @TypeConverter
   @JvmStatic
   fun stringToVisitStatusList(value: String?): List<VisitStatus>? {
      return value?.split(",")?.mapNotNull {
         try {
            VisitStatus.valueOf(it.trim())
         } catch (e: IllegalArgumentException) {
            null
         }
      }
   }

   // ==================== ENUM VALIDATION HELPERS ====================

   /**
    * Check if a string is a valid UserType
    */
   fun isValidUserType(value: String): Boolean {
      return try {
         UserType.valueOf(value)
         true
      } catch (e: IllegalArgumentException) {
         false
      }
   }

   /**
    * Check if a string is a valid UserRole
    */
   fun isValidUserRole(value: String): Boolean {
      return try {
         UserRole.valueOf(value)
         true
      } catch (e: IllegalArgumentException) {
         false
      }
   }

   /**
    * Check if a string is a valid VisitStatus
    */
   fun isValidVisitStatus(value: String): Boolean {
      return try {
         VisitStatus.valueOf(value)
         true
      } catch (e: IllegalArgumentException) {
         false
      }
   }

   /**
    * Check if a string is a valid TagStatus
    */
   fun isValidTagStatus(value: String): Boolean {
      return try {
         TagStatus.valueOf(value)
         true
      } catch (e: IllegalArgumentException) {
         false
      }
   }

   // ==================== ENUM MAPPING HELPERS ====================

   /**
    * Get all UserType values as display strings
    */
   fun getUserTypeDisplayStrings(): List<String> {
      return UserType.entries.map { it.toString() }
   }

   /**
    * Get all UserRole values as display strings
    */
   fun getUserRoleDisplayStrings(): List<String> {
      return UserRole.entries.map { it.toString() }
   }

   /**
    * Get all VisitStatus values as display strings
    */
   fun getVisitStatusDisplayStrings(): List<String> {
      return VisitStatus.entries.map { it.toString() }
   }

   /**
    * Get all TagStatus values as display strings
    */
   fun getTagStatusDisplayStrings(): List<String> {
      return TagStatus.entries.map { it.toString() }
   }

   /**
    * Map UserType to color resource
    */
   fun getUserTypeColorResource(userType: UserType): Int {
      return when (userType) {
         UserType.GUEST -> R.color.guest_500
         UserType.SUPPLIER -> R.color.supplier_500
         UserType.CONTRACTOR -> R.color.contractor_500
         UserType.VIP -> R.color.vip_500
         UserType.EMPLOYEE -> R.color.employee_500
      }
   }

   /**
    * Map VisitStatus to color resource
    */
   fun getVisitStatusColorResource(visitStatus: VisitStatus): Int {
      return when (visitStatus) {
         VisitStatus.ACTIVE -> R.color.visit_active
         VisitStatus.COMPLETED -> R.color.visit_completed
         VisitStatus.OVERDUE -> R.color.visit_overdue
         VisitStatus.CANCELLED -> R.color.visit_cancelled
         VisitStatus.PENDING -> R.color.visit_pending
         VisitStatus.EXTENDED -> R.color.visit_extended
      }
   }

   /**
    * Map TagStatus to color resource
    */
   fun getTagStatusColorResource(tagStatus: TagStatus): Int {
      return when (tagStatus) {
         TagStatus.AVAILABLE -> R.color.tag_available
         TagStatus.IN_USE -> R.color.tag_in_use
         TagStatus.LOST -> R.color.tag_lost
         TagStatus.DAMAGED -> R.color.tag_damaged
         TagStatus.MAINTENANCE -> R.color.tag_maintenance
         TagStatus.RESERVED -> R.color.tag_reserved
      }
   }

   // ==================== ENUM FILTERING HELPERS ====================

   /**
    * Get active visit statuses
    */
   fun getActiveVisitStatuses(): List<VisitStatus> {
      return listOf(VisitStatus.ACTIVE, VisitStatus.EXTENDED, VisitStatus.PENDING)
   }

   /**
    * Get completed visit statuses
    */
   fun getCompletedVisitStatuses(): List<VisitStatus> {
      return listOf(VisitStatus.COMPLETED)
   }

   /**
    * Get problem visit statuses
    */
   fun getProblemVisitStatuses(): List<VisitStatus> {
      return listOf(VisitStatus.OVERDUE, VisitStatus.CANCELLED)
   }

   /**
    * Get available tag statuses
    */
   fun getAvailableTagStatuses(): List<TagStatus> {
      return listOf(TagStatus.AVAILABLE, TagStatus.RESERVED)
   }

   /**
    * Get in-use tag statuses
    */
   fun getInUseTagStatuses(): List<TagStatus> {
      return listOf(TagStatus.IN_USE)
   }

   /**
    * Get problematic tag statuses
    */
   fun getProblematicTagStatuses(): List<TagStatus> {
      return listOf(TagStatus.LOST, TagStatus.DAMAGED, TagStatus.MAINTENANCE)
   }

   // ==================== ENUM SERIALIZATION FOR DATABASE QUERIES ====================

   /**
    * Convert enum list to SQL IN clause string
    */
   fun enumsToSqlInClause(enums: List<Enum<*>>): String {
      return enums.joinToString(",", "(", ")") { "'${it.name}'" }
   }

   /**
    * Convert enum list to SQL NOT IN clause string
    */
   fun enumsToSqlNotInClause(enums: List<Enum<*>>): String {
      return enums.joinToString(",", "(", ")") { "'${it.name}'" }
   }

   // ==================== ENUM COMPATIBILITY HELPERS ====================

   /**
    * Convert from legacy integer system to new enum system
    */
   fun legacyIntToUserType(legacyValue: Int): UserType {
      return when (legacyValue) {
         0 -> UserType.GUEST
         1 -> UserType.SUPPLIER
         2 -> UserType.CONTRACTOR
         3 -> UserType.VIP
         4 -> UserType.EMPLOYEE
         else -> UserType.GUEST
      }
   }

   /**
    * Convert from legacy string system to new enum system
    */
   fun legacyStringToVisitStatus(legacyValue: String): VisitStatus {
      return when (legacyValue.uppercase()) {
         "A", "ACTIVE", "1" -> VisitStatus.ACTIVE
         "C", "COMPLETE", "COMPLETED", "2" -> VisitStatus.COMPLETED
         "O", "OVERDUE", "3" -> VisitStatus.OVERDUE
         "X", "CANCEL", "CANCELLED", "4" -> VisitStatus.CANCELLED
         "P", "PENDING", "5" -> VisitStatus.PENDING
         "E", "EXTEND", "EXTENDED", "6" -> VisitStatus.EXTENDED
         else -> VisitStatus.ACTIVE
      }
   }

   // ==================== ENUM STATISTICS HELPERS ====================

   /**
    * Count occurrences of each UserType in a list
    */
   fun countUserTypes(userTypes: List<UserType>): Map<UserType, Int> {
      return userTypes.groupingBy { it }.eachCount()
   }

   /**
    * Count occurrences of each VisitStatus in a list
    */
   fun countVisitStatuses(visitStatuses: List<VisitStatus>): Map<VisitStatus, Int> {
      return visitStatuses.groupingBy { it }.eachCount()
   }

   /**
    * Count occurrences of each TagStatus in a list
    */
   fun countTagStatuses(tagStatuses: List<TagStatus>): Map<TagStatus, Int> {
      return tagStatuses.groupingBy { it }.eachCount()
   }

   // ==================== ENUM COMPARISON HELPERS ====================

   /**
    * Check if two enums are equal (null-safe)
    */
   fun <T : Enum<T>> areEnumsEqual(enum1: T?, enum2: T?): Boolean {
      return if (enum1 == null && enum2 == null) {
         true
      } else if (enum1 != null && enum2 != null) {
         enum1 == enum2
      } else {
         false
      }
   }

   /**
    * Check if enum is in a list (null-safe)
    */
   fun <T : Enum<T>> isEnumInList(enumValue: T?, list: List<T>): Boolean {
      return enumValue != null && list.contains(enumValue)
   }
}

/**
 * Additional enum classes needed for the converters
 */
package com.guestkeeper.pro.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.guestkeeper.pro.R

// ==================== ADDITIONAL ENUM CLASSES ====================

enum class UserType {
   GUEST,
   SUPPLIER,
   CONTRACTOR,
   VIP,
   EMPLOYEE;

   override fun toString(): String {
      return when (this) {
         GUEST -> "Guest"
         SUPPLIER -> "Supplier"
         CONTRACTOR -> "Contractor"
         VIP -> "VIP"
         EMPLOYEE -> "Employee"
      }
   }

   fun getColorResId(): Int {
      return when (this) {
         GUEST -> R.color.guest_500
         SUPPLIER -> R.color.supplier_500
         CONTRACTOR -> R.color.contractor_500
         VIP -> R.color.vip_500
         EMPLOYEE -> R.color.employee_500
      }
   }
}

enum class UserRole {
   ADMIN,
   RECEPTIONIST,
   MANAGER,
   SECURITY;

   override fun toString(): String {
      return when (this) {
         ADMIN -> "Administrator"
         RECEPTIONIST -> "Receptionist"
         MANAGER -> "Manager"
         SECURITY -> "Security"
      }
   }

   fun getPermissions(): List<String> {
      return when (this) {
         ADMIN -> listOf("ALL")
         RECEPTIONIST -> listOf("REGISTER_VISITOR", "VIEW_VISITORS", "CHECKOUT_VISITOR", "VIEW_REPORTS")
         MANAGER -> listOf("REGISTER_VISITOR", "VIEW_VISITORS", "CHECKOUT_VISITOR", "VIEW_REPORTS", "MANAGE_SETTINGS", "EXPORT_DATA")
         SECURITY -> listOf("VIEW_VISITORS", "CHECKOUT_VISITOR")
      }
   }
}

enum class VisitStatus {
   ACTIVE,
   COMPLETED,
   OVERDUE,
   CANCELLED,
   PENDING,
   EXTENDED;

   override fun toString(): String {
      return when (this) {
         ACTIVE -> "Active"
         COMPLETED -> "Completed"
         OVERDUE -> "Overdue"
         CANCELLED -> "Cancelled"
         PENDING -> "Pending"
         EXTENDED -> "Extended"
      }
   }

   @ColorRes
   fun getColorCode(): Int {
      return when (this) {
         ACTIVE -> R.color.visit_active
         COMPLETED -> R.color.visit_completed
         OVERDUE -> R.color.visit_overdue
         CANCELLED -> R.color.visit_cancelled
         PENDING -> R.color.visit_pending
         EXTENDED -> R.color.visit_extended
      }
   }

   fun isActive(): Boolean {
      return this == ACTIVE || this == EXTENDED || this == PENDING
   }
}

enum class TagStatus {
   AVAILABLE,
   IN_USE,
   LOST,
   DAMAGED,
   MAINTENANCE,
   RESERVED;

   override fun toString(): String {
      return when (this) {
         AVAILABLE -> "Available"
         IN_USE -> "In Use"
         LOST -> "Lost"
         DAMAGED -> "Damaged"
         MAINTENANCE -> "Maintenance"
         RESERVED -> "Reserved"
      }
   }

   fun canAssign(): Boolean {
      return this == AVAILABLE || this == RESERVED
   }

   fun isProblematic(): Boolean {
      return this == LOST || this == DAMAGED || this == MAINTENANCE
   }
}

enum class NotificationType {
   DEPARTURE_REMINDER,
   OVERDUE_ALERT,
   DAILY_SUMMARY,
   BACKUP_COMPLETE,
   TAG_LOST,
   SYSTEM_ERROR;

   override fun toString(): String {
      return when (this) {
         DEPARTURE_REMINDER -> "Departure Reminder"
         OVERDUE_ALERT -> "Overdue Alert"
         DAILY_SUMMARY -> "Daily Summary"
         BACKUP_COMPLETE -> "Backup Complete"
         TAG_LOST -> "Tag Lost Alert"
         SYSTEM_ERROR -> "System Error"
      }
   }

   fun getPriority(): Int {
      return when (this) {
         DEPARTURE_REMINDER -> 2
         OVERDUE_ALERT -> 3
         DAILY_SUMMARY -> 1
         BACKUP_COMPLETE -> 1
         TAG_LOST -> 4
         SYSTEM_ERROR -> 5
      }
   }
}

enum class BackupStatus {
   PENDING,
   IN_PROGRESS,
   COMPLETED,
   FAILED,
   PARTIAL;

   override fun toString(): String {
      return when (this) {
         PENDING -> "Pending"
         IN_PROGRESS -> "In Progress"
         COMPLETED -> "Completed"
         FAILED -> "Failed"
         PARTIAL -> "Partial"
      }
   }
}

enum class ReportFormat {
   CSV,
   PDF,
   EXCEL,
   JSON;

   override fun toString(): String {
      return when (this) {
         CSV -> "CSV"
         PDF -> "PDF"
         EXCEL -> "Excel"
         JSON -> "JSON"
      }
   }

   fun getFileExtension(): String {
      return when (this) {
         CSV -> ".csv"
         PDF -> ".pdf"
         EXCEL -> ".xlsx"
         JSON -> ".json"
      }
   }
}

enum class PhotoQuality {
   LOW,
   MEDIUM,
   HIGH,
   ORIGINAL;

   override fun toString(): String {
      return when (this) {
         LOW -> "Low"
         MEDIUM -> "Medium"
         HIGH -> "High"
         ORIGINAL -> "Original"
      }
   }

   fun getQualityValue(): Int {
      return when (this) {
         LOW -> 50
         MEDIUM -> 75
         HIGH -> 90
         ORIGINAL -> 100
      }
   }

   fun getMaxDimension(): Int {
      return when (this) {
         LOW -> 480
         MEDIUM -> 720
         HIGH -> 1080
         ORIGINAL -> 4096
      }
   }
}

enum class RetentionPolicy {
   THREE_MONTHS,
   SIX_MONTHS,
   ONE_YEAR,
   TWO_YEARS,
   FOREVER;

   override fun toString(): String {
      return when (this) {
         THREE_MONTHS -> "3 Months"
         SIX_MONTHS -> "6 Months"
         ONE_YEAR -> "1 Year"
         TWO_YEARS -> "2 Years"
         FOREVER -> "Forever"
      }
   }

   fun getDays(): Int {
      return when (this) {
         THREE_MONTHS -> 90
         SIX_MONTHS -> 180
         ONE_YEAR -> 365
         TWO_YEARS -> 730
         FOREVER -> Int.MAX_VALUE
      }
   }
}

enum class PaymentStatus {
   PENDING,
   COMPLETED,
   FAILED,
   REFUNDED,
   CANCELLED;

   override fun toString(): String {
      return when (this) {
         PENDING -> "Pending"
         COMPLETED -> "Completed"
         FAILED -> "Failed"
         REFUNDED -> "Refunded"
         CANCELLED -> "Cancelled"
      }
   }
}

enum class Gender {
   MALE,
   FEMALE,
   OTHER,
   UNSPECIFIED;

   override fun toString(): String {
      return when (this) {
         MALE -> "Male"
         FEMALE -> "Female"
         OTHER -> "Other"
         UNSPECIFIED -> "Unspecified"
      }
   }
}

enum class IdType {
   PASSPORT,
   DRIVERS_LICENSE,
   NATIONAL_ID,
   COMPANY_ID,
   OTHER;

   override fun toString(): String {
      return when (this) {
         PASSPORT -> "Passport"
         DRIVERS_LICENSE -> "Driver's License"
         NATIONAL_ID -> "National ID"
         COMPANY_ID -> "Company ID"
         OTHER -> "Other"
      }
   }
}

enum class VisitPurpose {
   BUSINESS_MEETING,
   DELIVERY,
   INTERVIEW,
   MAINTENANCE,
   TRAINING,
   SOCIAL_VISIT,
   OTHER;

   override fun toString(): String {
      return when (this) {
         BUSINESS_MEETING -> "Business Meeting"
         DELIVERY -> "Delivery"
         INTERVIEW -> "Interview"
         MAINTENANCE -> "Maintenance"
         TRAINING -> "Training"
         SOCIAL_VISIT -> "Social Visit"
         OTHER -> "Other"
      }
   }
}

/**
 * Main Converters class for Room database annotations
 */
class Converters {

   @TypeConverter
   fun userTypeToString(userType: UserType?): String? = EnumConverters.userTypeToString(userType)

   @TypeConverter
   fun stringToUserType(value: String?): UserType? = EnumConverters.stringToUserType(value)

   @TypeConverter
   fun userRoleToString(userRole: UserRole?): String? = EnumConverters.userRoleToString(userRole)

   @TypeConverter
   fun stringToUserRole(value: String?): UserRole? = EnumConverters.stringToUserRole(value)

   @TypeConverter
   fun visitStatusToString(visitStatus: VisitStatus?): String? = EnumConverters.visitStatusToString(visitStatus)

   @TypeConverter
   fun stringToVisitStatus(value: String?): VisitStatus? = EnumConverters.stringToVisitStatus(value)

   @TypeConverter
   fun tagStatusToString(tagStatus: TagStatus?): String? = EnumConverters.tagStatusToString(tagStatus)

   @TypeConverter
   fun stringToTagStatus(value: String?): TagStatus? = EnumConverters.stringToTagStatus(value)

   @TypeConverter
   fun notificationTypeToString(notificationType: NotificationType?): String? = EnumConverters.notificationTypeToString(notificationType)

   @TypeConverter
   fun stringToNotificationType(value: String?): NotificationType? = EnumConverters.stringToNotificationType(value)

   @TypeConverter
   fun backupStatusToString(backupStatus: BackupStatus?): String? = EnumConverters.backupStatusToString(backupStatus)

   @TypeConverter
   fun stringToBackupStatus(value: String?): BackupStatus? = EnumConverters.stringToBackupStatus(value)
}
