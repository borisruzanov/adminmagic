package com.boris.expert.adminmagic.repository

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DataRepository {

    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        lateinit var context: Context

        private var dataRepository: DataRepository? = null

        fun getInstance(mContext: Context): DataRepository {
            context = mContext
            if (dataRepository == null) {
                dataRepository = DataRepository()
            }
            return dataRepository!!
        }
    }



}