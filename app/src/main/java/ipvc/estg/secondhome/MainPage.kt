package ipvc.estg.secondhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val backButton = findViewById<ImageView>(R.id.updateButton)

        backButton.setOnClickListener {
            val intent = Intent(this, Update_user::class.java)
            startActivity(intent)
        }
    }
}