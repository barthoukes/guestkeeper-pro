package com.guestkeeper.pro.model

enum class VisitStatus {
    ACTIVE {
        override fun toString(): String = "Active"
        override fun getColorCode(): Int = 0xFF4CAF50.toInt() // Green
    },
    COMPLETED {
        override fun toString(): String = "Completed"
        override fun getColorCode(): Int = 0xFF2196F3.toInt() // Blue
    },
    OVERDUE {
        override fun toString(): String = "Overdue"
        override fun getColorCode(): Int = 0xFFF44336.toInt() // Red
    },
    CANCELLED {
        override fun toString(): String = "Cancelled"
        override fun getColorCode(): Int = 0xFF9E9E9E.toInt() // Gray
    };

    abstract fun getColorCode(): Int

    companion object {
        fun fromString(value: String): VisitStatus {
            return when (value.uppercase()) {
                "ACTIVE" -> ACTIVE
                "COMPLETED" -> COMPLETED
                "OVERDUE" -> OVERDUE
                "CANCELLED" -> CANCELLED
                else -> ACTIVE
            }
        }
    }
}

