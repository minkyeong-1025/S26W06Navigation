package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
    val filteredList by viewModel.filteredList.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    // by로 받은 값은 smart cast가 안 돼서 지역 변수에 한 번 담음
    val error = errorMessage

    Column(modifier = Modifier.fillMaxSize()) {
        DexHeader(count = pokemonList.size)

        when {
            isLoading -> LoadingView()
            error != null -> ErrorView(
                message = error,
                onRetry = { viewModel.loadPokemon() }
            )
            else -> {
                DexSearchBar(
                    query = query,
                    onQueryChange = { viewModel.onQueryChange(it) }   // 이벤트는 위로
                )

                if (filteredList.isEmpty() && query.isNotBlank()) {
                    EmptyView(query)
                } else {
                    LazyVerticalGrid(
                        // 화면 너비에 맞춰 열 개수 자동 결정 (카드 최소 폭 240dp)
                        columns = GridCells.Adaptive(minSize = 240.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredList, key = { it.id }) { pokemon ->
                            PokemonCard(pokemon) {
                                onNavigateToDetail(pokemon.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 상단 제목: 몬스터볼 아이콘 + Pokédex
@Composable
fun DexHeader(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PokeballIcon(iconSize = 36.dp)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = "Pokédex",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = if (count > 0) "PokéAPI · ${count}마리" else "PokéAPI",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// Canvas로 직접 그린 몬스터볼 아이콘
@Composable
fun PokeballIcon(iconSize: Dp) {
    Canvas(modifier = Modifier.size(iconSize)) {
        val radius = size.minDimension / 2
        val dark = Color(0xFF0B1326)
        // 위쪽 빨간 반원 (180도에서 시작해 180도만큼)
        drawArc(color = Color(0xFFE53935), startAngle = 180f, sweepAngle = 180f, useCenter = true)
        // 아래쪽 흰 반원
        drawArc(color = Color.White, startAngle = 0f, sweepAngle = 180f, useCenter = true)
        // 가운데 가로줄
        drawLine(
            color = dark,
            start = Offset(0f, center.y),
            end = Offset(size.width, center.y),
            strokeWidth = radius * 0.16f
        )
        // 가운데 버튼
        drawCircle(color = dark, radius = radius * 0.36f)
        drawCircle(color = Color.White, radius = radius * 0.2f)
    }
}

// 둥근 검색창
@Composable
fun DexSearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        placeholder = { Text("포켓몬 이름 또는 도감 번호 (#001)") },
        leadingIcon = {
            // Material Icons는 dependency가 필요해서 강의처럼 시스템 리소스 사용
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = "검색"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                TextButton(onClick = { onQueryChange("") }) {
                    Text("지우기")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,     // 밑줄 없애기
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@Composable
fun EmptyView(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "'$query'에 해당하는 포켓몬이 없어요",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text("포켓몬을 불러오는 중...")
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("다시 시도")
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: (Int) -> Unit
) {
    val types = pokemon.type.split(", ")
    val color = typeColor(types.first())
    val total = pokemon.stats.sumOf { it.value }   // 종족값 합계 (6개 능력치의 합)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),   // 그림자 대신 얇은 테두리
        onClick = { onClick(pokemon.id) }
    ) {
        // 위: 이미지 영역 (타입 색 → 투명 그라데이션)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clipToBounds()                       // 몬스터볼 무늬가 밖으로 안 나가게
                .background(
                    Brush.verticalGradient(
                        listOf(color.copy(alpha = 0.35f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            PokeballWatermark(color = color, modifier = Modifier.matchParentSize())

            Text(
                text = "#%03d".format(pokemon.id),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = lerp(color, Color.White, 0.4f),
            )

            AsyncImage(
                model = pokemon.artwork.ifEmpty { pokemon.image },
                contentDescription = "${pokemon.name} 이미지",
                modifier = Modifier.size(130.dp),
            )
        }

        // 아래: 정보 영역
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = pokemon.name,
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
                Text(
                    text = pokemon.englishName,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }

            Text(
                text = "${pokemon.height / 10.0}m / ${pokemon.weight / 10.0}kg",
                fontSize = 12.sp,
                color = lerp(color, Color.White, 0.3f),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                types.forEach { type -> TypeChip(type) }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row {
                Text(
                    text = "종족값 합계",
                    modifier = Modifier.weight(1f),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "$total",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            LinearProgressIndicator(
                progress = { total / 720f },          // 가장 높은 종족값(약 720) 기준 비율
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = color,
                trackColor = MaterialTheme.colorScheme.background,
                gapSize = 0.dp,                       // 막대와 배경 사이 틈 없애기
                drawStopIndicator = {},               // 끝의 점 없애기
            )
        }
    }
}

// 카드 오른쪽 아래의 은은한 몬스터볼 무늬
@Composable
fun PokeballWatermark(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.height * 0.42f
        val center = Offset(size.width - radius * 0.55f, size.height - radius * 0.55f)
        val lineColor = color.copy(alpha = 0.12f)
        val strokeWidth = radius * 0.18f

        drawCircle(lineColor, radius = radius, center = center, style = Stroke(strokeWidth))
        drawCircle(lineColor, radius = radius * 0.3f, center = center, style = Stroke(strokeWidth))
        drawLine(
            lineColor,
            start = Offset(center.x - radius, center.y),
            end = Offset(center.x + radius, center.y),
            strokeWidth = strokeWidth
        )
    }
}