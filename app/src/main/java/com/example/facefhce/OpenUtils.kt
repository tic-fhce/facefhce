package com.example.facefhce

import android.content.Context
import android.graphics.Bitmap
import com.ingenieriajhr.firstopencv.utilsOpen.LoadFileCascade
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

class OpenUtils(val context: Context){

    val loadFileCascade = LoadFileCascade(context)

    fun setUtils(bitmap: Bitmap): Bitmap {
        val mat = Mat()
        Utils.bitmapToMat(bitmap,mat)
        Imgproc.cvtColor(mat,mat, Imgproc.COLOR_BGR2GRAY)
        Utils.matToBitmap(mat,bitmap)
        return bitmap

    }
    fun recognizedFace(bitmap: Bitmap): Bitmap {
        val mat = Mat()
        Utils.bitmapToMat(bitmap,mat)

        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY)

        val cascadeClassifier = loadFileCascade.load_cascade(R.raw.haarcascade_frontalface_default,"haarcascade_frontalface_default.xml")

        val faces = MatOfRect()
        cascadeClassifier!!.detectMultiScale(mat,faces)

        val rectArray = faces.toArray()
        val mat2 = Mat()
        Utils.bitmapToMat(bitmap,mat2)

        for (rect in rectArray) {
            Imgproc.rectangle(
                mat2,
                Point(rect.x.toDouble(), rect.y.toDouble()),
                Point((rect.x + rect.width).toDouble(), (rect.y + rect.height).toDouble()),
                Scalar(0.0, 255.0, 0.0), // color del rectángulo (en BGR)
                2 // grosor del rectángulo
            )
        }
        Utils.matToBitmap(mat2, bitmap)
        return bitmap
    }
}