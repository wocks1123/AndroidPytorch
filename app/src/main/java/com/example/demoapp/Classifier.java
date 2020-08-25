package com.example.demoapp;

import android.graphics.Bitmap;
import android.util.Log;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

public class Classifier {
    private Module module = null;

    public Classifier(String modulePath)
    {
        module = Module.load(modulePath);
    }

    public int inferrence(Bitmap bitmap, int inputWidth, int inputHeight)
    {
        bitmap = Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true);
        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                bitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                TensorImageUtils.TORCHVISION_NORM_STD_RGB
        );

        IValue v = IValue.from(inputTensor);
        final Tensor outputTensor = module.forward(v).toTensor();
        int pred_label = getPrediction(outputTensor.getDataAsFloatArray());

        return pred_label;
    }

    private int getPrediction(final float[] resultArray)
    {
        int ret = -1;
        float max_val = -99999f;
        for(int i = 0; i < resultArray.length; i++)
        {
            if (max_val < resultArray[i])
            {
                max_val = resultArray[i];
                ret = i;
            }
        }
        return ret;
    }
}
