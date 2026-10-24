package kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 레퍼런스 디자인 색 (어두운 남색 바탕 + 노란 포인트)
private val DexYellow = Color(0xFFFFCB05)
private val DexBackground = Color(0xFF0B1326)
private val DexSurface = Color(0xFF171F33)
private val DexSurfaceHigh = Color(0xFF222A3D)
private val DexOnSurface = Color(0xFFDAE2FD)
private val DexOnSurfaceVariant = Color(0xFF9AA4C0)

private val DexColorScheme = darkColorScheme(
    primary = DexYellow,
    onPrimary = Color(0xFF3D2F00),
    background = DexBackground,
    onBackground = DexOnSurface,
    surface = DexBackground,
    onSurface = DexOnSurface,
    surfaceContainer = DexSurface,          // 카드 배경
    surfaceVariant = DexSurfaceHigh,        // 검색창 배경
    onSurfaceVariant = DexOnSurfaceVariant, // 흐린 글자
    primaryContainer = DexSurfaceHigh,
    outline = DexOnSurfaceVariant,
    outlineVariant = Color(0xFF2D3449),     // 구분선
)

// 기기 설정(라이트/다크)과 상관없이 항상 도감 테마 사용
@Composable
fun S26W06NavigationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DexColorScheme,
        typography = Typography,
        content = content
    )
}