package kr.ac.kumoh.ce.s20241187.s26w06navigation.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100
    ): PokemonListDto

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailDto

    // 한국어 이름과 설명은 종 (species) 정보에 있음
    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesDto
}

@Serializable
data class PokemonListDto(
    @SerialName("results") val results: List<ResultDto>
) {
    @Serializable
    data class ResultDto(
        @SerialName("url") val url: String
    )
}

@Serializable
data class PokemonDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("height") val height: Int = 0,          // 추가: 단위 10cm
    @SerialName("weight") val weight: Int = 0,          // 추가: 단위 100g
    @SerialName("types") val types: List<TypeSlotDto>,
    @SerialName("stats") val stats: List<StatSlotDto> = emptyList(),   // 추가
    @SerialName("sprites") val sprites: SpritesDto
) {
    @Serializable
    data class TypeSlotDto(
        @SerialName("slot") val slot: Int,
        @SerialName("type") val type: TypeDto
    )

    @Serializable
    data class TypeDto(
        @SerialName("name") val name: String
    )

    // 추가: 능력치 하나 { "base_stat": 45, "stat": { "name": "hp" } }
    @Serializable
    data class StatSlotDto(
        @SerialName("base_stat") val baseStat: Int,
        @SerialName("stat") val stat: StatDto
    )

    @Serializable
    data class StatDto(
        @SerialName("name") val name: String
    )

    @Serializable
    data class SpritesDto(
        @SerialName("front_default") val frontDefault: String? = null,
        @SerialName("other") val other: OtherDto? = null          // 추가
    )

    // 추가: 큰 공식 일러스트 { "official-artwork": { "front_default": "..." } }
    @Serializable
    data class OtherDto(
        @SerialName("official-artwork") val officialArtwork: ArtworkDto? = null
    )

    @Serializable
    data class ArtworkDto(
        @SerialName("front_default") val frontDefault: String? = null
    )
}

@Serializable
data class LanguageDto(
    @SerialName("name") val name: String
)

@Serializable
data class PokemonSpeciesDto(
    @SerialName("names") val names: List<NameDto> = emptyList(),
    @SerialName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntryDto> = emptyList()
) {
    @Serializable
    data class NameDto(
        @SerialName("name") val name: String,
        @SerialName("language") val language: LanguageDto
    )

    @Serializable
    data class FlavorTextEntryDto(
        @SerialName("flavor_text") val flavorText: String,
        @SerialName("language") val language: LanguageDto
    )
}