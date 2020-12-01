package com.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rickandmorty.R

class CharactersFragment : Fragment() {

    private val charactersViewModel: CharactersViewModel by viewModels()

    private lateinit var characterAdapter: CharacterRecyclerViewAdapter
    private lateinit var loading: ProgressBar
    private lateinit var loadingMore: ProgressBar
    private lateinit var rvCharacters: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        charactersViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CharactersState.Succeed -> {
                    characterAdapter.updateCharacters(state.characters)
                    loadingMore.visibility = View.GONE
                    loading.visibility = View.GONE
                }
                is CharactersState.Loading -> {
                    loadingMore.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                }
                is CharactersState.Failed -> {
                    loadingMore.visibility = View.GONE
                    loading.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                }
                is CharactersState.LoadingMore -> {
                    loadingMore.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCharacters = view.findViewById(R.id.rvCharacters)
        loading = view.findViewById(R.id.loading)
        loadingMore = view.findViewById(R.id.loadingMore)

        characterAdapter = CharacterRecyclerViewAdapter(emptyList()) { character, views ->
            val extras = FragmentNavigatorExtras(
                views.first to views.first.transitionName,
                views.second to views.second.transitionName
            )
            // TODO Ouvrir le détail du personnage (attention, il faut passer l'objet extras en paramètre)
            findNavController().navigate(CharactersFragmentDirections.actionNavigationEpisodesToNavigationChatacter(character.id, character.name, character.image), extras)
        }

        rvCharacters.adapter = characterAdapter
        rvCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // TODO Charger la page suivante des personnages
                    charactersViewModel.loadMore()
                }
            }
        })
    }
}