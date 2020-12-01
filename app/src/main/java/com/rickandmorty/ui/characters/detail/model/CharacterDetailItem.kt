package com.rickandmorty.ui.characters.detail.model

import com.rickandmorty.model.CharacterModel
import com.rickandmorty.model.EpisodeModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class CharacterDetailItem(
    val id: Int,
    val name: String,
    val species: String,
    val image: String,
    val status: Status,
    val type: String,
    val gender: Gender,
    val origin: String,
    val lastKnownLocation: String,
    val firstSeenEpisode: String?,
    val firstSeenEpisodeDate: Date?,
    val isFavorite: Boolean
) {
    enum class Status { Alive, unknown, Dead }
    enum class Gender { Male, Female, unknown, Genderless }
}

fun Date.toEpisodeDate(): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.US)
    return format.format(this)
}

fun CharacterModel.toItem(firstEpisode: EpisodeModel?, isFavorite: Boolean) =
    CharacterDetailItem(
        id = id,
        name = name,
        species = species,
        image = image,
        status = status.toStatusEnum(),
        type = type,
        gender = gender.toGenderEnum(),
        origin = origin.name,
        lastKnownLocation = location.name,
        firstSeenEpisode = firstEpisode?.name,
        firstSeenEpisodeDate = firstEpisode?.air_date?.toDate(),
        isFavorite = isFavorite
    )

private fun String.toStatusEnum(): CharacterDetailItem.Status = try {
    CharacterDetailItem.Status.valueOf(this)
} catch (e: Exception) {
    CharacterDetailItem.Status.unknown
}

private fun String.toGenderEnum(): CharacterDetailItem.Gender = try {
    CharacterDetailItem.Gender.valueOf(this)
} catch (e: Exception) {
    CharacterDetailItem.Gender.unknown
}

/**
 * Parse une date en string -> format date
 */

private fun String.toDate(): Date? {
    val format = SimpleDateFormat("MMM d, yyyy", Locale.US)
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        null
    }
}