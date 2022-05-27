package com.test.mycomposetodo.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.mycomposetodo.model.ToDo
import com.test.mycomposetodo.repository.todo.ToDoRepository
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@HiltViewModel
class EditorViewModel @Inject constructor(
    private val todoRepository: ToDoRepository
) : ViewModel(){
    val errorMessage = MutableStateFlow("")
    val done = MutableStateFlow(false)

    // Editの時
    private val todoId = MutableStateFlow(-1)
    val todo: Flow<ToDo> = todoId.flatMapLatest { todoId -> todoRepository.getById(todoId) }

    fun setId(todoId: Int) {
        /* MutableStateFlowは値が変化したときのみデータを流すという性質があるので、
           再コンポーズで同じ値が何度もセットされても、後続の処理には1度しかIDが流れません。
        */
        this.todoId.value = todoId
    }


    fun createToDo(todoTitle: String, todoDetail: String) {
        if (todoTitle.trim().isEmpty()) {
            errorMessage.value = "Input title"
            return
        }
        viewModelScope.launch {
            runCatching {
                todoRepository.create(todoTitle, todoDetail)
            }.onSuccess {
                done.value = true
            }.onFailure {
                errorMessage.value = it.message ?: ""
            }
        }
    }

    fun updateToDo(todo: ToDo, todoTitle: String, todoDetail: String) {
        if (todoTitle.trim().isEmpty()) {
            errorMessage.value = "Input title"
            return
        }
        viewModelScope.launch {
            runCatching {
                todoRepository.update(todo, todoTitle, todoDetail)
            }.onSuccess {
                done.value = true
            }.onFailure {
                errorMessage.value = it.message ?: ""
            }
        }
    }
}