package com.example.dainv.mymarket.view.main.category

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.ResourceWrapper
import com.example.dainv.mymarket.repository.ItemRepository
import javax.inject.Inject

class CategoryViewModel
@Inject
constructor(
        val itemRepository: ItemRepository
) : ViewModel() {
     var  listCategory :LiveData<ResourceWrapper<List<Category>?>>
    init {
        listCategory = itemRepository.getAllCategory()
    }

}