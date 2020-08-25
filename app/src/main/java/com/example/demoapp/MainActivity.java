package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private static String[] CIFAR10_CLASSES = {
            "airplane",
            "automobile",
            "bird",
            "deer",
            "dog",
            "cat",
            "frog",
            "horse",
            "truck",
            "ship"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Button buttonLoadImage = (Button) findViewById(R.id.button);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView textView = findViewById(R.id.result_text);
                textView.setText("");
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Button detectButton = (Button) findViewById(R.id.detect);
        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ImageView imageView = (ImageView) findViewById(R.id.image);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ///////////////////////////////////////////////////////////////////////////////////
                String extPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/models/mobilenetv2_w_cifar10_epoch80.pt";
                Classifier classifier = new Classifier(extPath);
                int result = classifier.inferrence(bitmap, 224, 224);
                ///////////////////////////////////////////////////////////////////////////////////

                TextView textView = findViewById(R.id.result_text);
                textView.setText("result : " +  CIFAR10_CLASSES[result]);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            imageView.setImageURI(null);
            imageView.setImageURI(selectedImage);
        }
    }

}
