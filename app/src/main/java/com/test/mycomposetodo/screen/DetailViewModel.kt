package com.test.mycomposetodo.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.mycomposetodo.model.ToDo
import com.test.mycomposetodo.model.getEmptyToDo
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
class DetailViewModel @Inject constructor(
    private val todoRepository: ToDoRepository
) : ViewModel(){
    private val todoId = MutableStateFlow(-1)
    val errorMessage = MutableStateFlow("")
    val deleted = MutableStateFlow(false)
    val updated = MutableStateFlow(false)
    val updatedToDo = MutableStateFlow(getEmptyToDo())

    val todo: Flow<ToDo> = todoId.flatMapLatest { todoId -> todoRepository.getById(todoId) }

    fun setId(todoId: Int) {
        /* MutableStateFlowは値が変化したときのみデータを流すという性質があるので、
            再コンポーズで同じ値が何度もセットされても、後続の処理には1度しかIDが流れません。
        */
        this.todoId.value = todoId
    }

    fun finishedChange(todo: ToDo) {
        viewModelScope.launch {
            try {
                todoRepository.update(todo, todo.title, todo.detail, !(todo.finished))
                updated.value = true
                updatedToDo.value = ToDo(
                    _id = todo._id,
                    title = todo.title,
                    detail = todo.detail,
                    created = todo.created,
                    modified = todo.modified,
                    finished = !(todo.finished)
                )
            } catch (e: Exception) {
                errorMessage.value = e.message ?: ""
            }
        }
    }

    fun delete(todo: ToDo) {
        viewModelScope.launch {
            try {
                todoRepository.delete(todo)
                deleted.value = true
            } catch (e: Exception) {
                errorMessage.value = e.message ?: ""
            }
        }
    }
}