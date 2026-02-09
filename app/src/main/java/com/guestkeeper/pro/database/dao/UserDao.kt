package com.guestkeeper.pro.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guestkeeper.pro.database.entity.User
import com.guestkeeper.pro.model.UserRole
import java.util.*

/**
 * Data Access Object (DAO) for User operations
 * Handles all database operations for user management and authentication
 */
@Dao
interface UserDao {

   // ==================== BASIC CRUD OPERATIONS ====================

   /**
    * Insert a new user into the database
    * @param user The user to insert
    * @return The inserted user's ID
    */
   @Insert(onConflict = OnConflictStrategy.ABORT)
   suspend fun insert(user: User): Long

   /**
    * Insert multiple users at once
    * @param users List of users to insert
    */
   @Insert(onConflict = OnConflictStrategy.ABORT)
   suspend fun insertAll(users: List<User>)

   /**
    * Update an existing user
    * @param user The user to update
    */
   @Update
   suspend fun update(user: User)

   /**
    * Delete a user from the database
    * @param user The user to delete
    */
   @Delete
   suspend fun delete(user: User)

   /**
    * Delete a user by their ID
    * @param userId The ID of the user to delete
    */
   @Query("DELETE FROM user WHERE id = :userId")
   suspend fun deleteById(userId: Long)

   /**
    * Delete all users (use with caution - keeps at least one admin)
    */
   @Query("DELETE FROM user WHERE id NOT IN (SELECT MIN(id) FROM user WHERE role = 'ADMIN')")
   suspend fun deleteAllNonEssential()

   // ==================== SINGLE USER QUERIES ====================

   /**
    * Get a user by their ID
    * @param userId The user ID
    * @return The user or null if not found
    */
   @Query("SELECT * FROM user WHERE id = :userId")
   suspend fun getById(userId: Long): User?

   /**
    * Get a user by their email
    * @param email The user's email
    * @return The user or null if not found
    */
   @Query("SELECT * FROM user WHERE email = :email")
   suspend fun getByEmail(email: String): User?

   /**
    * Get a user by their username (if implemented)
    * @param username The username
    * @return The user or null if not found
    */
   @Query("SELECT * FROM user WHERE LOWER(email) = LOWER(:username) OR LOWER(username) = LOWER(:username)")
   suspend fun getByUsername(username: String): User?

   /**
    * Get the currently logged-in user (last login within session timeout)
    * @param sessionTimeoutMinutes Session timeout in minutes
    * @return The active user or null
    */
   @Query("""
        SELECT * FROM user 
        WHERE last_login IS NOT NULL 
        AND (strftime('%s', 'now') * 1000 - last_login) < (:sessionTimeoutMinutes * 60 * 1000)
        AND is_active = 1
        ORDER BY last_login DESC 
        LIMIT 1
    """)
   suspend fun getActiveUser(sessionTimeoutMinutes: Int = 30): User?

   // ==================== LIST QUERIES ====================

   /**
    * Get all users
    * @return LiveData list of all users
    */
   @Query("SELECT * FROM user ORDER BY full_name ASC")
   fun getAll(): LiveData<List<User>>

   /**
    * Get all active users
    * @return LiveData list of active users
    */
   @Query("SELECT * FROM user WHERE is_active = 1 ORDER BY full_name ASC")
   fun getAllActive(): LiveData<List<User>>

   /**
    * Get users by role
    * @param role The user role to filter by
    * @return LiveData list of users with the specified role
    */
   @Query("SELECT * FROM user WHERE role = :role AND is_active = 1 ORDER BY full_name ASC")
   fun getByRole(role: UserRole): LiveData<List<User>>

   /**
    * Get users by multiple roles
    * @param roles List of user roles to filter by
    * @return List of users with any of the specified roles
    */
   @Query("SELECT * FROM user WHERE role IN (:roles) AND is_active = 1 ORDER BY full_name ASC")
   suspend fun getByRoles(roles: List<UserRole>): List<User>

