package And.cryptocurrency.com

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONException
import org.json.JSONObject
import android.widget.ProgressBar
import com.android.volley.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {
    private lateinit var currencyList: ListView
    private lateinit var myQueue: RequestQueue
    var defaultCurrency="usd";
    var AllinAll:Array<MutableList<String>> = emptyArray();

    override fun onCreate(savedInstanceState: Bundle?) {
        myQueue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar.visibility = ProgressBar.VISIBLE

        //ListView
        currencyList=findViewById<View>(R.id.CurrencyList) as ListView // находим список

        //ListView click listener
        currencyList.setOnItemClickListener { parent, view, position, id ->
              val inflatedView: View = layoutInflater.inflate(R.layout.activity_scnd, null)
              val CoinInfo = inflatedView.findViewById(R.id.CoinInfo) as TextView
              CoinInfo.text = AllinAll[1][position];//устанавливаем имя валюты в тулбар
              setContentView(inflatedView)
              visibilitySecondScreen(inflatedView,false);//видимость 2й экран
              serverRequestInfo(AllinAll[0][position],inflatedView);
        }

        //Работа с chips
        var Chips: ChipGroup=findViewById(R.id.chipGroup)
        var ChipEur:Chip=findViewById(R.id.chip_eur)
        var ChipUsd:Chip=findViewById(R.id.chip_usd)
        //отправляем текст нажатого Chips в serverRequest для изменения списка валют
        Chips.setOnCheckedChangeListener { group, checkedId ->
            if (ChipEur.isChecked){
                serverRequest("eur")
                ChipUsd.setChipBackgroundColorResource(R.color.lightgray)
                ChipUsd.setTextColor(resources.getColor(R.color.black))
                ChipEur.setChipBackgroundColorResource(R.color.lightorange)
                ChipEur.setTextColor(resources.getColor(R.color.orange))
            }
            else{
                serverRequest("usd")
                ChipEur.setChipBackgroundColorResource(R.color.lightgray)
                ChipEur.setTextColor(resources.getColor(R.color.black))
                ChipUsd.setChipBackgroundColorResource(R.color.lightorange)
                ChipUsd.setTextColor(resources.getColor(R.color.orange))
            }
        }

        serverRequest(defaultCurrency)//запрос на сервер
            ChipUsd.setChipBackgroundColorResource(R.color.lightorange)//так как default
            ChipUsd.setTextColor(resources.getColor(R.color.orange))
    }



    private fun serverRequest(currency:String) {
            var url ="https://api.coingecko.com/api/v3/coins/markets?vs_currency=$currency&order=market_cap_desc&per_page=20&page=1&sparkline=false"
            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val name_list: MutableList<String> = mutableListOf()
                        val symbol: MutableList<String> = mutableListOf()
                        val img: MutableList<String> = mutableListOf()
                        val price: MutableList<String> = mutableListOf()
                        val percent: MutableList<String> = mutableListOf()
                        val id: MutableList<String> = mutableListOf()

                        for(i in 0 until response.length()){
                            var element: JSONObject = response[i] as JSONObject;
                            name_list.add(i,element.get("name").toString())
                            symbol.add(i,element.get("symbol").toString())
                            img.add(i,element.get("image").toString())
                            price.add(i,element.get("current_price").toString())
                            percent.add(i,element.get("price_change_percentage_24h").toString())
                            id.add(i,element.get("id").toString())
                        }

                        AllinAll = arrayOf(id,name_list,symbol,price,percent,img)
                        var ListAdapter = ArrayListAdapter(this,AllinAll,currency)
                        currencyList.adapter = ListAdapter

                        val progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
                        progressBar.visibility = ProgressBar.INVISIBLE

                    }
                    catch (e: JSONException)
                    {
                        e.printStackTrace()
                    }
                }) {
                    error -> error.printStackTrace()
                    onErrorResponse(error);

            }
            myQueue.add(request)
    }

    fun onErrorResponse(volleyError: VolleyError) {
        var message: String? = null
        if (volleyError is NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!"
        } else if (volleyError is ServerError) {
            message = "The server could not be found. Please try again after some time!!"
        } else if (volleyError is AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!"
        } else if (volleyError is ParseError) {
            message = "Parsing error! Please try again after some time!!"
        } else if (volleyError is NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!"
        } else if (volleyError is TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection."
        }
    }

    private fun serverRequestInfo(id:String,v:View){
        var url = "https://api.coingecko.com/api/v3/coins/$id?localization=true"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                var element: JSONObject = response.get("description") as JSONObject;
                val description = v.findViewById(R.id.baseInfo) as TextView
                description.text=element.get("en").toString();

                var categories: JSONArray = response.get("categories") as JSONArray;
                var categoriesDesc = "";
                for (i in 0 until categories.length())
                    {
                        categoriesDesc = categoriesDesc+categories[i]+", ";
                    }
                categoriesDesc=categoriesDesc.substring(0,  categoriesDesc.length - 2);//удаляем последнюю запятую
                val categoryTextView = v.findViewById(R.id.categories) as TextView
                categoryTextView.text = categoriesDesc;

                visibilitySecondScreen(v,true);//видимость для 2го экрана
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) {
                error -> error.printStackTrace()
                onErrorResponse(error);
        }

        myQueue.add(request)

    }



    private fun visibilitySecondScreen(v:View,flag:Boolean)//видимость для 2го экрана
    {
        val progressBar = v.findViewById(R.id.progressBar2nd) as ProgressBar
        val Scrollbar = v.findViewById(R.id.scroolbar) as ScrollView
        if (flag)
        {
            progressBar.visibility = ProgressBar.INVISIBLE
            Scrollbar.visibility=ScrollView.VISIBLE
        }
        else
        {
            progressBar.visibility = ProgressBar.VISIBLE
            Scrollbar.visibility=ScrollView.INVISIBLE
        }
    }
    //Возврат на начальный экран
    fun GoBack(v: View?) {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
    }
}