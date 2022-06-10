package ipvc.estg.secondhome

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.models.User

class Card_user( c: List<User>) : RecyclerView.Adapter<Card_user.ViewHolder>() {

    var size=c.size;
    var user=c
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_users, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: Card_user.ViewHolder, position: Int) {
            holder.nomeText.text = user.get(position).username
            holder.anuncioText.text=user.get(position).numAnnouncements.toString()
            holder.button.setOnClickListener{
                Log.d("teste", "funciona"+position)
            }
        holder.itemView.setOnClickListener {v->
            val intent = Intent(v.context, ManagerAds::class.java)
            intent.putExtra("token",  user.get(position)._id)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.d("teste", size.toString())
        return size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeText: TextView
        var anuncioText: TextView
        var button: Button

        init {
            nomeText = itemView.findViewById(R.id.nome)
            anuncioText = itemView.findViewById(R.id.anuncio)
            button = itemView.findViewById(R.id.button)


        }
    }


}