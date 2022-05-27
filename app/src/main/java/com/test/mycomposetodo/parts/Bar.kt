package com.test.mycomposetodo.parts

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.mycomposetodo.R

@Composable
fun TopBar(title: String, navController:NavController) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        navigationIcon = {
            if(title != stringResource(id = R.string.app_name)) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
        },
        title = { Text (text = title) }
    )
}

// save: () -> Unitは、ラムダ式（関数による処理それ自体）を引数として受け取る（戻り値はなし）ということで、
// 引数の中の関数を親で指定して実行するという意味になる。
@Composable
fun EditorTopBar(title: String, navController:NavController, save: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 5.dp,
        navigationIcon = {
            IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
        },
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = save) {
                Icon(Icons.Filled.Done, "Save")
            }
        }
    )
}

// save: () -> Unitは、ラムダ式（関数による処理それ自体）を引数として受け取る（戻り値はなし）ということで、
// 引数の中の関数を親で指定して実行するという意味になる。
@Composable
fun DetailTopBar(title: String, navController:NavController, save: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 5.dp,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }
        },
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = save) {
                Icon(Icons.Filled.Done, "Save")
            }
        }
    )
}