package com.guestkeeper.pro.model

enum class UserRole {
    ADMIN {
        override fun toString(): String = "Administrator"
        override fun getPermissions(): List<String> = listOf(
            "ALL"
        )
    },
    RECEPTIONIST {
        override fun toString(): String = "Receptionist"
        override fun getPermissions(): List<String> = listOf(
            "REGISTER_VISITOR",
            "VIEW_VISITORS",
            "CHECKOUT_VISITOR",
            "VIEW_REPORTS"
        )
    };

    abstract fun getPermissions(): List<String>

    companion object {
        fun fromString(value: String): UserRole {
            return when (value.uppercase()) {
                "ADMIN" -> ADMIN
                "RECEPTIONIST" -> RECEPTIONIST
                else -> RECEPTIONIST
            }
        }
    }
}

