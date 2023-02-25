package com.example.cargaschile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cargaschile.databinding.ProvinciaItemBinding


class ProvinciaAdapter(private val collections: List<ProvinciaModel>) :
    RecyclerView.Adapter<ProvinciaAdapter.CollectionsViewHolder>() {

    lateinit var context: Context

    class CollectionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ProvinciaItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.provincia_item, parent, false)
        context = parent.context
        return CollectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        holder.binding.apply {
            val collection = collections[position]
            tvProvinciaTitle.text = collection.provinciaTitle
            val subItemAdapter = ComunaAdapter(collection.comunaModels)
            rvComuna.adapter = subItemAdapter
            rvComuna.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            tvProvinciaTitle.setOnClickListener {
                rvComuna.visibility = if (rvComuna.isShown) View.GONE else View.VISIBLE
            }
        }
    }

    override fun getItemCount() = collections.size
}