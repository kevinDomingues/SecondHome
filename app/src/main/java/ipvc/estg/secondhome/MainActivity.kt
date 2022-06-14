package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)

        val token = sharedPreferences.getString("token", "empty")

        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener{
            if(token != "empty"){
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }

    }
}