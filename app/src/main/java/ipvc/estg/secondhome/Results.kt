package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.LineAdapter.LineAdapter
import ipvc.estg.secondhome.LineAdapterFavs.LineAdapterFavs
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

    val filterButton = findViewById<Button>(R.id.filterresults)
    val filterDropdown = findViewById<RelativeLayout>(R.id.includeSpinnerresults)
    val orderByButton = findViewById<Button>(R.id.orderresults)
    val orderByDropdown = findViewById<RelativeLayout>(R.id.includeOrderSpinnerresults)

    val llAreaPlusButton = findViewById<LinearLayout>(R.id.llAreaPlus)
    val llAreaLessButton = findViewById<LinearLayout>(R.id.llAreaLess)
    val llPricePlusButton = findViewById<LinearLayout>(R.id.llPricePlus)
    val llPriceLessButton = findViewById<LinearLayout>(R.id.llPriceLess)
    val llNameLessButton = findViewById<LinearLayout>(R.id.llNameLess)
    val llNamePlusButton = findViewById<LinearLayout>(R.id.llNamePlus)

    val checkBoxWifi = findViewById<CheckBox>(R.id.wifi)
    val checkBoxAccessibility = findViewById<CheckBox>(R.id.accessibility)
    val editTextMin = findViewById<EditText>(R.id.minPrice)
    val editTextMax = findViewById<EditText>(R.id.maxPrice)
    val checkBoxApartment = findViewById<CheckBox>(R.id.apartment)
    val checkBoxRoom = findViewById<CheckBox>(R.id.room)
    val searchButton = findViewById<Button>(R.id.btnSearchresults)

    val recView = findViewById<RecyclerView>(R.id.resultadoRv)

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

    var ads : ArrayList<Advertisements> = ArrayList()

    if(!addressList.isNullOrEmpty()){
      val address = addressList!![0]

      val request = ServiceBuilder.buildService(EndPoints::class.java)
      val call = request.getAnnouncement(token!!)

      call.enqueue(object: Callback<ArrayList<Advertisements>> {
        override fun onResponse(
            call: Call<ArrayList<Advertisements>>,
            response: Response<ArrayList<Advertisements>>
        ) {
          if(response.isSuccessful){
            recView.layoutManager = LinearLayoutManager(this@Results)
            val adAdapter = LineAdapter(adList, token!!, address.latitude, address.longitude)
            recView.adapter = adAdapter

            for(ad: Advertisements in response.body()!!){
              if (calculateDistance(address.latitude, address.longitude, ad.lat, ad.lng) < 10000){
                ads.add(ad)
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

    var adsFiltered : ArrayList<Advertisements> = ArrayList()

    llAreaLessButton?.setOnClickListener{
      ads.sortByDescending { it.netArea }
      recView.adapter = LineAdapterFavs(ads, token!!)
    }

    llAreaPlusButton?.setOnClickListener{
      val recView = findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortBy { it.netArea }
      recView.adapter = LineAdapterFavs(ads, token!!)
    }

    llNameLessButton?.setOnClickListener{
      val recView = findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortByDescending { it.name }
      recView.adapter = LineAdapterFavs(ads, token!!)
    }

    llNamePlusButton?.setOnClickListener{
      val recView = findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortBy { it.name }
      recView?.adapter = LineAdapterFavs(ads, token!!)
    }

    llPriceLessButton?.setOnClickListener{
      val recView = findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortBy { it.price }
      recView.adapter = LineAdapterFavs(ads, token!!)
    }

    llPricePlusButton?.setOnClickListener{
      val recView = findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortByDescending { it.price }
      recView.adapter = LineAdapterFavs(ads, token!!)
    }

    searchButton?.setOnClickListener {

      ads.sortByDescending { it.price }
      recView.adapter = LineAdapterFavs(ads, token!!)
      if (adsFiltered.isNotEmpty()) {
        adsFiltered.clear()
      }

      if (checkBoxWifi!!.isChecked) {
        for (a in ads) {
          if (a.wifi) {
            adsFiltered.add(a)
          }
        }
        recView.adapter = LineAdapterFavs(adsFiltered, token!!)
      } else if (checkBoxAccessibility!!.isChecked) {
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.accessibilty) {
            adsFiltered.add(a)
          }
        }
        recView.adapter = LineAdapterFavs(adsFiltered, token!!)
      } else if (checkBoxWifi!!.isChecked && checkBoxAccessibility!!.isChecked) {
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.wifi && a.accessibilty) {
            adsFiltered.add(a)
          }
        }
        recView.adapter = LineAdapterFavs(adsFiltered, token!!)
      } else if (editTextMin!!.length()!=0 && editTextMax!!.length()!=0){
        for (a in ads) {
          if(a.price >= editTextMin?.text.toString().toInt() && a.price <= editTextMax?.text.toString().toInt())
            adsFiltered.add(a)
        }
        recView.adapter = LineAdapterFavs(adsFiltered, token!!)
      } else if (checkBoxApartment!!.isChecked){
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.type == 1) {
            adsFiltered.add(a)
          }
        }
        recView.adapter = LineAdapterFavs(adsFiltered, token!!)
      } else if (checkBoxRoom!!.isChecked){
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.type == 2) {
            adsFiltered.add(a)
          }
        }
        recView.adapter = LineAdapterFavs(adsFiltered, token!!)
      }
      else {
        recView.adapter = LineAdapterFavs(ads, token)
      }
    }

    filterDropdown.visibility = View.GONE
    filterButton.setOnClickListener{
      if(filterDropdown.isVisible){
        filterDropdown.visibility = View.GONE
      }
      else{
        if(orderByDropdown.isVisible){
          orderByDropdown.visibility = View.GONE
        }
        filterDropdown.visibility = View.VISIBLE
      }
    }

    orderByDropdown?.visibility = View.GONE
    orderByButton?.setOnClickListener{
      if(orderByDropdown.isVisible){
        orderByDropdown.visibility = View.GONE
      }
      else{
        if(filterDropdown.isVisible){
          filterDropdown.visibility = View.GONE
        }
        orderByDropdown.visibility = View.VISIBLE
      }
    }
  }

  private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, results)

    return results[0]
  }
}
