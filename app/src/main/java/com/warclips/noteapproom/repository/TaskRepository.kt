package com.warclips.noteapproom.repository

import com.warclips.noteapproom.database.TaskDatabase
import com.warclips.noteapproom.model.Task

class TaskRepository(private val db: TaskDatabase) {

    suspend fun insertTask(task: Task) = db.getTaskDao().insertTask(task)
    suspend fun deleteTask(task: Task) = db.getTaskDao().deleteTask(task)
    suspend fun updateTask(task: Task) = db.getTaskDao().updateTask(task)

    fun getAllTasks() = db.getTaskDao().getAllTasks()
    fun searchDatabase(query: String?) = db.getTaskDao().searchDatabase(query)
}