package com.android.haivest.ui.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.haivest.R
import com.android.haivest.databinding.FragmentAnalyzeBinding

class AnalyzeFragment : Fragment() {

    private lateinit var binding: FragmentAnalyzeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStartAnalyze.setOnClickListener {
            findNavController().navigate(R.id.action_analyzeFragment_to_cameraFragment)
        }

    }


}