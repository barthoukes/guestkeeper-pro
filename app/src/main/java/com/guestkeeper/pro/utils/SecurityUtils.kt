package com.guestkeeper.pro.utils

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    private const val AES_ALGORITHM = "AES"
    private const val AES_TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private val SECRET_KEY = "GuestKeeperPro2024!".toByteArray().copyOf(16)

    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun generateRandomPassword(length: Int = 8): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*"
        val random = Random()
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    fun generateUsername(email: String): String {
        return email.substringBefore("@")
            .replace(Regex("[^a-zA-Z0-9]"), "")
            .lowercase()
            .take(20)
    }

    fun encrypt(text: String): String {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val secretKey = SecretKeySpec(SECRET_KEY, AES_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(text.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedText: String): String {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val secretKey = SecretKeySpec(SECRET_KEY, AES_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        return emailRegex.matches(email)
    }

    fun isValidPhone(phone: String): Boolean {
        val phoneRegex = Regex("^[+]?[0-9]{10,15}\$")
        return phoneRegex.matches(phone.replace("\\s".toRegex(), ""))
    }

    fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size != 2) return email
        
        val localPart = parts[0]
        val domain = parts[1]
        
        return if (localPart.length <= 2) {
            "$localPart***@$domain"
        } else {
            "${localPart.first()}***${localPart.last()}@$domain"
        }
    }

    fun maskPhone(phone: String): String {
        return if (phone.length <= 4) {
            "***$phone"
        } else {
            "***${phone.takeLast(4)}"
        }
    }
}
