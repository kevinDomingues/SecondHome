package ipvc.estg.secondhome

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
lateinit var sharedPreferences: SharedPreferences
class Update_user : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val backButton = findViewById<Button>(R.id.backArrowUpd)
        val loginButton = findViewById<TextView>(R.id.loginBlueUpd)
        val birthDayText = findViewById<EditText>(R.id.birthdayDateUpd)
        val birthDayButton = findViewById<Button>(R.id.birthdayButtonUpd)
        val signUpButton = findViewById<Button>(R.id.btnSignUpUpd)

        val editEmail = findViewById<EditText>(R.id.email_inputUpd)
        val editPhoneNumber = findViewById<EditText>(R.id.tlm_inputUpd)
        val editName = findViewById<EditText>(R.id.name_inputUpd)
        val editUsername = findViewById<EditText>(R.id.user_inputUpd)
        val editPassword = findViewById<EditText>(R.id.pass_inputUpd)
        val editPassword2 = findViewById<EditText>(R.id.pass_input2Upd)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        birthDayText.setText("" + day + "/" + month + "/" + year)

        editEmail.setText(sharedPreferences.getString("email","defaultName"))
        editUsername.setText(sharedPreferences.getString("username","defaultName"))
        editName.setText(sharedPreferences.getString("name","defaultName"))
        editPhoneNumber.setText(sharedPreferences.getInt("contact",0))
        birthDayText.setText(sharedPreferences.getString("birthdayDate","0"))

        backButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        birthDayButton.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    birthDayText.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        signUpButton.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val contact = editPhoneNumber.text.toString().trim()
            val name = editName.text.toString().trim()
            val username = editUsername.text.toString().trim()
            val birthdayDate = birthDayText.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val password2 = editPassword2.text.toString().trim()

            if (email.isEmpty()) {
                editEmail.error = getString(R.string.errorEmailEmpty)
                editEmail.requestFocus()
                return@setOnClickListener
            }

            if (contact.isEmpty()) {
                editPhoneNumber.error = getString(R.string.errorPhoneNumberEmpty)
                editPhoneNumber.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                editName.error = getString(R.string.errorFullNameEmpty)
                editName.requestFocus()
                return@setOnClickListener
            }

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

            if (password2.isEmpty()) {
                editPassword2.error = getString(R.string.errorPasswordsEmpty)
                editPassword2.requestFocus()
                return@setOnClickListener
            }

            if (password != password2) {
                editPassword2.error = getString(R.string.errorPasswordsDoNotMatch)
                editPassword.requestFocus()
                return@setOnClickListener
            }

            updateUser(email, name, username, birthdayDate,  contact.toInt() );
        }


    }

    private fun updateUser(email: String,  name: String, username: String, birthDayDate: String, contact: Int) {
        val date = SimpleDateFormat("dd/MM/yyyy").parse(birthDayDate)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val token=sharedPreferences.getString("token","0")
        val call = request.updateUser(token!!,username, name, email,  date, contact)

        call.enqueue(object: Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if(response.isSuccessful){
                    val c: DefaultResponse = response.body()!!
                    Toast.makeText(this@Update_user, R.string.successfullUpdate, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Update_user, Login::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@Update_user, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })

    }

}