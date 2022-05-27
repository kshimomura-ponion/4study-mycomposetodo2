package com.test.mycomposetodo.repository.todo

import com.test.mycomposetodo.model.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    suspend fun create(title: String, detail:String)
    suspend fun update(todo: ToDo, title: String, detail: String): ToDo
    suspend fun update(todo: ToDo, title: String, detail: String, finished: Boolean): ToDo
    suspend fun delete(value: ToDo)

    fun getAll(): Flow<List<ToDo>>

    fun getById(todoId: Int): Flow<ToDo>
}