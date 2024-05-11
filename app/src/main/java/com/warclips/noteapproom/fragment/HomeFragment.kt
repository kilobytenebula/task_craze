package com.warclips.noteapproom.fragment

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.warclips.noteapproom.MainActivity
import com.warclips.noteapproom.R
import com.warclips.noteapproom.adapter.TaskAdapter
import com.warclips.noteapproom.databinding.FragmentHomeBinding
import com.warclips.noteapproom.model.Task
import com.warclips.noteapproom.viewmodel.TaskViewModel

class HomeFragment() : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,
    MenuProvider,
    Parcelable {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        taskViewModel = (activity as MainActivity).taskViewModel

        taskViewModel = (activity as MainActivity).taskViewModel
        setupHomeRecyclerView()

        binding.addTaskFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }
    }

    private fun updateUI(task: List<Task>?) {
        if (task != null) {
            if (task.isNotEmpty()) {
                binding.emptyTasksImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyTasksImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeFragment> {
        override fun createFromParcel(parcel: Parcel): HomeFragment {
            return HomeFragment(parcel)
        }

        override fun newArray(size: Int): Array<HomeFragment?> {
            return arrayOfNulls(size)
        }
    }

    private fun setupHomeRecyclerView() {
        taskAdapter = TaskAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }

        activity?.let {
            taskViewModel.getAllTasks().observe(viewLifecycleOwner) { task ->
                taskAdapter.differ.submitList(task)
                updateUI(task)
            }
        }
    }

    private fun searchDatabase(query: String?) {
        val searchQuery = "%$query%"
        taskViewModel.searchDatabase(searchQuery).observe(this) { list ->
            taskAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}