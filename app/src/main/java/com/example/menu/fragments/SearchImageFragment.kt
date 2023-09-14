package com.example.menu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.menu.R
import com.example.menu.adapters.ImageAdapter
import com.example.menu.data.Consumable
import com.example.menu.data.ConsumableViewModel
import com.example.menu.data.ConsumableViewModelFactory
import com.example.menu.data.MenuApplication
import com.example.menu.databinding.FragmentSearchImageBinding
import com.example.menu.network.Item
import com.example.menu.network.NetworkItemViewModel

class SearchImageFragment : Fragment() {

    private var _binding: FragmentSearchImageBinding? = null
    private val binding get() = _binding!!
    private val itemViewModel: NetworkItemViewModel by viewModels()
    private val consumableViewModel: ConsumableViewModel by activityViewModels {
        ConsumableViewModelFactory(
            (activity?.application as MenuApplication).database.getConsumableDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchImageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: SearchImageFragmentArgs by navArgs()
        val consumableId = args.consumableId
        val consumableName = args.name
        val consumablePrice = args.price
        val title = if (consumableId > 0) {
            getString(R.string.edit_fragment_title)
        } else {
            getString(R.string.add_fragment_title)
        }

        binding.apply {
            viewModel = itemViewModel
            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            lifecycleOwner = this@SearchImageFragment
            searchItemEditText.setText(consumableName)
            searchImagesButton.setOnClickListener {
                searchImages()
            }
        }

        itemViewModel.items.observe(viewLifecycleOwner) { itemList ->
            binding.imagesRecyclerView.adapter = ImageAdapter(itemList) { selectedItem ->
                val action =
                    SearchImageFragmentDirections.actionSearchImageFragmentToAddItemFragment(
                        title = title,
                        id = consumableId
                    )

                val consumable = Consumable(
                    id = consumableId,
                    name = consumableName,
                    price = consumablePrice.toDouble(),
                    imageUrl = selectedItem.urls.small,
                    description = selectedItem.description
                )
                consumableViewModel.updateBindConsumable(consumable)
                findNavController().navigate(action)
            }
        }
    }

    private fun searchImages() {
        val query = binding.searchItemEditText.text.toString().trim()
        itemViewModel.fetchItem(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}