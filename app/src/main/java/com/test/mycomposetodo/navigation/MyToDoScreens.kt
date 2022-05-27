package com.test.mycomposetodo.navigation

enum class MyToDoScreens {
    MainScreen,
    DetailScreen,
    EditorScreen;
    companion object {
        fun fromRoute(route: String?): MyToDoScreens
        = when (route?.substringBefore("/")) {
            MainScreen.name -> MainScreen
            DetailScreen.name -> DetailScreen
            EditorScreen.name -> EditorScreen
            null -> MainScreen
            else -> throw IllegalArgumentException("Route $route is not recognize")
        }
    }
}