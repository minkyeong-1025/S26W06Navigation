package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// 타입별 대표 색
private val typeColors = mapOf(
    "normal" to Color(0xFFA8A77A),
    "fire" to Color(0xFFEE8130),
    "water" to Color(0xFF6390F0),
    "electric" to Color(0xFFF7D02C),
    "grass" to Color(0xFF7AC74C),
    "ice" to Color(0xFF96D9D6),
    "fighting" to Color(0xFFC22E28),
    "poison" to Color(0xFFA33EA1),
    "ground" to Color(0xFFE2BF65),
    "flying" to Color(0xFFA98FF3),
    "psychic" to Color(0xFFF95587),
    "bug" to Color(0xFFA6B91A),
    "rock" to Color(0xFFB6A136),
    "ghost" to Color(0xFF735797),
    "dragon" to Color(0xFF6F35FC),
    "dark" to Color(0xFF705746),
    "steel" to Color(0xFFB7B7CE),
    "fairy" to Color(0xFFD685AD),
)

// 영어 타입 이름 → 한국어
private val typeNames = mapOf(
    "normal" to "노말", "fire" to "불꽃", "water" to "물", "electric" to "전기",
    "grass" to "풀", "ice" to "얼음", "fighting" to "격투", "poison" to "독",
    "ground" to "땅", "flying" to "비행", "psychic" to "에스퍼", "bug" to "벌레",
    "rock" to "바위", "ghost" to "고스트", "dragon" to "드래곤", "dark" to "악",
    "steel" to "강철", "fairy" to "페어리",
)

// 목록에 없는 타입이면 회색 / 영어 그대로
fun typeColor(type: String): Color = typeColors[type] ?: Color.Gray
fun typeName(type: String): String = typeNames[type] ?: type

@Composable
fun TypeChip(type: String) {
    val color = typeColor(type)
    Text(
        text = typeName(type),
        // 밝은 배경(전기, 얼음 등)은 검은 글자, 어두운 배경은 흰 글자
        color = if (color.luminance() > 0.5f) Color.Black else Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}