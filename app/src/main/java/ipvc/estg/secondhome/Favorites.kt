package ipvc.estg.secondhome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.LineAdapterFavs.LineAdapterFavs
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Favorites : Fragment(R.layout.fragment_favorites) {

  lateinit var sharedPreferences: SharedPreferences

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val filterButton = getView()?.findViewById<Button>(R.id.filter)
    val filterDropdown = getView()?.findViewById<RelativeLayout>(R.id.includeSpinner)
    sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", "empty")

    val request = ServiceBuilder.buildService(EndPoints::class.java)
    val call = request.getFavoritesFromSession(token!!)

    call.enqueue(object: Callback<ArrayList<Advertisements>> {
      override fun onResponse(
        call: Call<ArrayList<Advertisements>>,
        response: Response<ArrayList<Advertisements>>
      ) {
        if(response.isSuccessful){
          val recView = getView()?.findViewById<RecyclerView>(R.id.yourAdsRv)
          recView?.layoutManager = LinearLayoutManager(this@Favorites.context)
          recView?.adapter = LineAdapterFavs(response.body()!!)
        }
      }

      override fun onFailure(call: Call<ArrayList<Advertisements>>, t: Throwable) {
        val number = 1
      }
    })
    filterDropdown?.visibility = View.GONE
    filterButton?.setOnClickListener{
      if(filterDropdown!!.isVisible){
        filterDropdown.visibility = View.GONE
      }
      else{
        filterDropdown.visibility = View.VISIBLE
      }
    }

  }
}
