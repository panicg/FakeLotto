package com.panicdev.panic.common

object LocalDataController : PreferenceController() {

    var isWriteReview: Boolean
        set(value) = putBoolean("isWriteReview", value)
        get() = getBoolean("isWriteReview", false)

    var useCount: Int
        set(value) = putInt("useCount", value)
        get() = getInt("useCount", 0)
}