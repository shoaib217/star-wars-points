package com.example.starwarpoints

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarpoints.data.APIService
import com.example.starwarpoints.data.MatchListItem
import com.example.starwarpoints.data.Player1
import com.example.starwarpoints.data.Players
import com.example.starwarpoints.data.PlayersItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var players: Players? = null
    var matchList: ArrayList<MatchListItem>? = null
    private var _playerScreenUiState = MutableStateFlow<UiState<List<PlayersItem>>>(UiState.Loading)
    val playerScreenUiState: StateFlow<UiState<List<PlayersItem>>> = _playerScreenUiState
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
            matchList?.forEach { match->
                match.player1.playerName = playersData.firstOrNull {player-> player.id == match.player1.id }?.name ?: ""
                match.player2.playerName = playersData.firstOrNull {player-> player.id == match.player2.id }?.name ?: ""
            }
            playersData.sortByDescending { it.totalMatchPlayed }
            _playerScreenUiState.update {
                UiState.Success(playersData)
            }
        }
        setDummyData()

        Log.d("TAG", "getData players: $players")
        Log.d("TAG", "matchList : $matchList")


    }

    private fun setDummyData() {
        val players = Players()
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Boba-Fett-icon.png",
                1,
                "Boba Fett",
                matchList?.filter { 1 == it.player1.id || 1 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/C3PO-icon.png",
                2,
                "C3PO",
                matchList?.filter { 2 == it.player1.id || 2 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Chewbacca-icon.png",
                3,
                "Chewbacca",
                matchList?.filter { 3 == it.player1.id || 3 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Darth-Vader-icon.png",
                4,
                "Darth Vader",
                matchList?.filter { 4 == it.player1.id || 4 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Emperor-icon.png",
                5,
                "Emperor",
                (matchList?.filter { 5 == it.player1.id || 5 == it.player2.id}?.size ?: 0) + 5
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Han-Solo-icon.png",
                6,
                "Han Solo",
                matchList?.filter { 6 == it.player1.id || 6 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Leia-icon.png",
                7,
                "Princess Leia",
                matchList?.filter { 7 == it.player1.id || 7 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Luke-Skywalker-icon.png",
                8,
                "Luke Skywalker",
                matchList?.filter { 8 == it.player1.id || 8 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Obi-Wan-icon.png",
                9,
                "Obi Wan Kenobi",
                matchList?.filter { 9 == it.player1.id || 9 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/R2D2-icon.png",
                10,
                "R2D2",
                matchList?.filter { 10 == it.player1.id || 10 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Stormtrooper-icon.png",
                11,
                "Stormtrooper",
                matchList?.filter { 11 == it.player1.id || 11 == it.player2.id}?.size ?: 0
            )
        )
        players.add(
            PlayersItem(
                "http://icons.iconarchive.com/icons/creativeflip/starwars-longshadow-flat/128/Yoda-icon.png",
                12,
                "Yoda",
                matchList?.filter { 12 == it.player1.id || 12 == it.player2.id}?.size ?: 0
            )
        )

        matchList?.forEach { match->
            match.player1.playerName = players.firstOrNull {player-> player.id == match.player1.id }?.name ?: ""
            match.player2.playerName = players.firstOrNull {player-> player.id == match.player2.id }?.name ?: ""
        }

        players.sortByDescending { it.totalMatchPlayed }
        _playerScreenUiState.update {
            UiState.Success(players)
        }
    }

    fun getMatchPlayedList(): List<MatchListItem>? {
        return matchList?.filter { selectedPlayersItem.id == it.player1.id ||  selectedPlayersItem.id == it.player2.id}
    }

    fun getMatchCardColor(match: MatchListItem): Color {
        return if (selectedPlayersItem.id == match.player1.id){
            getColor(match.player1,match.player2)
        } else {
            getColor(match.player2,match.player1)
        }
    }

    private fun getColor(player: Player1, opponentPlayer: Player1): Color {
        return when {
            player.score > opponentPlayer.score -> {
                Color.Green
            }
            player.score < opponentPlayer.score -> {
                Color.Red
            }
            else -> {
                Color.White
            }
        }
    }
}

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()

    class Error(val message: String) : UiState<Nothing>()

    class Success <T>(val data: T) : UiState<T>()
}