   /**
    * Get all administrators
    * @return LiveData list of admin users
    */
   @Query("SELECT * FROM user WHERE role = 'ADMIN' AND is_active = 1 ORDER BY full_name ASC")
   fun getAdmins(): LiveData<List<User>>

   /**
    * Get all receptionists
    * @return LiveData list of receptionist users
    */
   @Query("SELECT * FROM user WHERE role = 'RECEPTIONIST' AND is_active = 1 ORDER BY full_name ASC")
   fun getReceptionists(): LiveData<List<User>>

   /**
    * Get recently created users
    * @param limit Maximum number of users to return
    * @return List of recently created users
    */
   @Query("SELECT * FROM user WHERE is_active = 1 ORDER BY created_at DESC LIMIT :limit")
   suspend fun getRecentlyCreated(limit: Int = 20): List<User>

   /**
    * Get recently active users (logged in)
    * @param limit Maximum number of users to return
    * @return List of recently active users
    */
   @Query("SELECT * FROM user WHERE last_login IS NOT NULL AND is_active = 1 ORDER BY last_login DESC LIMIT :limit")
   suspend fun getRecentlyActive(limit: Int = 20): List<User>

   // ==================== SEARCH QUERIES ====================

   /**
    * Search users by name
    * @param query Search query (partial name)
    * @return List of matching users
    */
   @Query("SELECT * FROM user WHERE full_name LIKE '%' || :query || '%' AND is_active = 1 ORDER BY full_name ASC")
   suspend fun searchByName(query: String): List<User>

   /**
    * Search users by email
    * @param query Search query (partial email)
    * @return List of matching users
    */
   @Query("SELECT * FROM user WHERE email LIKE '%' || :query || '%' AND is_active = 1 ORDER BY email ASC")
   suspend fun searchByEmail(query: String): List<User>

   /**
    * Search users with multiple criteria
    * @param nameQuery Partial name (optional)
    * @param emailQuery Partial email (optional)
    * @param role User role (optional)
    * @param isActive Active status (optional)
    * @return List of matching users
    */
   @Query("""
        SELECT * FROM user 
        WHERE (:nameQuery IS NULL OR full_name LIKE '%' || :nameQuery || '%')
        AND (:emailQuery IS NULL OR email LIKE '%' || :emailQuery || '%')
        AND (:role IS NULL OR role = :role)
        AND (:isActive IS NULL OR is_active = :isActive)
        ORDER BY full_name ASC
    """)
   suspend fun searchUsers(
      nameQuery: String? = null,
      emailQuery: String? = null,
      role: UserRole? = null,
      isActive: Boolean? = true
   ): List<User>

   // ==================== AUTHENTICATION OPERATIONS ====================

   /**
    * Authenticate a user by email and password hash
    * @param email User's email
    * @param passwordHash SHA-256 hashed password
    * @return The authenticated user or null
    */
   @Query("SELECT * FROM user WHERE email = :email AND password_hash = :passwordHash AND is_active = 1")
   suspend fun authenticate(email: String, passwordHash: String): User?

   /**
    * Update user's last login time
    * @param userId The user ID
    * @param loginTime The login timestamp
    */
   @Query("UPDATE user SET last_login = :loginTime, updated_at = :updateTime WHERE id = :userId")
   suspend fun updateLastLogin(userId: Long, loginTime: Long, updateTime: Date = Date())

   /**
    * Update user's password
    * @param userId The user ID
    * @param newPasswordHash New SHA-256 hashed password
    */
   @Query("UPDATE user SET password_hash = :newPasswordHash, updated_at = :updateTime WHERE id = :userId")
   suspend fun updatePassword(userId: Long, newPasswordHash: String, updateTime: Date = Date())

