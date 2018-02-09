package com.webrdaniel.collectmydata.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.webrdaniel.collectmydata.R;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        TextView github = findViewById(R.id.textView5);
        Spanned Text = Html.fromHtml(getString(R.string.app_info," <a href='https://github.com/DanielWebr/CollectMyData'>GitHub</a> "));
        github.setMovementMethod(LinkMovementMethod.getInstance());
        github.setText(Text);
        if(getSupportActionBar() != null)getSupportActionBar().setTitle(getString(R.string.about_app));
    }
}
