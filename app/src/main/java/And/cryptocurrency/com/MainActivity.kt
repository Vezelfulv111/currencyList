package And.cryptocurrency.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //ListView
        val currenses: Array<String> = resources.getStringArray(R.array.Currenses)
        val currencyList: ListView = findViewById<View>(R.id.CurrencyList) as ListView // находим список
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currenses)
        currencyList.setAdapter(adapter) // присваиваем адаптер списку


    }
}