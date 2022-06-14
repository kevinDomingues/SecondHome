package ipvc.estg.secondhome

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.secondhome.databinding.ActivityViewAdOnMapBinding

class ViewAdOnMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityViewAdOnMapBinding

    var searchLat: Double = 0.0
    var searchLng: Double = 0.0
    var lat: Double = 0.0
    var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lat = intent.getDoubleExtra("lat", 0.00)
        lng = intent.getDoubleExtra("lng", 0.00)
        searchLat = intent.getDoubleExtra("searchLat", 0.00)
        searchLng = intent.getDoubleExtra("searchLng", 0.00)

        binding = ActivityViewAdOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val distancia = findViewById<TextView>(R.id.txtcoordenadas)

        if(searchLat == 0.0 || searchLng == 0.0){
            val ad = LatLng(lat, lng)
            mMap.addMarker(MarkerOptions().position(ad).title("Ad"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ad))
        } else {
            val ad = LatLng(lat, lng)
            val searchLocation = LatLng(searchLat, searchLng)
            mMap.addMarker(MarkerOptions().position(searchLocation).title("Location"))
            mMap.addMarker(MarkerOptions().position(ad).title("Ad"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ad, 15.0f))

            val distance = Math.round(calculateDistance(lat, lng, searchLat, searchLat)/1000)
            distancia.setText(getString(R.string.distance)+" "+distance+" m")
            distancia.visibility = View.VISIBLE
        }

        mMap.uiSettings.isZoomControlsEnabled = true

    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)

        return results[0]
    }
}