package com.rickandmorty.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.rickandmorty.model.EpisodeModel
import com.rickandmorty.network.RickAndMortyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EpisodesViewModel : ViewModel() {
    private val rickAndMortyService = RickAndMortyService.build()
    private val _stateEpisode: MutableLiveData<EpisodesState> = MutableLiveData(EpisodesState.Loading)
    val stateEpisode: LiveData<EpisodesState> = _stateEpisode

    private var currentPage = 1

    private var maxPages = 1

    init {
        load()
    }

    private fun load() = viewModelScope.launch(Dispatchers.Main) {
        _stateEpisode.value = EpisodesState.Loading
        _stateEpisode.value = when(val result = rickAndMortyService.episodes(currentPage)) {
            is NetworkResponse.Success -> EpisodesState.Succeed(episodes = result.body.results)
                .also { maxPages = result.body.info.pages }
            else -> EpisodesState.Failed(error = "Echec au chargement des données")
        }
    }

    //Ici Vincent, là ! là !
    fun loadMore() = viewModelScope.launch(Dispatchers.Main) {
        val page = currentPage + 1
        val currentState = _stateEpisode.value
        if (maxPages+1 > page) {
            if (currentState is EpisodesState.Succeed) {
                _stateEpisode.value = EpisodesState.LoadingMore(currentState.episodes)
                _stateEpisode.value = when (val result = rickAndMortyService.episodes(page)) {
                    is NetworkResponse.Success -> EpisodesState.Succeed(episodes = currentState.episodes + result.body.results)
                        .also { currentPage = page }
                    else -> EpisodesState.Failed(error = "Echec au chargement des données.")
                }
            }
        }
    }
}

sealed class EpisodesState {
    object Loading : EpisodesState()
    data class LoadingMore(val episodes: List<EpisodeModel>) : EpisodesState()
    data class Succeed(val episodes: List<EpisodeModel>) : EpisodesState()
    data class Failed(val error: String) : EpisodesState()
}