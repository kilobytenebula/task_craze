package com.warclips.noteapproom.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.warclips.noteapproom.MainActivity
import com.warclips.noteapproom.R
import com.warclips.noteapproom.databinding.FragmentAddTaskBinding
import com.warclips.noteapproom.model.Task
import com.warclips.noteapproom.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment(R.layout.fragment_add_task), MenuProvider {

    private var addTaskBinding: FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    private var selectedPriority = -1
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view

        binding.addTextDate.setOnClickListener { showDatePicker() }
        binding.addTextTime.setOnClickListener { showTimePicker() }

        binding.radioGroupPriority.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonLow -> selectedPriority = 1
                R.id.radioButtonMedium -> selectedPriority = 2
                R.id.radioButtonHigh -> selectedPriority = 3
            }
        }

    }

    private fun saveTask(view: View) {
        val taskTitle = binding.addTaskTitle.text.toString()
        val taskDescription = binding.addTaskDesc.text.toString()

        if (taskTitle.isNotEmpty()) {
            val task = Task(0, taskTitle, taskDescription, selectedDate, selectedTime, selectedPriority)
            taskViewModel.addTask(task)

            Toast.makeText(requireContext(), "Task Saved!", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(requireContext(), "Please enter task title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveTask(addTaskView)
                true
            }
            else -> false
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDate = sdf.format(selectedDateCalendar.time)
                binding.addTextDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTimeCalendar = Calendar.getInstance()
                selectedTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTimeCalendar.set(Calendar.MINUTE, minute)
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                selectedTime = sdf.format(selectedTimeCalendar.time)
                binding.addTextTime.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }

}