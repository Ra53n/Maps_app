package com.example.mapsapp.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsapp.R
import com.example.mapsapp.domain.model.MarkerEntity

class MarkerEditingAdapter(
    private val onDeleteMarkerClick: (MarkerEntity) -> Unit,
    private val onSaveMarkerClick: (MarkerEntity) -> Unit
) :
    RecyclerView.Adapter<MarkerEditingAdapter.MarkerEditingViewHolder>() {

    private var markersList = emptyList<MarkerEntity>()

    fun setItems(list: List<MarkerEntity>) {
        markersList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MarkerEditingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.marker_item, parent, false)
        )

    override fun getItemCount() = markersList.size

    override fun onBindViewHolder(holder: MarkerEditingViewHolder, position: Int) {
        holder.bind(markersList[position])
    }

    inner class MarkerEditingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(marker: MarkerEntity) {
            val title = itemView.findViewById<AppCompatEditText>(R.id.markerTitle)
            title.setText(marker.title)
            val annotation = itemView.findViewById<AppCompatEditText>(R.id.markerAnnotation)
            annotation.setText(marker.annotation)
            itemView.findViewById<TextView>(R.id.coordinates).text =
                "Координаты: \n${marker.latLng.latitude}, ${marker.latLng.longitude}"
            itemView.findViewById<ImageButton>(R.id.deleteButton)
                .setOnClickListener { onDeleteMarkerClick(marker) }
            itemView.findViewById<ImageButton>(R.id.saveButton)
                .setOnClickListener {
                    onSaveMarkerClick(
                        marker.copy(
                            title = title.text.toString(),
                            annotation = annotation.text.toString()
                        )
                    )
                }
        }
    }
}