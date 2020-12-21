package com.rickandmorty.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.rickandmorty.model.CharacterModel
import com.rickandmorty.network.RickAndMortyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    private val rickAndMortyService = RickAndMortyService.build()

    private var currentPage = 1
    private val _state: MutableLiveData<CharactersState> = MutableLiveData(CharactersState.Loading)
    val state: LiveData<CharactersState> = _state
    private var maxPages = 1

    init {
        load()
    }

    //function to load the characters in the list from the first page of the web API
    private fun load() = viewModelScope.launch(Dispatchers.Main) {
        _state.value = CharactersState.Loading
        _state.value = when(val result = rickAndMortyService.characters(currentPage)) {

            is NetworkResponse.Success -> CharactersState.Succeed(characters = result.body.results)

                .also { maxPages = result.body.info.pages }
            else -> CharactersState.Failed(error = "Echec au chargement des données")
        }
    }

    //function to keep loading the characters in the list page by page from the web API
    //if it reaches the final page it will stop loading more characters
    fun loadMore() = viewModelScope.launch(Dispatchers.Main) {
        val page = currentPage + 1
        val currentState = _state.value
        if (maxPages+1 > page) {
            if (currentState is CharactersState.Succeed) {
                _state.value = CharactersState.LoadingMore(currentState.characters)
                _state.value = when (val result = rickAndMortyService.characters(page)) {
                    is NetworkResponse.Success -> CharactersState.Succeed(characters = currentState.characters + result.body.results)
                        .also { currentPage = page }
                    else -> CharactersState.Failed(error = "Echec au chargement des données.")
                }
            }
        }
    }
}

//use to know if it needs to load more characters, if ihe loading succeeded or if it failed
sealed class CharactersState {
    object Loading : CharactersState()
    data class LoadingMore(val characters: List<CharacterModel>) : CharactersState()
    data class Succeed(val characters: List<CharacterModel>) : CharactersState()
    data class Failed(val error: String) : CharactersState()
}
