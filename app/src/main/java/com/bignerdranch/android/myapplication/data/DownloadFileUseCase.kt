package com.bignerdranch.android.myapplication.data

import com.bignerdranch.android.myapplication.Model.Domain.DownloadInterface
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URL

class DownloadFileUseCase(){
    fun execute(): String {

        val text = URL("http://a0755299.xsph.ru/wp-content/uploads/3-1-1.txt").readText()
        return text
    }

}