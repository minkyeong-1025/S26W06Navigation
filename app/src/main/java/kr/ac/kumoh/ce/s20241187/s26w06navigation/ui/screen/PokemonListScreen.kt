package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(pokemonList, key = { it.id }) { pokemon ->
            PokemonCard(pokemon) {
                onNavigateToDetail(pokemon.id)
            }
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = { onClick(pokemon.id)}
    ) {
        Row(

        ) {
            AsyncImage(
                model = pokemon.image,
                contentDescription = "${pokemon.name} 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    //.clip(CircleShape)
                    .clip(RoundedCornerShape(percent = 10))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = pokemon.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = pokemon.type,
                )
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}