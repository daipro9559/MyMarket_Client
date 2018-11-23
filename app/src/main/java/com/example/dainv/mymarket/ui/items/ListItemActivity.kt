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
import android.view.inputmethod.InputMethodManager
import android.app.SearchManager
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.MenuItem
import com.example.dainv.mymarket.searchable.MySuggestionProvider
import android.animation.LayoutTransition
import android.app.Activity
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.transition.ChangeTransform
import android.support.transition.TransitionManager
import android.support.transition.TransitionSet
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dainv.mymarket.model.*
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.main.item.marked.ItemAdapter
import com.example.dainv.mymarket.util.Util
import kotlinx.android.synthetic.main.fragment_filter.*
import timber.log.Timber


class ListItemActivity : BaseActivity(), ListItemView {
    companion object {
        val CATEGORY_KEY = "category_key"
        val IS_MY_ITEM_KEY = "is_my_item"
    }

    private lateinit var listItemViewModel: ListItemViewModel
    private lateinit var bottomSheetBehavior: MyBottomSheetBehavior<CoordinatorLayout>
    @Inject
    lateinit var itemAdapter: Lazy<ItemMainAdapter>
    private val queryMap = HashMap<String, String>()
    private var categorySelect: Category? = null
    private var districtSelect: District? = null
    private var provinceSelected: Province? = null
    @Inject
    lateinit var listItemPresenter: ListItemPresenter
    private var searchView: SearchView? = null
    private var currentPage: Int = 0
    private var isLoadmore: Boolean = false

    // is my item list
    private var isMyItems: Boolean = false

    private var isUndoDelete = false
    private var positionDeleted: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
//        replaceFragment(ListItemFragment.newInstance(),ListItemFragment.TAG)
        setSupportActionBar(toolBar)
        if (intent.hasExtra("bundle")) {
            val bundle = intent.getBundleExtra("bundle")
            categorySelect = bundle.getParcelable(CATEGORY_KEY)
            title = categorySelect?.categoryName
            isMyItems = bundle.getBoolean(IS_MY_ITEM_KEY, false)
        }
        initView()
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        submitFilter()
//        listItemViewModel.getItem(queryMap)
        viewObserve()
        listItemPresenter.onCreate()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent!!.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            searchView?.setQuery(query, true)

        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.viewContainer, fragment, tag)
                .commit()
    }

    private fun initView() {
        // hide view when show MyItems
        if (isMyItems) {
            cardViewIsNewest.visibility = View.GONE
            cardViewFree.visibility = View.GONE
            viewDivider1.visibility = View.GONE
            title = getString(R.string.my_items_upload)
        }
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
                if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                    appBar.animate()
                            .translationY(0f)
//                        .alpha(1 - p1)
                            .setDuration(0)
                            .start()
                } else if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                    appBar.animate()
                            .translationY(-appBar.height.toFloat())
