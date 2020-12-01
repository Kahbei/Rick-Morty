package com.rickandmorty.model

data class CharacterModel(
    val id: Int,
    val name: String,
    val species: String, // [Human, Alien, Humanoid, unknown, Poopybutthole, Mythological Creature, Animal, Robot, Cronenberg, Disease, Planet]
    val image: String,
    val status: String, // [Alive, unknown, Dead]
    val type: String,
    val gender: String, // [Male, Female, unknown, Genderless]
    val origin: Origin,
    val location: Location,
    val episode: List<String>
) {
    data class Origin(val name: String, val url: String)
    data class Location(val name: String, val url: String)

    fun firstEpisode() = episode.mapNotNull { it.substringAfterLast("/").toIntOrNull() }.minOrNull()
}