package ipvc.estg.secondhome

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.Announcement
import ipvc.estg.secondhome.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST


class Card_ads(userToken: String, c: List<Announcement>) : RecyclerView.Adapter<Card_ads.ViewHolder>() {

    var size=c.size;
    var announcement=c

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card_ads.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cards_ads, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: Card_ads.ViewHolder, position: Int) {
        var stringPrice=announcement.get(position).price.toString()+'$'
        holder.nomeText.text=announcement.get(position).name
        holder.localizacao.text = announcement.get(position).location
        holder.area.text=announcement.get(position).netArea.toString()
        holder.bathroom.text=announcement.get(position).bathrooms.toString()
        holder.preco.text=stringPrice

        if(announcement.get(position).type==1){
            holder.tipo.text="Apartamento"
        }else if(  announcement.get(position).type==2){
            holder.tipo.text="Casa"
        }else{
            holder.tipo.text="Quarto"
        }

        holder.bed.text=announcement.get(position).rooms.toString()
        holder.contacto.text=announcement.get(position).contact
        if(announcement.get(position).wifi==true){
            holder.wifi.visibility=View.VISIBLE
        }
        if(announcement.get(position).accessibilty==true){
            holder.accesibility.visibility=View.VISIBLE
        }


        holder.button.setOnClickListener{it->
            Log.d("teste", "teste" +announcement.get(position)._id)
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.deleteAnnouncement(announcement.get(position)._id)
            call.enqueue(object : Callback<DefaultResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        val c = response.body()!!
                        (it.getContext() as Activity).finish()
                    }
                }
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Log.d("teste",t.toString())
                }
            })
        }
    }

    override fun getItemCount(): Int {
        Log.d("teste","size"+ size.toString())
        return size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeText: TextView
        var localizacao: TextView
        var button: ImageButton
        var tipo :TextView
        var preco:TextView
        var contacto:TextView
        var bed:TextView
        var bathroom:TextView
        var area:TextView
        var wifi:ImageView
        var accesibility:ImageView

        init {
            nomeText = itemView.findViewById(R.id.textView)
            localizacao = itemView.findViewById(R.id.localizacao)

           button = itemView.findViewById(R.id.imageButton)
            tipo= itemView.findViewById(R.id.tipo)
            preco=itemView.findViewById(R.id.preco)
            contacto=itemView.findViewById(R.id.contacto)
            bed=itemView.findViewById(R.id.textView9)
            bathroom=itemView.findViewById(R.id.textView10)
            area=itemView.findViewById(R.id.textView11)
            wifi=itemView.findViewById(R.id.imageView5)
            accesibility=itemView.findViewById(R.id.imageView7)

        }
    }


}