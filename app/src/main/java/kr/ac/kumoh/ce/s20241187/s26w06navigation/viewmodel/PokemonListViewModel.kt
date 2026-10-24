package kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.PokemonRepository

class PokemonListViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // 추가: 검색어
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    // 추가: 전체 목록과 검색어를 합쳐서 화면에 보여 줄 목록을 만듦
    // 둘 중 하나라도 바뀌면 자동으로 다시 계산됨
    val filteredList: StateFlow<List<Pokemon>> =
        combine(_pokemonList, _query) { list, query ->
            list.filter { it.matches(query) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        loadPokemon()
    }

    // 추가: 검색창에 글자를 입력할 때마다 호출
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _pokemonList.value = repository.getAllPokemon()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorMessage.value = "포켓몬을 불러오지 못했어요.\n인터넷 연결을 확인해 주세요."
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// 검색 규칙: 숫자면 도감 번호, 글자면 한국어 이름 또는 영어 이름에 포함되는지
private fun Pokemon.matches(query: String): Boolean {
    val text = query.trim().removePrefix("#")       // "#025" → "025"
    if (text.isEmpty()) return true                 // 검색어 없으면 전부 보여 줌
    text.toIntOrNull()?.let { number -> return id == number }   // "025" → 25번
    return name.contains(text, ignoreCase = true) ||
            englishName.contains(text, ignoreCase = true)
}