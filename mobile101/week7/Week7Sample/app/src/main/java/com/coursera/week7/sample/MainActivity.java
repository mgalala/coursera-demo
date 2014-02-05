package com.coursera.week7.sample;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView rootView = new TextView(this.getActivity());
//            rootView.setText("Hello World!");
//            rootView.setTextSize(50);

            Bitmap penguin = BitmapFactory.decodeResource(getResources(), R.drawable.penguin);
            Bitmap bitmap = Bitmap.createBitmap(480, 600, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(0xffff6600);

            Paint paint = new Paint();
            paint.setColor(0xff000099);
            paint.setStrokeWidth(16);
            canvas.drawLine(0, 0, 480, 600, paint);

            canvas.drawBitmap(penguin, 100, 100, null);

            ImageView imageView = new ImageView(this.getActivity());
            imageView.setImageBitmap(bitmap);


            return imageView;
        }
    }

}
