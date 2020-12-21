package com.rickandmorty.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(
    private val context: Context,
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "prefs",
        Context.MODE_PRIVATE
    )
) {

    // TODO Utiliser les sharedPreferences pour sauvegarder et récupérer le favori
    fun setCharacterFavorite(characterId: Int, isFavorite: Boolean) =
        sharedPreferences.edit().putBoolean("character-$characterId-isfavorite", isFavorite).apply()

    fun isCharacterFavorites(characterId: Int): Boolean =
        sharedPreferences.getBoolean("character-$characterId-isfavorite", false)

    fun setEpisodeFavorite(episodeId: Int, isFavorite: Boolean) =
        sharedPreferences.edit().putBoolean("episode-$episodeId-isfavorite", isFavorite).apply()

    fun isEpisodeFavorites(episodeId: Int): Boolean =
        sharedPreferences.getBoolean("episode-$episodeId-isfavorite", false)

}