   /**
    * Reset user's password (admin function)
    * @param userId The user ID
    * @param tempPasswordHash Temporary password hash
    * @param resetBy Admin user ID who reset the password
    */
   @Transaction
   suspend fun resetPassword(userId: Long, tempPasswordHash: String, resetBy: Long, updateTime: Date = Date()) {
      updatePassword(userId, tempPasswordHash, updateTime)
      // Log the reset action (could insert into audit log)
   }

   // ==================== USER MANAGEMENT OPERATIONS ====================

   /**
    * Activate a user account
    * @param userId The user ID
    */
   @Query("UPDATE user SET is_active = 1, updated_at = :updateTime WHERE id = :userId")
   suspend fun activateUser(userId: Long, updateTime: Date = Date())

   /**
    * Deactivate a user account
    * @param userId The user ID
    * @param reason Optional deactivation reason
    */
   @Query("UPDATE user SET is_active = 0, updated_at = :updateTime WHERE id = :userId")
   suspend fun deactivateUser(userId: Long, updateTime: Date = Date())

   /**
    * Update user's role
    * @param userId The user ID
    * @param newRole New user role
    */
   @Query("UPDATE user SET role = :newRole, updated_at = :updateTime WHERE id = :userId")
   suspend fun updateUserRole(userId: Long, newRole: UserRole, updateTime: Date = Date())

   /**
    * Update user's session timeout
    * @param userId The user ID
    * @param timeoutMinutes New session timeout in minutes
    */
   @Query("UPDATE user SET session_timeout = :timeoutMinutes, updated_at = :updateTime WHERE id = :userId")
   suspend fun updateSessionTimeout(userId: Long, timeoutMinutes: Int, updateTime: Date = Date())

   /**
    * Update user's profile information
    */
   @Query("""
        UPDATE user 
        SET full_name = :fullName, 
            phone = :phone,
            updated_at = :updateTime
        WHERE id = :userId
    """)
   suspend fun updateProfile(
      userId: Long,
      fullName: String?,
      phone: String?,
      updateTime: Date = Date()
   )

   // ==================== STATISTICS AND ANALYTICS ====================

   /**
    * Get the total number of users
    * @return Total user count
    */
   @Query("SELECT COUNT(*) FROM user")
   suspend fun getTotalUserCount(): Int

   /**
    * Get the number of active users
    * @return Active user count
    */
   @Query("SELECT COUNT(*) FROM user WHERE is_active = 1")
   suspend fun getActiveUserCount(): Int

   /**
    * Get user statistics by role
    * @return Map of role to count
    */
   @Query("SELECT role, COUNT(*) as count FROM user WHERE is_active = 1 GROUP BY role")
   suspend fun getUserStatistics(): Map<UserRole, Int>

   /**
    * Get the number of users created today
    * @return Today's user creation count
    */
   @Query("""
        SELECT COUNT(*) FROM user 
        WHERE DATE(created_at/1000, 'unixepoch') = DATE('now')
    """)
   suspend fun getTodayUserCount(): Int

   /**
    * Get the number of users who logged in today
    * @return Today's login count
    */
   @Query("""
        SELECT COUNT(*) FROM user 
        WHERE last_login IS NOT NULL 
        AND DATE(last_login/1000, 'unixepoch') = DATE('now')
    """)
   suspend fun getTodayLoginCount(): Int

   /**
    * Get user login frequency (users with most logins)
    * @param limit Maximum number of users to return
    * @return List of users with login counts (simplified)
    */
   @Query("""
        SELECT u.*, 
               (SELECT COUNT(*) FROM user_session WHERE user_id = u.id) as login_count
        FROM user u
        WHERE u.is_active = 1
        ORDER BY login_count DESC
        LIMIT :limit
    """)
   suspend fun getMostActiveUsers(limit: Int = 10): List<UserWithStats>

   /**
    * Get users who haven't logged in for X days
    * @param days Number of days
    * @return List of inactive users
    */
   @Query("""
        SELECT * FROM user 
        WHERE last_login < :cutoffDate 
        AND is_active = 1 
        ORDER BY last_login ASC
    """)
   suspend fun getInactiveUsersSince(cutoffDate: Date): List<User>

