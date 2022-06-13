package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import ipvc.estg.secondhome.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UpdateAds : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    private var house_type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_ads)

        val advertisementName = findViewById<EditText>(R.id.updateAddAdvName)
        val location = findViewById<EditText>(R.id.updateAddLocation)
        val rooms = findViewById<EditText>(R.id.updateAddRooms)
        val bathrooms = findViewById<EditText>(R.id.updateAddBathrooms)
        val area = findViewById<EditText>(R.id.updateAddArea)
        val rent = findViewById<EditText>(R.id.updateAddPrice)
        val constructionYear =  findViewById<EditText>(R.id.updateAddConstructionYear)
        val email = findViewById<EditText>(R.id.updateAddEmail)
        val contact = findViewById<EditText>(R.id.updateAddContact)
        val accessibility = findViewById<CheckBox>(R.id.updateAddMobility)
        val wifi = findViewById<CheckBox>(R.id.updateAddWifi)
        val housetype1Btn = findViewById<RadioButton>(R.id.updateHouse_type_1)
        val housetype2Btn = findViewById<RadioButton>(R.id.updateHouse_type_2)

        housetype1Btn?.setOnClickListener {
            onRadioButtonClicked(it)
        }

        housetype2Btn?.setOnClickListener {
            onRadioButtonClicked(it)
        }

        val updateBtn = findViewById<Button>(R.id.updateAddBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteAddBtn)

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
                    val ad: Advertisements = response.body()!!

                    advertisementName.setText(ad.name)
                    location.setText(ad.location)
                    rent.setText(ad.price.toString())
                    rooms.setText(ad.rooms.toString())
                    bathrooms.setText(ad.bathrooms.toString())
                    area.setText(ad.netArea.toString())
                    contact.setText(ad.contact)
                    constructionYear.setText(ad.constructionYear.toString())
                    email.setText(ad.email)

                    if(ad.accessibilty){
                        accessibility.isChecked = true
                    }

                    if(ad.wifi){
                        wifi.isChecked = true
                    }

                    if (ad.type == 1) {
                        housetype1Btn.isChecked = true
                    } else if (ad.type == 2) {
                        housetype2Btn.isChecked = true
                    }
                }
            }

            override fun onFailure(call: Call<Advertisements>, t: Throwable) {
                val number = 1
            }
        })

        updateBtn.setOnClickListener {
            var addressList: List<Address>? = null

            val geocoder = Geocoder(this)

            try{
                addressList = geocoder.getFromLocationName(location!!.text.toString().trim(), 1)
            } catch (e: IOException){
                e.printStackTrace()
            }

            val address = addressList!![0]


            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.updateAnnoucenement(token!!, adID!!,
                house_type,
                area.text.toString().toInt(),
                rooms.text.toString().toInt(),
                bathrooms.text.toString().toInt(),
                rent.text.toString().toInt(),
                location.text.toString().trim(),
                address.latitude,
                address.longitude,
                constructionYear.text.toString().toInt(),
                accessibility.isChecked,
                wifi.isChecked,
                email.text.toString().trim(),
                contact.text.toString().trim(),
                advertisementName.text.toString().trim()
                )

            call.enqueue(object: Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateAds,
                            R.string.ad_added,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@UpdateAds, MainPage::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(
                        this@UpdateAds,
                        R.string.errorSignUp,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        deleteBtn.setOnClickListener {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.deleteAnnouncement(token!!, adID!!)

            call.enqueue(object: Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateAds,
                            R.string.removed,
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@UpdateAds, MainPage::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(
                        this@UpdateAds,
                        R.string.errorSignUp,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.house_type_1 ->
                    if (checked) {
                        house_type = 1
                    }
                R.id.house_type_2 ->
                    if (checked) {
                        house_type= 2
                    }
            }
        }
    }
}