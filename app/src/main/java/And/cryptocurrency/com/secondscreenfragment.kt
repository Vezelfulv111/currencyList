package And.cryptocurrency.com

import android.app.Activity
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject



class secondscreenfragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {

        val view: View = inflater!!.inflate(R.layout.secondscreenfragment, container, false)

        //тулбар
        val toolbar1: androidx.appcompat.widget.Toolbar = activity?.findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        val toolbar2:  androidx.appcompat.widget.Toolbar = activity?.findViewById(R.id.toolbar2nd) as  androidx.appcompat.widget.Toolbar
        toolbar2.visibility = View.VISIBLE
        toolbar1.visibility = View.GONE

        val btnGoback =  activity?.findViewById(R.id.GoBack) as ImageButton//кнопка возврата на основной экран
        btnGoback.setOnClickListener {
            val fragmentManager: FragmentManager? = fragmentManager
            val ft: FragmentTransaction = fragmentManager?.beginTransaction()!!
            ft.replace(R.id.fragmentContainerView, basescreenfragment())
            ft.commit()
        }

        val title:  TextView = activity?.findViewById(R.id.description) as TextView
        title.text = ParamsClass.name

        serverRequestInfo(ParamsClass.id)

        visibilitySecondScreen(view,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun serverRequestInfo(id:String){
        val url = "https://api.coingecko.com/api/v3/coins/$id?localization=false"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val element: JSONObject = response.get("description") as JSONObject;
                    //загрузка иконки
                    val img = view?.findViewById(R.id.image2ndScreen) as ImageView
                    val imgObj=(response.get("image")) as JSONObject
                    val imgString=imgObj.get("large").toString()
                    Picasso.with(context).load(imgString).into(img)

                    val description = view?.findViewById(R.id.baseInfo) as TextView
                    description.text=element.get("en").toString()

                    var a = element.get("en").toString();
                    description.text=Html.fromHtml(a.replace("\n", "<br/>")).toString()

                    val categories: JSONArray = response.get("categories") as JSONArray;
                    var categoriesDesc = "";
                    for (i in 0 until categories.length())
                    {
                        categoriesDesc = categoriesDesc+categories[i]+", "
                    }
                    categoriesDesc=categoriesDesc.substring(0,  categoriesDesc.length - 2);//удаляем последнюю запятую
                    val categoryTextView = view?.findViewById(R.id.categories) as TextView
                    categoryTextView.text = categoriesDesc
                    visibilitySecondScreen(requireView(),true);//видимость для 2го экрана
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) {
                error -> error.printStackTrace()
                onErrorResponse(error)
        }

        val queue = SingleTonRqstQueue.getInstance(context as Activity).requestQueue
        queue.add(request)
        queue.cache.remove(url)//убираем кеш
    }

    private fun visibilitySecondScreen(view: View,flag:Boolean)//видимость для 2го экрана
    {
        val progressBar = view.findViewById(R.id.progressBar2nd) as ProgressBar
        val scrollbar =view.findViewById(R.id.scroolbar) as ScrollView
        if (flag)
        {
            progressBar.visibility = ProgressBar.INVISIBLE
            scrollbar.visibility=ScrollView.VISIBLE
        }
        else
        {
            progressBar.visibility = ProgressBar.VISIBLE
            scrollbar.visibility=ScrollView.INVISIBLE
        }
    }

    private fun onErrorResponse(volleyError: VolleyError) {
        ParamsClass.whichFragment=2;//нужно также передать с какого фрагмента переходим на error fragment
        val fragmentManager: FragmentManager? = fragmentManager
        val ft = fragmentManager?.beginTransaction()!!
        ft.replace(R.id.fragmentContainerView, errorfragment())
        ft.commit()
    }
}