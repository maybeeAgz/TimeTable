package com.bignerdranch.android.myapplication.View.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.fragment.app.setFragmentResultListener
import com.bignerdranch.android.myapplication.R


class FirstFrag(day:String, names_text: List<String>, mestos_text: List<String>, nazvs_text: List<String>, lessons_name_text: List<String>,l:Int) : Fragment() {

    val day_now = day
    val names_text_ = names_text
    val mestos_text_ = mestos_text
    val nazvs_text_ = nazvs_text
    val lessons_name_text_ = lessons_name_text
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setFragmentResultListener("requestKey") { key, bundle ->
            val result = bundle.getString("bundleKey")
        }
        val view = inflater.inflate(R.layout.fragment,null)

        var name_1 = view.findViewById<TextView>(R.id.Teacher)
        var nazv_1 = view.findViewById  <TextView>(R.id.nazv)
        var mesto_1 = view.findViewById <TextView>(R.id.mesto)
        var name_2 = view.findViewById  <TextView>(R.id.Teacher_2)
        var nazv_2 = view.findViewById  <TextView>(R.id.nazv_2)
        var mesto_2 = view.findViewById <TextView> (R.id.mesto_2)
        var name_3 = view.findViewById  <TextView>(R.id.Teacher_3)
        var nazv_3 = view.findViewById  <TextView>(R.id.nazv_3)
        var mesto_3 = view.findViewById <TextView> (R.id.mesto_3)
        var name_4 = view.findViewById  <TextView>(R.id.Teacher_4)
        var nazv_4 = view.findViewById  <TextView>(R.id.nazv_4)
        var mesto_4 = view.findViewById <TextView> (R.id.mesto_4)
        var name_5 = view.findViewById  <TextView>(R.id.Teacher_5)
        var nazv_5 = view.findViewById  <TextView>(R.id.nazv_5)
        var mesto_5 = view.findViewById <TextView> (R.id.mesto_5)
        var but_prob = view.findViewById<TextView> (R.id.textView3)

        var p_1 = view.findViewById<LinearLayout>(R.id.chet)
        var p_2 = view.findViewById<LinearLayout>(R.id.para_2_v)
        var p_3 = view.findViewById<LinearLayout>(R.id.para_3_v)
        var p_4 = view.findViewById<LinearLayout>(R.id.para_4_v)
        var p_5 = view.findViewById<LinearLayout>(R.id.para_5_v)

        var lesson_name_1 = view.findViewById<TextView>(R.id.lesson_name_1)
        var lesson_name_2 = view.findViewById<TextView>(R.id.lesson_name_2)
        var lesson_name_3 = view.findViewById<TextView>(R.id.lesson_name_3)
        var lesson_name_4 = view.findViewById<TextView>(R.id.lesson_name_4)
        var lesson_name_5 = view.findViewById<TextView>(R.id.lesson_name_5)

        val names = listOf(name_1, name_2, name_3, name_4, name_5)
        val nazvs = listOf(nazv_1, nazv_2, nazv_3, nazv_4, nazv_5)
        val mestos = listOf(mesto_1, mesto_2, mesto_3, mesto_4, mesto_5)
        val layouts = listOf(p_1,p_2,p_3,p_4,p_5)
        val lessons_name = listOf(lesson_name_1, lesson_name_2, lesson_name_3, lesson_name_4, lesson_name_5)

        preparation(names,nazvs,mestos,layouts,lessons_name)
        //Toast.makeText(context, format_translate(lessons_name_text_[0].toString()), Toast.LENGTH_SHORT).show()

        return view




    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun preparation(names:List<TextView>,nazvs:List<TextView>,mestos:List<TextView>,layouts:List<LinearLayout>,lessons_name:List<TextView>) {
        //получение кода
        try {
            for(m in 0..4) {
                var remake_text = format_translate(lessons_name_text_[m].toString())
                        nazvs[m].text = ""
                        names[m].text = names_text_[m].toString()
                        nazvs[m].text = nazvs_text_[m].toString()
                        mestos[m].text = mestos_text_[m].toString()
                        lessons_name[m].text = remake_text
                        if((remake_text == "Зачет с оценкой") or (remake_text == "Экзамен")){
                            lessons_name[m].background = getDrawable(resources,
                                R.drawable.red_pole,null)
                        }else if((remake_text == "Практическое занятие") or (remake_text == "Лабораторная работа")){
                            lessons_name[m].background = getDrawable(resources,
                                R.drawable.green_pole,null)
                        }else if((remake_text == "Семинар") or (remake_text == "Контрольная работа")){
                            lessons_name[m].background = getDrawable(resources,
                                R.drawable.light_red_pole,null)
                        }else if((remake_text == "Лекция") or (remake_text == "Групповое занятие")){
                            lessons_name[m].background = getDrawable(resources,
                                R.drawable.blue_pole,null)
                        }else{
                            lessons_name[m].background = getDrawable(resources,
                                R.drawable.purple_pole,null)
                        }
                    }
        } catch (e: Exception) {
            Log.wtf("My Application", "Error", e)
            e.printStackTrace()
        }
        //for(m in 0..4) {
        //    if (nazvs[m].text == "") {
        //        layouts[m].visibility = View.GONE
        //    }
        //}

    }
    //форматирование строки
    fun format_translate(format: String): String {
        if (format == "экз.") {
            return "Экзамен"
        } else if (format == "ЗСО"){
            return "Зачет с оценкой"
        } else if (format == "л") {
            return "Лекция"
        } else if(format == "ГЗ"){
            return "Групповое занятие"
        }else if(format == "Контр.р"){
            return "Контрольная работа"
        }else if(format == "ЛР"){
            return "Лабораторная работа"
        }else if(format == "См"){
            return "Семинар"
        }else if(format == "ПЗ"){
            return "Практическое занятие"
        }else{
            return format
        }
    }



}