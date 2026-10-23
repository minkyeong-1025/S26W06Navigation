package kr.ac.kumoh.ce.s20241187.s26w06navigation.repository

import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon
class MockPokemonRepository : PokemonRepository {

    companion object {
        private val pokemons = listOf(
            Pokemon(
                id = 1,
                name = "이상해씨",
                type = "풀/독",
                info = "태어날 때부터 등에 이상한 씨앗이 붙어 있으며, 그 씨앗으로부터 영양을 공급받아 성장한다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
            ),
            Pokemon(
                id = 4,
                name = "파이리",
                type = "불꽃",
                info = "꼬리의 불꽃은 생명의 등불이다. 즐거울 때는 불꽃이 흔들리고 화가 났을 때는 타오른다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
            ),
            Pokemon(
                id = 7,
                name = "꼬부기",
                type = "물",
                info = "등딱지로 몸을 보호하고 입에서 물을 뿜어내어 상대를 공격한다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png"
            ),
            Pokemon(
                id = 25,
                name = "피카츄",
                type = "전기",
                info = "볼의 양쪽에 작은 전기 주머니가 있다. 위급할 때 이 주머니에서 전기를 방출한다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"
            ),
            Pokemon(
                id = 39,
                name = "니드런♀",
                type = "독",
                info = "뿔이 작고 온순한 성격이지만, 독이 있으므로 주의해야 한다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/29.png" // 원본 ID 29 이미지 매칭
            ),
            Pokemon(
                id = 52,
                name = "니드런♂",
                type = "독",
                info = "귀를 쫑긋 세우고 주변 경계를 게을리하지 않는다. 뿔에 강력한 독이 있다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/32.png" // 원본 ID 32 이미지 매칭
            ),
            Pokemon(
                id = 133,
                name = "이브이",
                type = "노말",
                info = "불규칙한 유전자 지도를 가지고 있어 다양한 형태로 진화할 수 있는 잠재력이 있다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/133.png"
            ),
            Pokemon(
                id = 150,
                name = "뮤츠",
                type = "에스퍼",
                info = "유전자 조작을 통해 만들어진 포켓몬. 전투를 위해 생겨났기에 난폭한 성격이다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/150.png"
            ),
            Pokemon(
                id = 151,
                name = "뮤",
                type = "에스퍼",
                info = "모든 포켓몬의 유전자를 담고 있어, 모든 기술을 쓸 수 있다고 전해지는 환상의 포켓몬.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/151.png"
            ),
            Pokemon(
                id = 152,
                name = "치코리타",
                type = "풀",
                info = "머리의 잎사귀로 주변의 냄새를 맡거나 기온을 감지한다. 햇빛을 쬐는 것을 좋아한다.",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/152.png"
            )
        )
    }

    override suspend fun getAllPokemon(): List<Pokemon> = pokemons

    override suspend fun getPokemonById(id: Int): Pokemon?
            = pokemons.find { it.id == id }
}