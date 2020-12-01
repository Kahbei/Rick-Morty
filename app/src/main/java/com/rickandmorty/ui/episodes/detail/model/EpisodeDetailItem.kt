package com.rickandmorty.ui.episodes.detail.model

import com.rickandmorty.model.EpisodeModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class EpisodeDetailItem(
    val id: Int,
    val episode_num: String,
    val episode_name: String,
    val air_date: Date?
)

fun Date.toEpisodeDate(): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.US)
    return format.format(this)
}

fun EpisodeModel.toItem() =
    EpisodeDetailItem(
        id = id,
        episode_num = episode,
        episode_name = name,
        air_date = air_date?.toDate()
    )

private fun String.toDate(): Date? {
    val format = SimpleDateFormat("MMM d, yyyy", Locale.US)
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        null
    }
}