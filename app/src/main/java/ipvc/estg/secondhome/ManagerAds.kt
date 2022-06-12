package ipvc.estg.secondhome

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Announcement
import ipvc.estg.secondhome.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerAds : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<Card_ads.ViewHolder>? = null
private lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        layoutManager = LinearLayoutManager(this)

         recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager


        val token : String? = intent.getStringExtra("token")
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.annoucements(token!!)
        call.enqueue(object : Callback<List<Announcement>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<Announcement>>, response: Response<List<Announcement>>) {
                if (response.isSuccessful) {
                    val c = response.body()!!
                    adapter = Card_ads(token!!,c)
                    recyclerView.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<Announcement>>, t: Throwable) {
                Log.d("teste",t.toString())
            }
        })


    }

    override fun onResume() {
        super.onResume()

        val token : String? = intent.getStringExtra("token")
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.annoucements(token!!)
        call.enqueue(object : Callback<List<Announcement>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<Announcement>>, response: Response<List<Announcement>>) {
                if (response.isSuccessful) {
                    val c = response.body()!!
                    adapter = Card_ads(token!!,c)
                    recyclerView.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<Announcement>>, t: Throwable) {
                Log.d("teste",t.toString())
            }
        })
    }
}