package com.rickandmorty.ui.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rickandmorty.R
import com.rickandmorty.model.EpisodeModel

typealias OnClickItem = (episode: EpisodeModel, views: Pair<View, View>) -> Unit

class EpisodesRecyclerViewAdapter (
    private var valuesEpisode: List<EpisodeModel>,
    private var onClickItem: OnClickItem,
): RecyclerView.Adapter<EpisodesRecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val main: View = view.findViewById(R.id.episodeMain)
        val episodeNum: TextView = view.findViewById(R.id.episodeNumber)
        val episodeName: TextView = view.findViewById(R.id.episodeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.episode_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = valuesEpisode[position]
        holder.episodeName.text = item.name
        holder.episodeNum.text = item.episode
        holder.main.setOnClickListener{
            holder.episodeNum.transitionName = "SXXEXX"
            holder.episodeName.transitionName = "episodeName"
            onClickItem(item, holder.episodeName to holder.episodeNum)
        }
    }

    override fun getItemCount(): Int = valuesEpisode.size

    override fun getItemId(position: Int) = valuesEpisode[position].id.toLong()

    fun updateEpisode(episodes: List<EpisodeModel>){
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return valuesEpisode.size
            }

            override fun getNewListSize(): Int {
                return episodes.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return valuesEpisode[oldItemPosition].id ==
                        episodes[newItemPosition].id
            }

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                val newProduct = episodes[newItemPosition]
                val oldProduct = valuesEpisode[oldItemPosition]
                return (newProduct.id == oldProduct.id)
            }
        })

        valuesEpisode = episodes
        result.dispatchUpdatesTo(this)
    }
}