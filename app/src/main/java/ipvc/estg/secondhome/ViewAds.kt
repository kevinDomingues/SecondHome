package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        val lat = intent.getDoubleExtra("lat", 0.00)
        val lng = intent.getDoubleExtra("lng", 0.00)
        val searchLat = intent.getDoubleExtra("searchLat", 0.00)
        val searchLng = intent.getDoubleExtra("searchLng", 0.00)
        
        val viewOnMapBtn = findViewById<Button>(R.id.viewAdsOnMap)


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
                    val accessibility = findViewById<ImageView>(R.id.viewAdsAccessibility)
                    val wifi = findViewById<ImageView>(R.id.viewAdsWifi)

                    val ad: Advertisements = response.body()!!

                    adName.setText(ad.name)
                    location.setText(ad.location)
                    price.setText(ad.price.toString() + " €")
                    rooms.setText(ad.rooms.toString())
                    bathrooms.setText(ad.bathrooms.toString())
                    area.setText(ad.netArea.toString() + " m2")
                    contacts.setText(ad.contact)

                    if (ad.type == 1) {
                        type.setText(getString(R.string.house_type_1))
                    } else if (ad.type == 2) {
                        type.setText(getString(R.string.house_type_2))
                    }

                    if(!ad.accessibilty){
                        accessibility.visibility = View.INVISIBLE
                    }

                    if(!ad.wifi){
                        wifi.visibility = View.INVISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<Advertisements>, t: Throwable) {
                val number = 1
            }
        })

        viewOnMapBtn.setOnClickListener {
            val intent = Intent(this, ViewAdOnMap::class.java).apply {
                putExtra("lat", lat)
                putExtra("lng", lng)
                putExtra("searchLat", searchLat)
                putExtra("searchLng", searchLng)
            }
            startActivity(intent)
        }
    }
}
