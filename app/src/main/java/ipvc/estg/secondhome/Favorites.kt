package ipvc.estg.secondhome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.LineAdapterFavs.LineAdapterFavs
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import kotlinx.android.synthetic.main.filter_spinner.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Favorites : Fragment(R.layout.fragment_favorites) {

  lateinit var sharedPreferences: SharedPreferences

  lateinit var token: String

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val filterButton = getView()?.findViewById<Button>(R.id.filter)
    val filterDropdown = getView()?.findViewById<RelativeLayout>(R.id.includeSpinner)
    val orderByButton = getView()?.findViewById<Button>(R.id.order)
    val orderByDropdown = getView()?.findViewById<RelativeLayout>(R.id.includeOrderSpinner)

    val llAreaPlusButton = getView()?.findViewById<LinearLayout>(R.id.llAreaPlus)
    val llAreaLessButton = getView()?.findViewById<LinearLayout>(R.id.llAreaLess)
    val llPricePlusButton = getView()?.findViewById<LinearLayout>(R.id.llPricePlus)
    val llPriceLessButton = getView()?.findViewById<LinearLayout>(R.id.llPriceLess)
    val llNameLessButton = getView()?.findViewById<LinearLayout>(R.id.llNameLess)
    val llNamePlusButton = getView()?.findViewById<LinearLayout>(R.id.llNamePlus)

    val checkBoxWifi = getView()?.findViewById<CheckBox>(R.id.wifi)
    val checkBoxAccessibility = getView()?.findViewById<CheckBox>(R.id.accessibility)
    val editTextMin = getView()?.findViewById<EditText>(R.id.minPrice)
    val editTextMax = getView()?.findViewById<EditText>(R.id.maxPrice)
    val checkBoxApartment = getView()?.findViewById<CheckBox>(R.id.apartment)
    val checkBoxRoom = getView()?.findViewById<CheckBox>(R.id.room)


    val searchButton = getView()?.findViewById<Button>(R.id.btnSearch)



    var ads : ArrayList<Advertisements> = ArrayList()

    var adsFiltered : ArrayList<Advertisements> = ArrayList()

    sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
    token = sharedPreferences.getString("token", "empty")!!

    val request = ServiceBuilder.buildService(EndPoints::class.java)
    val call = request.getAnnouncement(token!!)

    call.enqueue(object: Callback<ArrayList<Advertisements>> {
      override fun onResponse(
        call: Call<ArrayList<Advertisements>>,
        response: Response<ArrayList<Advertisements>>
      ) {
        if(response.isSuccessful){
          val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
          recView?.layoutManager = LinearLayoutManager(this@Favorites.context)
          ads = response.body()!!
          recView?.adapter = LineAdapterFavs(ads, token)

        }
      }

      override fun onFailure(call: Call<ArrayList<Advertisements>>, t: Throwable) {
        val number = 1
      }
    })

    llAreaLessButton?.setOnClickListener{
      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortByDescending { it.netArea }
      recView?.adapter = LineAdapterFavs(ads, token)
      Toast.makeText(this.context, "Ordenar Area Mais", Toast.LENGTH_SHORT).show()
    }

    llAreaPlusButton?.setOnClickListener{
      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortBy { it.netArea }
      recView?.adapter = LineAdapterFavs(ads, token)
      Toast.makeText(this.context, "Ordenar Area Menos", Toast.LENGTH_SHORT).show()
    }

    llNameLessButton?.setOnClickListener{
      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortByDescending { it.name }
      recView?.adapter = LineAdapterFavs(ads, token)
      Toast.makeText(this.context, "Ordenar Nome Menos", Toast.LENGTH_SHORT).show()
    }

    llNamePlusButton?.setOnClickListener{
      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortBy { it.name }
      recView?.adapter = LineAdapterFavs(ads, token)
      Toast.makeText(this.context, "Ordenar Nome Mais", Toast.LENGTH_SHORT).show()
    }

    llPriceLessButton?.setOnClickListener{
      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortBy { it.price }
      recView?.adapter = LineAdapterFavs(ads, token)
      Toast.makeText(this.context, "Ordenar Preço Menos", Toast.LENGTH_SHORT).show()
    }

    llPricePlusButton?.setOnClickListener{
      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortByDescending { it.price }
      recView?.adapter = LineAdapterFavs(ads, token)
      Toast.makeText(this.context, "Ordenar Preço Mais", Toast.LENGTH_SHORT).show()
    }

    searchButton?.setOnClickListener {

      val recView = getView()?.findViewById<RecyclerView>(R.id.favoritesRv)
      ads.sortByDescending { it.price }
      recView?.adapter = LineAdapterFavs(ads, token)
      if (adsFiltered.isNotEmpty()) {
        adsFiltered.clear()
      }

      if (checkBoxWifi!!.isChecked) {
        for (a in ads) {
          if (a.wifi) {
            adsFiltered.add(a)
          }
        }
        recView?.adapter = LineAdapterFavs(adsFiltered, token)
      } else if (checkBoxAccessibility!!.isChecked) {
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.accessibilty) {
            adsFiltered.add(a)
          }
        }
        recView?.adapter = LineAdapterFavs(adsFiltered, token)
      } else if (checkBoxWifi!!.isChecked && checkBoxAccessibility!!.isChecked) {
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.wifi && a.accessibilty) {
            adsFiltered.add(a)
          }
        }
        recView?.adapter = LineAdapterFavs(adsFiltered, token)
      } else if (editTextMin!!.length()!=0 && editTextMax!!.length()!=0){
        for (a in ads) {
          if(a.price >= editTextMin?.text.toString().toInt() && a.price <= editTextMax?.text.toString().toInt())
            adsFiltered.add(a)
        }
        recView?.adapter = LineAdapterFavs(adsFiltered, token)
      } else if (checkBoxApartment!!.isChecked){
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.type == 1) {
            adsFiltered.add(a)
          }
        }
        recView?.adapter = LineAdapterFavs(adsFiltered, token)
      } else if (checkBoxRoom!!.isChecked){
        if (adsFiltered.isNotEmpty()) {
          adsFiltered.clear()
        }
        for (a in ads) {
          if (a.type == 2) {
            adsFiltered.add(a)
          }
        }
        recView?.adapter = LineAdapterFavs(adsFiltered, token)
      }
      else {
        recView?.adapter = LineAdapterFavs(ads, token)
      }
    }

    filterDropdown?.visibility = View.GONE
    filterButton?.setOnClickListener{
      if(filterDropdown!!.isVisible){
        filterDropdown.visibility = View.GONE
      }
      else{
        if(orderByDropdown!!.isVisible){
          orderByDropdown.visibility = View.GONE
        }
        filterDropdown.visibility = View.VISIBLE
      }
    }

    orderByDropdown?.visibility = View.GONE
    orderByButton?.setOnClickListener{
      if(orderByDropdown!!.isVisible){
        orderByDropdown.visibility = View.GONE
      }
      else{
        if(filterDropdown!!.isVisible){
          filterDropdown.visibility = View.GONE
        }
        orderByDropdown.visibility = View.VISIBLE
      }
    }

  }
}
