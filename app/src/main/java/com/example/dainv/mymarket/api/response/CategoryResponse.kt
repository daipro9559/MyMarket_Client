package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Category
import com.google.gson.annotations.Expose

class CategoryResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:ArrayList<Category>
)