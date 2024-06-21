package com.android.haivest.ui.business.recommend

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.haivest.data.factory.ViewModelFactoryRecommend
import com.android.haivest.databinding.FragmentRecommendBinding

class RecommendFragment : Fragment() {

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!
    private val navArgs by navArgs<RecommendFragmentArgs>()

    private val viewModel: RecommendViewModel by viewModels {
        ViewModelFactoryRecommend.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE
        val cityResult = navArgs.recommendRequest.city

        viewModel.recommendBusiness(cityResult)
        viewModel.recommendResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            if (result != null) {
                val data = result.data
                binding.apply {

                    tvName.text = data?.namaTanaman.toString()
                    tvScientificName.text = data?.namaTanaman.toString()

                    val dftFlowRate = data?.dft?.flowRate.toString()
                    val dftNutrientSolution = data?.dft?.nutrientSolution.toString()
                    val dftPlantSpacing = data?.dft?.plantSpacing.toString()
                    val dftReservoirDepth = data?.dft?.reservoirDepth.toString()

                    tvDft.text = """
                        • Flow Rate : $dftFlowRate
                        • Nutrient Solution : $dftNutrientSolution
                        • Plant Spacing : $dftPlantSpacing
                        • Reservoir Depth : $dftReservoirDepth
                    """.trimIndent()

                    val nftChannelSlope = data?.nft?.channelSlope.toString()
                    val nftFlowRate = data?.nft?.flowRate.toString()
                    val nftNutrientSolution = data?.nft?.channelSlope.toString()
                    val nftPlantSpacing = data?.nft?.channelSlope.toString()

                    tvNft.text = """
                        • Channel Slope : $nftChannelSlope
                        • Flow Rate : $nftFlowRate
                        • Nutrient Solution : $nftNutrientSolution
                        • Plant Spacing : $nftPlantSpacing
                    """.trimIndent()

                    tvTips.text = data?.tips?.joinToString("\n") { "• $it" }
                }
            } else {
                binding.tvName.text = "No prediction result found"
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}