package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

fun typeColor(type: String): Color = typeColors[type] ?: Color.Gray
fun typeName(type: String): String = typeNames[type] ?: type

// 변경: 꽉 찬 원색 → 연한 배경 + 밝은 색 글자 + 얇은 테두리
@Composable
fun TypeChip(type: String) {
    val color = typeColor(type)
    val shape = RoundedCornerShape(50)
    Text(
        text = typeName(type),
        // 어두운 배경에서도 잘 보이게 타입 색을 흰색 쪽으로 45% 섞음
        color = lerp(color, Color.White, 0.45f),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(shape)
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color.copy(alpha = 0.45f), shape)
            .padding(horizontal = 10.dp, vertical = 3.dp),
    )
}