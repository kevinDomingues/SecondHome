package ipvc.estg.secondhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)

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

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val c: User = response.body()!!
                    var editor = sharedPreferences.edit()
                    editor.putString("token", c.token)
                    editor.commit()

                    Toast.makeText(
                        this@Login,
                        getString(R.string.successfulLogin) + ", " + c.name,
                        Toast.LENGTH_SHORT
                    ).show()


                    if (c.permissionLevel == 1) {
                        val intent = Intent(this@Login, MainPage::class.java)
                        startActivity(intent)
                    } else if (c.permissionLevel == 2){
                        val intent = Intent(
                            this@Login,
                            Manager::class.java
                        )
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@Login, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

