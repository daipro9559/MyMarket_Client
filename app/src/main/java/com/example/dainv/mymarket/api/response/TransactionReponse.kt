package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.entity.Transaction
import com.google.gson.annotations.Expose

data class TransactionResponse(
        @Expose
        val success: Boolean,
        @Expose
        val message: String,
        @Expose
        val lastPage: Boolean,
        @Expose
        val data: List<Transaction>
)