   // ==================== VALIDATION AND CHECKS ====================

   /**
    * Check if an email already exists
    * @param email The email to check
    * @return True if the email exists
    */
   @Query("SELECT COUNT(*) FROM user WHERE email = :email")
   suspend fun emailExists(email: String): Boolean

   /**
    * Check if there's at least one admin user
    * @return True if at least one admin exists
    */
   @Query("SELECT COUNT(*) FROM user WHERE role = 'ADMIN' AND is_active = 1")
   suspend fun hasAdminUser(): Boolean

   /**
    * Check if a user can be deleted (not the last admin)
    * @param userId The user ID to check
    * @return True if the user can be deleted
    */
   suspend fun canDeleteUser(userId: Long): Boolean {
      val user = getById(userId) ?: return false

      if (user.role == UserRole.ADMIN) {
         val adminCount = getAdminsCount()
         return adminCount > 1
      }

      return true
   }

   /**
    * Get count of admin users
    */
   @Query("SELECT COUNT(*) FROM user WHERE role = 'ADMIN' AND is_active = 1")
   suspend fun getAdminsCount(): Int

   /**
    * Validate user data before creation/update
    */
   suspend fun validateUser(email: String, userId: Long? = null): Pair<Boolean, String?> {
      if (email.isBlank()) {
         return Pair(false, "Email cannot be empty")
      }

      val existingUser = getByEmail(email)
      if (existingUser != null && existingUser.id != userId) {
         return Pair(false, "Email already exists")
      }

      return Pair(true, null)
   }

   // ==================== BULK OPERATIONS ====================

   /**
    * Create multiple users at once
    * @param users List of users to create
    * @return List of created user IDs
    */
   @Transaction
   suspend fun createUsers(users: List<User>): List<Long> {
      val ids = mutableListOf<Long>()
      users.forEach { user ->
         try {
            ids.add(insert(user))
         } catch (e: Exception) {
            // Log error but continue with others
            println("Failed to create user ${user.email}: ${e.message}")
         }
      }
      return ids
   }

   /**
    * Update multiple users' status
    * @param userIds List of user IDs
    * @param active New active status
    */
   @Query("""
        UPDATE user 
        SET is_active = :active, 
            updated_at = :updateTime
        WHERE id IN (:userIds)
    """)
   suspend fun updateUsersStatus(userIds: List<Long>, active: Boolean, updateTime: Date = Date())

   /**
    * Reset multiple users' passwords
    * @param userIds List of user IDs
    * @param tempPasswordHash Temporary password hash
    * @param resetBy Admin user ID
    */
   @Transaction
   suspend fun bulkResetPasswords(userIds: List<Long>, tempPasswordHash: String, resetBy: Long) {
      val updateTime = Date()
      userIds.forEach { userId ->
         resetPassword(userId, tempPasswordHash, resetBy, updateTime)
      }
   }

   // ==================== SESSION MANAGEMENT ====================

   /**
    * Clear all user sessions (set last_login to null)
    */
   @Query("UPDATE user SET last_login = NULL, updated_at = :updateTime")
   suspend fun clearAllSessions(updateTime: Date = Date())

   /**
    * Clear session for specific user
    * @param userId The user ID
    */
   @Query("UPDATE user SET last_login = NULL, updated_at = :updateTime WHERE id = :userId")
   suspend fun clearUserSession(userId: Long, updateTime: Date = Date())

   /**
    * Get users with expired sessions
    * @param sessionTimeoutMinutes Session timeout in minutes
    * @return List of users with expired sessions
    */
   @Query("""
        SELECT * FROM user 
        WHERE last_login IS NOT NULL 
        AND (strftime('%s', 'now') * 1000 - last_login) > (:sessionTimeoutMinutes * 60 * 1000)
        AND is_active = 1
    """)
   suspend fun getUsersWithExpiredSessions(sessionTimeoutMinutes: Int = 30): List<User>

