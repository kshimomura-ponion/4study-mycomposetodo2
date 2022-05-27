package com.test.mycomposetodo.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.mycomposetodo.R
import com.test.mycomposetodo.model.ToDo
import com.test.mycomposetodo.model.getEmptyToDo
import com.test.mycomposetodo.navigation.MyToDoScreens
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DetailScreen(navController: NavController, detailViewModel: DetailViewModel) {
    // 初期値が必要なので、読み込み中を表すIDをもつToDoを仮としてセットしておきます。
    val todo = remember { mutableStateOf(getEmptyToDo())}

    val scaffoldState = rememberScaffoldState()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isShowFinishDialog = remember { mutableStateOf(false) }

    val errorMessage = detailViewModel.errorMessage.collectAsState()
    val deleted = detailViewModel.deleted.collectAsState()
    val updated = detailViewModel.updated.collectAsState()
    val updatedToDo = detailViewModel.updatedToDo.collectAsState()

    if (errorMessage.value.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = errorMessage.value
            )
            detailViewModel.errorMessage.value = ""
        }
    }

    if (deleted.value) {
        // 再コンポーズ時にもう一度実行されたら困る
        detailViewModel.deleted.value = false
        navController.popBackStack()
    }

    if (updated.value) {
        detailViewModel.updated.value = false
        todo.value = updatedToDo.value
    } else {
        todo.value = detailViewModel.todo.collectAsState(getEmptyToDo()).value
    }

    Scaffold(
        topBar = {
            DetailTopBar(
                navController = navController,
                todo = todo.value,
                toEdit = { navController.navigate(route = MyToDoScreens.EditorScreen.name + "/" + todo.value._id.toString())},
                showDeleteDialog = { isShowDeleteDialog.value = true },
                showFinishDialog = { isShowFinishDialog.value = true },
            )
        }
    ) {
        DetailBody(
            todo = todo.value,
            isShowDeleteDialog = isShowDeleteDialog,
            isShowFinishDialog = isShowFinishDialog,
            performDelete = { detailViewModel.delete(todo.value) },
            performFinished = { detailViewModel.finishedChange(todo.value) }
        )
    }
}

@Composable
fun DetailTopBar(
    navController: NavController,
    todo: ToDo,
    toEdit: () -> Unit,
    showDeleteDialog: () -> Unit,
    showFinishDialog: () -> Unit,
){
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }},
        title = {
            if (todo._id == -1) {
                Text(stringResource(id = R.string.loading))
            } else {
                Text(todo.title)
            }
        },
        actions = {
            if (todo._id != -1) {
                IconButton(onClick = toEdit) {
                    Icon(Icons.Filled.Edit, "Edit")
                }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Filled.MoreVert, "Menu")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        showDeleteDialog()
                    }) {
                        Text(stringResource(id = R.string.delete_button))
                    }
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        showFinishDialog()
                    }) {
                    Text(text = if(todo.finished) stringResource(id = R.string.off_finish_status) else stringResource(
                        id = R.string.on_finish_status))
                    }
                }
            }
        },
        backgroundColor = Color.Transparent
    )
}

@Composable
fun DetailBody(
    todo: ToDo,
    isShowDeleteDialog: MutableState<Boolean>,
    isShowFinishDialog: MutableState<Boolean>,
    performDelete: () -> Unit,
    performFinished: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()
        ) {
            if (todo.finished) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_star_24),
                    contentDescription = "finished"
                )
                Text(text = stringResource(id = R.string.finish_status), modifier = Modifier.padding(start = 10.dp))
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_star_border_24),
                    contentDescription = "not_finished"
                )
                Text(text = stringResource(id = R.string.not_finish_status), modifier = Modifier.padding(start = 10.dp))
            }
        }
        Text(
            todo.title,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Text(
            todo.detail,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1.0f, true)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
    }
    if (isShowDeleteDialog.value) {
        AlertDialog(onDismissRequest = {
            isShowDeleteDialog.value = false
        }, title = {
            Text(stringResource(id = R.string.confirm_delete))
        }, confirmButton = {
            TextButton(onClick = {
                isShowDeleteDialog.value = false
                performDelete()
            }) {
                Text(stringResource(id = android.R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = { isShowDeleteDialog.value = false }) {
                Text(stringResource(id = android.R.string.cancel))
            }
        }
        )
    }
    if (isShowFinishDialog.value) {
        AlertDialog(onDismissRequest = {
            isShowFinishDialog.value = false
        }, title = {
            Text(text = if(todo.finished) stringResource(id = R.string.off_finish_status) else stringResource(
                id = R.string.on_finish_status))
        }, confirmButton = {
            TextButton(onClick = {
                isShowFinishDialog.value = false
                performFinished()
            }) {
                Text(stringResource(id = android.R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = { isShowFinishDialog.value = false }) {
                Text(stringResource(id = android.R.string.cancel))
            }
        }
        )
    }
}

