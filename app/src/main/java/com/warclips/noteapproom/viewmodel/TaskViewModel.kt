package com.warclips.noteapproom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.warclips.noteapproom.model.Task
import com.warclips.noteapproom.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    app: Application,
    private val taskRepository: TaskRepository): AndroidViewModel(app) {

        fun addTask(task: Task) =
            viewModelScope.launch {
                taskRepository.insertTask(task)
            }

        fun deleteTask(task: Task) =
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }

    fun updateTask(task: Task) =
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }

    fun getAllTasks() = taskRepository.getAllTasks()

    fun searchDatabase(query: String) = taskRepository.searchDatabase(query)
}