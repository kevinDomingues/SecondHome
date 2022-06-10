package ipvc.estg.secondhome.models

import java.util.*

data class Announcement (
    val idUser: String,
    val type: Number,
    val typology: Number,
    val netArea: Number,
    val bathrooms: Number,
    val price: Number,
    val location: String,
    val constructionYear: Number,
    val hourDate: Date,
    val accessibilty:Boolean,
    val wifi:Boolean,
    val rooms : Int
)