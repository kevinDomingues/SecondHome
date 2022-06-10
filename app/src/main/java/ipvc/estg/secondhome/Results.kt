package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class Results : AppCompatActivity() {

  private lateinit var adList: ArrayList<Advertisements>

  lateinit var sharedPreferences: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_resultados)

    val parametro = intent.getStringExtra(PARAM_NAME)
    val textView = findViewById<TextView>(R.id.tv1resultados)

    textView.text = parametro

    val backButton = findViewById<ImageView>(R.id.btnBackPage)

    backButton.setOnClickListener {
      val intent = Intent(this, Search::class.java)
      startActivity(intent)
    }

    sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", "empty")

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
          recView.adapter = LineAdapter(response.body()!!)
        }
      }

      override fun onFailure(call: Call<ArrayList<Advertisements>>, t: Throwable) {
      val number = 1
      }
    })
  }
}
