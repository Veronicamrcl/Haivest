package com.android.haivest.ui.analyze.detect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.haivest.R
import com.android.haivest.data.model.ImageResult
import com.android.haivest.data.utils.createFile
import com.android.haivest.data.utils.rotateBitmap
import com.android.haivest.data.utils.toBitmap
import com.android.haivest.data.utils.toFile
import com.android.haivest.databinding.FragmentCameraBinding

class CameraFragment: Fragment() {

    private var _bindings: FragmentCameraBinding? = null
    private val bindings get() = _bindings
    private var camerasSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imgCapture: ImageCapture? = null
    private lateinit var soundsPool: SoundPool
    private var soundsId = 0
    private var soundsPoolLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindings = FragmentCameraBinding.inflate(inflater, container, false)
        return bindings?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchesPermission.launch(PERMISSIONS.first())
        soundsPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        soundsPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) soundsPoolLoaded = true
        }
        soundsId = soundsPool.load(requireActivity(), R.raw.shutter_camera, 1)
        setsUpView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    private fun setsUpView() {
        bindings?.apply {
            btnCapture.setOnClickListener {
                if (soundsPoolLoaded) {
                    soundsPool.play(soundsId, 1f, 1f, 1, 0, 1f)
                }
                takesPhoto()
            }
            btnAddGallery.setOnClickListener {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                launchesGallery.launch(PickVisualMediaRequest(mediaType))
            }
            btnFlipCamera.setOnClickListener {
                camerasSelector = if (camerasSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
                startsCamera()
            }
            btnClose.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        startsCamera()
    }

    private fun startsCamera() {
        val camerasProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        camerasProviderFuture.addListener({
            val camerasProvider = camerasProviderFuture.get()
            val previews = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(bindings?.viewFinder?.surfaceProvider)
                }
            imgCapture = ImageCapture.Builder().build()
            try {
                camerasProvider.unbindAll()
                camerasProvider.bindToLifecycle(
                    this,
                    camerasSelector,
                    previews,
                    imgCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.open_camera_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun takesPhoto() {
        // EspressoIdlingResource.increment()
        val imgCapture = imgCapture ?: return
        val photoFiles = createFile(requireActivity().application)
        val outputsOptions = ImageCapture.OutputFileOptions.Builder(photoFiles).build()
        imgCapture.takePicture(
            outputsOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imageBitmaps = photoFiles.toBitmap()
                    val rotatedBitmaps = imageBitmaps.rotateBitmap(
                        camerasSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    movesToUpload(ImageResult(photoFiles, imageBitmap = rotatedBitmaps))
                    //  EspressoIdlingResource.decrement()
                }
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.taking_photo_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    //  EspressoIdlingResource.decrement()
                }
            }
        )
    }

    private fun movesToUpload(imageResult: ImageResult) {
        val action = CameraFragmentDirections.actionCameraFragmentToResultFragment(
                imageResult
            )
        findNavController().navigate(action)
    }

    private val launchesGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val imgFile = it.toFile(requireActivity())
            movesToUpload(ImageResult(imgFile, it, isFromCamera = false))
        }
    }

    private val launchesPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted and !isAllPermissionsGranted()) {
            Toast.makeText(requireActivity(), getString(R.string.access_denied), Toast.LENGTH_SHORT).show()
        }
    }
    private fun isAllPermissionsGranted() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onDestroyView() {
        super.onDestroyView()
        soundsPool.release()
        _bindings = null
    }
    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }


}