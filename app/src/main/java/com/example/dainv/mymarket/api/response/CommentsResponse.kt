package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Comment
import com.google.gson.annotations.Expose

data class CommentsResponse(@Expose
                            val success: Boolean,
                            @Expose
                            val message: String,
                            @Expose
                            val lastPage: Boolean,
                            @Expose
                            val data: List<Comment>)