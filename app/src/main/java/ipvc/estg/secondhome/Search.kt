package ipvc.estg.secondhome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

const val PARAM_NAME = "nome"


class Search : Fragment(R.layout.fragment_search) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val parametro = activity?.intent?.getStringExtra(PARAM_NAME)
    val goToNextPageBtn = getView()?.findViewById<Button>(R.id.btnNextPag)

    goToNextPageBtn?.setOnClickListener {
      val editText = getView()?.findViewById<EditText>(R.id.et1)
      val intent = Intent(activity, Results::class.java).apply {
        putExtra(PARAM_NAME, editText?.text.toString())
      }
      startActivity(intent)
    }
  }

  fun changeName(view: View) {
      val editText = getView()?.findViewById<EditText>(R.id.et1)
    }

}
