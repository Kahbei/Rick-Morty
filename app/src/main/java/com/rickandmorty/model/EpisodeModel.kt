package com.rickandmorty.model

data class EpisodeModel(
    val id: Int,
    val name: String,
    val air_date: String, // Date when it was on air for the 1st time
    val episode: String // Will the num of the episode. Ex.: S01E03
)