   // ==================== AUDIT AND LOGGING ====================

   /**
    * Get user activity log (requires separate activity table)
    * This is a simplified version using the user table
    */
   @Query("""
        SELECT u.email, 
               u.last_login,
               COUNT(v.id) as visits_created,
               MAX(v.created_at) as last_activity
        FROM user u
        LEFT JOIN visit v ON u.id = v.created_by
        WHERE u.is_active = 1
        GROUP BY u.id
        ORDER BY u.last_login DESC
    """)
   suspend fun getUserActivityLog(): List<UserActivity>

   /**
    * Get user creation statistics by date
    */
   @Query("""
        SELECT DATE(created_at/1000, 'unixepoch') as date,
               COUNT(*) as count
        FROM user
        GROUP BY DATE(created_at/1000, 'unixepoch')
        ORDER BY date DESC
        LIMIT 30
    """)
   suspend fun getUserCreationStats(): Map<String, Int>

   // ==================== DATA CLASSES FOR COMPLEX QUERIES ====================

   /**
    * Data class for users with statistics
    */
   data class UserWithStats(
      val user: User,
      val loginCount: Int,
      val visitsCreated: Int = 0
   )

   /**
    * Data class for user activity
    */
   data class UserActivity(
      val email: String,
      val lastLogin: Date?,
      val visitsCreated: Int,
      val lastActivity: Date?
   )

   /**
    * Data class for user creation statistics
    */
   data class UserCreationStat(
      val date: String,
      val count: Int
   )

   // ==================== TRANSACTIONAL OPERATIONS ====================

   /**
    * Create a new user with validation
    * @param user The user to create
    * @return Pair of user ID and error message
    */
   @Transaction
   suspend fun createUserWithValidation(user: User): Pair<Long?, String> {
      val (isValid, errorMessage) = validateUser(user.email)

      if (!isValid) {
         return Pair(null, errorMessage ?: "Invalid user data")
      }

      try {
         val userId = insert(user)
         return Pair(userId, "User created successfully")
      } catch (e: Exception) {
         return Pair(null, "Failed to create user: ${e.message}")
      }
   }

   /**
    * Update user with validation
    * @param user The user to update
    * @return Pair of success boolean and error message
    */
   @Transaction
   suspend fun updateUserWithValidation(user: User): Pair<Boolean, String> {
      val (isValid, errorMessage) = validateUser(user.email, user.id)

      if (!isValid) {
         return Pair(false, errorMessage ?: "Invalid user data")
      }

      try {
         update(user)
         return Pair(true, "User updated successfully")
      } catch (e: Exception) {
         return Pair(false, "Failed to update user: ${e.message}")
      }
   }

   /**
    * Delete user with safety checks
    * @param userId The user ID to delete
    * @param deletedBy Admin user ID performing deletion
    * @return Pair of success boolean and error message
    */
   @Transaction
   suspend fun deleteUserWithChecks(userId: Long, deletedBy: Long): Pair<Boolean, String> {
      // Check if user exists
      val user = getById(userId) ?: return Pair(false, "User not found")

      // Check if it's the last admin
      if (!canDeleteUser(userId)) {
         return Pair(false, "Cannot delete the last administrator")
      }

      // Check if user is deleting themselves
      if (userId == deletedBy) {
         return Pair(false, "Cannot delete your own account")
      }

      try {
         deleteById(userId)
         return Pair(true, "User deleted successfully")
      } catch (e: Exception) {
         return Pair(false, "Failed to delete user: ${e.message}")
      }
   }

