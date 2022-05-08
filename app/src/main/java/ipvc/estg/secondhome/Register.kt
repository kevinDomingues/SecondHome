package ipvc.estg.secondhome

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val backButton = findViewById<Button>(R.id.backArrowRegister)
        val loginButton = findViewById<TextView>(R.id.loginBlue)
        val birthDayText = findViewById<EditText>(R.id.birthdayDate)
        val birthDayButton = findViewById<Button>(R.id.birthdayButton)
        val signUpButton = findViewById<Button>(R.id.btnSignUp)

        val editEmail = findViewById<EditText>(R.id.email_input)
        val editPhoneNumber = findViewById<EditText>(R.id.tlm_input)
        val editName = findViewById<EditText>(R.id.name_input)
        val editUsername = findViewById<EditText>(R.id.user_input)
        val editPassword = findViewById<EditText>(R.id.pass_input)
        val editPassword2 = findViewById<EditText>(R.id.pass_input2)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        birthDayText.setText(""+day+"/"+month+"/"+year)

        backButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        birthDayButton.setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view, mYear, mMonth, mDay ->
                birthDayText.setText(""+mDay+"/"+mMonth+"/"+mYear)
            }, year, month, day)
            dpd.show()
        }

        signUpButton.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val phoneNumber = editPhoneNumber.text.toString().trim()
            val name = editName.text.toString().trim()
            val username = editUsername.text.toString().trim()
            val birthdayDate = birthDayText.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val password2 = editPassword2.text.toString().trim()

            if(email.isEmpty()){
                editEmail.error = getString(R.string.errorEmailEmpty)
                editEmail.requestFocus()
                return@setOnClickListener
            }

            if(phoneNumber.isEmpty()){
                editPhoneNumber.error = getString(R.string.errorPhoneNumberEmpty)
                editPhoneNumber.requestFocus()
                return@setOnClickListener
            }

            if(name.isEmpty()){
                editName.error = getString(R.string.errorFullNameEmpty)
                editName.requestFocus()
                return@setOnClickListener
            }

            if(username.isEmpty()){
                editUsername.error = getString(R.string.errorUsernameEmpty)
                editUsername.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                editPassword.error = getString(R.string.errorPasswordsEmpty)
                editPassword.requestFocus()
                return@setOnClickListener
            }

            if(password2.isEmpty()){
                editPassword2.error = getString(R.string.errorPasswordsEmpty)
                editPassword2.requestFocus()
                return@setOnClickListener
            }

            if(password != password2){
                editPassword2.error = getString(R.string.errorPasswordsDoNotMatch)
                editPassword.requestFocus()
                return@setOnClickListener
            }

            handleSignUp(email, phoneNumber.toInt(), name, username, birthdayDate, password)
        }
    }

    fun handleSignUp(email: String, phoneNumber: Int, name: String, username: String, birthDayDate: String, password: String){
        val date = SimpleDateFormat("dd/MM/yyyy").parse(birthDayDate)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.signUp(username, name, email, password, date, phoneNumber)
        
        call.enqueue(object: Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if(response.isSuccessful){
                    val c: DefaultResponse = response.body()!!
                    Toast.makeText(this@Register, R.string.successfulSignUp, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Register, Login::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@Register, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })
    }
}