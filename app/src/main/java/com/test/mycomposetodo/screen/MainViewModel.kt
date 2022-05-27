package com.test.mycomposetodo.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.mycomposetodo.model.ToDo
import com.test.mycomposetodo.model.getEmptyToDo
import com.test.mycomposetodo.repository.todo.ToDoRepository
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val todoRepository: ToDoRepository
) : ViewModel(){
    val todoList = todoRepository.getAll()
    val errorMessage = MutableStateFlow("")
    val updated = MutableStateFlow(false)
    val updatedToDo = MutableStateFlow(getEmptyToDo())
    val deleted = MutableStateFlow(false)

 /*   fun save(todo: ToDo, todoFinished: Boolean) {
        viewModelScope.launch {
            try {
                todoRepository.update(todo, todo.title, todo.detail, todoFinished)
                saved.value = true
            } catch (e: Exception) {
                errorMessage.value = e.message ?: ""
            }
        }
    }
*/

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
