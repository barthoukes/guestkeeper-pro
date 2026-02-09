package com.guestkeeper.pro.database.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Room Database Type Converters for Date and Time objects
 *
 * These converters handle the conversion between:
 * - Date <-> Long (timestamp)
 * - Time <-> String (HH:mm)
 * - Calendar <-> Long
 * - Timezone handling
 */

object DateConverter {

   // ==================== DATE CONVERTERS ====================

   /**
    * Convert Long timestamp to Date
    * Returns null if the timestamp is null
    */
   @TypeConverter
   @JvmStatic
   fun fromTimestamp(value: Long?): Date? {
      return value?.let { Date(it) }
   }

   /**
    * Convert Date to Long timestamp
    * Returns null if the Date is null
    */
   @TypeConverter
   @JvmStatic
   fun dateToTimestamp(date: Date?): Long? {
      return date?.time
   }

   /**
    * Convert Date to ISO 8601 String (yyyy-MM-dd'T'HH:mm:ss'Z')
    * Useful for database storage and JSON serialization
    */
   @TypeConverter
   @JvmStatic
   fun dateToIsoString(date: Date?): String? {
      return date?.let {
         val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
         sdf.timeZone = TimeZone.getTimeZone("UTC")
         sdf.format(it)
      }
   }

   /**
    * Convert ISO 8601 String to Date
    */
   @TypeConverter
   @JvmStatic
   fun fromIsoString(dateString: String?): Date? {
      return dateString?.let {
         try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(it)
         } catch (e: Exception) {
            null
         }
      }
   }

   // ==================== TIME CONVERTERS ====================

   /**
    * Convert Time (HH:mm) String to Date with current date
    * Useful for storing time-only values
    */
   @TypeConverter
   @JvmStatic
   fun timeStringToDate(timeString: String?): Date? {
      return timeString?.let {
         try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val time = sdf.parse(it)

            val calTime = Calendar.getInstance()
            calTime.time = time

            calendar.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            calendar.time
         } catch (e: Exception) {
            null
         }
      }
   }

   /**
    * Convert Date to Time String (HH:mm)
    */
   @TypeConverter
   @JvmStatic
   fun dateToTimeString(date: Date?): String? {
      return date?.let {
         val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
         sdf.format(it)
      }
   }

   /**
    * Convert Date to Time String with seconds (HH:mm:ss)
    */
   @TypeConverter
   @JvmStatic
   fun dateToTimeWithSecondsString(date: Date?): String? {
      return date?.let {
         val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
         sdf.format(it)
      }
   }

   // ==================== CALENDAR CONVERTERS ====================

   /**
    * Convert Calendar to Long timestamp
    */
   @TypeConverter
   @JvmStatic
   fun calendarToTimestamp(calendar: Calendar?): Long? {
      return calendar?.timeInMillis
   }

   /**
    * Convert Long timestamp to Calendar
    */
   @TypeConverter
   @JvmStatic
   fun timestampToCalendar(timestamp: Long?): Calendar? {
      return timestamp?.let {
         val calendar = Calendar.getInstance()
         calendar.timeInMillis = it
         calendar
      }
   }

   // ==================== DATE STRING FORMAT CONVERTERS ====================

   /**
    * Convert Date to formatted date string (yyyy-MM-dd)
    */
   @TypeConverter
   @JvmStatic
   fun dateToFormattedString(date: Date?): String? {
      return date?.let {
         val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
         sdf.format(it)
      }
   }

   /**
    * Convert formatted date string (yyyy-MM-dd) to Date
    */
   @TypeConverter
   @JvmStatic
   fun formattedStringToDate(dateString: String?): Date? {
      return dateString?.let {
         try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.parse(it)
         } catch (e: Exception) {
            null
         }
      }
   }

   /**
    * Convert Date to readable date string (dd/MM/yyyy)
    */
   @TypeConverter
   @JvmStatic
   fun dateToReadableString(date: Date?): String? {
      return date?.let {
         val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
         sdf.format(it)
      }
   }

   /**
    * Convert Date to date and time string (dd/MM/yyyy HH:mm)
    */
   @TypeConverter
   @JvmStatic
   fun dateToDateTimeString(date: Date?): String? {
      return date?.let {
         val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
         sdf.format(it)
      }
   }

   // ==================== TIMEZONE HANDLING ====================

   /**
    * Convert Date to UTC timestamp (milliseconds since epoch)
    */
   @TypeConverter
   @JvmStatic
   fun dateToUtcTimestamp(date: Date?): Long? {
      return date?.let {
         val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
         calendar.time = it
         calendar.timeInMillis
      }
   }

   /**
    * Convert UTC timestamp to Date in local timezone
    */
   @TypeConverter
   @JvmStatic
   fun utcTimestampToDate(timestamp: Long?): Date? {
      return timestamp?.let {
         val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
         calendar.timeInMillis = it
         calendar.time
      }
   }

   /**
    * Get timezone ID string from Date
    */
   @TypeConverter
   @JvmStatic
   fun dateToTimezoneId(date: Date?): String? {
      return TimeZone.getDefault().id
   }

   // ==================== DURATION CONVERTERS ====================

   /**
    * Convert duration in minutes to Date (from now)
    */
   @TypeConverter
   @JvmStatic
   fun minutesToDate(minutes: Int?): Date? {
      return minutes?.let {
         val calendar = Calendar.getInstance()
         calendar.add(Calendar.MINUTE, it)
         calendar.time
      }
   }

   /**
    * Convert two Dates to duration in minutes
    */
   @TypeConverter
   @JvmStatic
   fun datesToDurationMinutes(startDate: Date?, endDate: Date?): Int? {
      return if (startDate != null && endDate != null) {
         val duration = endDate.time - startDate.time
         (duration / (1000 * 60)).toInt()
      } else {
         null
      }
   }

   // ==================== ARRAY CONVERTERS ====================

   /**
    * Convert List of Dates to comma-separated String of timestamps
    */
   @TypeConverter
   @JvmStatic
   fun dateListToString(dates: List<Date>?): String? {
      return dates?.joinToString(",") { it.time.toString() }
   }

   /**
    * Convert comma-separated String of timestamps to List of Dates
    */
   @TypeConverter
   @JvmStatic
   fun stringToDateList(dateString: String?): List<Date>? {
      return dateString?.split(",")?.mapNotNull {
         try {
            Date(it.toLong())
         } catch (e: Exception) {
            null
         }
      }
   }

   // ==================== VALIDATION HELPERS ====================

   /**
    * Check if a date string is valid
    */
   fun isValidDateString(dateString: String, format: String = "yyyy-MM-dd"): Boolean {
      return try {
         val sdf = SimpleDateFormat(format, Locale.getDefault())
         sdf.isLenient = false
         sdf.parse(dateString)
         true
      } catch (e: Exception) {
         false
      }
   }

   /**
    * Check if a time string is valid
    */
   fun isValidTimeString(timeString: String): Boolean {
      return try {
         val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
         sdf.isLenient = false
         sdf.parse(timeString)
         true
      } catch (e: Exception) {
         false
      }
   }

   /**
    * Check if a date is within a valid range (not too far in past or future)
    */
   fun isDateInValidRange(date: Date, maxPastDays: Int = 365, maxFutureDays: Int = 365): Boolean {
      val calendar = Calendar.getInstance()
      val now = calendar.time

      // Calculate min and max dates
      calendar.time = now
      calendar.add(Calendar.DAY_OF_YEAR, -maxPastDays)
      val minDate = calendar.time

      calendar.time = now
      calendar.add(Calendar.DAY_OF_YEAR, maxFutureDays)
      val maxDate = calendar.time

      return date.after(minDate) && date.before(maxDate)
   }

   // ==================== FORMATTING HELPERS (Non-TypeConverter) ====================

   /**
    * Format date for display based on locale
    */
   fun formatDateForDisplay(date: Date, locale: Locale = Locale.getDefault()): String {
      return SimpleDateFormat("dd/MM/yyyy", locale).format(date)
   }

   /**
    * Format time for display based on locale
    */
   fun formatTimeForDisplay(date: Date, locale: Locale = Locale.getDefault()): String {
      return SimpleDateFormat("HH:mm", locale).format(date)
   }

   /**
    * Format date and time for display
    */
   fun formatDateTimeForDisplay(date: Date, locale: Locale = Locale.getDefault()): String {
      return SimpleDateFormat("dd/MM/yyyy HH:mm", locale).format(date)
   }

   /**
    * Format date relative to now (Today, Yesterday, etc.)
    */
   fun formatDateRelative(date: Date): String {
      val calendarNow = Calendar.getInstance()
      val calendarDate = Calendar.getInstance()
      calendarDate.time = date

      return when {
         isSameDay(calendarNow, calendarDate) -> "Today"
         isYesterday(calendarNow, calendarDate) -> "Yesterday"
         isSameWeek(calendarNow, calendarDate) -> "This week"
         isSameYear(calendarNow, calendarDate) -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(date)
         else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
      }
   }

   /**
    * Format duration between two dates
    */
   fun formatDuration(startDate: Date, endDate: Date): String {
      val duration = endDate.time - startDate.time
      val minutes = duration / (1000 * 60)

      return when {
         minutes < 60 -> "${minutes}m"
         minutes < 1440 -> "${minutes / 60}h ${minutes % 60}m"
         else -> "${minutes / 1440}d ${(minutes % 1440) / 60}h"
      }
   }

   // ==================== DATE COMPARISON HELPERS ====================

   /**
    * Check if two dates are on the same day
    */
   private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
         cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
   }

   /**
    * Check if date is yesterday
    */
   private fun isYesterday(calNow: Calendar, calDate: Calendar): Boolean {
      val calYesterday = Calendar.getInstance()
      calYesterday.add(Calendar.DAY_OF_YEAR, -1)

      return calDate.get(Calendar.YEAR) == calYesterday.get(Calendar.YEAR) &&
         calDate.get(Calendar.DAY_OF_YEAR) == calYesterday.get(Calendar.DAY_OF_YEAR)
   }

   /**
    * Check if two dates are in the same week
    */
   private fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
         cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
   }

   /**
    * Check if two dates are in the same year
    */
   private fun isSameYear(cal1: Calendar, cal2: Calendar): Boolean {
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
   }

   // ==================== DATE MANIPULATION HELPERS ====================

   /**
    * Get start of day for a date (00:00:00)
    */
   fun getStartOfDay(date: Date): Date {
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.set(Calendar.HOUR_OF_DAY, 0)
      calendar.set(Calendar.MINUTE, 0)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)
      return calendar.time
   }

   /**
    * Get end of day for a date (23:59:59)
    */
   fun getEndOfDay(date: Date): Date {
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.set(Calendar.HOUR_OF_DAY, 23)
      calendar.set(Calendar.MINUTE, 59)
      calendar.set(Calendar.SECOND, 59)
      calendar.set(Calendar.MILLISECOND, 999)
      return calendar.time
   }

   /**
    * Add days to a date
    */
   fun addDays(date: Date, days: Int): Date {
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.add(Calendar.DAY_OF_YEAR, days)
      return calendar.time
   }

   /**
    * Add hours to a date
    */
   fun addHours(date: Date, hours: Int): Date {
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.add(Calendar.HOUR_OF_DAY, hours)
      return calendar.time
   }

   /**
    * Add minutes to a date
    */
   fun addMinutes(date: Date, minutes: Int): Date {
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.add(Calendar.MINUTE, minutes)
      return calendar.time
   }

   // ==================== DATE VALIDATION FOR VISITOR MANAGEMENT ====================

   /**
    * Check if a visit time is valid (not in the past and within business hours)
    */
   fun isValidVisitTime(arrivalTime: Date, businessStartHour: Int = 9, businessEndHour: Int = 17): Boolean {
      val calendar = Calendar.getInstance()
      calendar.time = arrivalTime

      // Check if not in the past
      val now = Date()
      if (arrivalTime.before(now)) {
         return false
      }

      // Check if within business hours (9 AM to 5 PM)
      val hour = calendar.get(Calendar.HOUR_OF_DAY)
      return hour in businessStartHour until businessEndHour
   }

   /**
    * Check if departure time is after arrival time
    */
   fun isValidDepartureTime(arrivalTime: Date, departureTime: Date): Boolean {
      return departureTime.after(arrivalTime)
   }

   /**
    * Check if visit duration is reasonable (not too long)
    */
   fun isValidVisitDuration(arrivalTime: Date, departureTime: Date, maxHours: Int = 8): Boolean {
      val duration = departureTime.time - arrivalTime.time
      val hours = duration / (1000 * 60 * 60)
      return hours <= maxHours
   }

   // ==================== SERIALIZATION FOR DATABASE ====================

   /**
    * Convert Date to database-safe string
    */
   fun dateToDatabaseString(date: Date): String {
      return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
   }

   /**
    * Parse database string to Date
    */
   fun databaseStringToDate(dateString: String): Date? {
      return try {
         SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString)
      } catch (e: Exception) {
         null
      }
   }

   // ==================== TIMEZONE CONVERSION ====================

   /**
    * Convert date from one timezone to another
    */
   fun convertTimezone(date: Date, fromZone: TimeZone, toZone: TimeZone): Date {
      val calendar = Calendar.getInstance(fromZone)
      calendar.time = date

      val targetCalendar = Calendar.getInstance(toZone)
      targetCalendar.timeInMillis = calendar.timeInMillis

      return targetCalendar.time
   }

   /**
    * Get current date in UTC
    */
   fun getCurrentUtcDate(): Date {
      val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
      return calendar.time
   }

   /**
    * Convert local date to UTC
    */
   fun localToUtc(date: Date): Date {
      return convertTimezone(date, TimeZone.getDefault(), TimeZone.getTimeZone("UTC"))
   }

   /**
    * Convert UTC to local date
    */
   fun utcToLocal(date: Date): Date {
      return convertTimezone(date, TimeZone.getTimeZone("UTC"), TimeZone.getDefault())
   }
}

