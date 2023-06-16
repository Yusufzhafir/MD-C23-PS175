package com.digidig.binit.ui.detection

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.digidig.binit.databinding.ActivityTrashImageDetectionBinding
import com.digidig.binit.ml.ConvertedModelBinit
import com.digidig.binit.utils.compressImageFile
import com.digidig.binit.utils.createCustomTempFile
import com.digidig.binit.utils.uriToFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class TrashImageDetection : AppCompatActivity() {

    companion object {
        private const val TAG = "TRASH IMAGE DETECTION"
    }
    private lateinit var binding: ActivityTrashImageDetectionBinding

    private lateinit var bitmap: Bitmap
    private var getFile: File? = null

    private lateinit var viewModel: TrashImageDetectionViewModel

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            binding.ivSampah.setImageURI(selectedImg)
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this)
                Log.d("add","besar file ${myFile.length()}")
                if (myFile.length()>1000000){
                    getFile = createCustomTempFile(this)
                    compressImageFile(myFile, getFile!!,1_000_000)
                    Log.d("add","besar file compressed ${getFile!!.length()}")
                }else{
                    getFile = myFile
                }

                bitmap = BitmapFactory.decodeFile(getFile!!.path)
//                binding.ivSampah.setImageBitmap(bitmap)
            }
        }
    }

    fun setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )//enable fullscreen
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashImageDetectionBinding.inflate(layoutInflater)

        setFullScreen()
        setContentView(binding.root)

        //setup view model
        viewModel = ViewModelProvider(this)[TrashImageDetectionViewModel::class.java]



        viewModel.apply {
            textResult.observe(this@TrashImageDetection){
                binding.resTv.text = it
            }

            isLoading.observe(this@TrashImageDetection){ loading ->
                if (loading) {
                    showLoading(true)
                } else {
                    // Loading operation completed, hide the progress bar
                    showLoading(false)
                }
            }
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(NormalizeOp(0f,1f ))
            .add(ResizeOp(224,224,ResizeOp.ResizeMethod.BILINEAR))
            .build()

        binding.apply {
            selectBtn.setOnClickListener {
                startGallery()
            }

            predictBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {

                    //load and process image
                    var tensorImage = TensorImage(DataType.FLOAT32)
                    tensorImage.load(bitmap)
                    Log.d(TAG,tensorImage.buffer.toString())
                    tensorImage = imageProcessor.process(tensorImage)

                    withContext(Dispatchers.Main){
                        viewModel.isLoading.value = true
                        val model = ConvertedModelBinit.newInstance(this@TrashImageDetection)

                        // Creates inputs for reference.
                        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

                        inputFeature0.loadBuffer(tensorImage.buffer)

                        // Runs model inference and gets result.
                        val outputs = model.process(inputFeature0)
                        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                        var maxIdx = 0
                        outputFeature0.forEachIndexed{index,value ->
                            Log.d(TAG,"THE VALUE OF INDEX $index IS $value ")
                            maxIdx = if (value>outputFeature0[maxIdx]){
                                index
                            }else{
                                maxIdx
                            }
                        }

                        viewModel.textResult.value = labels[maxIdx]
                        // Update the isLoading value
                        viewModel.isLoading.value = false
                        model.close()
                    }

                }
            }
        }

    }
    var labels = listOf("organic","recycle","hazardous")

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}