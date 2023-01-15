package com.bignerdranch.android.myapplication.View

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.*
import android.net.*
import android.os.Build
import android.os.Bundle
import java.util.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isInvisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bignerdranch.android.myapplication.Model.Domain.DownloadInterface
import com.bignerdranch.android.myapplication.data.DownloadFileUseCase
import com.bignerdranch.android.myapplication.View.Fragments.CustomDialogFragment
import com.bignerdranch.android.myapplication.R
import com.bignerdranch.android.myapplication.ViewModel.GetDate
import com.bignerdranch.android.myapplication.ViewModel.ViewModelClass
import com.bignerdranch.android.myapplication.ViewPagerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_second.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.net.URL

class MainActivity : FragmentActivity(), CustomDialogFragment.ReturnData{

    private lateinit var prefs: SharedPreferences
    private lateinit var data: SharedPreferences
    private lateinit var navigationView: NavigationView
    private lateinit var right: TextView
    private lateinit var left: TextView
    private lateinit var drawLayout: DrawerLayout
    private lateinit var test_text: TextView
    private lateinit var test_text_2: TextView
    private lateinit var progBar: ProgressBar
    private lateinit var day_1: TextView
    private lateinit var day_2: TextView
    private lateinit var day_3: TextView
    private lateinit var day_4: TextView
    private lateinit var day_5: TextView
    private lateinit var day_6: TextView
    private lateinit var monthDate: TextView
    private lateinit var UserInfo: TextView
    private lateinit var viewPager: ViewPager2
    var c:Calendar = Calendar.getInstance()
    var c_dop:Calendar = Calendar.getInstance()
    lateinit var br: BroadcastReceiver

    var r1 : String = ""// файл1
    private var dx:Float = 0.0f
    val tags_g = arrayListOf<String>()//теги групп
    val tags_t = arrayListOf<String>()//теги учителей
    val tags_a = arrayListOf<String>()//теги аудиторий
    var NamesArray = arrayListOf<ArrayList<ArrayList<String>>>()

    val scope = CoroutineScope(Dispatchers.Main)
    val scope_URL = CoroutineScope(Dispatchers.Main)

