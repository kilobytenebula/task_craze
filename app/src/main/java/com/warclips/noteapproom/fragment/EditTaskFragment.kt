package com.warclips.noteapproom.fragment

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.warclips.noteapproom.MainActivity
import com.warclips.noteapproom.R
import com.warclips.noteapproom.databinding.FragmentEditTaskBinding
import com.warclips.noteapproom.model.Task
import com.warclips.noteapproom.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskFragment : Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditTaskFragmentArgs by navArgs()

    private var selectedPriority: Int = -1
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)


        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.editTaskTitle.setText(currentTask.taskTitle)
        binding.editTaskDesc.setText(currentTask.taskDescription)
        binding.editTextDate.setText(currentTask.date)
        binding.editTextTime.setText(currentTask.time)

        when (currentTask.priority) {
            1 -> binding.radioButtonLow.isChecked = true
            2 -> binding.radioButtonMedium.isChecked = true
            3 -> binding.radioButtonHigh.isChecked = true
            else -> {-1}
        }

        binding.editTextDate.setOnClickListener { showDatePicker() }
        binding.editTextTime.setOnClickListener { showTimePicker() }

        // Listen for changes in the selection of radio buttons
        binding.radioGroupPriority.setOnCheckedChangeListener { _, checkedId ->
            selectedPriority = when (checkedId) {
                R.id.radioButtonLow -> 1
                R.id.radioButtonMedium -> 2
                R.id.radioButtonHigh -> 3
                else -> {-1}
            }
            }

        binding.editTaskFab.setOnClickListener {
            val taskTitle = binding.editTaskTitle.text.toString().trim()
            val taskDesc = binding.editTaskDesc.text.toString().trim()
            val selectedTextDate = binding.editTextDate.text.toString().trim()
            val selectedTextTime = binding.editTextTime.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                val task = Task(currentTask.id, taskTitle, taskDesc, selectedTextDate, selectedTextTime, selectedPriority)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteTask() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Lose your precious task?")
            setPositiveButton("Delete") { _, _ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteTask()
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
                binding.editTextDate.setText(selectedDate)
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
                binding.editTextTime.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editTaskBinding = null
    }

}