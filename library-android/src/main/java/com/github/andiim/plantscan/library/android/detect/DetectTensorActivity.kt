package com.github.andiim.plantscan.library.android.detect

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.andiim.plantscan.library.android.databinding.ActivityTensorDetectBinding
import com.github.andiim.plantscan.library.android.ml.BestInt8
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class DetectTensorActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityTensorDetectBinding
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewBinding = ActivityTensorDetectBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val fileName = "labels.txt"
        val labels =
            application.assets.open(fileName).bufferedReader().use { it.readText() }.split("\n")

        viewBinding.select.setOnClickListener { view ->
            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 200);
        }

        viewBinding.predict.setOnClickListener {
            var resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = BestInt8.newInstance(this)

            val tbuffer = TensorImage(DataType.FLOAT32)
            tbuffer.load(resized)
            val byteBuffer = tbuffer.buffer

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputAsTensorBuffer
            var max = getMax(outputFeature0.floatArray)

            viewBinding.textView.text = labels[max]
            // textView.text = outputFeature0.floatArray[max].toString()
            model.close()
        }
    }

    fun getMax(arr: FloatArray): Int {
        var ind = 0;
        var min = 0.0f;

        for (i in 0..51) {
            if (arr[i] > min) {
                Log.d("MainActivity", "Position =$i")
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 250) {
            viewBinding.img.setImageURI(data?.data)

            var uri: Uri? = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
    }
}
