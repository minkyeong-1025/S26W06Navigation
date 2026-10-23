package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.PokemonRepository
import kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel.PokemonListViewModel

@Composable
fun PokemonListScreen(
    repository: PokemonRepository,
    viewModel: PokemonListViewModel = viewModel {
        PokemonListViewModel(repository)
    },
    onNavigateToDetail: (Int) -> Unit,
) {
    val pokemonList by viewModel.pokemonList.collectAsStateWithLifecycle()

    LazyColumn {
        items(pokemonList) { pokemon ->
            Button(
                onClick = {
                    onNavigateToDetail(pokemon.id)
                }
            ) {
                Text(pokemon.name)
            }
        }
    }
}