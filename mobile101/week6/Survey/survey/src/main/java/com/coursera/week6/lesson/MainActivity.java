package com.coursera.week6.lesson;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void processForm(View view) {
//        view.setVisibility(View.INVISIBLE);
        EditText email = (EditText) findViewById(R.id.email);
        String emailText = email.getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String comments = ((EditText) findViewById(R.id.comments)).getText().toString();

        int position = emailText.indexOf("@");
//        if (position == -1) {
//            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_LONG).show();
//            email.requestFocus();
//            return;
//        }
        String username = emailText.substring(0, position);
        String name = ((EditText) findViewById(R.id.comments)).getText().toString();
//        Toast.makeText(this, "Thank you " + username, Toast.LENGTH_LONG).show();
//        Animation anitmation = AnimationUtils.makeOutAnimation(this, true);
//        view.startAnimation(anitmation);

        //Simple send
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, "Wonderful app!");

        //sms send
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("sms:" + phone));
//        intent.putExtra("sms_body", comments);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "someone@somewhwere.co", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "News");
        intent.putExtra(Intent.EXTRA_TEXT, name + " says " + comments);
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Please configure your email client!", Toast.LENGTH_LONG).show();
        } else {
            startActivity(Intent.createChooser(intent, "Please choose your mail app"));
        }

//        try {
//            startActivity(intent);
//        } catch (Exception e) {
//            Toast.makeText(this, "Can not send email from " + username, Toast.LENGTH_LONG).show();
//            Log.e(this.getClass().getName(), "Cannot send mail", e);
//        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        EditText name;
        EditText phone;
        EditText email;
        EditText comment;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            name = (EditText) rootView.findViewById(R.id.name);
            phone = (EditText) rootView.findViewById(R.id.phone);
            email = (EditText) rootView.findViewById(R.id.email);
            comment = (EditText) rootView.findViewById(R.id.comments);

            return rootView;
        }
    }

}
