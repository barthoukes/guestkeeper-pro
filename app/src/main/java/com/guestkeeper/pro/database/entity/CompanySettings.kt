package com.guestkeeper.pro.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.guestkeeper.pro.database.converter.DateConverter
import java.util.*

/**
 * Company settings and branding configuration
 * Stores company-specific customization for the app
 */
@Entity(tableName = "company_settings")
@TypeConverters(DateConverter::class)
data class CompanySettings(
   @PrimaryKey(autoGenerate = true)
   val id: Long = 1, // Always 1 for singleton

   // ==================== COMPANY BRANDING ====================
   @ColumnInfo(name = "company_name")
   val companyName: String = "GuestKeeper Pro",

   @ColumnInfo(name = "logo_path")
   val logoPath: String? = null,

   @ColumnInfo(name = "welcome_message")
   val welcomeMessage: String = "Welcome to our premises. Please register at the reception.",

   @ColumnInfo(name = "contact_email")
   val contactEmail: String? = null,

   @ColumnInfo(name = "contact_phone")
   val contactPhone: String? = null,

   @ColumnInfo(name = "address")
   val address: String? = null,

   @ColumnInfo(name = "website")
   val website: String? = null,

   // ==================== VISITOR SETTINGS ====================
   @ColumnInfo(name = "default_visit_duration_hours")
   val defaultVisitDurationHours: Int = 2,

   @ColumnInfo(name = "max_visit_duration_hours")
   val maxVisitDurationHours: Int = 8,

   @ColumnInfo(name = "business_start_hour")
   val businessStartHour: Int = 9, // 9 AM

   @ColumnInfo(name = "business_end_hour")
   val businessEndHour: Int = 17, // 5 PM

   @ColumnInfo(name = "require_photo")
   val requirePhoto: Boolean = false,

   @ColumnInfo(name = "require_host_employee")
   val requireHostEmployee: Boolean = false,

   // ==================== SECURITY SETTINGS ====================
   @ColumnInfo(name = "session_timeout_minutes")
   val sessionTimeoutMinutes: Int = 30,

   @ColumnInfo(name = "max_login_attempts")
   val maxLoginAttempts: Int = 5,

   @ColumnInfo(name = "password_expiry_days")
   val passwordExpiryDays: Int = 90,

   @ColumnInfo(name = "enable_biometric_login")
   val enableBiometricLogin: Boolean = true,

   @ColumnInfo(name = "privacy_mode_enabled")
   val privacyModeEnabled: Boolean = false,

   // ==================== NOTIFICATION SETTINGS ====================
   @ColumnInfo(name = "enable_notifications")
   val enableNotifications: Boolean = true,

   @ColumnInfo(name = "departure_reminder_minutes")
   val departureReminderMinutes: Int = 15,

   @ColumnInfo(name = "overdue_check_interval_minutes")
   val overdueCheckIntervalMinutes: Int = 30,

   @ColumnInfo(name = "daily_summary_hour")
   val dailySummaryHour: Int = 18, // 6 PM

   @ColumnInfo(name = "notification_sound_enabled")
   val notificationSoundEnabled: Boolean = true,

   @ColumnInfo(name = "notification_vibration_enabled")
   val notificationVibrationEnabled: Boolean = true,

   // ==================== DATA MANAGEMENT ====================
   @ColumnInfo(name = "auto_checkout_enabled")
   val autoCheckoutEnabled: Boolean = true,

   @ColumnInfo(name = "auto_checkout_grace_minutes")
   val autoCheckoutGraceMinutes: Int = 15,

   @ColumnInfo(name = "data_retention_days")
   val dataRetentionDays: Int = 365,

   @ColumnInfo(name = "auto_backup_enabled")
   val autoBackupEnabled: Boolean = true,

   @ColumnInfo(name = "backup_time_hour")
   val backupTimeHour: Int = 2, // 2 AM

   @ColumnInfo(name = "export_include_photos")
   val exportIncludePhotos: Boolean = true,

   @ColumnInfo(name = "export_format")
   val exportFormat: String = "PDF", // PDF, CSV, EXCEL

   // ==================== UI/UX SETTINGS ====================
   @ColumnInfo(name = "theme_mode")
   val themeMode: String = "SYSTEM", // LIGHT, DARK, SYSTEM

   @ColumnInfo(name = "language")
   val language: String = "en",

   @ColumnInfo(name = "date_format")
   val dateFormat: String = "dd/MM/yyyy",

   @ColumnInfo(name = "time_format")
   val timeFormat: String = "24h", // 12h or 24h

   @ColumnInfo(name = "enable_animations")
   val enableAnimations: Boolean = true,

   @ColumnInfo(name = "high_contrast_mode")
   val highContrastMode: Boolean = false,

   @ColumnInfo(name = "font_size")
   val fontSize: String = "MEDIUM", // SMALL, MEDIUM, LARGE

   // ==================== CAMERA SETTINGS ====================
   @ColumnInfo(name = "camera_quality")
   val cameraQuality: String = "MEDIUM", // LOW, MEDIUM, HIGH

   @ColumnInfo(name = "camera_flash_default")
   val cameraFlashDefault: Boolean = false,

   @ColumnInfo(name = "camera_sound_enabled")
   val cameraSoundEnabled: Boolean = false,

   @ColumnInfo(name = "max_photo_size_mb")
   val maxPhotoSizeMB: Int = 2,

   // ==================== PRINTING SETTINGS ====================
   @ColumnInfo(name = "print_badge_enabled")
   val printBadgeEnabled: Boolean = false,

   @ColumnInfo(name = "badge_template")
   val badgeTemplate: String = "STANDARD",

   @ColumnInfo(name = "print_company_logo")
   val printCompanyLogo: Boolean = true,

   @ColumnInfo(name = "print_qr_code")
   val printQrCode: Boolean = true,

   // ==================== NETWORK & SYNC ====================
   @ColumnInfo(name = "sync_enabled")
   val syncEnabled: Boolean = false,

   @ColumnInfo(name = "sync_interval_minutes")
   val syncIntervalMinutes: Int = 60,

   @ColumnInfo(name = "cloud_backup_enabled")
   val cloudBackupEnabled: Boolean = false,

   @ColumnInfo(name = "last_sync_time")
   val lastSyncTime: Date? = null,

   // ==================== LICENSING ====================
   @ColumnInfo(name = "license_key")
   val licenseKey: String? = null,

   @ColumnInfo(name = "license_valid_until")
   val licenseValidUntil: Date? = null,

   @ColumnInfo(name = "is_trial_mode")
   val isTrialMode: Boolean = true,

   @ColumnInfo(name = "trial_days_remaining")
   val trialDaysRemaining: Int = 7,

   // ==================== AUDIT & METADATA ====================
   @ColumnInfo(name = "created_at")
   val createdAt: Date = Date(),

   @ColumnInfo(name = "updated_at")
   val updatedAt: Date = Date(),

   @ColumnInfo(name = "last_modified_by")
   val lastModifiedBy: Long? = null, // User ID

   @ColumnInfo(name = "version")
   val version: Int = 1
) {
   /**
    * Check if license is valid
    */
   fun isLicenseValid(): Boolean {
      if (isTrialMode) {
         return trialDaysRemaining > 0
      }

      return licenseValidUntil?.after(Date()) ?: false
   }

   /**
    * Get business hours as formatted string
    */
   fun getBusinessHoursFormatted(): String {
      return "$businessStartHour:00 - $businessEndHour:00"
   }

   /**
    * Get date format pattern for SimpleDateFormat
    */
   fun getDateFormatPattern(): String {
      return when (dateFormat) {
         "MM/dd/yyyy" -> "MM/dd/yyyy"
         "yyyy-MM-dd" -> "yyyy-MM-dd"
         "dd.MM.yyyy" -> "dd.MM.yyyy"
         else -> "dd/MM/yyyy" // default
      }
   }

   /**
    * Get time format pattern for SimpleDateFormat
    */
   fun getTimeFormatPattern(): String {
      return if (timeFormat == "12h") {
         "hh:mm a"
      } else {
         "HH:mm"
      }
   }

   /**
    * Get date-time format pattern
    */
   fun getDateTimeFormatPattern(): String {
      return "${getDateFormatPattern()} ${getTimeFormatPattern()}"
   }

   /**
    * Check if current time is within business hours
    */
   fun isWithinBusinessHours(currentHour: Int): Boolean {
      return currentHour in businessStartHour until businessEndHour
   }

   /**
    * Get camera quality value (0-100)
    */
   fun getCameraQualityValue(): Int {
      return when (cameraQuality) {
         "LOW" -> 50
         "MEDIUM" -> 75
         "HIGH" -> 90
         else -> 75
      }
   }

   /**
    * Get maximum photo size in bytes
    */
   fun getMaxPhotoSizeBytes(): Long {
      return maxPhotoSizeMB * 1024L * 1024L
   }
}

