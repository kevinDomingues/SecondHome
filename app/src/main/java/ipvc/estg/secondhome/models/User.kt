package ipvc.estg.secondhome.models

import java.util.*

data class User(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val contact: Int,
    val birthdayDate: Date,
    val token: String
)
