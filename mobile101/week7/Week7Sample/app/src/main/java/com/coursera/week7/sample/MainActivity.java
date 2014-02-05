package com.coursera.week7.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends ActionBarActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        int count = sharedPreferences.getInt("count", 0);

        count++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("count", count);
//        editor.commit();
//        TextView textView = new TextView(this);
//        textView.setText("Count: " + count);
//        Log.w("MYCLASS", "My count is: " + count);
//        setContentView(textView);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements Runnable {
        Uri uri;
        ImageView imageView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView rootView = new TextView(this.getActivity());
//            rootView.setText("Hello World!");
//            rootView.setTextSize(50);

//            Bitmap penguin = BitmapFactory.decodeResource(getResources(), R.drawable.penguin);
//            Bitmap bitmap = Bitmap.createBitmap(480, 600, Bitmap.Config.ARGB_8888);
//
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawColor(0xffff6600);
//
//            Paint paint = new Paint();
//            paint.setColor(0xff000099);
//            paint.setStrokeWidth(16);
//            canvas.drawLine(0, 0, 480, 600, paint);
//
//            canvas.drawBitmap(penguin, 100, 100, null);
//
//            ImageView imageView = new ImageView(this.getActivity());
//            imageView.setImageBitmap(bitmap);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select ..."), 1);
                }
            };
            Button button = (Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(onClickListener);

            View.OnClickListener shareListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveAndShare(view);
                }
            };

            Button saveButton = (Button) rootView.findViewById(R.id.button2);
            saveButton.setOnClickListener(shareListener);

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 & resultCode == RESULT_OK) {
                uri = data.getData();
                Toast.makeText(this.getActivity(), uri.toString(), Toast.LENGTH_LONG).show();
                imageView = (ImageView) this.getActivity().findViewById(R.id.imageView);

                imageView.post(this);
            }


        }

        Bitmap bitmap = null;

        public void run() {
            InputStream inputStream = null;
            try {
                inputStream = this.getActivity().getContentResolver().openInputStream(uri);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, opts);

                int imageHeight = opts.outHeight;
                int imageWidth = opts.outWidth;
                inputStream.close();

                int displayWidth = getResources().getDisplayMetrics().widthPixels;
                int displayHeight = getResources().getDisplayMetrics().heightPixels;


                int sample = 1;
//                    while (imageWidth / sample > displayWidth || imageHeight / sample > displayHeight) {
                while (displayWidth * sample < imageWidth || displayHeight * sample < imageHeight) {
                    sample = sample * 2;
                }
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = sample;
                inputStream = this.getActivity().getContentResolver().openInputStream(uri);

                Bitmap oldBitmap = BitmapFactory.decodeStream(inputStream, null, opts);
                inputStream.close();

                // mutable bitmap
                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = Bitmap.createBitmap(oldBitmap.getWidth(), oldBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(oldBitmap, 0, 0, null);

                TextPaint tp = new TextPaint();
                tp.setTextSize(oldBitmap.getHeight() / 2);
                tp.setColor(0x800000ff);  //AARRGGBB
                //0xff ...... not transparent (opaque)
                //0x00 ...... transparent
                canvas.drawText("Gotcha", 0, oldBitmap.getHeight() / 2, tp);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void saveAndShare(View v) {
            if (bitmap == null) {
                return;
            }
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path.mkdirs();

            String fileName = "Imagen_" + System.currentTimeMillis() + ".jpg";
            File file = new File(path, fileName);
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri uri1 = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri1);
            this.getActivity().sendBroadcast(intent);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share using..."));
        }
    }

}
