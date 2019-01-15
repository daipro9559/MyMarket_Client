package com.example.dainv.mymarket.ui.main.stands

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.card.MaterialCardView
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.entity.Category
import com.example.dainv.mymarket.entity.District
import com.example.dainv.mymarket.entity.Province
import com.example.dainv.mymarket.ui.BaseFragment
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.common.ItemStandAdapter
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.items.ListItemViewModel
import com.example.dainv.mymarket.ui.stand.detail.StandDetailActivity
import com.example.dainv.mymarket.util.Util
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_stands.*
import timber.log.Timber
import javax.inject.Inject

class StandsFragment : BaseFragment() {
    @Inject
    lateinit var itemStandAdapter: Lazy<ItemStandAdapter>
    lateinit var standsViewModel: StandsViewModel
    lateinit var listItemViewModel: ListItemViewModel

    companion object {
        val TAG = "stands fragment"
        fun newInstance(): StandsFragment {
            val standsFragment = StandsFragment()
            return standsFragment
        }
    }

    override fun getLayoutID() = R.layout.fragment_stands

    private var isLoadMore = false
    private var currentPage = 0
    //    private lateinit var bottomSheetBehaviorFilter: BottomSheetBehavior<MaterialCardView>
    private var categorySelect: Category? = null
    private var districtSelect: District? = null
    private var provinceSelected: Province? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolBar.setTitle(R.string.list_stand)
        standsViewModel = ViewModelProviders.of(this, viewModelFactory)[StandsViewModel::class.java]
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        initView()
//        recycleView.addItemDecoration(DividerItemDecoration(context, (recycleView.layoutManager as LinearLayoutManager).orientation))
        standsViewModel.listStandLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            it!!.r?.let { it ->
                itemStandAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadMore) {
                    itemStandAdapter.get().addItems(it.data)
                    isLoadMore = false
                } else {
                    itemStandAdapter.get().swapItems(it.data)
                }
            }
        })
        itemStandAdapter.get().loadMoreLiveData.observe(this, Observer {
            isLoadMore = true
            currentPage = it!!
            getStand()
        })
        itemStandAdapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(activity, StandDetailActivity::class.java)
            intent.putExtra(StandDetailActivity.STAND_KEY, it)
            intent.putExtra(StandDetailActivity.IS_MY_STAND, false)
            startActivityWithAnimation(intent)
        })
    }

    private fun initView() {
        recycleView.layoutManager = GridLayoutManager(context, 2)
        recycleView.adapter = itemStandAdapter.get()
        cardCategory.setOnClickListener {
            listItemViewModel.getAllCategory().observe(this, Observer {
                if (it!!.resourceState == ResourceState.SUCCESS) {
                    val categoryAll = Util.categoryAll(context!!)
                    val arrayList = ArrayList<Category>(it.r)
                    arrayList.add(0, categoryAll)
                    val dialoSelectCategory = DialogSelectCategory.newInstance(arrayList)
                    dialoSelectCategory.callback = {
                        currentPage = 0
                        categorySelect = it
                        txtCategory.text = categorySelect?.categoryName
                        getStand()
                    }
                    dialoSelectCategory.show(fragmentManager, DialogSelectCategory.TAG)
                }
            })
        }
        cardProvinceFilter.setOnClickListener {
            listItemViewModel.getAllProvince().observe(this, Observer {
                if (it?.resourceState == ResourceState.SUCCESS) {
                    val arrayList = ArrayList<Province>(it.r)
                    arrayList.add(0, Province(0, getString(R.string.all_province)))
                    val dialogSelectProvince = DialogSelectProvince.newInstance(arrayList)
                    dialogSelectProvince.callback = {
                        currentPage = 0
                        provinceFilter.text = it.provinceName
                        provinceSelected = it
                        if (provinceSelected?.provinceID != 0) {
                            cardDistrictFilter.visibility = View.VISIBLE
                        } else {
                            cardDistrictFilter.visibility = View.GONE
                        }
                        districtFilter.text = getString(R.string.all_district)
                        districtSelect = null
                        getStand()
                    }
                    dialogSelectProvince.show(fragmentManager, DialogSelectProvince.TAG)
                }
            })
        }
        cardDistrictFilter.setOnClickListener {
            listItemViewModel.getDistrics(provinceSelected!!.provinceID).observe(this, Observer {
                if (it?.resourceState == ResourceState.SUCCESS) {
                    val arrayList = ArrayList<District>(it.r)
                    arrayList.add(0, District(0, getString(R.string.all_district), provinceSelected!!.provinceID))
                    val dialogSelectDistrict = DialogSelectDistrict.newInstance(arrayList)
                    dialogSelectDistrict.callback = {
                        currentPage = 0
                        districtSelect = it
                        districtFilter.text = it.districtName
                        getStand()
                    }
                    dialogSelectDistrict.show(fragmentManager, DialogSelectDistrict.TAG)
                }
            })
        }
    }

    private fun getStand(){
        var districtFetch : District? = if(districtSelect != null && districtSelect?.districtID ==0){
            null
        }else{
            districtSelect
        }
        var provinceFetch : Province? = if(provinceSelected != null && provinceSelected?.provinceID ==0){
            null
        }else{
            provinceSelected
        }
        var categoryFetch : Category? = if(categorySelect != null && categorySelect?.categoryID ==0){
            null
        }else{
            categorySelect
        }

        standsViewModel.getStands(currentPage,districtFetch,provinceFetch,categoryFetch)
    }

}