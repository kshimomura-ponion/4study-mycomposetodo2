package com.test.mycomposetodo.model

import android.provider.ContactsContract
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDAO {

    // SQL中の:で始まるパラメータ(今回はstartCreatedとlimit)は、メソッドの引数で渡された値に置き換えられて実行されます。
    // 戻り値の型をKotlinコルーチンのFlow<T>にすることで、データベースの監視機能がつきます。
    @Query("select * from ToDo where created < :startCreated order by created desc limit :limit")
    fun getWithCreated(startCreated: Long, limit: Int): Flow<List<ToDo>>

    @Insert
    suspend fun create(todo: ToDo)

    @Update
    suspend fun update(todo: ToDo)

    @Delete
    suspend fun delete(todo: ToDo)

    @Query("select * from ToDo order by created desc")
    fun getAll(): Flow<List<ToDo>>

    // 1つしかデータを取らないけどリストの形で取得しなければならない・・
    @Query("select * from ToDo where _id = :todoId limit 1")
    fun getById(todoId: Int): Flow<List<ToDo>>
}