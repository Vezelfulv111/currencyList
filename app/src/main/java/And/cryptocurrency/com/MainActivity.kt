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
import androidx.appcompat.widget.Toolbar
import com.android.volley.*
import org.json.JSONArray
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = 3.00159265359

        val a1="{input.format(2)}";
        val formatted = String.format("%.2f", input) ;

        val a4=4;
    }

}