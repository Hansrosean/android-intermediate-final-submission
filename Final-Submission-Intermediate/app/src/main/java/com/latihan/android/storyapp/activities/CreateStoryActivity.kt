package com.latihan.android.storyapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.latihan.android.storyapp.R
import com.latihan.android.storyapp.databinding.ActivityCreateStoryBinding
import com.latihan.android.storyapp.helper.createTempFile
import com.latihan.android.storyapp.helper.reduceImageSize
import com.latihan.android.storyapp.helper.uriToFile
import com.latihan.android.storyapp.viewmodels.CreateStoryViewModel
import com.latihan.android.storyapp.viewmodels.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CreateStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var photoPath: String
    private var getFile: File? = null
    private lateinit var token: String

    private val createStoryViewModel by viewModels<CreateStoryViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    companion object {
        private const val PERMISSION_CODE = 10
        private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Create Story"

        setupViewModel()

        showProgressBar(false)

        if (!collectAllPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSION,
                PERMISSION_CODE
            )
        }

        dataStoreViewModel.getUser().observe(this) { user ->
            token = user.token
            binding.btnPhoto.setOnClickListener { takePhoto() }
            binding.btnGallery.setOnClickListener { openGallery() }
            binding.btnUpload.setOnClickListener { uploadImage(token) }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (!collectAllPermissions()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_fail),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun collectAllPermissions() = PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupViewModel() {
        createStoryViewModel.isLoading.observe(this) { showProgressBar(it) }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.latihan.android.storyapp",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launchCamera.launch(intent)
        }
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val localFile = File(photoPath)
            getFile = localFile

            val photoResult = BitmapFactory.decodeFile(getFile?.path)
            binding.imgPost.setImageBitmap(photoResult)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooseImage = Intent.createChooser(intent, "Choose a Picture")
        launchGallery.launch(chooseImage)
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage: Uri = result.data?.data as Uri
            val localFile = uriToFile(selectedImage, this)

            getFile = localFile
            binding.imgPost.setImageURI(selectedImage)
        }
    }

    private fun uploadImage(token: String) {
        showProgressBar(true)
        if (getFile != null) {
            val image = reduceImageSize(getFile as File)
            val description = binding.edtDescription.text.toString()

            if (description.isEmpty()) {
                showProgressBar(false)

                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.upload_failed))
                    setMessage(getString(R.string.description_null))
                    setPositiveButton("YEP") { _, _ ->
                    }
                    create()
                    show()
                }
            } else {
                dataStoreViewModel.getUser().observe(this) {
                    createStoryViewModel.createStory("Bearer $token", image, description)
                    createStoryViewModel.message.observe(this) { message ->
                        Toast.makeText(this@CreateStoryActivity, message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@CreateStoryActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        } else {
            showProgressBar(false)
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.upload_failed))
                setMessage(getString(R.string.photo_null))
                setPositiveButton("YEP") { _, _ ->
                }
                create()
                show()
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}