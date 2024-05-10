package com.warclips.noteapproom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.warclips.noteapproom.database.TaskDatabase
import com.warclips.noteapproom.repository.TaskRepository
import com.warclips.noteapproom.viewmodel.TaskViewModel
import com.warclips.noteapproom.viewmodel.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var taskViewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    private fun setupViewModel() {
        val taskRepository = TaskRepository(TaskDatabase(this))
        val factory = TaskViewModelFactory(application, taskRepository)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
    }
}