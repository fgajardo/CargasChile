package com.example.cargaschile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cargaschile.databinding.ComunaItemBinding
import com.example.cargaschile.databinding.ComunaItemSelBinding

class ComunaAdapter (private val comunaModel: List<ComunaModel>) :
    RecyclerView.Adapter<ComunaAdapter.ViewHolder>() {

    private var selectedPos = RecyclerView.NO_POSITION
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ComunaItemBinding.bind(itemView)
    }
    class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ComunaItemSelBinding.bind(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        if(position == selectedPos) return 2
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.comuna_item, parent, false)
        if(2 == viewType)
            view = LayoutInflater.from(parent.context).inflate(R.layout.comuna_item_sel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //if(0 == holder.itemViewType)
        holder.binding.apply {
            tvComunaTitle.text= comunaModel[position].comunaTitle
            holder.itemView.isSelected = (selectedPos == position)
            tvComunaTitle.setOnClickListener{
                Shared.cb(comunaModel[position].comunaID)
                /*notifyItemChanged(selectedPos)
                selectedPos = holder.layoutPosition
                println("sp ahora es $selectedPos")
                notifyItemChanged(selectedPos)*/
            }
        } //else holder
    }

    override fun getItemCount() = comunaModel.size
}