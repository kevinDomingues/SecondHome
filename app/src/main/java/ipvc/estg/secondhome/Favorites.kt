package ipvc.estg.secondhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class Favorites : Fragment(R.layout.fragment_favorites) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val filterButton = getView()?.findViewById<Button>(R.id.filter)
    val filterDropdown = getView()?.findViewById<RelativeLayout>(R.id.includeSpinner)
    filterDropdown?.setVisibility(View.GONE)
    filterButton?.setOnClickListener{
      if(filterDropdown!!.isVisible){
        filterDropdown?.setVisibility(View.GONE)
      }
      else{
        filterDropdown?.setVisibility(View.VISIBLE)
      }
    }

  }
}
