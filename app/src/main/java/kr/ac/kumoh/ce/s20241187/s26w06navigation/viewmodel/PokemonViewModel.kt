package kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon

private val pokemons = listOf(
    Pokemon(1, "이상해씨", "풀/독", "태어날 때부터 등에 식물의 씨앗이 있어 조금씩 자란다."),
    Pokemon(4, "파이리", "불꽃", "태어날 때부터 꼬리에 불이 붙어 있다. 꺼지면 죽는다."),
    Pokemon(7, "꼬부기", "물", "등껍질이 단단하여 공격을 잘 막아내고 물을 뿜어낸다."),
    Pokemon(25, "피카츄", "전기", "뺨에 전기 주머니가 있어서 위기에 처하면 전기를 방출한다.")
)

class PokemonListViewModel : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(pokemons)
    val pokemonList = _pokemonList.asStateFlow()
}

class PokemonDetailViewModel(
    private val pokemonId: Int
) : ViewModel() {
    private val _pokemon = MutableStateFlow(
        pokemons.find { it.id == pokemonId }
            ?: Pokemon(-1, "알수 없음", "없음", "Unknown")
    )
    val pokemon = _pokemon.asStateFlow()
}