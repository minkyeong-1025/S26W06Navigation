package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Stat
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())   // 내용이 길면 스크롤
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 큰 공식 일러스트 (없으면 작은 이미지)
        AsyncImage(
            model = pokemon.artwork.ifEmpty { pokemon.image },
            contentDescription = "${pokemon.name} 이미지",
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(percent = 10))
                .background(typeColor(pokemon.type.split(", ").first()).copy(alpha = 0.3f))
        )

        Spacer(Modifier.height(16.dp))

        // 도감 번호 + 이름
        Text(
            text = "No. %03d".format(pokemon.id),     // 4 → No. 004
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = pokemon.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        // 타입 칩: "grass, poison" → ["grass", "poison"]
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.type.split(", ").forEach { type ->
                TypeChip(type)
            }
        }

        Spacer(Modifier.height(16.dp))

        // 키 / 몸무게
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            InfoItem(label = "키", value = "${pokemon.height / 10.0} m")
            InfoItem(label = "몸무게", value = "${pokemon.weight / 10.0} kg")
        }

        Spacer(Modifier.height(16.dp))

        // 설명
        Text(
            text = pokemon.info,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

        Spacer(Modifier.height(24.dp))

        // 능력치
        Text(
            text = "능력치",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        pokemon.stats.forEach { stat ->
            StatBar(stat)
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = MaterialTheme.colorScheme.outline)
    }
}

// PokeAPI의 영어 능력치 이름 → 한국어
private val statNames = mapOf(
    "hp" to "HP",
    "attack" to "공격",
    "defense" to "방어",
    "special-attack" to "특수공격",
    "special-defense" to "특수방어",
    "speed" to "스피드",
)

@Composable
fun StatBar(stat: Stat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = statNames[stat.name] ?: stat.name,
            modifier = Modifier.width(72.dp),
        )
        Text(
            text = stat.value.toString(),
            modifier = Modifier.width(40.dp),
            fontWeight = FontWeight.Bold,
        )
        LinearProgressIndicator(
            progress = { stat.value / 255f },   // 능력치 최댓값 255 기준 비율
            modifier = Modifier
                .weight(1f)                     // 남은 가로 공간 전부
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
        )
    }
}