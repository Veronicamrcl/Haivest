package com.android.haivest.ui.analyze.detect

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.android.haivest.data.factory.ViewModelFactoryDetect
import com.android.haivest.databinding.FragmentResultBinding
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val navArgs by navArgs<ResultFragmentArgs>()

    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactoryDetect.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        observeViewModel()
    }

    private fun setUpView() {
        val imgResult = navArgs.imageResult
        val imageFile = if (imgResult.isFromCamera) {
            imgResult.imageBitmap?.let { saveBitmapToFile(it) }
        } else {
            imgResult.imageUri?.let { getFileFromUri(it) }
        }

        imageFile?.let { file ->
            Glide.with(requireActivity())
                .load(file)
                .into(binding.ivDetetcted)
            binding.progressBar.visibility = View.VISIBLE
            viewModel.predictDisease(file)
        } ?: run {
            binding.tvDiseaseDetected.text = "Error loading image"
        }
    }

    private fun observeViewModel() {
        viewModel.predictResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            result.fold(
                onSuccess = { response ->
                    binding.tvDiseaseDetected.text = response.message
                    binding.tvCauseDisease.text = response.causeDisease
                    binding.tvSymptom.text = response.symptoms.joinToString("\n") { "• $it" }
                    binding.tvTips.text = response.tips.joinToString("\n") { "• $it" }
                },
                onFailure = { error ->
                    binding.tvDiseaseDetected.text = "Prediction failed: ${error.message}"
                }
            )
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val path = getPathFromUri(uri)
        return if (path != null) {
            File(path)
        } else {
            null
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        return if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx).also {
                cursor.close()
            }
        } else {
            uri.path
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val filesDir = requireActivity().filesDir
        val imageFile = File(filesDir, "detected_image_${System.currentTimeMillis()}.png")
        try {
            FileOutputStream(imageFile).use { os ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                os.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imageFile
    }
}
