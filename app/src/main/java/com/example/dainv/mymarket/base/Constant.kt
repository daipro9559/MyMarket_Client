package com.example.dainv.mymarket.base


object Constant {
    const val IS_DEBUG = true
    //192.168.1.102
    //10.102.0.23
    //10.30.0.227
//<<<<<<< Updated upstream
    const val BASE_URL = "http://10.30.0.227:3000/v1/"
//=======
//    const val BASE_URL = "http://10.102.0.23:3000/v1/"
//>>>>>>> Stashed changes
    const val HEADER = "Authorization"
    // share Preference key
    const val TOKEN = "token bear"
    const val TOKEN_FIREBASE ="token firebase"
    const val APP_NAME = "My Market"
    const val USER_TYPE ="user_type"
    const val USER_ID = "user_id "
    const val IS_HAVE_ADDRESS="is have address"


    // save filter value
    const val CATEGORY_NAME = "category_name_filter"
    const val CATEGORY_ID = "category_id_filter"
    const val PROVINCE_NAME = "province_name_filter"
    const val PROVINCE_ID = "province_id_filter"
    const val DISTRICT_ID = "district_id_filter"
    const val DISTRICT_NAME= "district_name_filter"
    const val ACTION_REQUEST_TRANSACTION = "request confirm transaction "

}