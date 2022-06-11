package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.LineAdapter.LineAdapter
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class Results : AppCompatActivity() {

  private var adList: ArrayList<Advertisements> = ArrayList()

  lateinit var sharedPreferences: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_resultados)

    val parametro = intent.getStringExtra(PARAM_NAME)
    val textView = findViewById<TextView>(R.id.tv1resultados)

    textView.text = parametro

    val backButton = findViewById<ImageView>(R.id.btnBackPage)

    backButton.setOnClickListener {
      val intent = Intent(this, MainPage::class.java)
      startActivity(intent)
    }

    sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", "empty")

    var addressList: List<Address>? = null

    val geocoder = Geocoder(this)

    try{
      addressList = geocoder.getFromLocationName(parametro, 1)
    } catch (e: IOException){
      e.printStackTrace()
    }

    val address = addressList!![0]

    val request = ServiceBuilder.buildService(EndPoints::class.java)
    val call = request.getAnnouncement(token!!)

    call.enqueue(object: Callback<ArrayList<Advertisements>> {
      override fun onResponse(
          call: Call<ArrayList<Advertisements>>,
          response: Response<ArrayList<Advertisements>>
      ) {
        if(response.isSuccessful){
          val recView = findViewById<RecyclerView>(R.id.resultadoRv)
          recView.layoutManager = LinearLayoutManager(this@Results)
          val adAdapter = LineAdapter(adList)
          recView.adapter = adAdapter

          for(ad: Advertisements in response.body()!!){
            if (calculateDistance(address.latitude, address.longitude, ad.lat, ad.lng) < 10000){
              adAdapter.add(ad)
            }
          }
        }
      }

      override fun onFailure(call: Call<ArrayList<Advertisements>>, t: Throwable) {
      val number = 1
      }
    })
  }

  private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, results)

    return results[0]
  }
}
