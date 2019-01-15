package com.example.dainv.mymarket.ui.stand.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseDialog
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.entity.Stand
import kotlinx.android.synthetic.main.dialog_write_comment.*

class DialogWriteComment : BaseDialog() {

    companion object {
        fun newInstance() = DialogWriteComment()
    }
    private lateinit var standDetailViewModel: StandDetailViewModel
    override fun getLayoutId() = R.layout.dialog_write_comment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            standDetailViewModel = ViewModelProviders.of(it,viewModelFactory)[StandDetailViewModel::class.java]

        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnConfirm.setOnClickListener {
            if (edtComment.text.isNullOrEmpty()){
                edtComment.error = getString(R.string.comment_require)
                return@setOnClickListener
            }
            try {
                val stand = activity!!.intent.getParcelableExtra<Stand>(StandDetailActivity.STAND_KEY)
                if (stand != null){
                    standDetailViewModel.createComment(edtComment.text.toString(),stand.standID)
                    dismiss()
                }else{
                    dismiss()
                }
            }catch (e : Exception){

            }
        }
        standDetailViewModel.createCommentResult.observe(this, Observer {
            it?.let {resource->
                if (resource.resourceState == ResourceState.LOADING){
                }else{
                    if (resource.resourceState == ResourceState.SUCCESS){
                    }
                }
            }
        })
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}