package ipvc.estg.secondhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var backButton = findViewById<Button>(R.id.backArrowRegister)
        var loginButton = findViewById<TextView>(R.id.loginBlue)

        backButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}