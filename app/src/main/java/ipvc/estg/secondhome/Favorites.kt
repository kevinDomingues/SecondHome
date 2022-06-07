package ipvc.estg.secondhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

class Favorites : Fragment(R.layout.fragment_favorites) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val filterButton = getView()?.findViewById<Button>(R.id.filter)
    val spinner = getView()?.findViewById<Spinner>(R.id.filterSpinner)
    filterButton?.setOnClickListener {

    }

  }
}
