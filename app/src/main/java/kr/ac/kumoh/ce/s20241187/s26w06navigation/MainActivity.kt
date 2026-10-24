package kr.ac.kumoh.ce.s20241187.s26w06navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
//import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kr.ac.kumoh.ce.s20241187.s26w06navigation.navigation.PokemonRoute
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.ApiPokemonRepository
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.MockPokemonRepository
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.PokemonRepository
import kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen.PokemonDetailScreen
import kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.screen.PokemonListScreen
import kr.ac.kumoh.ce.s20241187.s26w06navigation.ui.theme.S26W06NavigationTheme
//import kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel.PokemonDetailViewModel
//import kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel.PokemonListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 배경이 어두워서 상태 표시줄 아이콘(시계, 배터리)을 흰색으로
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        //val repository: PokemonRepository = MockPokemonRepository()
        val repository = ApiPokemonRepository()

        setContent {
            S26W06NavigationTheme {
                MainScreen(repository)
            }
        }
    }
}

@Composable
fun MainScreen(
    repository: PokemonRepository
) {
    val backStack = rememberNavBackStack(PokemonRoute.PokemonList)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,

            entryDecorators = listOf(
                // Add the default decorators for managing scenes and saving state
                rememberSaveableStateHolderNavEntryDecorator(),
                // Then add the view model store decorator
                // 이걸 안쓰면 처음 한 번만 viewmodel 생성
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<PokemonRoute.PokemonList> {
                    PokemonListScreen(
                        repository = repository,
                        onNavigateToDetail = { id ->
                            backStack.add(PokemonRoute.PokemonDetail(pokemonId = id))
                        }
                    )
                }
                entry<PokemonRoute.PokemonDetail> { key ->
                    PokemonDetailScreen(
                        pokemonId = key.pokemonId,
                        repository = repository,
                    )
                }
            }
        )
    }
}
