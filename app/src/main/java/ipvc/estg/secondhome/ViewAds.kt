package ipvc.estg.secondhome

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.LineAdapter.LineAdapter
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAds : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ads)

        val adID = intent.getStringExtra(PARAM_NAME)

        sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "empty")

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAnnouncementById(token!!, adID!!)

        call.enqueue(object: Callback<Advertisements> {
            override fun onResponse(
                call: Call<Advertisements>,
                response: Response<Advertisements>
            ) {
                if(response.isSuccessful) {
                    val adName = findViewById<TextView>(R.id.viewAdsAdName)
                    val location = findViewById<TextView>(R.id.viewAdsLocation)
                    val price = findViewById<TextView>(R.id.viewAdsPrice)
                    val rooms = findViewById<TextView>(R.id.viewAdsNumberRooms)
                    val bathrooms = findViewById<TextView>(R.id.viewAdsNumberBathrooms)
                    val area = findViewById<TextView>(R.id.viewAdsArea)
                    val contacts = findViewById<TextView>(R.id.viewAdsContacts)
                    val type = findViewById<TextView>(R.id.viewAdsType)

                    val ad: Advertisements = response.body()!!

                    adName.setText(ad.name)
                    location.setText(ad.location)
                    price.setText(ad.price.toString())
                    rooms.setText(ad.rooms.toString())
                    bathrooms.setText(ad.bathrooms.toString())
                    area.setText(ad.netArea.toString())
                    contacts.setText(ad.contact)

                    if (ad.type == 1) {
                        type.setText(getString(R.string.house_type_1))
                    } else if (ad.type == 2) {
                        type.setText(getString(R.string.house_type_2))
                    }
                }
            }

            override fun onFailure(call: Call<Advertisements>, t: Throwable) {
                val number = 1
            }
        })
    }
}
