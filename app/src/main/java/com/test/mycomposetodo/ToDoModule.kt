package com.test.mycomposetodo

import android.content.Context
import androidx.room.Room
import com.test.mycomposetodo.model.ToDoDAO
import com.test.mycomposetodo.model.ToDoDatabase
import com.test.mycomposetodo.repository.todo.ToDoRepository
import com.test.mycomposetodo.repository.todo.ToDoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToDoModule {
    // Databaseの作り方をHiltに教える
    @Singleton
    @Provides
    fun provideToDoDatabase(
        // どんなコンテキストを渡すかを指定するアノテーションをつける
        @ApplicationContext context: Context
    ): ToDoDatabase {
        return Room.databaseBuilder(
            context,
            ToDoDatabase::class.java,
            "todo.db"
        ).build()
    }

    // DatabaseをHiltに教える
    @Singleton
    @Provides
    fun provideToDoDAO(db: ToDoDatabase): ToDoDAO {
        return db.todoDAO()
    }
    // ToDoRepositoryインスタンスの作り方をHiltに教える
    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ToDoRepositoryModule {
        @Binds
        @Singleton
        abstract fun bindToDoRepository(
            impl: ToDoRepositoryImpl
        ): ToDoRepository
    }
}