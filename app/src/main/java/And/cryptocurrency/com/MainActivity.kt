package And.cryptocurrency.com

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var currencyList: ListView
    private lateinit var myQueue: RequestQueue
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

        var value: String

        ServerRequest()

    }

    fun ServerRequest() {
            val temp_url = "ditto"
            var url = "https://api.github.com/"
            url ="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false"
            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val name_list: MutableList<String> = mutableListOf()

                        for(i in 0 until response.length()){
                            var currency_element: JSONObject = response[i] as JSONObject;
                            name_list.add(i,currency_element.get("id").toString())
                        }

                        val adapter: ArrayAdapter<String> =ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name_list)
                        currencyList.setAdapter(adapter) // присваиваем адаптер списку

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) {
                    error -> error.printStackTrace() }
            myQueue.add(request)
    }
}