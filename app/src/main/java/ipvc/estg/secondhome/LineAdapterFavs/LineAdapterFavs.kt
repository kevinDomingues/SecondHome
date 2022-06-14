package ipvc.estg.secondhome.LineAdapterFavs


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ipvc.estg.secondhome.PARAM_NAME
import ipvc.estg.secondhome.R
import ipvc.estg.secondhome.ViewAds
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Advertisements
import ipvc.estg.secondhome.models.Announcement
import ipvc.estg.secondhome.models.DefaultResponse
import kotlinx.android.synthetic.main.adline.view.*
import kotlinx.android.synthetic.main.adlinefav.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LineAdapterFavs(val list: ArrayList<Advertisements>, val token: String):RecyclerView.Adapter<LineViewHolder>(){

  private lateinit var context: Context

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

    val itemView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.adlinefav, parent, false );

    context=parent.context
    return LineViewHolder(itemView)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun onBindViewHolder(holder: LineViewHolder, position: Int) {

    val currentPlace = list[position]

    if(!currentPlace.images.isNullOrEmpty()){
      var imageUrl = "http://localhost:3000/"
      if (currentPlace.images.contains(",")) {
        imageUrl += currentPlace.images.substring(0, currentPlace.images.indexOf(","))
      }
      imageUrl += currentPlace.images.trim()

      Picasso.get().load(imageUrl).into(holder.adImage)
    }

    holder.adName.text = currentPlace.name
    holder.contacts.text = currentPlace.contact
    holder.price.text = currentPlace.price.toString() + " €"
    holder.rooms.text = currentPlace.rooms.toString()
    holder.bathrooms.text = currentPlace.bathrooms.toString()
    holder.location.text = currentPlace.location
    holder.area.text = currentPlace.netArea.toString()

    if (currentPlace.type == 1) {
      holder.type.setText(R.string.house_type_1)
    } else if (currentPlace.type == 2) {
      holder.type.setText(R.string.house_type_2)
    }

    holder.removeFav.setOnClickListener {
      val request = ServiceBuilder.buildService(EndPoints::class.java)
      val call = request.deleteFavorite(token, currentPlace._id)


      call.enqueue(object : Callback<DefaultResponse> {
        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
          if (response.isSuccessful) {
            Toast.makeText(context, R.string.successfulRemoveFav, Toast.LENGTH_SHORT).show()
          }
        }

        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
          Toast.makeText(context,R.string.unsuccessfulRemoveFav, Toast.LENGTH_SHORT).show()
        }
      })
    }

    holder.card.setOnClickListener {
      val intent = Intent(context, ViewAds::class.java).apply {
        putExtra(PARAM_NAME, currentPlace.idAnnouncement)
        putExtra("lat", currentPlace.lat)
        putExtra("lng", currentPlace.lng)
        putExtra("searchLat", 0.0)
        putExtra("searchLng", 0.0)
      }
      context.startActivity(intent)
    }
  }

  fun add(newhabitacoes: Advertisements) {
    list.add(newhabitacoes)
    notifyDataSetChanged()
  }

}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
  val adName = itemView.adnamefav
  val contacts = itemView.contactsfav
  val price = itemView.precofav
  val rooms = itemView.numquartosfav
  val bathrooms = itemView.numbathroomsfav
  val adImage: ImageView = itemView.adimagefav
  val removeFav : ImageButton = itemView.removeFav
  val location = itemView.locationFav
  val type = itemView.tipofav
  val area = itemView.areafav
  val card: ConstraintLayout = itemView.cardfav
}
