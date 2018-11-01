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
import android.app.SearchManager
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.MenuItem
import com.example.dainv.mymarket.searchable.MySuggestionProvider
import android.animation.LayoutTransition
import android.support.v4.app.Fragment
import android.widget.LinearLayout
import timber.log.Timber


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
//        replaceFragment(ListItemFragment.newInstance(),ListItemFragment.TAG)
        setSupportActionBar(toolBar)
        if ( intent.hasExtra("category")){
            categorySelect = intent.getParcelableExtra("category")
            title = categorySelect.categoryName
        }
        if (categorySelect != null) {
            queryMap["categoryID"] = categorySelect.categoryID.toString()
        }
        initView()
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        listItemViewModel.getItem(queryMap)
        viewObserve()
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
//        enableHomeBack()
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

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id._menu_filter -> {
                // show dialog filter
            }
        }
        return super.onOptionsItemSelected(item)
    }
}