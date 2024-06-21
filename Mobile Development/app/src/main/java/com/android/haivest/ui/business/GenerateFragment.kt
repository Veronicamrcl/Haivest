package com.android.haivest.ui.business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.haivest.data.model.ImageResult
import com.android.haivest.data.network.request.RecommendRequest
import com.android.haivest.databinding.FragmentGenerateBinding

class GenerateFragment : Fragment() {

    private lateinit var binding: FragmentGenerateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val city = binding.editTextCity.text

//        val city = binding.spinnerCity.selectedItem
//        val city = "Jakarta"

        binding.generateButton.setOnClickListener {
            if (city != null) {
                movesToUpload(RecommendRequest(city.toString()))
            } else {
                Toast.makeText(requireContext(), "No selected item", Toast.LENGTH_SHORT).show()
            }

        }

//        setupSpinnerCity()
//        setupSpinnerAge()
//        setupSpinnerIncome()
//        setupSpinnerArea()

    }

    private fun setupSpinnerCity() {
        val cities = listOf("Jakarta", "Bandung", "Semarang", "Surabaya", "Depok")
        val cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerCity.adapter = cityAdapter
    }

    private fun setupSpinnerAge() {
        val age = listOf("0-17 years old", "18-30 years old", "31-45 years old", "46-60 years old", "60+ years old")
        val ageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, age)
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerAge.adapter = ageAdapter
    }

    private fun setupSpinnerIncome() {
        val income = listOf("0-1 million Rupiah", "2-4 million Rupiah", "5-10 million Rupiah", "10-20 million Rupiah")
        val incomeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, income)
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerIncome.adapter = incomeAdapter
    }

    private fun setupSpinnerArea() {
        val area = listOf("0-3m2", "4-6m2", "7-9m2", "10-12m2", "13-15m2","16-18m2", "19-21m2", "22-24m2", "25-27m2", "28-30m2" )
        val areaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, area)
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerSurfaceArea.adapter = areaAdapter
    }

    private fun movesToUpload(recommendRequest: RecommendRequest) {
        val action = GenerateFragmentDirections.actionGenerateFragmentToRecommendFragment(
            recommendRequest
        )
        findNavController().navigate(action)
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

}
