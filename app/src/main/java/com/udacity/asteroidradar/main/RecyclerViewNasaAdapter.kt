package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.GridViewItemBinding

class RecyclerViewNasaAdapter(val onClickListener:OnClickListener):
    ListAdapter<Asteroid, RecyclerViewNasaAdapter.AsteriodViewHolder>(DiffCallback) {
    class AsteriodViewHolder(private val binding: GridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun  bind(asteroid: Asteroid){
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }

    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */

    companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteriodViewHolder {
        return AsteriodViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AsteriodViewHolder, position: Int) {
        val asteriod = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteriod) }
        holder.bind(asteriod)
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit){
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}