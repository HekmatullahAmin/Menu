package com.example.menu.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.menu.R
import com.example.menu.data.Consumable
import com.example.menu.data.ConsumableViewModel
import com.example.menu.data.ConsumableViewModelFactory
import com.example.menu.data.MenuApplication
import com.example.menu.databinding.FragmentAddConsumableBinding


class AddConsumableFragment : Fragment() {

    private var _binding: FragmentAddConsumableBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConsumableViewModel by activityViewModels {
        ConsumableViewModelFactory(
            (activity?.application as MenuApplication).database.getConsumableDao()
        )
    }
    private val args: AddConsumableFragmentArgs by navArgs()
    private lateinit var consumable: Consumable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddConsumableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val consumableId = args.id

        viewModel.bindConsumable.observe(viewLifecycleOwner) { currentConsumable ->
            currentConsumable?.let {
                bind(currentConsumable)
                consumable = currentConsumable
            }
        }

        binding.apply {

            consumableImageView.setOnClickListener {
                moveToSearchFragmentScreen(consumableId)
            }

            saveConsumableButton.setOnClickListener {
                val tempName = binding.consumableNameEditText.text.toString()
                val tempPrice = binding.consumablePriceEditText.text.toString()
                if (isEntryValid()) {
                    consumable.name = tempName
                    consumable.price = tempPrice.toDouble()
                    if (consumableId > 0) {
                        updateConsumableToDB(consumable)
                    } else {
                        saveConsumableToDB(consumable)
                    }
                    viewModel.resetBindConsumable()
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun isEntryValid(): Boolean {
        val imageView = binding.consumableImageView
        val currentDrawable = imageView.drawable
        val defaultDrawable =
            AppCompatResources.getDrawable(imageView.context, R.drawable.search_image)

        return viewModel.isEntryValid(
            binding.consumableNameEditText.text.toString().trim(),
            binding.consumablePriceEditText.text.toString().trim(),
            currentDrawable,
            defaultDrawable
        )
    }

    private fun moveToSearchFragmentScreen(consumableId: Int) {
        val tempName = binding.consumableNameEditText.text.toString().trim()
        var tempPrice = binding.consumablePriceEditText.text.toString().trim()
        if (tempPrice.isBlank()) {
            tempPrice = "0"
        }
        val action =
            AddConsumableFragmentDirections.actionAddItemFragmentToSearchImageFragment(
                name = tempName,
                consumableId = consumableId,
                price = tempPrice
            )
        this.findNavController().navigate(action)
    }

    private fun saveConsumableToDB(consumable: Consumable) {

        viewModel.insertConsumable(
            consumable.name,
            consumable.price.toString(),
            consumable.imageUrl,
            consumable.description
        )
        findNavController().navigate(R.id.action_addItemFragment_to_menuListFragment)
    }

    private fun updateConsumableToDB(consumable: Consumable) {
        viewModel.updateConsumable(
            consumable.id,
            consumable.name,
            consumable.price.toString(),
            consumable.imageUrl,
            consumable.description
        )
        findNavController().navigate(R.id.action_addItemFragment_to_menuListFragment)
    }

    private fun bind(consumable: Consumable) {
        binding.apply {
            consumableNameEditText.setText(consumable.name)
            if (consumable.price != 0.0) {
                consumablePriceEditText.setText(consumable.price.toString())
            }
            val imageUri = consumable.imageUrl.toUri().buildUpon().scheme("https").build()
            consumableImageView.load(imageUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.resetBindConsumable()
        viewModel.resetCurrentConsumable()
    }
}