/**
 * Additional converter class for Room database annotations
 * This class provides the TypeConverters that Room needs
 */
class Converters {

   @TypeConverter
   fun fromTimestamp(value: Long?): Date? = DateConverter.fromTimestamp(value)

   @TypeConverter
   fun dateToTimestamp(date: Date?): Long? = DateConverter.dateToTimestamp(date)

   @TypeConverter
   fun dateToIsoString(date: Date?): String? = DateConverter.dateToIsoString(date)

   @TypeConverter
   fun fromIsoString(dateString: String?): Date? = DateConverter.fromIsoString(dateString)

   @TypeConverter
   fun timeStringToDate(timeString: String?): Date? = DateConverter.timeStringToDate(timeString)

   @TypeConverter
   fun dateToTimeString(date: Date?): String? = DateConverter.dateToTimeString(date)

   @TypeConverter
   fun calendarToTimestamp(calendar: Calendar?): Long? = DateConverter.calendarToTimestamp(calendar)

   @TypeConverter
   fun timestampToCalendar(timestamp: Long?): Calendar? = DateConverter.timestampToCalendar(timestamp)

   @TypeConverter
   fun dateToFormattedString(date: Date?): String? = DateConverter.dateToFormattedString(date)

   @TypeConverter
   fun formattedStringToDate(dateString: String?): Date? = DateConverter.formattedStringToDate(dateString)
}
