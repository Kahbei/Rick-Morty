package com.rickandmorty.ui.characters.detail

import androidx.lifecycle.*
import com.haroldadmin.cnradapter.NetworkResponse
import com.rickandmorty.data.PreferenceRepository
import com.rickandmorty.model.EpisodeModel
import com.rickandmorty.network.RickAndMortyService
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem
import com.rickandmorty.ui.characters.detail.model.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val rickAndMortyService = RickAndMortyService.build()

    private val _state: MutableLiveData<CharacterState> = MutableLiveData(CharacterState.Loading)
    val state: LiveData<CharacterState> = _state

    fun load(id: Int) = viewModelScope.launch(Dispatchers.Main) {
        _state.value = CharacterState.Loading
        _state.value = when (val result = rickAndMortyService.character(id)) {
            is NetworkResponse.Success -> {
                val episode = result.body.firstEpisode()?.let { loadEpisode(it) }
                CharacterState.Succeed(
                    characterDetail = result.body.toItem(
                        episode,
                        preferenceRepository.isCharacterFavorites(id)
                    )
                )
            }
            else -> CharacterState.Failed(error = "Echec au chargement des données.")
        }
    }

    fun favorite(id: Int, isFavorite: Boolean) {
        _state.value = when (val state = _state.value) {
            is CharacterState.Succeed -> {
                preferenceRepository.setCharacterFavorite(id, isFavorite).let {
                    CharacterState.Succeed(state.characterDetail.copy(isFavorite = isFavorite))
                }
            }
            else -> state
        }
    }

    /**
     * Va appeler un épisode et fait un test réseaux.
     */
    private suspend fun loadEpisode(id: Int) : EpisodeModel? {
        return when (val result = rickAndMortyService.episode(id)){
            is NetworkResponse.Success -> result.body
            else -> null
        }
    }
}

sealed class CharacterState {
    object Loading : CharacterState()
    data class Succeed(val characterDetail: CharacterDetailItem) : CharacterState()
    data class Failed(val error: String) : CharacterState()
}

class CharacterDetailViewModelFactory(
    private val preferenceRepository: PreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CharacterDetailViewModel(preferenceRepository) as T
    }
}