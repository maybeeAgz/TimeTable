package com.bignerdranch.android.myapplication.Model.Domain

import kotlinx.coroutines.CoroutineScope
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser

class FormatTextUseCase(private val downloadInterface:DownloadInterface, coroutineScope: CoroutineScope,name:String, date: String){


    fun execute(name:String, r1:String = downloadInterface.execute(), date: String):ArrayList<ArrayList<String>>{
        var names = arrayListOf<String>("", "", "", "", "")
        var mesto = arrayListOf<String>("", "", "", "", "")
        var nazv = arrayListOf<String>("", "", "", "", "")
        var lesson_name = arrayListOf<String>("", "", "", "", "")

        val name = name
        val csvParser = CSVParser(
            (r1).reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )

        for (line in csvParser) {
            val Group = line.get(0)
            val Les = line.get(2)
            val Aud = line.get(3)
            val Name = line.get(6)
            val Subject = line.get(8)
            val Subj_type = line.get(9)
            val Date = line.get(10)
            if (date == Date) {
                if ((Group == name) or (Name == name) or (Aud == name)) {
                    names[Les.toInt() - 1] = Name
                    nazv[Les.toInt() - 1] = Subject
                    mesto[Les.toInt() - 1] = Aud
                    lesson_name[Les.toInt() - 1] = Subj_type
                }
            }
        }


        var list = arrayListOf(names,mesto,nazv,lesson_name)
        return list
    }


}