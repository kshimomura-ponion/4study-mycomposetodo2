package com.test.mycomposetodo.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.test.mycomposetodo.screen.*

@Composable
fun MyToDoNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MyToDoScreens.MainScreen.name
    ) {
        composable(MyToDoScreens.MainScreen.name) {
            val mainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(MyToDoScreens.DetailScreen.name + "/{todoId}",
            arguments = listOf(navArgument(name = "todoId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val detailViewModel = hiltViewModel<DetailViewModel>()
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
            // ViewModelに渡す
            detailViewModel.setId(todoId)
            DetailScreen(
                navController = navController,
                detailViewModel = detailViewModel
            )
        }
        composable(MyToDoScreens.EditorScreen.name) {
            val editorViewModel = hiltViewModel<EditorViewModel>()
            EditorScreen(
                navController = navController,
                editorViewModel = editorViewModel
            )
        }
        composable(MyToDoScreens.EditorScreen.name + "/{todoId}",
            arguments = listOf(navArgument(name = "todoId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val editorViewModel = hiltViewModel<EditorViewModel>()
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
            // ViewModelに渡す
            editorViewModel.setId(todoId)
            EditorScreen(
                navController = navController,
                editorViewModel = editorViewModel
            )
        }
    }
}