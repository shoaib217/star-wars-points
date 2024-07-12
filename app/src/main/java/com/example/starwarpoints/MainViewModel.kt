package com.example.starwarpoints

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarpoints.data.APIService
import com.example.starwarpoints.data.MatchListItem
import com.example.starwarpoints.data.Players
import com.example.starwarpoints.data.PlayersItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var players: Players? = null
    var matchList: ArrayList<MatchListItem>? = null
    private var _playerScreenUiState = MutableStateFlow<UiState<Players>>(UiState.Loading)
    val playerScreenUiState: StateFlow<UiState<Players>> = _playerScreenUiState
    init {
        viewModelScope.launch {
            getData()
        }
    }

    private suspend fun getData() {
        val apiService = APIService.getInstance()
        players = apiService.getPlayers()
        matchList = apiService.getMatchList()
        players?.let { playersData ->
            _playerScreenUiState.update {
                UiState.Success(playersData)
            }
        }

    }
}

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()

    class Error(val message: String) : UiState<Nothing>()

    class Success <T>(val data: T) : UiState<T>()
}