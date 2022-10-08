package com.remotecontroller.connection

import android.util.TypedValue
import com.remotecontroller.ui.MainActivity

fun dpTopx(dp: Int):Int{
    //метод перевода px в dp
    val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            MainActivity.getR().displayMetrics
    )
    return Math.round(px)
}