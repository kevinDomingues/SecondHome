package ipvc.estg.secondhome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Evaluation
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

class Evaluation : Fragment(R.layout.fragment_evaluation) {

    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
        var ratingBValue = getView()?.findViewById<RatingBar>(R.id.rating)
        var button = getView()?.findViewById<Button>(R.id.btnSubmitEvaluation)
        var textView = getView()?.findViewById<TextView>(R.id.ratingDisplayTV)
        var textView2 = getView()?.findViewById<TextView>(R.id.stars)


        ratingBValue?.onRatingBarChangeListener = OnRatingBarChangeListener  {
                _, rating, _ ->
            textView?.text = "" + ratingBValue?.rating?.toFloat()
            textView2?.text = " " + getString(R.string.nStarsText)
        }

        button?.setOnClickListener {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            var editText = getView()?.findViewById<TextInputEditText>(R.id.tietDescription)
            val valueOfRB: Float? = ratingBValue?.rating
            val descriptionText: Editable? = editText?.getText()
            val call = request.sendEvaluation(descriptionText.toString(), valueOfRB?.toFloat())

            call.enqueue(object : Callback<Evaluation> {
                override fun onResponse(call: Call<Evaluation>, response: Response<Evaluation>) {
                    if(response.isSuccessful){
                        val c: Evaluation = response.body()!!
                        ratingBValue?.setRating(2.5F)
                        editText?.getText()?.clear()
                        Toast.makeText(getActivity(), R.string.evaluationSuccessToast , Toast.LENGTH_LONG ).show();
                    }
                }
                override fun onFailure(call: Call<Evaluation>, t: Throwable
                ) {
                    Toast.makeText(getActivity(),  R.string.evaluationFailToast , Toast.LENGTH_LONG ).show();
                }
            })
        }

    }

}