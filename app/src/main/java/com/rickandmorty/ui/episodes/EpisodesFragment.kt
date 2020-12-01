package com.rickandmorty.ui.episodes

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

class EpisodesFragment : Fragment() {

    private val episodesViewModel: EpisodesViewModel by viewModels()
    private lateinit var episodeAdapter: EpisodesRecyclerViewAdapter
    private lateinit var loading: ProgressBar
    private lateinit var loadingMore: ProgressBar
    private lateinit var rvEpisodes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        episodesViewModel.stateEpisode.observe(viewLifecycleOwner){ state ->
            when (state) {
                is EpisodesState.Succeed -> {
                    episodeAdapter.updateEpisode(state.episodes)
                    loadingMore.visibility = View.GONE
                    loading.visibility = View.GONE
                }
                is EpisodesState.Loading -> {
                    loadingMore.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                }
                is EpisodesState.Failed -> {
                    loadingMore.visibility = View.GONE
                    loading.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                }
                is EpisodesState.LoadingMore -> {
                    loadingMore.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvEpisodes = view.findViewById(R.id.rvEpisodes)
        loading = view.findViewById(R.id.loading)
        loadingMore = view.findViewById(R.id.loadingMore)

        episodeAdapter = EpisodesRecyclerViewAdapter(emptyList()) { episode, views ->
            val extras = FragmentNavigatorExtras(
                views.first to views.first.transitionName,
                views.second to views.second.transitionName
            )
            findNavController().navigate(EpisodesFragmentDirections.actionNavigationEpisodesToNavigationEpisode(episode.id, episode.name, episode.episode), extras)
        }

        rvEpisodes.adapter = episodeAdapter
        rvEpisodes.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    episodesViewModel.loadMore()
                }
            }
        })
    }
}