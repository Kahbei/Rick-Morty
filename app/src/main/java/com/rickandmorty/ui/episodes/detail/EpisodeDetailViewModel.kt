package com.rickandmorty.ui.episodes.detail

import androidx.lifecycle.*
import com.haroldadmin.cnradapter.NetworkResponse
import com.rickandmorty.data.PreferenceRepository
import com.rickandmorty.network.RickAndMortyService
import com.rickandmorty.ui.episodes.detail.model.EpisodeDetailItem
import com.rickandmorty.ui.episodes.detail.model.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EpisodeDetailViewModel (
    private val preferenceRepository: PreferenceRepository
) : ViewModel(){
    private val rickAndMortyService = RickAndMortyService.build()

    private val _state: MutableLiveData<EpisodeState> = MutableLiveData(EpisodeState.Loading)
    val state: LiveData<EpisodeState> = _state

    fun load(id: Int) = viewModelScope.launch(Dispatchers.Main) {
        _state.value = EpisodeState.Loading
        _state.value = when (val result = rickAndMortyService.episode(id)) {
            is NetworkResponse.Success -> {
                EpisodeState.Succeed(
                    episodeDetail = result.body.toItem()
                )
            }
            else -> EpisodeState.Failed(error = "Echec au chargement des données.")
        }
    }
}

sealed class EpisodeState {
    object Loading : EpisodeState()
    data class Succeed(val episodeDetail: EpisodeDetailItem) : EpisodeState()
    data class Failed(val error: String) : EpisodeState()
}

class EpisodeDetailViewModelFactory(
    private val preferenceRepository: PreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EpisodeDetailViewModel(preferenceRepository) as T
    }
}