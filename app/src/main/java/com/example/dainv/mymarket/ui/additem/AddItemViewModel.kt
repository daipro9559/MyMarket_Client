package com.example.dainv.mymarket.ui.additem

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.PersistableBundle
import com.example.dainv.mymarket.model.AddItemBody
import com.example.dainv.mymarket.service.UploadService
import com.google.gson.Gson
import javax.inject.Inject

class AddItemViewModel @Inject constructor(
        val context:Context,
        val gson :Gson,addItemCase: AddItemCase): ViewModel(){
    private val itemParam = MutableLiveData<AddItemBody>()
    val addItemResult = Transformations.switchMap(itemParam){
        return@switchMap addItemCase.sellItem(null,it)
    }

    public fun sellItem(itemBody: AddItemBody,listImagePath:ArrayList<String>? = null){
        val bundle = PersistableBundle()
        val itemBodyJson= gson.toJson(itemBody)
        bundle.putString(UploadService.ADD_ITEM_BODY_JSON,itemBodyJson)
        listImagePath?.let {
            val listImagePathJson = gson.toJson(listImagePath)
            bundle.putString(UploadService.LIST_IMAGE_PATH,listImagePathJson)
            UploadService.startService(context,bundle)
            return
        }
        UploadService.startService(context,bundle)
//        itemParam.value = itemBody
    }
}