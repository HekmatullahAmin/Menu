package com.example.menu.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.menu.R
import com.example.menu.adapters.ConsumableAdapter
import com.example.menu.data.Consumable
import com.example.menu.data.ConsumableViewModel
import com.example.menu.data.ConsumableViewModelFactory
import com.example.menu.data.MenuApplication
import com.example.menu.databinding.FragmentMenuListBinding
import okio.Lock

class MenuListFragment : Fragment() {

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConsumableViewModel by activityViewModels {
        ConsumableViewModelFactory(
            (activity?.application as MenuApplication).database.getConsumableDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        

        val slidingPaneLayout = binding.slidingPaneLayout
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            MenuListOnBackPressedCallback(slidingPaneLayout, binding)
        )

        val adapter = ConsumableAdapter(
            onConsumableClicked = {
                binding.slidingPaneLayout.openPane()
                viewModel.updateCurrentConsumable(it)
            },
            onDeleteClicked = {
                viewModel.delete(it)
            },
            onEditClicked = {
                val action = MenuListFragmentDirections.actionMenuListFragmentToAddItemFragment(
                    title = getString(R.string.edit_fragment_title),
                    id = it.id,
                )
                viewModel.updateBindConsumable(it)
                findNavController().navigate(action)
            }
        )

        binding.apply {
            menuRecyclerView.adapter = adapter
            addItemFAB.setOnClickListener {
                val action = MenuListFragmentDirections.actionMenuListFragmentToAddItemFragment(
                    title = getString(R.string.add_fragment_title),
                )
                this@MenuListFragment.findNavController().navigate(action)
            }
        }

        viewModel.getAllConsumables().observe(viewLifecycleOwner) { consumableList ->
            adapter.submitList(consumableList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MenuListOnBackPressedCallback(
    private val slidingPaneLayout: SlidingPaneLayout,
    private val binding: FragmentMenuListBinding
) :
    OnBackPressedCallback(slidingPaneLayout.isOpen && slidingPaneLayout.isSlideable),
    SlidingPaneLayout.PanelSlideListener {

    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }

    override fun handleOnBackPressed() {
        slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
    }

    override fun onPanelOpened(panel: View) {
        isEnabled = true
        binding.addItemFAB.visibility = View.INVISIBLE
    }

    override fun onPanelClosed(panel: View) {
        isEnabled = false
        binding.addItemFAB.visibility = View.VISIBLE
    }

}