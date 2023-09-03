package com.example.facefhce

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.facefhce.databinding.ActivityMainBinding
import com.ingenieriiajhr.jhrCameraX.BitmapResponse
import com.ingenieriiajhr.jhrCameraX.CameraJhr
import org.opencv.android.OpenCVLoader


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var cameraJhr: CameraJhr
    lateinit var openUtils : OpenUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(OpenCVLoader.initDebug()) Log.d("OPEN2023CV","SUCESS")
        else Log.d("OPEN2023CV","ERROR")

        openUtils = OpenUtils(this)

        cameraJhr = CameraJhr(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (cameraJhr.allpermissionsGranted() && !cameraJhr.ifStartCamera){
            startCameraJhr()
        }else{
            cameraJhr.noPermissions()
        }
    }

    private fun startCameraJhr() {
        cameraJhr.addlistenerBitmap(object : BitmapResponse {
            override fun bitmapReturn(bitmap: Bitmap?) {
                val newBitmap = openUtils.recognizedFace(bitmap!!)
                if (bitmap!=null){
                    runOnUiThread {
                        binding.imageView.setImageBitmap(newBitmap)
                    }
                }
            }
        })

        cameraJhr.initBitmap()

        //selector camera LENS_FACING_FRONT = 0;    LENS_FACING_BACK = 1;
        //aspect Ratio  RATIO_4_3 = 0; RATIO_16_9 = 1;  false returImageProxy, true return bitmap
        cameraJhr.start(0,0,binding.cameraPreview,false,false,true)
    }

    fun Bitmap.rotate(degrees:Float) = Bitmap.createBitmap(this,0,0,width,height,
        Matrix().apply { postRotate(degrees) },true)
}