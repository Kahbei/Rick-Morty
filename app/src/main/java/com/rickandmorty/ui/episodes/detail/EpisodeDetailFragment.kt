package com.rickandmorty.ui.episodes.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.rickandmorty.R
import com.rickandmorty.data.PreferenceRepository
import com.rickandmorty.data.episodesPreference
import com.rickandmorty.ui.episodes.detail.model.EpisodeDetailItem
import com.rickandmorty.ui.episodes.detail.model.toEpisodeDate
import com.sackcentury.shinebuttonlib.ShineButton

class EpisodeDetailFragment: Fragment() {
    private val episodeDetailViewModel: EpisodeDetailViewModel by viewModels {
        EpisodeDetailViewModelFactory(episodesPreference(requireContext()))
    }
    private var episodeId = 1
    private var episodeNum = ""
    private var episodeName = ""

    private val args: EpisodeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_episode_detail, container, false)
        episodeId = args.episodeId
        episodeName = args.episodeName
        episodeNum = args.episodeNum

        loadDefaultValues(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        val loading = view.findViewById<ProgressBar>(R.id.loading)
        val episodeViews = view.findViewById<Group>(R.id.episode)

        episodeDetailViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EpisodeState.Succeed -> {
                    loading.visibility = View.INVISIBLE
                    episodeViews.visibility = View.VISIBLE
                    populate(state.episodeDetail, view)
                }
                is EpisodeState.Loading -> {
                    episodeViews.visibility = View.INVISIBLE
                    loading.visibility = View.VISIBLE
                }
                is EpisodeState.Failed -> {
                    episodeViews.visibility = View.INVISIBLE
                    loading.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        episodeDetailViewModel.load(episodeId)
    }

    private fun populate(episodeDetail: EpisodeDetailItem, view: View) {
        view.findViewById<TextView>(R.id.txtAirDate).text = episodeDetail.air_date?.toEpisodeDate()
        view.findViewById<ShineButton>(R.id.siFavorite).apply {
            setOnClickListener {
                episodeDetailViewModel.favorite(episodeDetail.id, !episodeDetail.isFavorite)
            }
            isChecked = episodeDetail.isFavorite
        }
    }

    private fun loadDefaultValues(view: View) {
        view.findViewById<TextView>(R.id.txtEpisodeNumber).text = episodeNum
        view.findViewById<TextView>(R.id.txtEpisodeName).text = episodeName
    }

}