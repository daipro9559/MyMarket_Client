package com.example.dainv.mymarket.service.response

import com.example.dainv.mymarket.model.Category
import com.google.gson.annotations.Expose

class CategoryResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:List<Category>
)