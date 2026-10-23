package kr.ac.kumoh.ce.s20241187.s26w06navigation.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val KOREAN = "ko"

class ApiPokemonRepository(
    private val api: PokeApi = pokeService
) : PokemonRepository {

    companion object {
        private val jsonConfig = Json {
            // 서버가 내가 안 만든 데이터(Key)를 보내더라도
            // 에러 내지 말고 그냥 무시
            ignoreUnknownKeys = true
        }

        private val pokeService: PokeApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(
                    jsonConfig.asConverterFactory("application/json".toMediaType())
                )
                .build()
                .create(PokeApi::class.java)
        }
    }

    override suspend fun getAllPokemon(): List<Pokemon> = coroutineScope {
        api.getPokemonList().results
            .mapNotNull { result ->
                // url: "https://pokeapi.co/api/v2/pokemon/1/"에서
                // id인 1만 분리
                val id = result.url
                    .trimEnd('/')
                    .substringAfterLast('/')
                    .toIntOrNull()
                    ?: return@mapNotNull null
                async {
                    runCatching { fetchPokemon(id) }.getOrNull()
                }
            }
            .awaitAll()
            .filterNotNull()
    }

    override suspend fun getPokemonById(id: Int): Pokemon? =
        runCatching { fetchPokemon(id) }.getOrNull()

    private suspend fun fetchPokemon(id: Int): Pokemon = coroutineScope {
        // 기본 형태의 포켓몬은 pokemon id와 species id가 같으므로
        // 상세 정보와 종 정보를 동시에 요청할 수 있다
        val detail = async { api.getPokemonDetail(id) }
        val species = async { runCatching { api.getPokemonSpecies(id) }.getOrNull() }

        val dto = detail.await()
        // 종 정보를 못 받아도 나머지 정보는 보여줄 수 있게 null을 허용한다
        val speciesDto = species.await()

        // 타입이 여러 개일 때 정렬 후, name 리스트 생성
        val typeList = dto.types
            .sortedBy { it.slot }
            .map { it.type.name }
        val types = typeList
            .joinToString(", ")
            .ifEmpty { "unknown" }

        Pokemon(
            id = dto.id,
            name = speciesDto?.koreanName ?: dto.name,
            type = types,
            info = speciesDto?.koreanInfo.orEmpty(),
            image = dto.sprites.frontDefault ?: ""
        )
    }
}

// 한국어 이름은 모든 종에 대해 하나씩 존재한다
private val PokemonSpeciesDto.koreanName: String?
    get() = names.firstOrNull { it.language.name == KOREAN }?.name

// 게임 버전마다 설명이 여러 개인데, 가장 먼저 나오는(구버전) 설명을 사용한다
private val PokemonSpeciesDto.koreanInfo: String?
    get() = flavorTextEntries
        .firstOrNull { it.language.name == KOREAN }
        ?.flavorText
        // 설명에 줄바꿈(\n)과 페이지 구분자(\f)가 섞여 있어 공백으로 바꾼다
        ?.replace(Regex("\\s+"), " ")
        ?.trim()