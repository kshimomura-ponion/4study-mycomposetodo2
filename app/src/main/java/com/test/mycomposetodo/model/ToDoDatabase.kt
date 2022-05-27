package com.test.mycomposetodo.model

import androidx.room.Database
import androidx.room.RoomDatabase

// こちらの抽象クラスはビルド時に自動的に実装される
@Database (entities = [ToDo::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun todoDAO():ToDoDAO
}