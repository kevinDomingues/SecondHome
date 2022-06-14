package ipvc.estg.secondhome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.LineAdapter.MyAdsLineAdapter
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourAds : Fragment(R.layout.fragment_your_ads) {

    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "empty")

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAnnouncement(token!!)

        call.enqueue(object: Callback<ArrayList<Advertisements>> {
            override fun onResponse(
                call: Call<ArrayList<Advertisements>>,
                response: Response<ArrayList<Advertisements>>
            ) {
                if(response.isSuccessful){
                    val recView = getView()?.findViewById<RecyclerView>(R.id.yourAdsRv)
                    recView?.layoutManager = LinearLayoutManager(this@YourAds.context)
                    recView?.adapter = MyAdsLineAdapter(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<Advertisements>>, t: Throwable) {
                val number = 1
            }
        })
    }
}
