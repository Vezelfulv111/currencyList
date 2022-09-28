package And.cryptocurrency.com

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*

import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt


class ArrayListAdapter(private val context: Activity, private var Allinall: Array<MutableList<String>>,private var currency: String
)
    : ArrayAdapter<Any>(context, R.layout.listelement, Allinall[1] as List<Any>) {


    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.listelement, null, true)

        //проверка на то, какая валюта
            if (currency=="usd"){currency="$"}
            if (currency=="eur"){currency="€"}

        val currencyName1 = rowView.findViewById(R.id.currency_name_1) as TextView
        currencyName1.text = Allinall[1][position]

        val currencyName2 = rowView.findViewById(R.id.currency_name_2) as TextView
        currencyName2.text = Allinall[2][position]
        //текущий курс
        val price = rowView.findViewById(R.id.price) as TextView
        val value =(Allinall[3][position].toDouble()*100).roundToInt()/100.0//округляем число. Если использовать только строчку ниже, нет округления
        price.text = currency+String.format("%.2f", value)

        //изменение в процентах
        val percent = rowView.findViewById(R.id.percent) as TextView
        var roundPercent =  (Allinall[4][position].toDouble()*100).roundToInt()/100.0//округляем до двух знаков после запятой
        val resultPercentText=String.format("%.2f", roundPercent)+"%";
        percent.text = resultPercentText;
        //проверка на цвет процента
            if (Allinall[4][position].toDouble()>0)
            {
                percent.setTextColor(Color.parseColor("#0aad3f"))
                percent.text = "+$resultPercentText"
            }
            else if (Allinall[4][position].toDouble()<0)
            {
                percent.setTextColor(Color.parseColor("#D31313"))
            }

        val img = rowView.findViewById(R.id.img) as ImageView
        Picasso.with(context).load(Allinall[5][position]).into(img);

        return rowView
    }



}

