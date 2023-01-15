package com.bignerdranch.android.myapplication.ViewModel

import android.widget.TextView
import com.bignerdranch.android.myapplication.R
import java.util.*

class GetDate() {
    fun month_in_string(number: Int): String {
        val month = arrayOf(
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        )
        return month[number]
    }

    fun number_month(number: String): String {
        if (((number.toInt() + 1) < 10)) {
            return "0" + (number.toInt() + 1).toString()
        } else {
            return (number.toInt() + 1).toString()
        }
    }

    fun GetDateForText(c:Calendar):String{
        return c.get(Calendar.DAY_OF_MONTH).toString() + "-" + number_month(c.get(Calendar.MONTH).toString()) + "-" + c.get(Calendar.YEAR).toString()
    }
    fun MonthAndDayNow(c:Calendar): String {
        c.time
        var DateNow: String = c.get(Calendar.DAY_OF_MONTH).toString() + " " + month_in_string(c.get(Calendar.MONTH)).toString() + " - " + (c.get(Calendar.YEAR))
        return DateNow
    }
    fun EngToRusTime(a:Int):Int{
        if(a == 1){
            return 7
        }else{
            return (a - 1)
        }
    }
}