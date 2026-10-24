package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.Alignment
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
    // 첫 번째 타입을 대표 타입으로 사용 ("grass, poison" → "grass")
    val mainType = pokemon.type.split(", ").first()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        // 추가: 카드 배경을 대표 타입 색으로 연하게
        colors = CardDefaults.cardColors(
            containerColor = typeColor(mainType).copy(alpha = 0.15f)
        ),
        onClick = { onClick(pokemon.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.image,
                contentDescription = "${pokemon.name} 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(percent = 10))
                    // 변경: 이미지 배경도 타입 색으로 (조금 더 진하게)
                    .background(typeColor(mainType).copy(alpha = 0.3f))
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = pokemon.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Spacer(Modifier.height(16.dp))
                // 변경: 글자 대신 타입 칩
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pokemon.type.split(", ").forEach { type ->
                        TypeChip(type)
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}