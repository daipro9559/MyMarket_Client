package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.model.Transaction
import com.example.dainv.mymarket.model.User
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