package com.example.dainv.mymarket.util

import com.example.dainv.mymarket.entity.ErrorResponse

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
 */
 class ApiResponse<T> {
    var body: T? = null
    var code: Int = 500
    var throwable: Throwable? = null
    lateinit var errorResponse: ErrorResponse


    constructor(code: Int) {
        this.code = code
    }
    constructor(body: T,code: Int){
        this.body = body
        this.code = code
    }
    constructor(throwable: Throwable,code: Int){
        this.throwable = throwable
        this.code = code
    }
    companion object {
        fun <T> createSuccessResponse(body: T, code: Int) = ApiResponse(body, code)
        fun <T> createErrorResponse(throwable: Throwable,code: Int) = ApiResponse<T>(throwable,code)
    }

}
