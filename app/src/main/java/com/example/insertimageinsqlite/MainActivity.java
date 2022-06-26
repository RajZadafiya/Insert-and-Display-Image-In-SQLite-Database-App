package com.example.insertimageinsqlite;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    ImageView imageView, imageview2;

    Bitmap bitmap = null;

    byte img[];

    private MyDataBase mdb = null;

    private SQLiteDatabase db = null;

    private Cursor c = null;

    private static final String DATABASE_NAME = "ImageDb.db";

    public static final int DATABASE_VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        imageview2 = (ImageView) findViewById(R.id.imageView2);
        mdb = new MyDataBase(getApplicationContext(), DATABASE_NAME, null,
                DATABASE_VERSION);

    }

    public void selectImage(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),


                        selectedImage);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);

                img = bos.toByteArray();

                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }


    }


    @SuppressWarnings("deprecation")

    public void uploadImage(View view) {

        db = mdb.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("image", img);

        db.insert("tableimage", null, cv);

        imageView.setAlpha(0);

        Toast.makeText(this, "inserted successfully", Toast.LENGTH_SHORT).show();


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("Range")
    public void retImage(View view) throws NoSuchFieldException, IllegalAccessException {
        db = mdb.getReadableDatabase();
        Cursor c = db.rawQuery("select * from tableimage", null);
        if (c.moveToNext()) {
            byte[] image = c.getBlob(0);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bmp);
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//            String[] col = {"image"};
//
//            db = mdb.getReadableDatabase();
//
//            c = db.query("tableimage", col, null, null, null, null, null);
////            CursorWindow cw = new CursorWindow("test", 5000000);
////            AbstractWindowedCursor ac = (AbstractWindowedCursor) c;
////            ac.setWindow(cw);
//         //   CursorWindow.class.getDeclaredField("sCursorWindowSize").setAccessible(true);
//           // CursorWindow.class.getDeclaredField("sCursorWindowSize").set(null, 50 * 1024 * 1024); // 50 MB
//
//            if (c != null) {
//                do {
//                    img = c.getBlob(c.getColumnIndex("image"));
//                } while (c.moveToNext());
//            }
//
//            Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);
//
//
//            imageview2.setImageBitmap(b1);
//
//            Toast.makeText(this, "Retrive successfully", Toast.LENGTH_SHORT).show();

        }

    }
}