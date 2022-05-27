package com.test.mycomposetodo.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.mycomposetodo.R
import com.test.mycomposetodo.model.getEmptyToDo
import com.test.mycomposetodo.parts.EditorTopBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("StateFlowValueCalledInComposition")
@ExperimentalCoroutinesApi
@Composable
fun EditorScreen(navController: NavController, editorViewModel: EditorViewModel) {
    val todo = editorViewModel.todo.collectAsState(getEmptyToDo())

    val todoTitle = remember { mutableStateOf("")}
    val todoDetail = remember { mutableStateOf("")}

    if (todo.value._id != -1) {
        todoTitle.value = todo.value.title
        todoDetail.value = todo.value.detail
    }

    val scaffoldState = rememberScaffoldState()

    val errorMessage = editorViewModel.errorMessage.collectAsState()
    val done = editorViewModel.done.collectAsState()

    if (errorMessage.value.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = errorMessage.value
            )
            editorViewModel.errorMessage.value = ""
        }
    }

    // done.valueの値を取得して、trueならばfalseを入れておく
    // 二度実行されないようにするため
    if(done.value) {
        editorViewModel.done.value = false
        navController.popBackStack()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { EditorTopBar(
            title = stringResource(id = R.string.edit_todo),
            navController = navController
        ){
            if (todo.value._id == -1) {
                editorViewModel.createToDo(todoTitle.value, todoDetail.value)
            } else {
                editorViewModel.updateToDo(todo.value, todoTitle.value, todoDetail.value)
            }
        }}
    ) {
        EditorToDoBody(todoTitle, todoDetail)
    }
}

@Composable
fun EditorToDoBody(
    todoTitle: MutableState<String>,
    todoDetail: MutableState<String>) {
 Column {
        TextField(
            value = todoTitle.value,
            onValueChange = { todoTitle.value = it },
            label = { Text(stringResource(id = R.string.todo_title)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        TextField(
            value = todoDetail.value,
            onValueChange = { todoDetail.value = it },
            label = { Text(stringResource(id = R.string.todo_detail)) },
            singleLine = false,
            modifier = Modifier
                .weight(1.0f, true)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}