package kr.ac.kumoh.ce.s20241187.s26w06navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.theme.S26W06NavigationTheme

sealed interface PokemonRoute : NavKey {
    @Serializable
    data object PokemonList : PokemonRoute

    @Serializable
    data class PokemonDetail(val pokemonId: Int) : PokemonRoute
}

data class Pokemon(val id: Int, val name: String, val type: String, val info: String)

val pokemons = listOf(
    Pokemon(1, "이상해씨", "풀/독", "태어날 때부터 등에 식물의 씨앗이 있어 조금씩 자란다."),
    Pokemon(4, "파이리", "불꽃", "태어날 때부터 꼬리에 불이 붙어 있다. 꺼지면 죽는다."),
    Pokemon(7, "꼬부기", "물", "등껍질이 단단하여 공격을 잘 막아내고 물을 뿜어낸다."),
    Pokemon(25, "피카츄", "전기", "뺨에 전기 주머니가 있어서 위기에 처하면 전기를 방출한다.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S26W06NavigationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(PokemonRoute.PokemonList)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,

            entryDecorators = listOf(
                // Add the default decorators for managing scenes and saving state
                rememberSaveableStateHolderNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<PokemonRoute.PokemonList> {
                    PokemonListScreen(
                        pokemonList = pokemons,
                        onNavigateToDetail = { id ->
                            backStack.add(PokemonRoute.PokemonDetail(pokemonId = id))
                        }
                    )
                }
                entry<PokemonRoute.PokemonDetail> { key ->
                    PokemonDetailScreen(key.pokemonId)
                }
            }
        )
    }
}

@Composable
fun PokemonListScreen(
    pokemonList: List<Pokemon>,
    onNavigateToDetail: (Int) -> Unit,
) {
    LazyColumn {
        items(pokemons) { pokemon ->
            Button(
                onClick = {
                    onNavigateToDetail(pokemon.id)
                }
            ) {
                Text(pokemon.name)
            }
        }
    }
}

@Composable
fun PokemonDetailScreen(
    pokemonId: Int
) {
    pokemons.find {it.id == pokemonId}?.let {
        Text(it.info)
    }
}