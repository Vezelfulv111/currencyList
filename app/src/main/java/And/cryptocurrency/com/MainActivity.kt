package And.cryptocurrency.com

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONException
import org.json.JSONObject
import android.view.LayoutInflater
import android.content.Intent
import android.widget.TextView



class MainActivity : AppCompatActivity() {
    private lateinit var currencyList: ListView
    private lateinit var myQueue: RequestQueue
    var defaultCurrency="usd";
    override fun onCreate(savedInstanceState: Bundle?) {
        myQueue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ListView
        val currenses: Array<String> = resources.getStringArray(R.array.Currenses)
        currencyList=findViewById<View>(R.id.CurrencyList) as ListView // находим список
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currenses)
        currencyList.setAdapter(adapter) // присваиваем адаптер списку
        serverRequest(defaultCurrency)

        //ListView click listenerer
        currencyList.setOnItemClickListener { parent, view, position, id ->
            val currency_name_1 = view.findViewById(R.id.currency_name_1) as TextView
            val inflatedView: View = layoutInflater.inflate(R.layout.activity_scnd, null)
            val CoinInfo = inflatedView.findViewById(R.id.CoinInfo) as TextView
            CoinInfo.text = currency_name_1.text.toString();//устанавливаем имя валюты в тулбар
            setContentView(inflatedView)
        }


        //Работа с chips
        var Chips: ChipGroup=findViewById(R.id.chipGroup)
        //отправляем текст нажатого Chips в serverRequest для изменения списка валют
        Chips.setOnCheckedChangeListener { group, checkedId ->
            findViewById<Chip>(checkedId)?.apply {
                serverRequest(text.toString());
            }
        }
    }

    fun serverRequest(currency:String) {
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

                        for(i in 0 until response.length()){
                            var element: JSONObject = response[i] as JSONObject;
                            name_list.add(i,element.get("name").toString())
                            symbol.add(i,element.get("symbol").toString())
                            img.add(i,element.get("image").toString())
                            price.add(i,element.get("current_price").toString())
                            percent.add(i,element.get("price_change_percentage_24h").toString())
                        }

                        var AllinAll = arrayOf(name_list,symbol,price,percent,img)
                        var ListAdapter = ArrayListAdapter(this,AllinAll,currency)
                        currencyList.adapter = ListAdapter

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) {
                    error -> error.printStackTrace() }
            myQueue.add(request)
    }

    //Возврат на начальный экран
    fun GoBack(v: View?) {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
    }
}