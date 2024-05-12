package com.warclips.noteapproom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.warclips.noteapproom.databinding.TaskLayoutBinding
import com.warclips.noteapproom.fragment.HomeFragmentDirections
import com.warclips.noteapproom.model.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val itemBinding: TaskLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskDescription == newItem.taskDescription &&
                    oldItem.taskTitle == newItem.taskTitle &&
                    oldItem.date == newItem.date &&
                    oldItem.time == newItem.time &&
                    oldItem.priority == newItem.priority
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = differ.currentList[position]

        holder.itemBinding.taskTitle.text = currentTask.taskTitle
        holder.itemBinding.taskDescription.text = currentTask.taskDescription
        holder.itemBinding.taskDate.text = currentTask.date
        holder.itemBinding.taskTime.text = currentTask.time

        val priorityText = when (currentTask.priority) {
            1 -> "Low Priority"
            2 -> "Medium Priority"
            3 -> "High Priority"
            else -> "No Priority"
        }

        holder.itemBinding.taskPriority.text = priorityText

        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditTaskFragment(currentTask)
            it.findNavController().navigate(direction)
        }
    }
}