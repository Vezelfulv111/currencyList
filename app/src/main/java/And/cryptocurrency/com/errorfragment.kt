package And.cryptocurrency.com

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class errorfragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View {

        val view: View = inflater.inflate(R.layout.errorfragment, container, false)

        val btnBack =  activity?.findViewById(R.id.GoBack) as ImageButton//кнопка возврата на основной экран
        btnBack.setOnClickListener {
            val fragmentManager: FragmentManager? = fragmentManager
            val ft: FragmentTransaction = fragmentManager?.beginTransaction()!!
            ft.replace(R.id.fragmentContainerView, basescreenfragment())
            ft.commit()
        }

        val refreshButton: Button = view.findViewById(R.id.refreshBtn) as Button//кнопка перезагрузки
        refreshButton.setOnClickListener{
            val fragmentManager: FragmentManager? = fragmentManager
            val ft = fragmentManager?.beginTransaction()!!
            if (ParamsClass.whichFragment==1)
            { ft.replace(R.id.fragmentContainerView, basescreenfragment())}
            else if(ParamsClass.whichFragment==2)
            { ft.replace(R.id.fragmentContainerView, secondscreenfragment())}
            ft.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    }