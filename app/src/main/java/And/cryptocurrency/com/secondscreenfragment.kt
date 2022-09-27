package And.cryptocurrency.com

import android.app.Activity
import android.content.Intent
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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream


class secondscreenfragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {

        val view: View = inflater!!.inflate(R.layout.secondscreenfragment, container, false)

        //тулбар
        var Toolbar1: androidx.appcompat.widget.Toolbar = getActivity()?.findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        var Toolbar2:  androidx.appcompat.widget.Toolbar = getActivity()?.findViewById(R.id.toolbar2nd) as  androidx.appcompat.widget.Toolbar
        Toolbar2.setVisibility(View.VISIBLE);
        Toolbar1.setVisibility(View.GONE);


        val BtnGoback =  getActivity()?.findViewById(R.id.GoBack) as ImageButton//кнопка возврата на основной экран
        BtnGoback.setOnClickListener {
            val fragmentManager: FragmentManager? = fragmentManager
            val ft: FragmentTransaction = fragmentManager?.beginTransaction()!!
            ft.replace(R.id.fragmentContainerView, basescreenfragment())
            ft.commit()
        }

        var Title:  TextView = getActivity()?.findViewById(R.id.description) as TextView
        Title.setText(ParamsClass.name)

        serverRequestInfo(ParamsClass.id);

        visibilitySecondScreen(view,false);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun serverRequestInfo(id:String){
        var url = "https://api.coingecko.com/api/v3/coins/$id?localization=true"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    var element: JSONObject = response.get("description") as JSONObject;
                    val description = view?.findViewById(R.id.baseInfo) as TextView
                    description.text=element.get("en").toString();

                    var categories: JSONArray = response.get("categories") as JSONArray;
                    var categoriesDesc = "";
                    for (i in 0 until categories.length())
                    {
                        categoriesDesc = categoriesDesc+categories[i]+", ";
                    }
                    categoriesDesc=categoriesDesc.substring(0,  categoriesDesc.length - 2);//удаляем последнюю запятую
                    val categoryTextView = view?.findViewById(R.id.categories) as TextView
                    categoryTextView.text = categoriesDesc;

                    visibilitySecondScreen(requireView(),true);//видимость для 2го экрана
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) {
                error -> error.printStackTrace()
                onErrorResponse(error);
        }

        val queue = SingleTonRqstQueue.getInstance(getContext() as Activity).requestQueue
        queue.add(request)
    }

    private fun visibilitySecondScreen(view: View,flag:Boolean)//видимость для 2го экрана
    {
        val progressBar = view.findViewById(R.id.progressBar2nd) as ProgressBar
        val Scrollbar =view.findViewById(R.id.scroolbar) as ScrollView
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

    private fun onErrorResponse(volleyError: VolleyError) {
        //нужно также передать с какого фрагмента переходим на error fragment
        ParamsClass.whichFragment=2;
        val fragmentManager: FragmentManager? = fragmentManager
        val ft: FragmentTransaction = fragmentManager?.beginTransaction()!!
        ft.replace(R.id.fragmentContainerView, errorfragment())
        ft.commit()
    }
}