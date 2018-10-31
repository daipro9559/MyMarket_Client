package com.example.dainv.mymarket.searchable

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider : SearchRecentSuggestionsProvider(){
    companion object {
        const val AUTHORITY = "com.example.dainv.mymarket"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}