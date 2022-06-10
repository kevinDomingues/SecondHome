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
import ipvc.estg.secondhome.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Manager : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<Card_user.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.allUsers()
        call.enqueue(object : Callback<List<User>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val c = response.body()!!
                    adapter = Card_user(c)
                    recyclerView.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("teste",t.toString())
            }
        })


    }
}