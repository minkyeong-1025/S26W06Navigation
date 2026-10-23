package kr.ac.kumoh.ce.s20241187.s26w06navigation.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.json.Json
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

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

    override suspend fun getAllPokemon(): List<Pokemon> = supervisorScope {
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

    private suspend fun fetchPokemon(id: Int): Pokemon {
        val dto = api.getPokemonDetail(id)

        // 타입이 여러 개일 때 정렬 후, name 리스트 생성
        val typeList = dto.types
            .sortedBy { it.slot }
            .map { it.type.name }
        val types = typeList
            .joinToString(", ")
            .ifEmpty { "unknown" }

        return Pokemon(
            id = dto.id,
            name = dto.name,
            type = types,
            info = "",
            image = dto.sprites.frontDefault ?: ""
        )
    }
}