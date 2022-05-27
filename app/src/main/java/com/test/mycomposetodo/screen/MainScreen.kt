package com.test.mycomposetodo.screen

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.mycomposetodo.R
import com.test.mycomposetodo.model.ToDo
import com.test.mycomposetodo.model.getEmptyToDo
import com.test.mycomposetodo.navigation.MyToDoScreens
import com.test.mycomposetodo.parts.TopBar

@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel) {
    // ViewModelに追加したFlowをJetpack Composeの状態として扱うためにcollectAsStateを使用する
    val todoList = mainViewModel.todoList.collectAsState(emptyList())
    Scaffold(
        topBar = {
            TopBar(stringResource(id = R.string.app_name), navController) },
            floatingActionButton = { CreateToDo(navController) }
        ) {
        // ここからToDoのリスト表示
        ToDoList(todoList, mainViewModel){ todo ->
            // 画面遷移を子要素から受け取って親要素で指定する
            navController.navigate(route = MyToDoScreens.DetailScreen.name + "/" + (todo._id).toString())

        }
    }
}

@Composable
fun CreateToDo(navController: NavController) {
    // 作成画面に遷移
    FloatingActionButton(onClick = {
        navController.navigate(route = MyToDoScreens.EditorScreen.name)
    }) {
        Icon(Icons.Filled.Add, "Add ToDo")
    }
}

@Composable
fun ToDoList(list: State<List<ToDo>>, mainViewModel: MainViewModel, itemSelected: (todo: ToDo) -> Unit) {
    LazyColumn {
        items(list.value) { todo ->
            ToDoListItem(todo, mainViewModel, itemSelected)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ToDoListItem(todo: ToDo, mainViewModel: MainViewModel, itemSelected:(todo: ToDo) -> Unit) {
    // Finished切り替え用のダイアログを表示
    val showDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val selectedToDo: MutableState<ToDo> = remember { mutableStateOf(getEmptyToDo()) }

    val updated = mainViewModel.updated.collectAsState()
    val updatedToDo = mainViewModel.updatedToDo.collectAsState()
    // そのままでは完了状態が変化したことが反映されないため
    if (updated.value) {
        mainViewModel.updated.value = false
        selectedToDo.value = updatedToDo.value
    } else {
        selectedToDo.value = todo
    }

    // .padding()の後に.clickable()を記述すると、タップ可能な領域がパディングの内側のみになる
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    // 通常タップの時
                    onTap = { itemSelected(todo) },
                    // 長押しのとき
                    onLongPress = {
                        showDialog.value = true
                        selectedToDo.value = todo
                    }
                )
            }
    ) {
        Row(
                modifier = Modifier.fillMaxWidth()
            ) {
            if (todo.finished) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_star_24),
                    contentDescription = "finished"
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_star_border_24),
                    contentDescription = "not_finished"
                )
            }
                Text(
                    todo.title,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Text(
                DateFormat.format("yyyy-MM-dd hh:mm:ss", todo.created).toString(),
                style = MaterialTheme.typography.body2
            )
    }
    if(showDialog.value) {
        ToDoChangeDialog(mainViewModel, selectedToDo, showDialog)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ToDoChangeDialog(
    mainViewModel: MainViewModel,
    todo: MutableState<ToDo>,
    showDialog: MutableState<Boolean>,
    //finishChanged:(todo:ToDo) -> Unit
){


    // 完了/未完了の切り替え
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text(stringResource(id = R.string.confirm_finish_or_delete)) },
        confirmButton = {
            TextButton( onClick = {
                showDialog.value = false
                mainViewModel.finishedChange(todo.value)

            }) { Text(text = if(todo.value.finished) stringResource(id = R.string.off_finish_status) else stringResource(
                id = R.string.on_finish_status)
            ) } },
        dismissButton = {
            TextButton( onClick = {
                showDialog.value = false
                mainViewModel.delete(todo.value)
            }) { Text(text = stringResource(id = R.string.delete_button)) }}
    )
}
