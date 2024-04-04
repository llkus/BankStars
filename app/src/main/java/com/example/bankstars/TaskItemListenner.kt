package com.example.bankstars

import android.view.View

interface TaskItemListenner{

    fun setOnItemClickListenner(view: View, position:Int)

    fun setOnItemLongClickListenner(view: View, position:Int)
}