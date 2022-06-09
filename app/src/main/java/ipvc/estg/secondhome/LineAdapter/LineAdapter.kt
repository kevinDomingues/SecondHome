package ipvc.estg.secondhome.LineAdapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.secondhome.R
import ipvc.estg.secondhome.models.Habitacoes
import kotlinx.android.synthetic.main.adline.view.*

class LineAdapter(val list: ArrayList<Habitacoes>):RecyclerView.Adapter<LineViewHolder>(){

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
    val itemView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.adline, parent, false );
    return LineViewHolder(itemView)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
    val currentPlace = list[position]

    holder.location.text = currentPlace.location
    holder.netArea.text = currentPlace.netArea.toString()
    holder.price.text = currentPlace.price.toString()
  }

  fun add(newhabitacoes: Habitacoes) {
    list.add(newhabitacoes)
    notifyDataSetChanged()
  }

}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
  val location = itemView.localizacao
  val netArea = itemView.area
  val price = itemView.preco
}
