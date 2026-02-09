package com.guestkeeper.pro.model

enum class UserType {
    GUEST {
        override fun toString(): String = "Guest"
    },
    SUPPLIER {
        override fun toString(): String = "Supplier"
    },
    CONTRACTOR {
        override fun toString(): String = "Contractor"
    };

    companion object {
        fun fromString(value: String): UserType {
            return when (value.uppercase()) {
                "GUEST" -> GUEST
                "SUPPLIER" -> SUPPLIER
                "CONTRACTOR" -> CONTRACTOR
                else -> GUEST
            }
        }

        fun getAllTypes(): List<String> {
            return values().map { it.toString() }
        }
    }
}

