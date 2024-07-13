package com.example.starwarpoints

import android.util.Log
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
    lateinit var selectedPlayersItem: PlayersItem
    init {
        viewModelScope.launch {
            getData()
        }
    }

    private suspend fun getData() {
        val apiService = APIService.getInstance()
        val playerResponse = apiService.getPlayers()
        val matchListResponse = apiService.getMatchList()
        if (playerResponse.isSuccessful){
            playerResponse.body()?.let {
                players = it
            }
        } else {
            _playerScreenUiState.update {
                UiState.Error(playerResponse.message())
            }
        }

        if (matchListResponse.isSuccessful) {
            matchListResponse.body()?.let {
                matchList = it
            }
        }

        players?.let { playersData ->
            playersData.forEach { player->
                player.totalMatchPlayed = matchList?.filter { player.id == it.player1.id || player.id == it.player2.id}?.size ?: 0
            }
            _playerScreenUiState.update {
                UiState.Success(playersData)
            }
        }
        val players = Players()
        players.add(PlayersItem("http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Boba-Fett-icon.png",1,"abc",21))
        players.add(PlayersItem("http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Boba-Fett-icon.png",2,"abc1",22))
        players.add(PlayersItem("http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Boba-Fett-icon.png",3,"abc2",23))
        _playerScreenUiState.update {
            UiState.Success(players)
        }
        Log.d("TAG", "getData players: $players")
        Log.d("TAG", "matchList : $matchList")


    }
}

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()

    class Error(val message: String) : UiState<Nothing>()

    class Success <T>(val data: T) : UiState<T>()
}