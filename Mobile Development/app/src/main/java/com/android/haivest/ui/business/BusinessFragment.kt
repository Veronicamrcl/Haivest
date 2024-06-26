package com.android.haivest.ui.business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.haivest.R
import com.android.haivest.databinding.FragmentBussinessBinding

class BusinessFragment : Fragment() {

    private var binding: FragmentBussinessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBussinessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.buttonStartBusiness?.setOnClickListener {
            findNavController().navigate(R.id.action_businessFragment_to_generateFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}