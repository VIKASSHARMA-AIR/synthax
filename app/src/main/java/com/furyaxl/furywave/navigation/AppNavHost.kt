package com.furyaxl.furywave.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.furyaxl.furywave.data.repository.PlaylistRepository
import com.furyaxl.furywave.feature.home.HomeScreen
import com.furyaxl.furywave.feature.home.HomeViewModel
import com.furyaxl.furywave.feature.library.LibraryScreen
import com.furyaxl.furywave.feature.library.LibraryViewModel
import com.furyaxl.furywave.feature.library.PlaylistDetailScreen
import com.furyaxl.furywave.feature.player.MiniPlayer
import com.furyaxl.furywave.feature.player.NowPlayingScreen
import com.furyaxl.furywave.feature.search.SearchScreen
import com.furyaxl.furywave.feature.search.SearchViewModel
import com.furyaxl.furywave.feature.settings.SettingsScreen
import com.furyaxl.furywave.feature.settings.SettingsViewModel
import com.furyaxl.furywave.player.PlayerController
import com.furyaxl.furywave.ui.theme.DarkBackground
import com.furyaxl.furywave.ui.theme.DarkSurface
import com.furyaxl.furywave.ui.theme.ElectricCyan
import com.furyaxl.furywave.ui.theme.TextMuted
import com.furyaxl.furywave.ui.theme.TextSecondary

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Route.Home.path, Icons.Default.Home),
    BottomNavItem("Search", Route.Search.path, Icons.Default.Search),
    BottomNavItem("Library", Route.Library.path, Icons.Default.LibraryMusic),
    BottomNavItem("Settings", Route.Settings.path, Icons.Default.Settings)
)

@Composable
fun AppNavHost(
    playerController: PlayerController,
    playlistRepository: PlaylistRepository,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isNowPlayingRoute = currentRoute == Route.NowPlaying.path

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            if (!isNowPlayingRoute) {
                Column {
                    MiniPlayer(
                        controller = playerController,
                        onExpand = { navController.navigate(Route.NowPlaying.path) }
                    )
                    NavigationBar(
                        containerColor = DarkSurface
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                },
                                label = { Text(item.title) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ElectricCyan,
                                    selectedTextColor = ElectricCyan,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = DarkSurface
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.Home.path,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Route.Home.path) {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    HomeScreen(
                        viewModel = homeViewModel,
                        onNavigateToNowPlaying = { navController.navigate(Route.NowPlaying.path) }
                    )
                }

                composable(Route.Search.path) {
                    val searchViewModel: SearchViewModel = hiltViewModel()
                    SearchScreen(
                        viewModel = searchViewModel,
                        onNavigateToNowPlaying = { navController.navigate(Route.NowPlaying.path) }
                    )
                }

                composable(Route.Library.path) {
                    val libraryViewModel: LibraryViewModel = hiltViewModel()
                    LibraryScreen(
                        viewModel = libraryViewModel,
                        onNavigateToPlaylist = { playlistId ->
                            navController.navigate(Route.PlaylistDetail.createRoute(playlistId))
                        },
                        onNavigateToNowPlaying = { navController.navigate(Route.NowPlaying.path) }
                    )
                }

                composable(
                    route = Route.PlaylistDetail.ROUTE_DEF,
                    arguments = listOf(navArgument("id") { type = NavType.LongType })
                ) { backStackEntry ->
                    val playlistId = backStackEntry.arguments?.getLong("id") ?: 0L
                    PlaylistDetailScreen(
                        playlistId = playlistId,
                        playlistRepository = playlistRepository,
                        playerController = playerController,
                        onBack = { navController.popBackStack() },
                        onNavigateToNowPlaying = { navController.navigate(Route.NowPlaying.path) }
                    )
                }

                composable(Route.Settings.path) {
                    val settingsViewModel: SettingsViewModel = hiltViewModel()
                    SettingsScreen(viewModel = settingsViewModel)
                }

                composable(Route.NowPlaying.path) {
                    NowPlayingScreen(
                        controller = playerController,
                        onCollapse = { navController.popBackStack() },
                        onNavigateToEqualizer = {
                            navController.navigate(Route.Settings.path)
                        }
                    )
                }
            }
        }
    }
}

