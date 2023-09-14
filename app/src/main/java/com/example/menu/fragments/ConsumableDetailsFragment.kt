package com.example.menu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.menu.R
import com.example.menu.data.ConsumableViewModel
import com.example.menu.data.ConsumableViewModelFactory
import com.example.menu.data.MenuApplication
import com.example.menu.databinding.FragmentConsumableDetailsBinding

class ConsumableDetailsFragment : Fragment() {

    private var _binding: FragmentConsumableDetailsBinding? = null
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
        _binding = FragmentConsumableDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentConsumable.observe(viewLifecycleOwner) { consumable ->
            binding.apply {
                consumableNameTV.text = consumable.name
                consumableDescriptionTV.text = consumable.description
            }
        }

        var imageLoader = ImageLoader.Builder(requireContext())
            .crossfade(true)
            .build()

        val imageUri = R.drawable.background

        val request = ImageRequest.Builder(requireContext())
            .data(imageUri)
            .target {
                binding.fragmentConsumableDetails.background = it
            }
            .build()

        imageLoader.enqueue(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}