package ipvc.estg.secondhome.LineAdapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import ipvc.estg.secondhome.PARAM_NAME
import ipvc.estg.secondhome.R
import ipvc.estg.secondhome.Results
import ipvc.estg.secondhome.ViewAds
import ipvc.estg.secondhome.models.Advertisements
import kotlinx.android.synthetic.main.adline.view.*

class LineAdapter(val list: ArrayList<Advertisements>):RecyclerView.Adapter<LineViewHolder>(){

  private lateinit var context: Context;

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
    val itemView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.adline, parent, false );

    context = parent.context
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
    holder.rooms.text = currentPlace.rooms.toString()+" "+holder.itemView.getContext().getString(R.string.number_rooms)

    holder.card.setOnClickListener {
      val intent = Intent(context, ViewAds::class.java).apply {
        putExtra(PARAM_NAME, currentPlace._id)
      }
      context.startActivity(intent)
    }
  }

  fun add(newAd: Advertisements) {
    list.add(newAd)
    notifyDataSetChanged()
  }

}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
  val adName = itemView.adname
  val contacts = itemView.contacts
  val price = itemView.preco
  val rooms = itemView.numquartos
  val adImage: ImageView = itemView.adimage
  val card: MaterialCardView = itemView.card
}
