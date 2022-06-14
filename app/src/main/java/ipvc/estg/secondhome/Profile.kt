package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.text.SpannableString
import android.widget.*
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Profile : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_profile)

      sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
      val token = sharedPreferences.getString("token", "empty")

      val backButton = findViewById<ImageView>(R.id.backArrow)

      backButton.setOnClickListener {
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
      }

      val editAccount = findViewById<Button>(R.id.editarConta)

      editAccount.setOnClickListener {
        val intent = Intent(this, Update_user::class.java)
        startActivity(intent)
      }

      profile(token!!)
    }

    fun parseDate(date: String): String {
      val parsedDate = ""+date.substring(8,10)+"/"+date.substring(5, 7)+"/"+date.substring(0,4)

      return parsedDate

    }
      fun profile(token: String) {
        val username = findViewById<EditText>(R.id.username)
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val contact = findViewById<EditText>(R.id.contact)
        val birthdayDate = findViewById<EditText>(R.id.birthdayDate)

        username.inputType = TYPE_NULL
        name.inputType = TYPE_NULL
        email.inputType = TYPE_NULL
        contact.inputType = TYPE_NULL
        birthdayDate.inputType = TYPE_NULL

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUserById(token)

        call.enqueue(object : Callback<User> {
          override fun onResponse(
            call: Call<User>,
            response: Response<User>
          ) {
            if (response.isSuccessful) {
              val c: User = response.body()!!

              username.setText(c.username)
              name.setText(c.name)
              email.setText(c.email)
              contact.setText(c.contact.toString())
              birthdayDate.setText(parseDate(c.birthdayDate))
            }
          }
          override fun onFailure(call: Call<User>, t: Throwable) {
            Toast.makeText(this@Profile, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
          }
        })
      }
}