/**
 * Data class for updating company settings (partial updates)
 */
data class CompanySettingsUpdate(
   val companyName: String? = null,
   val logoPath: String? = null,
   val welcomeMessage: String? = null,
   val contactEmail: String? = null,
   val contactPhone: String? = null,
   val address: String? = null,
   val website: String? = null,
   val defaultVisitDurationHours: Int? = null,
   val maxVisitDurationHours: Int? = null,
   val businessStartHour: Int? = null,
   val businessEndHour: Int? = null,
   val requirePhoto: Boolean? = null,
   val requireHostEmployee: Boolean? = null,
   val sessionTimeoutMinutes: Int? = null,
   val maxLoginAttempts: Int? = null,
   val passwordExpiryDays: Int? = null,
   val enableBiometricLogin: Boolean? = null,
   val privacyModeEnabled: Boolean? = null,
   val enableNotifications: Boolean? = null,
   val departureReminderMinutes: Int? = null,
   val overdueCheckIntervalMinutes: Int? = null,
   val dailySummaryHour: Int? = null,
   val notificationSoundEnabled: Boolean? = null,
   val notificationVibrationEnabled: Boolean? = null,
   val autoCheckoutEnabled: Boolean? = null,
   val autoCheckoutGraceMinutes: Int? = null,
   val dataRetentionDays: Int? = null,
   val autoBackupEnabled: Boolean? = null,
   val backupTimeHour: Int? = null,
   val exportIncludePhotos: Boolean? = null,
   val exportFormat: String? = null,
   val themeMode: String? = null,
   val language: String? = null,
   val dateFormat: String? = null,
   val timeFormat: String? = null,
   val enableAnimations: Boolean? = null,
   val highContrastMode: Boolean? = null,
   val fontSize: String? = null,
   val cameraQuality: String? = null,
   val cameraFlashDefault: Boolean? = null,
   val cameraSoundEnabled: Boolean? = null,
   val maxPhotoSizeMB: Int? = null,
   val printBadgeEnabled: Boolean? = null,
   val badgeTemplate: String? = null,
   val printCompanyLogo: Boolean? = null,
   val printQrCode: Boolean? = null,
   val syncEnabled: Boolean? = null,
   val syncIntervalMinutes: Int? = null,
   val cloudBackupEnabled: Boolean? = null,
   val licenseKey: String? = null,
   val isTrialMode: Boolean? = null
)
