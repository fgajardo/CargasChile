package com.example.cargaschile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cargaschile.databinding.RegionItemBinding

class RegionAdapter(private val collections: List<RegionModel>) :
    RecyclerView.Adapter<RegionAdapter.CollectionsViewHolder>() {

    lateinit var context: Context

    class CollectionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RegionItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.region_item, parent, false)
        context = parent.context
        return CollectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        holder.binding.apply {
            val collection = collections[position]
            tvRegionTitle.text = collection.regionTitle
            val subItemAdapter = ProvinciaAdapter(collection.provinciaModels)
            rvProvincia.adapter = subItemAdapter
            rvProvincia.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            tvRegionTitle.setOnClickListener {
                rvProvincia.visibility = if (rvProvincia.isShown) View.GONE else View.VISIBLE
            }
        }
    }

    override fun getItemCount() = collections.size
}