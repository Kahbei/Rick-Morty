package com.rickandmorty.data

import android.content.Context
import android.content.SharedPreferences

class episodesPreference(
    private val context: Context,
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "prefs",
        Context.MODE_PRIVATE
    )
) {

    fun setFavorite(episodeId: Int, isFavorite: Boolean) =
        sharedPreferences.edit().putBoolean("episode-$episodeId-isfavorite", isFavorite).apply()

    fun isFavorites(episodeId: Int): Boolean =
        sharedPreferences.getBoolean("episode-$episodeId-isfavorite", false)
}