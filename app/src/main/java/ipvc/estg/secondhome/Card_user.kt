package ipvc.estg.secondhome

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.api.EndPoints
import ipvc.estg.secondhome.api.ServiceBuilder
import ipvc.estg.secondhome.models.DefaultResponse
import ipvc.estg.secondhome.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        holder.button.setOnClickListener{
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.deleteUser(user.get(position)._id)
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
        Log.d("teste", size.toString())
        return size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeText: TextView
        var anuncioText: TextView
        var button: ImageButton

        init {
            nomeText = itemView.findViewById(R.id.nome)
            anuncioText = itemView.findViewById(R.id.anuncio)
            button = itemView.findViewById(R.id.button)


        }
    }


}