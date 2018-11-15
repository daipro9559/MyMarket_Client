package com.example.dainv.mymarket.ui.items

import android.animation.LayoutTransition
import android.app.ListActivity
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.R.id.recyclerView
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.ErrorResponse
import com.example.dainv.mymarket.searchable.MySuggestionProvider
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_list_item.*
import timber.log.Timber
import javax.inject.Inject

class ListItemFragment : BaseFragment() {

    companion object {
        const val TAG = "List item fragment"
        fun newInstance(): ListItemFragment {
            var bundle = Bundle()
            val listItemFragment = ListItemFragment()
            listItemFragment.arguments = bundle
            return listItemFragment
        }
    }

    lateinit var listItemViewModel: ListItemViewModel
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private val queryMap = HashMap<String, String>()
    private lateinit var categorySelect: Category
    private lateinit var districtSelect: District
    override fun getLayoutID() = R.layout.fragment_list_item
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity!!.intent.hasExtra("category")) {
            categorySelect = activity!!.intent.getParcelableExtra("category")
            toolBar.title = categorySelect.categoryName
        }
        if (categorySelect != null) {
            queryMap["categoryID"] = categorySelect.categoryID.toString()
        }
        initView()
        listItemViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[ListItemViewModel::class.java]
        listItemViewModel.getItem(queryMap)
        viewObserve()
    }



    private fun initView() {
//        enableHomeBack()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = itemAdapter.get()
        toolBar.inflateMenu(R.menu.menu_search)
        searchViewInit()
    }

    private fun viewObserve() {
        itemAdapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(activity!!, ItemDetailActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        })
        listItemViewModel.listItemLiveData.observe(this, Observer {
            it!!.r?.let {
//                itemAdapter.get().submitList(it)
            }
        })
        listItemViewModel.errorLiveData.observe(this, Observer {
            if (it == ErrorResponse.UN_AUTHORIZED) {
                val activityParent = (activity as ListItemActivity)
                activityParent.unAuthorize()
            }
        })
    }

    private fun searchViewInit() {
        val menu = toolBar.menu
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.menu_search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(activity!!.componentName))
        searchView.apply {
            setIconifiedByDefault(true)
            isQueryRefinementEnabled = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    SearchRecentSuggestions(context.applicationContext, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
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

        }
        val searchBar = searchView.findViewById(R.id.search_bar) as LinearLayout
        searchBar.layoutTransition = LayoutTransition()
    }

     fun setViewFilter(){

    }

     fun onNewIntent(intent: Intent){

    }

}

