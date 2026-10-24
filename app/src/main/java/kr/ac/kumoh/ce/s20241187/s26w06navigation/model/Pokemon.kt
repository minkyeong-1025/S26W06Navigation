package kr.ac.kumoh.ce.s20241187.s26w06navigation.model

data class Pokemon(
    val id: Int,
    val name: String,
    val type: String,
    val info: String,
    val image: String,
    val artwork: String = "",            // 추가: 큰 공식 일러스트 주소
    val height: Int = 0,                 // 추가: 키 (단위 10cm)
    val weight: Int = 0,                 // 추가: 몸무게 (단위 100g)
    val stats: List<Stat> = emptyList(), // 추가: 능력치 목록
)

// 추가: 능력치 하나 (예: "hp", 45)
data class Stat(
    val name: String,
    val value: Int,
)