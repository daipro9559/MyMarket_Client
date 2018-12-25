package com.example.dainv.mymarket.entity

class ResourceWrapper<R>(var r :R?,var resourceState: ResourceState,var message:String?,val throwable: Throwable? =null){

    companion object {
        fun <R> loading() =  ResourceWrapper<R>(null,ResourceState.LOADING,null,null)
        fun <R> error(error:String,throwable: Throwable?)= ResourceWrapper<R>(null,ResourceState.ERROR,error,throwable)
        fun <R> success(r:R): ResourceWrapper<R?> = ResourceWrapper(r,ResourceState.SUCCESS,"success",null)
    }
}
