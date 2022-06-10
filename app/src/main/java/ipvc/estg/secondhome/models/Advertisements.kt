package ipvc.estg.secondhome.models

data class Advertisements(
  val idUser: String,
  val name: String,
  val type: Int,
  val rooms: Int,
  val netArea: Int,
  val bathrooms: Int,
  val price: Int,
  val location: String,
  val constructionYear: Int,
  val accessibilty: Boolean,
  val email: String,
  val contact: String,
  val images: String
)