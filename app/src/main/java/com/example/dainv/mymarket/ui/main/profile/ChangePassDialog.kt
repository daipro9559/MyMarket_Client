package com.example.dainv.mymarket.ui.main.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_change_pass.*

class ChangePassDialog :BaseDialog() {
    private lateinit var profileViewModel: ProfileViewModel
    companion object {
        fun newInstance():ChangePassDialog{
            return ChangePassDialog()
        }
    }
    override fun getLayoutId()  = R.layout.dialog_change_pass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel = ViewModelProviders.of(this,viewModelFactory)[ProfileViewModel::class.java]

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnChange.setOnClickListener {
            // TODO check valid password or retype password
            profileViewModel.changPass(edtPasswordOld.text.toString().trim(),
                    edtPasswordNew.text.toString())
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
        profileViewModel.changePassResultLiveData.observe(this, Observer {
            it?.r?.success?.let {success->
                if (success){
                    Toast.makeText(context,R.string.change_pass_completed,Toast.LENGTH_LONG).show()
                    dismiss()
                }
            }
        })
    }


}