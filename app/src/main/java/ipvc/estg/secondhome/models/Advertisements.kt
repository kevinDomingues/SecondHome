package ipvc.estg.secondhome.models

data class Advertisements(
  val _id: String,
  val idUser: String,
  val name: String,
  val type: Int,
  val rooms: Int,
  val netArea: Int,
  val bathrooms: Int,
  val price: Int,
  val location: String,
  val lat: Double,
  val lng: Double,
  val constructionYear: Int,
  val accessibilty: Boolean,
  val email: String,
  val contact: String,
  val images: String,
  val wifi: Boolean
)
