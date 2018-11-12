package com.example.dainv.mymarket.repository

import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class StandRepository @Inject constructor(sharePreferencHelper: SharePreferencHelper)
    :BaseRepository(sharePreferencHelper) {
}