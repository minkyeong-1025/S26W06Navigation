package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.PokemonRepository
import kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    repository: PokemonRepository,
    viewModel: PokemonDetailViewModel = viewModel {
        PokemonDetailViewModel(
            repository = repository,
            pokemonId = pokemonId
        )
    },
) {
    val pokemon by viewModel.pokemon.collectAsStateWithLifecycle()

    Text(pokemon.name)
}