package ipvc.estg.secondhome

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.models.Announcement

class Card_ads(userToken: String, c: List<Announcement>) : RecyclerView.Adapter<Card_ads.ViewHolder>() {

    var size=c.size;
    var announcement=c
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card_ads.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cards_ads, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: Card_ads.ViewHolder, position: Int) {

        holder.localizacao.text = announcement.get(position).location
        holder.area.text=announcement.get(position).netArea.toString()
        holder.bathroom.text=announcement.get(position).bathrooms.toString()
        holder.preco.text=announcement.get(position).price.toString()
        holder.tipo.text=announcement.get(position).type.toString()
        holder.bed.text=announcement.get(position).rooms.toString()
        if(announcement.get(position).wifi==true){
            holder.wifi.visibility=View.VISIBLE
        }



//        holder.button.setOnClickListener{
//            Log.d("teste", "funciona"+position)
//        }
    }

    override fun getItemCount(): Int {
        Log.d("teste","size"+ size.toString())
        return size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var nomeText: TextView
        var localizacao: TextView
//        var button: Button
        var tipo :TextView
        var preco:TextView
        var contacto:TextView
        var bed:TextView
        var bathroom:TextView
        var area:TextView
        var wifi:ImageView

        init {
//            nomeText = itemView.findViewById(R.id.textView)
            localizacao = itemView.findViewById(R.id.localizacao)

//            button = itemView.findViewById(R.id.button)
            tipo= itemView.findViewById(R.id.tipo)
            preco=itemView.findViewById(R.id.preco)
            contacto=itemView.findViewById(R.id.contacto)
            bed=itemView.findViewById(R.id.textView9)
            bathroom=itemView.findViewById(R.id.textView10)
            area=itemView.findViewById(R.id.textView11)
            wifi=itemView.findViewById(R.id.imageView5)
        }
    }


}