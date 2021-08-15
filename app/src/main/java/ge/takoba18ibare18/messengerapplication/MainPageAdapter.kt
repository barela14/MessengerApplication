package ge.takoba18ibare18.messengerapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainPageAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val cell = LayoutInflater.from(parent.context).inflate(R.layout.main_page_messege_cell, parent, false)
        return RecyclerViewViewHolder(cell)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}

class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.findViewById(R.id.profile_image)
    var name: TextView = itemView.findViewById(R.id.name)
    var text: TextView = itemView.findViewById(R.id.text)
    var time: TextView = itemView.findViewById(R.id.time)
}