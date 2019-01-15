package com.example.dainv.mymarket.ui.main.profile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.ui.BaseFragment
import com.example.dainv.mymarket.databinding.FragmentProfileBinding
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.glide.GlideApp
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.additem.DialogMethodAddPhoto
import com.example.dainv.mymarket.ui.items.ListItemActivity
import com.example.dainv.mymarket.ui.map.MapActivity
import com.example.dainv.mymarket.ui.marked.ItemsMarkedActivity
import com.example.dainv.mymarket.ui.my.items.MyItemsActivity
import com.example.dainv.mymarket.ui.my.stands.MyStandsActivity
import com.example.dainv.mymarket.ui.notification.SettingNotificationActivity
import com.example.dainv.mymarket.ui.stand_followed.StandFollowedActivity
import com.example.dainv.mymarket.ui.transaction.TransactionActivity
import com.example.dainv.mymarket.util.Util
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import java.io.File

class ProfileFragment : BaseFragment() {
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
    private val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    private val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    lateinit var profileViewModel: ProfileViewModel
    private lateinit var mCurrentImagePath: String

    companion object {
        val TAG = "profile fragment"
        fun newInstance(): ProfileFragment {
            val profileFragment = ProfileFragment()
            return profileFragment
        }
    }

    override fun getLayoutID() = R.layout.fragment_profile
    var viewDataBinding: FragmentProfileBinding? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding = DataBindingUtil.bind(view!!)
        profileViewModel = ViewModelProviders.of(this, viewModelFactory)[ProfileViewModel::class.java]
        initView()
        profileViewModel.getProfile()
        profileViewModel.profileLiveData.observe(this, Observer {
            loadingLayout.visibility = if (it?.resourceState == ResourceState.LOADING) View.VISIBLE else View.GONE
            Timber.e("get profile state ${it!!.resourceState.toString()}")
            viewDataBinding!!.profileSource = it
            if (it!!.resourceState == ResourceState.SUCCESS) {
                txtName.text = it.r!!.name
                txtPhone.text = it.r!!.phone
                it?.r?.avatar?.let {
                    GlideApp.with(this)
                            .load(Constant.BASE_URL + it)
                            .into(avatar)
                    GlideApp.with(this)
                            .load(Constant.BASE_URL + it)
                            .into(avatarPrimary)
                }
            }
        })
        profileViewModel.logoutResult.observe(this, Observer {
            loadingLayout.visibility = if (it?.resourceState == ResourceState.LOADING) View.VISIBLE else View.GONE
            it?.r?.let { success ->
                if (success) {
                    logout()
                }
            }
        })
        profileViewModel.avatarUpdateLiveData.observe(this, Observer {
            loadingLayout.visibility = if (it?.resourceState == ResourceState.LOADING) View.VISIBLE else View.GONE
            it?.r?.let { success ->
                profileViewModel.getProfile()

            }
        })
    }

    private fun initView() {
        txtStandFollow.setOnClickListener {
            startActivityWithAnimation(Intent(activity, StandFollowedActivity::class.java))
        }
        icLogout.setOnClickListener {
            profileViewModel.logout()
        }
        txtMyStand.setOnClickListener {
            startActivityWithAnimation(Intent(activity, MyStandsActivity::class.java))
        }
        txtItemUploaded.setOnClickListener {
            val intent = Intent(activity, MyItemsActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(ListItemActivity.IS_MY_ITEM_KEY, true)
            intent.putExtra("bundle", bundle)
            startActivityWithAnimation(intent)
        }
        txtItemMarked.setOnClickListener {
            startActivityWithAnimation(Intent(activity, ItemsMarkedActivity::class.java))
        }
        txtNotification.setOnClickListener {
            startActivityWithAnimation(Intent(activity, SettingNotificationActivity::class.java))
        }
        txtChangePass.setOnClickListener {
            ChangePassDialog.newInstance().show(fragmentManager, ChangePassDialog.javaClass.name)
        }
        txtTransaction.setOnClickListener {
            val intent = Intent(activity, TransactionActivity::class.java)
            startActivityWithAnimation(intent)
        }
        avatar.setOnClickListener {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
        }
    }

    private fun showDialogSelectPickPhoto() {
        val dialogMethodAddPhoto = DialogMethodAddPhoto.newsInstance(getString(R.string.update_avatar))
        dialogMethodAddPhoto.clickCallback = {
            if (it == R.id.actionOne) {
                dispatchTakePictureIntent()
            } else if (it == R.id.actionTwo) {
                //TODO need check permission read storage
                val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, AddItemActivity.REQUEST_PICk_PHOTO)
            }
        }
        dialogMethodAddPhoto.show(fragmentManager, DialogMethodAddPhoto.TAG)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    showDialogSelectPickPhoto()
                } else {
                    val showRationale1 = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, CAMERA_PERMISSION)
                    val showRationale2 = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, READ_EXTERNAL_STORAGE_PERMISSION)
                    val showRationale3 = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, WRITE_EXTERNAL_STORAGE_PERMISSION)
                    if (showRationale1 && showRationale2 && showRationale3) {
                    } else {

                    }
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    private fun checkMultiplePermissions(permissionCode: Int) {
        val permissions = arrayOf(CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION)
        if (!hasPermissions(context, *permissions)) {
            requestPermissions(permissions, permissionCode)
        } else {
            showDialogSelectPickPhoto()
        }
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    Util.createImageFile(context!!)
                } catch (ex: Throwable) {
                    // ErrorResponse occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    mCurrentImagePath = it.absolutePath
                    val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, AddItemActivity.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val dialogSelect = fragmentManager!!.findFragmentByTag(DialogMethodAddPhoto.TAG)
            if (dialogSelect != null && dialogSelect is DialogMethodAddPhoto) {
                dialogSelect.dismiss()
            }
            if (requestCode == AddItemActivity.REQUEST_TAKE_PHOTO) {
                galleryAddPic(mCurrentImagePath)
                profileViewModel.updateAvatar(mCurrentImagePath)
            } else if (requestCode == AddItemActivity.REQUEST_PICk_PHOTO) {
                profileViewModel.updateAvatar(Util.getRealPathFromURI(context!!, data!!.data))
            }
        }

    }

    private fun galleryAddPic(path: String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(path)
            mediaScanIntent.data = Uri.fromFile(f)
            context!!.sendBroadcast(mediaScanIntent)
        }
    }
}