   /**
    * Change user role with validation
    * @param userId The user ID
    * @param newRole New role
    * @param changedBy Admin user ID
    * @return Pair of success boolean and error message
    */
   @Transaction
   suspend fun changeUserRole(userId: Long, newRole: UserRole, changedBy: Long): Pair<Boolean, String> {
      val user = getById(userId) ?: return Pair(false, "User not found")

      // Check if changing the last admin to non-admin
      if (user.role == UserRole.ADMIN && newRole != UserRole.ADMIN) {
         val adminCount = getAdminsCount()
         if (adminCount <= 1) {
            return Pair(false, "Cannot change the last administrator to another role")
         }
      }

      // Cannot change own role to non-admin if last admin
      if (userId == changedBy && newRole != UserRole.ADMIN) {
         val adminCount = getAdminsCount()
         if (adminCount <= 1) {
            return Pair(false, "Cannot change your own role from administrator")
         }
      }

      try {
         updateUserRole(userId, newRole)
         return Pair(true, "User role updated successfully")
      } catch (e: Exception) {
         return Pair(false, "Failed to update user role: ${e.message}")
      }
   }

   // ==================== BACKUP AND RESTORE ====================

   /**
    * Export all users to a list (for backup)
    * @return List of all users (without passwords for security)
    */
   @Query("SELECT id, email, full_name, phone, role, is_active, created_at, updated_at FROM user ORDER BY id ASC")
   suspend fun exportAllUsers(): List<UserExport>

   /**
    * Import users from a list (for restore)
    * @param users List of users to import
    * @param defaultPasswordHash Default password hash for imported users
    */
   @Transaction
   suspend fun importUsers(users: List<User>, defaultPasswordHash: String) {
      // Clear existing users except the current admin
      deleteAllNonEssential()

      // Insert imported users
      users.forEach { user ->
         // If user has no password hash, use default
         val userToInsert = if (user.passwordHash.isBlank()) {
            user.copy(passwordHash = defaultPasswordHash)
         } else {
            user
         }
         insert(userToInsert)
      }
   }

   /**
    * Data class for user export (without sensitive data)
    */
   data class UserExport(
      val id: Long,
      val email: String,
      val fullName: String?,
      val phone: String?,
      val role: UserRole,
      val isActive: Boolean,
      val createdAt: Date,
      val updatedAt: Date
   )

   // ==================== INITIALIZATION ====================

   /**
    * Initialize with default admin user if no users exist
    * @param adminEmail Default admin email
    * @param adminPasswordHash Hashed default password
    */
   @Transaction
   suspend fun initializeDefaultAdmin(adminEmail: String, adminPasswordHash: String): Boolean {
      val userCount = getTotalUserCount()

      if (userCount == 0) {
         val defaultAdmin = User(
            email = adminEmail,
            passwordHash = adminPasswordHash,
            fullName = "Administrator",
            role = UserRole.ADMIN,
            sessionTimeout = 30,
            isActive = true,
            createdAt = Date(),
            updatedAt = Date()
         )

         try {
            insert(defaultAdmin)
            return true
         } catch (e: Exception) {
            println("Failed to create default admin: ${e.message}")
            return false
         }
      }

      return false
   }

   /**
    * Check and fix database consistency
    * @return List of fixed issues
    */
   @Transaction
   suspend fun checkAndFixConsistency(): List<String> {
      val fixes = mutableListOf<String>()

      // Ensure at least one admin exists
      if (!hasAdminUser()) {
         // Find any active user and make them admin
         val anyUser = getAllActive().value?.firstOrNull()
         if (anyUser != null) {
            updateUserRole(anyUser.id, UserRole.ADMIN)
            fixes.add("Promoted user ${anyUser.email} to administrator (no admin found)")
         }
      }

      // Fix users with invalid emails
      val invalidEmailUsers = searchUsers(emailQuery = "@", isActive = null)
         .filter { !it.email.contains("@") }

      invalidEmailUsers.forEach { user ->
         deactivateUser(user.id)
         fixes.add("Deactivated user ${user.email} (invalid email)")
      }

      return fixes
   }
}
