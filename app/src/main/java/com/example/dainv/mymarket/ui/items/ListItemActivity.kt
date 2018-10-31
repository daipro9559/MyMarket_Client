package com.example.dainv.mymarket.ui.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.ErrorResponse
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_items.*
import javax.inject.Inject
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.view.inputmethod.InputMethodManager
import android.support.v4.view.MenuItemCompat.getActionView
import android.app.SearchManager
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.SearchView
import com.example.dainv.mymarket.searchable.MySuggestionProvider


class ListItemActivity : BaseActivity() {
    lateinit var listItemViewModel: ListItemViewModel
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private val queryMap = HashMap<String, String>()
    private lateinit var categorySelect: Category
    private lateinit var districtSelect: District
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        setSupportActionBar(toolBar)
        categorySelect = intent.getParcelableExtra("category")
        if (categorySelect != null) {
            queryMap["categoryID"] = categorySelect.categoryID.toString()
        }
        initView()
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        listItemViewModel.getItem(queryMap)
        viewObserve()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = itemAdapter.get()
        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!edtSearch.text.isNullOrEmpty()) {
                    queryMap["name"] = edtSearch.text.toString()
                    listItemViewModel.getItem(queryMap)

                } else {
                    if (queryMap.containsKey("name")) {
                        queryMap.remove("name")
                    }
                    listItemViewModel.getItem(queryMap)
                }
                val inputKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputKeyboard.hideSoftInputFromWindow(edtSearch.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun viewObserve() {
        itemAdapter.get().itemClickObserve.observe(this, Observer {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        })
        listItemViewModel.listItemLiveData.observe(this, Observer {
            it!!.r?.let {
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
        menuInflater!!.inflate(R.menu.menu_search,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(true)
        searchView.isQueryRefinementEnabled = true
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                SearchRecentSuggestions(applicationContext, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                        .saveRecentQuery(p0, null)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
        return true
    }
}