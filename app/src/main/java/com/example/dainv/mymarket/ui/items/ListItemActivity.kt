package com.example.dainv.mymarket.ui.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_items.*
import javax.inject.Inject
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.view.inputmethod.InputMethodManager
import android.app.SearchManager
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.MenuItem
import com.example.dainv.mymarket.searchable.MySuggestionProvider
import android.animation.LayoutTransition
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.transition.ChangeTransform
import android.support.transition.TransitionManager
import android.support.transition.TransitionSet
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dainv.mymarket.model.*
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.util.Util
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.fragment_filter.*
import timber.log.Timber


class ListItemActivity : BaseActivity(), ListItemView {


    private lateinit var listItemViewModel: ListItemViewModel
    private lateinit var bottomSheetBehavior: MyBottomSheetBehavior<CoordinatorLayout>
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private val queryMap = HashMap<String, String>()
    private var categorySelect: Category? = null
    private var districtSelect: District? = null
    private var provinceSelected: Province? = null
    @Inject
    lateinit var listItemPresenter: ListItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
//        replaceFragment(ListItemFragment.newInstance(),ListItemFragment.TAG)
        setSupportActionBar(toolBar)
        if (intent.hasExtra("category")) {
            categorySelect = intent.getParcelableExtra("category")
            title = categorySelect?.categoryName
        }
        initView()
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        submitFilter(true)
//        listItemViewModel.getItem(queryMap)
        viewObserve()
        listItemPresenter.onCreate()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent!!.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Timber.e(query)
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.viewContainer, fragment, tag)
                .commit()
    }

    private fun initView() {
        categorySelect?.let {
            categoryFilter.text = it.categoryName
        }
        bottomSheetBehavior = MyBottomSheetBehavior.from(rootViewBottom)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                Timber.e("" + p1)
                appBar.animate()
                        .translationY(-appBar.height + appBar.height * (1 - p1))
//                        .alpha(1 - p1)
                        .setDuration(0)
                        .start()
            }

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_COLLAPSED){
                    appBar.animate()
                            .translationY(0f)
//                        .alpha(1 - p1)
                            .setDuration(0)
                            .start()
                }else if (p1 == BottomSheetBehavior.STATE_EXPANDED){
                    appBar.animate()
                            .translationY(-appBar.height.toFloat())
//                        .alpha(1 - p1)
                            .setDuration(0)
                            .start()
                }
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = itemAdapter.get()
//        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (!edtSearch.text.isNullOrEmpty()) {
//                    queryMap["name"] = edtSearch.text.toString()
//                    listItemViewModel.getItem(queryMap)
//
//                } else {
//                    if (queryMap.containsKey("name")) {
//                        queryMap.remove("name")
//                    }
//                    listItemViewModel.getItem(queryMap)
//                }
//                val inputKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputKeyboard.hideSoftInputFromWindow(edtSearch.windowToken, 0)
//                return@OnEditorActionListener true
//            }
//            false
//        })
        edtPriceFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    txtConvertPriceFrom.text = Util.convertPriceToFormat(s.toString().toLong())
                }
            }

        })
        edtPriceTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    txtConvertPriceTo.text = Util.convertPriceToFormat(s.toString().toLong())
                }
            }
        })

        filter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            submitFilter()
        }
        saveFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            saveAndFilter()
        }
        cardCategoryFilter.setOnClickListener {
            listItemViewModel.getAllCategory().observe(this, Observer {
                if (it!!.resourceState == ResourceState.SUCCESS) {
                    val categoryAll = Util.categoryAll(applicationContext)
                    val arrayList = ArrayList<Category>(it.r)
                    arrayList.add(0, categoryAll)
                    val dialoSelectCategory = DialogSelectCategory.newInstance(arrayList)
                    dialoSelectCategory.callback = {
                        categorySelect = it
                        categoryFilter.text = categorySelect?.categoryName
                    }
                    dialoSelectCategory.show(supportFragmentManager, DialogSelectCategory.TAG)
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
                        provinceFilter.text = it.provinceName
                        provinceSelected = it
                        if (provinceSelected?.provinceID !=0) {
                            titleDistrict.visibility = View.VISIBLE
                            cardDistrictFilter.visibility = View.VISIBLE
                        }else{
                            titleDistrict.visibility = View.GONE
                            cardDistrictFilter.visibility = View.GONE
                        }
                        districtFilter.text = getString(R.string.all_district)
                        districtSelect = null
                    }
                    dialogSelectProvince.show(supportFragmentManager, DialogSelectProvince.TAG)
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
                        districtSelect = it
                        districtFilter.text =it.districtName

                    }
                    dialogSelectDistrict.show(supportFragmentManager, DialogSelectDistrict.TAG)
                }
            })
        }
        needToBuy.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && needToSell.isChecked) needToSell.isChecked =false
        }
        needToSell.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && needToBuy.isChecked) needToBuy.isChecked = false
        }
        priceUp.setOnCheckedChangeListener { buttonView, isChecked ->  if (isChecked && priceDown.isChecked) priceDown.isChecked = false}
        priceDown.setOnCheckedChangeListener { buttonView, isChecked ->  if (isChecked && priceUp.isChecked) priceUp.isChecked = false}

    }

    private fun viewObserve() {
        itemAdapter.get().itemClickObserve.observe(this, Observer {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        })
        listItemViewModel.listItemLiveData.observe(this, Observer {
            it!!.r?.let {
//                appBar.setExpanded(true)
                itemAdapter.get().submitList(it)
            }
        })
        listItemViewModel.errorLiveData.observe(this, Observer {
            if (it == ErrorResponse.UN_AUTHORIZED) {
                unAuthorize()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater!!.inflate(R.menu.menu_search, menu)
        searchViewInit(menu)
        return true
    }


    private fun searchViewInit(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.menu_search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.apply {
            setIconifiedByDefault(true)
            isQueryRefinementEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    SearchRecentSuggestions(applicationContext, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                            .saveRecentQuery(p0, null)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }

            })
            this.inputType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
            this.imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(p0: Int): Boolean {
                    return true
                }

                override fun onSuggestionClick(p0: Int): Boolean {
                    return true
                }

            })
            val searchBar = searchView.findViewById(R.id.search_bar) as LinearLayout
            searchBar.layoutTransition = LayoutTransition()
            TransitionManager.beginDelayedTransition(searchView, TransitionSet()
                    .addTransition(ChangeTransform())
                    .setDuration(500))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id._menu_filter -> {
                // show dialog filter
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        super.onBackPressed()
    }

    private fun submitFilter(isLoadPreference:Boolean = true) {
        val filterParam = FilterParam.Builder()
                .setCategory(categorySelect?.categoryID)
                .setProvince(provinceSelected?.provinceID)
                .setDistrict(districtSelect?.districtID)
                .setIsNewest(true)
                .setNeedToBuy(needToBuy.isChecked)
                .setNeedToSell(needToSell.isChecked)
                .setPriceMax(if (edtPriceTo.text.isNullOrBlank()) 0 else edtPriceTo.text.toString().toLong())
                .setPriceMin(if (edtPriceFrom.text.isNullOrBlank()) 0 else edtPriceTo.text.toString().toLong())
                .setPriceDown(priceDown.isChecked)
                .setPriceUp(priceUp.isChecked)
                .build()
        listItemPresenter.submit(filterParam,isLoadPreference)
    }

    private fun saveAndFilter() {

    }

    override fun setDefault(provinceName: String, districtName: String) {
        provinceFilter.text = provinceName
        districtFilter.text = districtName
    }

    override fun error(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun submit(queryMap: Map<String, String>) {
        listItemViewModel.getItem(queryMap)
    }
}