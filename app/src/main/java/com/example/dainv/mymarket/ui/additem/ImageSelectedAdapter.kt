package com.example.dainv.mymarket.ui.additem

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemChooseImageBinding
import com.example.dainv.mymarket.databinding.ItemImageBinding
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ImageSelectedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: ArrayList<String> = ArrayList()
    private  val TYPE_SELECT= 1
    private val TYPE_SHOW = 2
    val chooseObserver = MutableLiveData<String>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        return if (p1==TYPE_SELECT){
            val chooseBinding = DataBindingUtil.inflate<ItemChooseImageBinding>(LayoutInflater.from(p0.context),
                    R.layout.item_choose_image,
                    p0,
                    false)
            ItemViewHolder(chooseBinding)
        }else{
            val imageBinding = DataBindingUtil.inflate<ItemImageBinding>(LayoutInflater.from(p0.context),
                    R.layout.item_image,
                    p0,
                    false)
            ItemViewHolder(imageBinding)
        }
    }

    override fun getItemCount(): Int {
        return items.size+1
    }
    override fun getItemViewType(position: Int): Int {
        return if (position+1==itemCount) TYPE_SELECT else TYPE_SHOW
    }

    public fun getItems() = items

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if(getItemViewType(p1) == TYPE_SELECT){
            val chooseViewHolder = p0 as ItemViewHolder<ItemChooseImageBinding>
            chooseViewHolder.getViewBinding().cardView.setOnClickListener{
                chooseObserver.value = ""
            }
        }else{
            val imageViewHolder = p0 as ItemViewHolder<ItemImageBinding>
            imageViewHolder.getViewBinding().imagePath = items[p1]
            imageViewHolder.getViewBinding().executePendingBindings()
            imageViewHolder.getViewBinding().deleted.setOnClickListener{
                items.removeAt(p1)
                notifyItemRemoved(p1)
            }
        }
    }

    public fun addItemToFirst(item:String){
        items.add(0,item)
        notifyItemChanged(0)
    }

}