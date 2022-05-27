package com.test.mycomposetodo.repository.todo

import com.test.mycomposetodo.model.ToDo
import com.test.mycomposetodo.model.ToDoDAO
import com.test.mycomposetodo.model.getEmptyToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
    private val dao: ToDoDAO
): ToDoRepository {
    override suspend fun create(title: String, detail: String) {
        val now = System.currentTimeMillis()
        val todo = ToDo(
            title = title,
            detail = detail,
            created = now,
            modified = now,
            finished = false
        )
        withContext(Dispatchers.IO) {
            dao.create(todo)
        }
    }
    override suspend fun update(todo: ToDo, title: String, detail: String):ToDo {
        val now = System.currentTimeMillis()
        val updateToDo = ToDo(
            _id = todo._id,
            title = title,
            detail = detail,
            created = todo.created,
            modified = now,
            finished = todo.finished
        )
        withContext(Dispatchers.IO) {
            dao.update(updateToDo)
        }
        return updateToDo
    }
    override suspend fun update(todo: ToDo, title: String, detail: String, finished: Boolean):ToDo {
        val now = System.currentTimeMillis()
        val updateToDo = ToDo(
            _id = todo._id,
            title = title,
            detail = detail,
            created = todo.created,
            modified = now,
            finished = finished
        )
        withContext(Dispatchers.IO) {
            dao.update(updateToDo)
        }
        return updateToDo
    }

    override suspend fun delete(todo: ToDo) {
        dao.delete(todo)
    }

    override fun getAll(): Flow<List<ToDo>> {
        return dao.getAll()
    }

    override fun getById(todoId: Int): Flow<ToDo> {
        if (todoId == -1){
            return flowOf(getEmptyToDo())
        } else {
            return dao.getById(todoId).take(1).map { list -> list[0] }
        }
    }
}