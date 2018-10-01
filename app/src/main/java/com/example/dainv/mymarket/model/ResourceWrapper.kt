package com.example.dainv.mymarket.model

class ResourceWrapper<R>(var r :R?,var resourceState: ResourceState,var message:String){

    companion object {
        fun <R> loading() =  ResourceWrapper<R>(null,ResourceState.LOADING,"")
        fun <R> error(error:String)= ResourceWrapper<R>(null,ResourceState.ERROR,error)
        fun <R> success(r:R) = ResourceWrapper<R>(r,ResourceState.SUCCESS,"success")
    }
}
