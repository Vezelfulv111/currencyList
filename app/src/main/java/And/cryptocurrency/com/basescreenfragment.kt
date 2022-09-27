    package And.cryptocurrency.com

    import android.app.Activity
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.View.GONE
    import android.view.View.VISIBLE
    import android.view.ViewGroup
    import android.widget.*
    import androidx.fragment.app.FragmentManager
    import androidx.fragment.app.FragmentTransaction
    import com.android.volley.*
    import com.android.volley.toolbox.JsonArrayRequest
    import com.android.volley.toolbox.Volley
    import com.google.android.material.chip.Chip
    import com.google.android.material.chip.ChipGroup
    import org.json.JSONException
    import org.json.JSONObject
    import java.io.File
    import java.io.FileInputStream
    import java.io.ObjectInputStream





    class basescreenfragment : Fragment() {

        private lateinit var currencyList: ListView
        var defaultCurrency="usd";
        var AllinAll:Array<MutableList<String>> = emptyArray();

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
                View? {

            val view: View = inflater!!.inflate(R.layout.basescreenfragment, container, false)
             //тулбар
             var Toolbar1: androidx.appcompat.widget.Toolbar = getActivity()?.findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
             var Toolbar2:  androidx.appcompat.widget.Toolbar = getActivity()?.findViewById(R.id.toolbar2nd) as  androidx.appcompat.widget.Toolbar
                   Toolbar1.setVisibility(View.VISIBLE);
                   Toolbar2.setVisibility(View.GONE);

            val progressBar = view.findViewById(R.id.progressBar) as ProgressBar
            progressBar.visibility = ProgressBar.VISIBLE

            //ListView
            currencyList=view.findViewById(R.id.CurrencyList) as ListView // находим список

            //ListView click listener
            currencyList.setOnItemClickListener { parent, view, position, id ->
                val fragment = secondscreenfragment()
                ParamsClass.id= AllinAll[0][position];
                ParamsClass.name=AllinAll[1][position];
                val fragmentManager: FragmentManager? = fragmentManager
                val ft: FragmentTransaction = fragmentManager?.beginTransaction()!!
                ft.replace(R.id.fragmentContainerView, fragment)
                ft.commit()
            }

            //Работа с chips
            var Chips: ChipGroup = getActivity()?.findViewById(R.id.chipGroup) as ChipGroup
            var ChipEur: Chip =getActivity()?.findViewById(R.id.chip_eur) as Chip
            var ChipUsd: Chip =getActivity()?.findViewById(R.id.chip_usd) as Chip
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


            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        }

        private fun onErrorResponse(volleyError: VolleyError) {
            //нужно также передать с какого фрагмента переходим на error fragment
            ParamsClass.whichFragment=1;
            val fragmentManager: FragmentManager? = fragmentManager
            val ft: FragmentTransaction = fragmentManager?.beginTransaction()!!
            ft.replace(R.id.fragmentContainerView, errorfragment())
            ft.commit()
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
                        var ListAdapter = ArrayListAdapter(getContext() as Activity,AllinAll,currency)
                        currencyList.adapter = ListAdapter

                        val progressBar = view?.findViewById<View>(R.id.progressBar) as ProgressBar
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
            val queue = SingleTonRqstQueue.getInstance(getContext() as Activity).requestQueue
            queue.add(request)
        }

    }