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
import ipvc.estg.secondhome.models.User
import ipvc.estg.secondhome.utils.parseDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class Update_user : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        sharedPreferences = getSharedPreferences("PREFERENCE_AUTH", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token","0")

        populateUser(token!!)

        val backButton = findViewById<Button>(R.id.backArrowUpd)
        val birthDayText = findViewById<EditText>(R.id.birthdayDateUpd)
        val birthDayButton = findViewById<Button>(R.id.birthdayButtonUpd)
        val updateButton = findViewById<Button>(R.id.btnUpdate)

        val editEmail = findViewById<EditText>(R.id.email_inputUpd)
        val editPhoneNumber = findViewById<EditText>(R.id.tlm_inputUpd)
        val editName = findViewById<EditText>(R.id.name_inputUpd)
        val editUsername = findViewById<EditText>(R.id.user_inputUpd)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        birthDayText.setText("" + day + "/" + month + "/" + year)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUserById(token!!)

        call.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val c: User = response.body()!!
                    editEmail.setText(c.email);
                    editUsername.setText(c.username)
                    editName.setText(c.name)
                    editPhoneNumber.setText(c.contact.toString(), TextView.BufferType.EDITABLE)
                    birthDayText.setText(parseDate(c.birthdayDate))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@Update_user, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })

        backButton.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
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

        updateButton.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val contact = editPhoneNumber.text.toString().trim()
            val name = editName.text.toString().trim()
            val username = editUsername.text.toString().trim()
            val birthdayDate = birthDayText.text.toString().trim()

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

            updateUser(email, name, username, birthdayDate, contact.toInt(), token!!)
        }

    }

    private fun populateUser(
        token: String
    ) {
        val editEmail = findViewById<EditText>(R.id.email_inputUpd)
        val editPhoneNumber = findViewById<EditText>(R.id.tlm_inputUpd)
        val editName = findViewById<EditText>(R.id.name_inputUpd)
        val editUsername = findViewById<EditText>(R.id.user_inputUpd)
        val birthDayText = findViewById<EditText>(R.id.birthdayDateUpd)

        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val call = request.getUserById(token)

        call.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val c: User = response.body()!!

                    editEmail.setText(c.email);
                    editUsername.setText(c.username)
                    editName.setText(c.name)
                    editPhoneNumber.setText(c.contact.toString())
                    birthDayText.setText(parseDate(c.birthdayDate))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@Update_user, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUser(
        email: String,
        name: String,
        username: String,
        birthDayDate: String,
        contact: Int,
        token: String
    ) {
        val date = SimpleDateFormat("dd/MM/yyyy").parse(birthDayDate)

        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val call = request.updateUser(token!!, username, name, email, date, contact)

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    val c: DefaultResponse = response.body()!!
                    Toast.makeText(this@Update_user, R.string.successfullUpdate, Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@Update_user, Profile::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@Update_user, R.string.errorSignUp, Toast.LENGTH_SHORT).show()
            }
        })

    }
}