//                        .alpha(1 - p1)
                            .setDuration(0)
                            .start()
                }
            }
        })
        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        recyclerView.itemAnimator = DefaultItemAnimator()
        itemAdapter.get().isMyItems = isMyItems
        recyclerView.adapter = itemAdapter.get()
        if (isMyItems) {
            val recycleViewSwipeHelper = RecycleViewSwipeHelper(applicationContext)
            val itemTouchHelper = ItemTouchHelper(recycleViewSwipeHelper)
            itemTouchHelper.attachToRecyclerView(recyclerView)
            recycleViewSwipeHelper.onSwipedCompleted.subscribe {
                positionDeleted = it
                val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.deleting), Snackbar.LENGTH_LONG)
                snackbar.duration = 2000
                var isUndo = false
                snackbar.setAction(R.string.undo_delete) {
                    itemAdapter.get().notifyItemInserted(positionDeleted)
                    isUndo = true
                }
                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (!isUndo) {
                            listItemViewModel.deleteItem(itemAdapter.get().items[positionDeleted].itemID)
                        }
                    }
                })
                snackbar.show()
                itemAdapter.get().notifyItemRemoved(it)
            }
        }
        edtPriceFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkboxFree.isChecked = false
                if (s.toString().isNotEmpty()) {
                    txtConvertPriceFrom.text = Util.convertPriceToText(s.toString().toLong(),applicationContext)
                } else {
                    txtConvertPriceFrom.text = getString(R.string.not_set)
                }
            }
        })
        edtPriceTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkboxFree.isChecked = false
                if (s.toString().isNotEmpty()) {
                    txtConvertPriceTo.text = Util.convertPriceToText(s.toString().toLong(),applicationContext)
                } else {
                    txtConvertPriceTo.text = getString(R.string.not_set)
                }
            }
        })

        filter.setOnClickListener {
            currentPage = 0
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            submitFilter()
        }
        saveFilter.setOnClickListener {
            currentPage = 0
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
                        title = categorySelect?.categoryName
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
                        if (provinceSelected?.provinceID != 0) {
                            titleDistrict.visibility = View.VISIBLE
                            cardDistrictFilter.visibility = View.VISIBLE
                        } else {
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
                        districtFilter.text = it.districtName

                    }
                    dialogSelectDistrict.show(supportFragmentManager, DialogSelectDistrict.TAG)
                }
            })
        }
        needToBuy.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && needToSell.isChecked) needToSell.isChecked = false
        }
        needToSell.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && needToBuy.isChecked) needToBuy.isChecked = false
        }
        priceUp.setOnCheckedChangeListener { _, isChecked -> if (isChecked && priceDown.isChecked) priceDown.isChecked = false }
        priceDown.setOnCheckedChangeListener { _, isChecked -> if (isChecked && priceUp.isChecked) priceUp.isChecked = false }
        checkboxNewest.setOnCheckedChangeListener { checkbox, isChecked ->
            currentPage = 0
            checkboxNewest.isChecked = isChecked
            submitFilter(false)
        }
        checkboxFree.setOnCheckedChangeListener { checkbox, isChecked ->
            currentPage = 0
            checkboxFree.isChecked = isChecked
            submitFilter(false)
        }
    }

    private fun viewObserve() {
        //deleted completed
        listItemViewModel.deleteResult.observe(this, Observer {
            it?.r?.let {success->
                if (success){
                    itemAdapter.get().items.removeAt(positionDeleted)
                }else{
                    Toast.makeText(applicationContext,R.string.can_not_delete,Toast.LENGTH_LONG).show()
                    itemAdapter.get().notifyItemInserted(positionDeleted)
                }
            }
        })
        itemAdapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(this, ItemDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("item", it)
            intent.putExtra("itemBundle", bundle)
            intent.action = ItemDetailActivity.ACTION_NORMAL
            startActivity(intent)
        })
        itemAdapter.get().loadMoreLiveData.observe(this, Observer {
            isLoadmore = true
            currentPage++
            submitFilter(false)
        })
        listItemViewModel.listItemLiveData.observe(this, Observer {
            if (!isLoadmore) {
                viewLoading(it!!.resourceState, loadingLayout)
            }
            it!!.r?.let { it ->
                appBar.setExpanded(true)
//                itemAdapter.get().submitList(it)
                itemAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadmore) {
                    itemAdapter.get().addItems(it.data)
                    isLoadmore = false
                } else {
                    itemAdapter.get().swapItems(it.data)
                }
            }
        })
        listItemViewModel.errorLiveData.observe(this, Observer {
            if (it == ErrorResponse.UN_AUTHORIZED) {
                unAuthorize()
            }
        })
        listItemViewModel.itemMarkResult.observe(this, Observer {
            it?.r?.let {
                if (it) {
                    Toast.makeText(applicationContext, getString(R.string.mark_item_completed), Toast.LENGTH_LONG).show()
                }
            }
        })
        listItemViewModel.itemUnmarkResult.observe(this, Observer {
            it?.r?.let { it ->
                if (it) {
                    Toast.makeText(applicationContext, getString(R.string.unmark_item_completed), Toast.LENGTH_LONG).show()
                }
            }
        })
        itemAdapter.get().itemMarkObserve.subscribe { itemId -> listItemViewModel.markItem(itemId) }
        itemAdapter.get().itemUnMarkObserve.subscribe { itemId -> listItemViewModel.unMarkItem(itemId) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!isMyItems) {
            menuInflater!!.inflate(R.menu.menu_search, menu)
            searchViewInit(menu)
        }
        return true
    }


    private fun searchViewInit(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.menu_search).actionView as SearchView
        searchView?.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView?.apply {
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    SearchRecentSuggestions(applicationContext, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                            .saveRecentQuery(p0, null)
                    submitFilter(false)
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }

            })
            val searchEdittext = findViewById<EditText>(R.id.search_src_text)
            searchEdittext.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchEdittext.text.toString().isNullOrEmpty()) {
                        submitFilter(false)
                        onActionViewCollapsed()
                        clearFocus()
                        toolBar.collapseActionView()
                        return@setOnEditorActionListener true
                    }
                }
                false
            }
            this.inputType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
            this.imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(p0: Int): Boolean {
//                    searchView?.setQuery(query,false)
//                    submitFilter(false)
                    return true
                }

                override fun onSuggestionClick(p0: Int): Boolean {
//                    searchView?.setQuery(query,false)
//                    submitFilter(false)
                    return false
                }

            })
//            suggestionsAdapter.cursor.
            val searchBar = searchView?.findViewById(R.id.search_bar) as LinearLayout
            searchBar.layoutTransition = LayoutTransition()
            TransitionManager.beginDelayedTransition(searchView!!, TransitionSet()
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

    private fun submitFilter(isLoadPreference: Boolean = true) {
        val filterParamBuilder = FilterParam.Builder()
        if (isMyItems) {
            filterParamBuilder.setIsMyItems(true)
        } else {
            filterParamBuilder.setCategory(categorySelect?.categoryID)
                    .setProvince(provinceSelected?.provinceID)
                    .setDistrict(districtSelect?.districtID)
                    .setIsNewest(checkboxNewest.isChecked)
                    .setNeedToBuy(needToBuy.isChecked)
                    .setNeedToSell(needToSell.isChecked)
                    .setPriceMax(if (edtPriceTo.text.isNullOrBlank()) null else edtPriceTo.text.toString().toLong())
                    .setPriceMin(if (edtPriceFrom.text.isNullOrBlank()) null else edtPriceFrom.text.toString().toLong())
                    .setPriceDown(priceDown.isChecked)
                    .setPriceUp(priceUp.isChecked)
                    .setIsFree(checkboxFree.isChecked)
                    .setQuery(if (searchView?.query.isNullOrEmpty()) null else searchView?.query.toString())
        }
                .setPage(currentPage)
//                .build()
        listItemPresenter.submit(filterParamBuilder.build(), isLoadPreference)
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
        hideKeyborad()
        listItemViewModel.getItem(queryMap)
    }

    private fun hideKeyborad() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val viewCurrentFocus = currentFocus
        viewCurrentFocus?.let {
            imm.hideSoftInputFromWindow(viewCurrentFocus.windowToken, 0)
        }
    }

}