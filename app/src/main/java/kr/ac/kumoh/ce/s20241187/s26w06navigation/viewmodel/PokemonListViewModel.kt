package kr.ac.kumoh.ce.s20241187.s26w06navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.ce.s20241187.s26w06navigation.model.Pokemon
import kr.ac.kumoh.ce.s20241187.s26w06navigation.repository.PokemonRepository
import kotlin.coroutines.cancellation.CancellationException

class PokemonListViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    // 추가: 불러오는 중인지
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 추가: 에러 메시지 (에러가 없으면 null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadPokemon()
    }

    // 추가: 처음 실행할 때, 그리고 "다시 시도" 버튼을 누를 때 호출
    fun loadPokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _pokemonList.value = repository.getAllPokemon()
            } catch (e: CancellationException) {
                throw e    // 화면을 나가서 coroutine이 취소된 건 에러가 아니므로 그대로 넘김
            } catch (e: Exception) {
                _errorMessage.value = "포켓몬을 불러오지 못했어요.\n인터넷 연결을 확인해 주세요."
            } finally {
                _isLoading.value = false    // 성공하든 실패하든 로딩은 끝
            }
        }
    }
}