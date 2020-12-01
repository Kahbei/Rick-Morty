package com.rickandmorty.ui.characters.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.rickandmorty.R
import com.rickandmorty.data.PreferenceRepository
import com.rickandmorty.ui.characters.CharactersFragment
import com.rickandmorty.ui.characters.CharactersFragmentDirections
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem.Gender.*
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem.Status.Alive
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem.Status.Dead
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem.Status.unknown
import com.rickandmorty.ui.characters.detail.model.toEpisodeDate
import com.rickandmorty.ui.utils.RoundCornersTransform
import com.sackcentury.shinebuttonlib.ShineButton
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.rickandmorty.ui.characters.detail.model.CharacterDetailItem.Gender.unknown as unknownGender

class CharacterDetailFragment : Fragment() {

    private val characterDetailViewModel: CharacterDetailViewModel by viewModels {
        CharacterDetailViewModelFactory(PreferenceRepository(requireContext()))
    }
    private var characterId = 1
    private var characterImage = ""
    private var characterName = ""

    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character_detail, container, false)
        // TODO Récupérer les informations depuis la liste des personnages
        characterId = args.characterId
        characterName = args.characterName
        characterImage = args.characterUrlImg

        loadDefaultValues(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        val loading = view.findViewById<ProgressBar>(R.id.loading)
        val characterViews = view.findViewById<Group>(R.id.character)

        characterDetailViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CharacterState.Succeed -> {
                    loading.visibility = View.INVISIBLE
                    characterViews.visibility = View.VISIBLE
                    populate(state.characterDetail, view)
                }
                is CharacterState.Loading -> {
                    characterViews.visibility = View.INVISIBLE
                    loading.visibility = View.VISIBLE
                }
                is CharacterState.Failed -> {
                    characterViews.visibility = View.INVISIBLE
                    loading.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        characterDetailViewModel.load(characterId)
    }

    private fun populate(characterDetail: CharacterDetailItem, view: View) {
        view.findViewById<TextView>(R.id.txtStatusSpecies).apply {
            text = getString(
                R.string.character_status_species,
                characterDetail.status.toString(),
                characterDetail.species
            )
            val status = when (characterDetail.status) {
                Alive -> R.color.statusAlive to R.drawable.bg_alive
                Dead -> R.color.statusDead to R.drawable.bg_dead
                unknown -> R.color.statusUnknown to R.drawable.bg_unknown
            }
            setTextColor(ContextCompat.getColor(requireContext(), status.first))
            setBackgroundResource(status.second)
        }
        val iconGender = when (characterDetail.gender) {
            Female -> R.drawable.ic_female
            Male -> R.drawable.ic_male
            unknownGender -> R.drawable.ic_unkown_gender
            Genderless -> R.drawable.ic_genderless
        }
        view.findViewById<ImageView>(R.id.imgGender).setImageResource(iconGender)
        view.findViewById<TextView>(R.id.txtOrigin).text =
            getString(R.string.character_origine, characterDetail.origin)

        view.findViewById<TextView>(R.id.txtLocation).text = characterDetail.lastKnownLocation
        view.findViewById<TextView>(R.id.txtFirstSeen).text = getString(
            R.string.character_first_seen,
            characterDetail.firstSeenEpisode,
            characterDetail.firstSeenEpisodeDate?.toEpisodeDate()
        )

        view.findViewById<ShineButton>(R.id.siFavorite).apply {
            setOnClickListener {
                characterDetailViewModel.favorite(characterDetail.id, !characterDetail.isFavorite)
            }
            isChecked = characterDetail.isFavorite
        }
    }

    private fun loadDefaultValues(view: View) {
        Picasso.get().load(characterImage)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .transform(RoundCornersTransform(32.0f))
            .into(view.findViewById<ImageView>(R.id.imgAvatar))
        view.findViewById<TextView>(R.id.txtName).text = characterName
    }
}
