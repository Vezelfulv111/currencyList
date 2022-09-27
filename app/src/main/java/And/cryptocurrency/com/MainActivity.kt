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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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



}