package ipvc.estg.secondhome.models

import java.util.*

data class User(
    val username: String,
    val name: String,
    val email: String,
    val contact: Int,
    val birthdayDate: String,
    val token: String
)
