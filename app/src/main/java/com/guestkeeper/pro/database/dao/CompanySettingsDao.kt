package com.guestkeeper.pro.database.dao

import androidx.room.*
import com.guestkeeper.pro.database.entity.CompanySettings
import java.util.*

@Dao
interface CompanySettingsDao {

   /**
    * Get company settings (singleton - always returns first record)
    */
   @Query("SELECT * FROM company_settings LIMIT 1")
   suspend fun getSettings(): CompanySettings?

   /**
    * Insert or update company settings
    * Since there's only one row, we use REPLACE strategy
    */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertOrUpdate(settings: CompanySettings)

   /**
    * Update specific fields of company settings
    */
   @Query("""
        UPDATE company_settings 
        SET company_name = COALESCE(:companyName, company_name),
            logo_path = COALESCE(:logoPath, logo_path),
            welcome_message = COALESCE(:welcomeMessage, welcome_message),
            contact_email = COALESCE(:contactEmail, contact_email),
            contact_phone = COALESCE(:contactPhone, contact_phone),
            address = COALESCE(:address, address),
            updated_at = :updateTime
        WHERE id = 1
    """)
   suspend fun updateBasicInfo(
      companyName: String? = null,
      logoPath: String? = null,
      welcomeMessage: String? = null,
      contactEmail: String? = null,
      contactPhone: String? = null,
      address: String? = null,
      updateTime: Date = Date()
   )

   /**
    * Update security settings
    */
   @Query("""
        UPDATE company_settings 
        SET session_timeout_minutes = COALESCE(:sessionTimeout, session_timeout_minutes),
            max_login_attempts = COALESCE(:maxLoginAttempts, max_login_attempts),
            password_expiry_days = COALESCE(:passwordExpiryDays, password_expiry_days),
            enable_biometric_login = COALESCE(:enableBiometricLogin, enable_biometric_login),
            privacy_mode_enabled = COALESCE(:privacyMode, privacy_mode_enabled),
            updated_at = :updateTime
        WHERE id = 1
    """)
   suspend fun updateSecuritySettings(
      sessionTimeout: Int? = null,
      maxLoginAttempts: Int? = null,
      passwordExpiryDays: Int? = null,
      enableBiometricLogin: Boolean? = null,
      privacyMode: Boolean? = null,
      updateTime: Date = Date()
   )

   /**
    * Reset to default settings
    */
   @Query("DELETE FROM company_settings")
   suspend fun deleteAll()

   @Transaction
   suspend fun resetToDefault() {
      deleteAll()
      insertOrUpdate(CompanySettings()) // Default constructor
   }

   /**
    * Update trial days remaining
    */
   @Query("UPDATE company_settings SET trial_days_remaining = :days, updated_at = :updateTime WHERE id = 1")
   suspend fun updateTrialDays(days: Int, updateTime: Date = Date())

   /**
    * Activate license
    */
   @Query("""
        UPDATE company_settings 
        SET license_key = :licenseKey,
            license_valid_until = :validUntil,
            is_trial_mode = false,
            updated_at = :updateTime
        WHERE id = 1
    """)
   suspend fun activateLicense(licenseKey: String, validUntil: Date, updateTime: Date = Date())

   /**
    * Check if settings exist
    */
   @Query("SELECT COUNT(*) FROM company_settings")
   suspend fun count(): Int

   /**
    * Initialize with default settings if not exists
    */
   @Transaction
   suspend fun initializeIfNeeded() {
      if (count() == 0) {
         insertOrUpdate(CompanySettings())
      }
   }
}