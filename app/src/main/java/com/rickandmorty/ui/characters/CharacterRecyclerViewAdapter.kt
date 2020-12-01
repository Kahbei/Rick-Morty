package com.rickandmorty.ui.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rickandmorty.R
import com.rickandmorty.model.CharacterModel
import com.squareup.picasso.Picasso

typealias OnClickItem = (character: CharacterModel, views: Pair<View, View>) -> Unit

class CharacterRecyclerViewAdapter(
    private var values: List<CharacterModel>,
    private val onItemClick: OnClickItem,
) : RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.main.setOnClickListener {
            // TODO Décommenter après avoir ajouter le nom du personnage
            holder.name.transitionName = "name"
            holder.image.transitionName = "image"
            onItemClick(item, holder.name to holder.image)
        }
        Picasso.get().load(item.image).into(holder.image)
        holder.name.text = item.name
    }

    override fun getItemCount(): Int = values.size

    override fun getItemId(position: Int) = values[position].id.toLong()

    fun updateCharacters(characters: List<CharacterModel>) {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return values.size
            }

            override fun getNewListSize(): Int {
                return characters.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return values[oldItemPosition].id ==
                        characters[newItemPosition].id
            }

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                val newProduct = characters[newItemPosition]
                val oldProduct = values[oldItemPosition]
                return (newProduct.id == oldProduct.id)
            }
        })
        values = characters
        result.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val main: View = view.findViewById(R.id.main)
        val image: ImageView = view.findViewById(R.id.imgAvatar)
        val name: TextView = view.findViewById(R.id.nameInList)
    }
}