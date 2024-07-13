package com.example.starwarpoints

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.SubcomposeAsyncImage
import com.example.starwarpoints.MainActivity.Companion.TAG
import com.example.starwarpoints.data.Players
import com.example.starwarpoints.data.PlayersItem
import com.example.starwarpoints.ui.theme.StarWarPointsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarPointsTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                var toolbarName by remember {
                    mutableStateOf(getString(R.string.star_wars_blaster_tournament))
                }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }, topBar = {
                        TopAppBar(
                            title = { Text(text = toolbarName) },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                        )
                    }) { innerPadding ->
                    NavHost(navController = navController, startDestination = PLAYERS_SCREEN) {
                        composable(PLAYERS_SCREEN) {
                            toolbarName = getString(R.string.star_wars_blaster_tournament)
                            PlayerList(modifier = Modifier.padding(innerPadding), mainViewModel, onCardClick = {
                                mainViewModel.selectedPlayersItem = it
                                navController.navigate(MATCH_SCREEN)
                            })
                        }
                        composable(
                            MATCH_SCREEN,
                        ) {
                            toolbarName = mainViewModel.selectedPlayersItem.name
                            MatchListScreen(
                                modifier = Modifier.padding(innerPadding),
                                mainViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val PLAYERS_SCREEN = "players_screen"
        const val MATCH_SCREEN = "match_screen/"
        val TAG: String = MainActivity::class.java.simpleName
    }
}


@Composable
fun MySnackBar(snackBarHostState: SnackbarHostState, message: String) {
    val scope = rememberCoroutineScope()
    LaunchedEffect("key1") {
        scope.launch {
            val snackBarResult = snackBarHostState
                .showSnackbar(
                    message = message,
                    actionLabel = "close",
                    duration = SnackbarDuration.Short
                )

            when (snackBarResult) {
                SnackbarResult.Dismissed -> {
                    Log.d(TAG, "Dismissed")
                }

                SnackbarResult.ActionPerformed -> {
                    Log.d(TAG, "ActionPerformed")
                }
            }
        }
    }
}


@Composable
fun PlayerList(modifier: Modifier, mainViewModel: MainViewModel,onCardClick: (PlayersItem) -> Unit) {
    val uiState by mainViewModel.playerScreenUiState.collectAsState(initial = UiState.Loading)
    when (val data = uiState) {
        is UiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            MySnackBar(snackBarHostState = SnackbarHostState(), message = data.message)
        }

        is UiState.Success -> {
            ShowPlayerList(data.data, modifier,onCardClick)
        }
    }

}

@Composable
fun ShowPlayerList(players: Players, modifier: Modifier,onCardClick: (PlayersItem) -> Unit) {
    print("players: ${players.size}")
    print("players: ${players}")
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.points_table),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(12.dp)
        )
        LazyColumn {
            items(players) { player ->
                PlayerCard(player,onCardClick)
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 14.dp))
            }
        }
    }
}

@Composable
fun PlayerCard(player: PlayersItem,onCardClick: (PlayersItem) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable {
                onCardClick(player)
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = player.icon.trim(),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(10)),
                alignment = Alignment.Center,
                contentScale = ContentScale.None,
                loading = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                },
                onError = {
                    Log.d("Error Loading player Icon", "$it")
                },
                onSuccess = {
                    Log.d(TAG, "PlayerCard success: $it")
                }
            )
            Text(text = player.name, fontWeight = FontWeight.W600, fontSize = 16.sp, modifier = Modifier.fillMaxSize(0.7f))
        }
        Text(
            text = player.totalMatchPlayed.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}


@Composable
fun MatchListScreen(modifier: Modifier, mainViewModel: MainViewModel) {


}