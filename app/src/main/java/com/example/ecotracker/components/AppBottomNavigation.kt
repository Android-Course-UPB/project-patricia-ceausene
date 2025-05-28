package com.example.ecotracker.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ecotracker.R
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Secondary

@Composable
fun AppBottomNavigation(
    navController: NavController,
) {
    val items = listOf(
       AppBottomNavigationItem(
           title = "Home",
           selectedIcon = painterResource(id = R.drawable.baseline_home_24),
           unselectedIcon = painterResource(id = R.drawable.outline_home_24),
           screen = "home"
       ),
        AppBottomNavigationItem(
            title = "Actions",
            selectedIcon = painterResource(id = R.drawable.baseline_compost_24),
            unselectedIcon = painterResource(id = R.drawable.outline_compost_24),
            screen = "actions"
        ),
        AppBottomNavigationItem(
            title = "Progress",
            selectedIcon = painterResource(id = R.drawable.baseline_trending_up_24),
            unselectedIcon = painterResource(id = R.drawable.outline_trending_up_24),
            screen = "progress"
        ),
        AppBottomNavigationItem(
            title = "Leaderboard",
            selectedIcon = painterResource(id = R.drawable.baseline_leaderboard_filled),
            unselectedIcon = painterResource(id = R.drawable.outline_leaderboard),
            screen ="leaderboard"
        ),
        AppBottomNavigationItem(
            title = "Profile",
            selectedIcon = painterResource(id = R.drawable.baseline_person_24),
            unselectedIcon = painterResource(id = R.drawable.profile),
            screen = "profile"
        ),
    )

    // Observe the current back stack entry to get the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine the selected index based on the current route
    val selectedItemIndex = items.indexOfFirst { it.screen == currentRoute }.coerceAtLeast(0)

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    if (selectedItemIndex != index) {  // Only navigate if not already selected
                        navController.navigate(item.screen) {
                            // Prevent stacking of navigation graph
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        maxLines = 1,
                        fontSize = 9.sp
                    )
                },
                alwaysShowLabel = true,
                icon = {
                        Icon(
                            painter = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    indicatorColor =  Secondary
                )
            )
        }
    }
}


data class AppBottomNavigationItem(
    val title: String,
    val unselectedIcon: Painter,
    val selectedIcon: Painter,
    val screen: String
)