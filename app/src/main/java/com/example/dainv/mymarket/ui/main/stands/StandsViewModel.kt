package com.example.dainv.mymarket.ui.main.stands

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.entity.Category
import com.example.dainv.mymarket.entity.District
import com.example.dainv.mymarket.entity.Province
import com.example.dainv.mymarket.repository.StandRepository
import javax.inject.Inject

class StandsViewModel
    @Inject
    constructor(private val standRepository: StandRepository)
    :ViewModel() {

    private val listStandTrigger = MutableLiveData<Map<String,String>>()

    val listStandLiveData = Transformations.switchMap(listStandTrigger){
        return@switchMap standRepository.getStands(it)
    }
    init {
        getStands(0,null,null,null)

    }
    fun getStands(page:Int,district: District?,province: Province?,category: Category?){
        val query = HashMap<String,String>()
        query["page"] = page.toString()
        province?.let {
            query["provinceID"] = province.provinceID.toString()
        }
        district?.let {
            query["districtID"] = district.districtID.toString()
        }
        category?.let {
            query["categoryID"] = category.categoryID.toString()
        }
        listStandTrigger.value = query
    }

}