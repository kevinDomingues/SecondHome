package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.DefaultResponse
import ipvc.estg.secondhome.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

lateinit var sharedPreference: SharedPreferences

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val backButton = findViewById<ImageView>(R.id.backArrow)

        val registerButton = findViewById<TextView>(R.id.registerBlue)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        val editUsername = findViewById<EditText>(R.id.user_inputLogin)
        val editPassword = findViewById<EditText>(R.id.pass_inputLogin)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (username.isEmpty()) {
                editUsername.error = getString(R.string.errorUsernameEmpty)
                editUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editPassword.error = getString(R.string.errorPasswordsEmpty)
                editPassword.requestFocus()
                return@setOnClickListener
            }

            handleLogin(username, password)
        }
    }

    fun handleLogin(username: String, password: String) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(username, password)

        call.enqueue(object: Callback<User> {
          @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val c: User = response.body()!!
                    var editor = sharedPreference.edit()
                    editor.putString("token", c.token)
                    editor.commit()

                    Toast.makeText(
                        this@Login,
                        getString(R.string.successfulLogin) + ", " + c.name,
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@Login,MainPage::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@Login, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
      })
    }
  }