    private lateinit var vm: ViewModelClass


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_second)

        vm = ViewModelProvider(this).get(ViewModelClass::class.java)

        init()      // инициализация переменных


        /**
         *  Работа с подключением
         */
        val filterMy = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            r1 = prefs.getString("setting","").toString()
            br = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val ConnectivityManager_my =
                        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (ConnectivityManager_my != null) {
                        val capabilities =
                            ConnectivityManager_my.getNetworkCapabilities(ConnectivityManager_my.activeNetwork)
                        if (capabilities != null) {
                            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                Toast.makeText(context, "wifi", Toast.LENGTH_SHORT).show()
                                if (prefs.getBoolean("starting",true)) {
                                    DownloadText()
                                } else {
                                    Toast.makeText(context, "else", Toast.LENGTH_SHORT).show()
                                    ReloadFun(0)
                                }
                            }
                            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                Toast.makeText(context, "мобильный", Toast.LENGTH_SHORT).show()
                                if (r1.length <= 1) {
                                    DownloadText()
                                } else {
                                    ReloadFun(0)
                                }
                            }
                            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                                Toast.makeText(context, "проводной", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                }

            }

        registerReceiver(br, filterMy)
            //Toast.makeText(this, br.toString(), Toast.LENGTH_LONG).show()

        monthDate.text = GetDate().MonthAndDayNow(c)    // установка даты
        DaysPager()         // установка номеров слайдов

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val days = listOf(day_1,day_2,day_3,day_4,day_5,day_6)
                for(a in days){
                    a.background = getColor(R.color.white).toDrawable()
                    a.setTextColor(getColor(R.color.black))
                }
                days[position].background = getDrawable(R.drawable.date_pole)
                days[position].setTextColor(getColor(R.color.white))
            }

        })



        navig.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.r1 ->{
                    //Toast.makeText(this@MainActivity, "1", Toast.LENGTH_SHORT).show()
                    var dialog = CustomDialogFragment(tags_g,1, this)
                    dialog.show(supportFragmentManager,"cast")
                    drawLayout.close()
                    drawLayout.isInvisible
                    true

                }
                R.id.r2 ->{
                    //Toast.makeText(this@MainActivity, "2", Toast.LENGTH_SHORT).show()
                    var dialog = CustomDialogFragment(tags_t,2, this)
                    dialog.show(supportFragmentManager,"cast")
                    drawLayout.close()
                    true
                }
                R.id.r3 ->{
                    //Toast.makeText(this@MainActivity, "3", Toast.LENGTH_SHORT).show()
                    var dialog = CustomDialogFragment(tags_a,3, this)
                    dialog.show(supportFragmentManager,"cast")
                    drawLayout.close()
                    true
                }
                R.id.r5 ->{
                    val day = c.get(Calendar.DAY_OF_MONTH)
                    val month = c.get(Calendar.MONTH)
                    val year = c.get(Calendar.YEAR)
                    val DatePickShowDialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener{
                        view, year, month, dayOfMonth ->
                        c.set(year,month,dayOfMonth)
                        ReloadFun()
                    },year,month,day)
                    DatePickShowDialog.show()
                    drawLayout.close()
                    true
                }
                R.id.r4 ->{
                    Toast.makeText(this@MainActivity, "Чуть позже...", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
            true

        }

        left.setOnClickListener {
            if(progBar.visibility != View.VISIBLE) {
                c.add(Calendar.DAY_OF_MONTH, -7)
                ReloadFun(0)
            }
            DaysPager()
        }
        right.setOnClickListener {
            if(progBar.visibility != View.VISIBLE) {
                c.add(Calendar.DAY_OF_MONTH, 7)
                ReloadFun(0)
            }
            DaysPager()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun DownloadText(){
        val downloadfile = DownloadFileUseCase()
        try {
            scope_URL.launch {
                withContext(Dispatchers.Main){
                    progBar.visibility = View.VISIBLE
                }
                Toast.makeText(this@MainActivity,"Загрузка расписания", Toast.LENGTH_SHORT).show()

                r1 = async(Dispatchers.IO) {
                    val text = URL("http://a0755299.xsph.ru/wp-content/uploads/3-1-1.txt").readText()
                    return@async text
                }.await().toString()

                //Toast.makeText(this@MainActivity,data.size, Toast.LENGTH_SHORT).show()
                val editor = prefs.edit()
                //editor.putBoolean("starting", false).apply()
                if (r1 != "") {
                    tags_get()
                    ReloadFun()
                    Toast.makeText(this@MainActivity,"Загрузка завершена| 100%", Toast.LENGTH_SHORT).show()
                    withContext(Dispatchers.Main){
                        progBar.visibility = View.GONE
                    }
                }
            }


        } catch (e: Exception) { }

    }


    /**
     * Функция обновления данных
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun ReloadFun(a: Int = 1){
        NamesArray.clear()
        scope.launch {
            withContext(Dispatchers.Main){
                viewPager.visibility = View.GONE
                progBar.visibility = View.VISIBLE
            }
            monthDate.text = GetDate().MonthAndDayNow(c)
            val DayNowVar:Deferred<ArrayList<ArrayList<ArrayList<String>>>> = async{ DayNow() }

            withContext(Dispatchers.Main) {
                viewPager.adapter = ViewPagerAdapter(this@MainActivity, DayNowVar.await())
                progBar.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
            }
        }

        val days = listOf(day_1,day_2,day_3,day_4,day_5,day_6)
        for(a in days){
            a.background = getColor(R.color.white).toDrawable()
        }
    }

    /**
     * Получение данных из таблицы и ее обрабтка
     */
    suspend fun DayNow(): ArrayList<ArrayList<ArrayList<String>>> = withContext(Dispatchers.Default){
        c.time
        c_dop.time
        // Получение дня
        val DayOfWeekNow: Int = GetDate().EngToRusTime(c.get(Calendar.DAY_OF_WEEK)) - 1
        c.add(Calendar.DAY_OF_YEAR, (-1)*DayOfWeekNow)
        val Day_1:String = GetDate().GetDateForText(c)
        c.add(Calendar.DAY_OF_YEAR, 1)
        val Day_2 = GetDate().GetDateForText(c)
        c.add(Calendar.DAY_OF_YEAR, 1)
        val Day_3 = GetDate().GetDateForText(c)
        c.add(Calendar.DAY_OF_YEAR, 1)
        val Day_4 = GetDate().GetDateForText(c)
        c.add(Calendar.DAY_OF_YEAR, 1)
        val Day_5 = GetDate().GetDateForText(c)
        c.add(Calendar.DAY_OF_YEAR, 1)
        val Day_6 = GetDate().GetDateForText(c)
        c.add(Calendar.DAY_OF_YEAR, -5)
        val Day1: Deferred<ArrayList<ArrayList<String>>> = async {
            preparation(Day_1)
        }
        val Day2: Deferred<ArrayList<ArrayList<String>>> = async {
            WeekPrep(Day_2)
        }
        val Day3: Deferred<ArrayList<ArrayList<String>>> = async {
            WeekPrep(Day_3)
        }
        val Day4: Deferred<ArrayList<ArrayList<String>>> = async {
            WeekPrep(Day_4)
        }
        val Day5: Deferred<ArrayList<ArrayList<String>>> = async {
            WeekPrep(Day_5)
        }
        val Day6: Deferred<ArrayList<ArrayList<String>>> = async {
            WeekPrep(Day_6)
        }
        val arr:ArrayList<ArrayList<ArrayList<String>>> = arrayListOf(Day1.await(),Day2.await(),Day3.await(),Day4.await(),Day5.await(),Day6.await())
        return@withContext arr
        //Toast.makeText(applicationContext, NamesArray.toString(), Toast.LENGTH_SHORT).show()
    }

    /**
     * Создание массива пар на неделю
     */
    suspend fun WeekPrep(Day: String):ArrayList<ArrayList<String>> = withContext(Dispatchers.Default){
        val mas: Deferred<ArrayList<ArrayList<String>>> = async(Dispatchers.Default){
            preparation(Day)
        }
        return@withContext mas.await()

    }

    /**
     * Возвращает значения слайдов пэйджера
     */
    fun DaysPager(s:Int = 1){
        var Days = listOf(day_1,day_2,day_3,day_4,day_5,day_6)
        var DayNameNumber = GetDate().EngToRusTime(c.get(Calendar.DAY_OF_WEEK))
        // установка слайда по дню недели
        if(DayNameNumber != 7) {
            Days[(DayNameNumber-1)* s].background = getDrawable(R.drawable.date_pole)
            viewPager.setCurrentItem((DayNameNumber + 1)*s)
        }else{
            Days[0].background = getDrawable(R.drawable.date_pole)
            viewPager.setCurrentItem(0)
        }

        var razn = DayNameNumber - 1
        c.add(Calendar.DAY_OF_MONTH,- razn)
        for(a in Days){
            a.text = c.get(Calendar.DAY_OF_MONTH).toString()
            c.add(Calendar.DAY_OF_MONTH,1)
        }
        c.add(Calendar.DAY_OF_MONTH,razn-6)
    }

    /**
     * Инициализация переменных
     */
    fun init(){
        //создание сохранялки
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        data = getSharedPreferences("data", Context.MODE_PRIVATE)
        test_text = findViewById(R.id.test_text)        // передвижение экрана
        test_text_2 = findViewById(R.id.test_text_2)    // номер страницы

        progBar = findViewById(R.id.ProgBar)            // Прогресс бар

        day_1 = findViewById(R.id.day_1)                // слои с номерами дней
        day_2 = findViewById(R.id.day_2)                //
        day_3 = findViewById(R.id.day_3)                //
        day_4 = findViewById(R.id.day_4)                //
        day_5 = findViewById(R.id.day_5)                //
        day_6 = findViewById(R.id.day_6)                //

        UserInfo = findViewById(R.id.User_name)         // информация о номере группы, аудитории или преподавателе

        navigationView = findViewById(R.id.navig)       // выдвигающееся поле

        drawLayout = findViewById(R.id.draw_lay)        // слой главный

        viewPager = findViewById(R.id.pager)            // пэйджер

        left = findViewById(R.id.textView4)             // левая кнопка
        right = findViewById(R.id.textView6)             // правая кнопка
        monthDate = findViewById(R.id.month_date)
    }

    /**
     * Получение данных о количестве групп, преподавателей и аудиторий
     */
    fun tags_get() {
        //получение кода
        try {
            if(r1.length >= 1) {
                val csvParser = CSVParser(
                    (r1).reader(), CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()
                        .withDelimiter(';')
                )
                for (line in csvParser) {
                    val Group = line.get(0)
                    val Aud = line.get(3)
                    val Name = line.get(6)
                    if (Group !in tags_g) {
                        tags_g.add(Group)
                    }
                    if (Name !in tags_t) {
                        tags_t.add(Name)
                    }
                    if (Aud !in tags_a) {
                        tags_a.add(Aud)
                    }

                }
            }
        } catch (e: Exception) {
        }
    }






    override fun onPause() {
        super.onPause()
        val editor = prefs.edit()
        editor.putString("setting", r1).apply()
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        scope.cancel()
        val editor = prefs.edit()
        editor.putString("setting", r1).apply()
        unregisterReceiver(br)
    }






    //подготовка к работе с текстом для выбора текста
    suspend fun preparation(date:String):ArrayList<ArrayList<String>> = withContext(Dispatchers.Default) {
            //получение кода
        var names = arrayListOf<String>("", "", "", "", "")
        var mesto = arrayListOf<String>("", "", "", "", "")
        var nazv = arrayListOf<String>("", "", "", "", "")
        var lesson_name = arrayListOf<String>("", "", "", "", "")

        val name = UserInfo.text
        var a = 0
        if(r1.length >= 1) {
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


        }
        var list = arrayListOf(names,mesto,nazv,lesson_name)
        return@withContext list
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun ReturnDataFin(s: String) {
        UserInfo.text = s
        ReloadFun()
    }


}









