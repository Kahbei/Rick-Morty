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
    fun setFavorite(characterId: Int, isFavorite: Boolean) =
        sharedPreferences.edit().putBoolean("character-$characterId-isfavorite", isFavorite).apply()

    fun isFavorites(characterId: Int): Boolean =
        sharedPreferences.getBoolean("character-$characterId-isfavorite", false)
}