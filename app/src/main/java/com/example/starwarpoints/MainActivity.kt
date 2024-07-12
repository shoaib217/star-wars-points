package com.example.starwarpoints

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.starwarpoints.MainActivity.Companion.TAG
import com.example.starwarpoints.data.Players
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
                    },topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(R.string.star_wars_blaster_tournament)) },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                        )
                    }) { innerPadding ->
                    NavHost(navController = navController, startDestination = PLAYERS_SCREEN) {
                        composable(PLAYERS_SCREEN) {
                            toolbarName = getString(R.string.star_wars_blaster_tournament)
                            PlayerList(modifier = Modifier.padding(innerPadding),mainViewModel)
                        }
                        composable(
                            "$MATCH_SCREEN{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) {
                            MatchListScreen(modifier = Modifier.padding(innerPadding), it.arguments?.getInt("id"),mainViewModel)
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
fun PlayerList(modifier: Modifier, mainViewModel: MainViewModel) {
    val uiState by mainViewModel.playerScreenUiState.collectAsState(initial = UiState.Loading)
    when (val data =uiState) {
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
            ShowPlayerList(data.data)
        }
    }

}

@Composable
fun ShowPlayerList(players: Players) {
    print("players: ${players.size}")
    print("players: ${players}")
}


@Composable
fun MatchListScreen(modifier: Modifier, int: Int?, mainViewModel: MainViewModel) {

}