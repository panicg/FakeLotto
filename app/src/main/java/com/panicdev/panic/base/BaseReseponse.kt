package com.panicdev.panic.base

import com.google.gson.annotations.SerializedName

class BaseResponse<E> {

    @SerializedName("result")
    val result: DataResult? = null

    @SerializedName("data")
    var data: E? = null

    val isSuccess : Boolean
        get() = result?.resultCode == 200
}


data class AnyResponse(val ignore : Any?)

class DataResult {

    @SerializedName("resultCode")
    var resultCode: Int = 200

    @SerializedName("resultMsg")
    var errorMessage: String = ""

    companion object {
        val Unknown: DataResult
            get() {

                val obj = DataResult()
                obj.resultCode = 501
                obj.errorMessage = "Unknown data"

                return obj
            }
    }

}