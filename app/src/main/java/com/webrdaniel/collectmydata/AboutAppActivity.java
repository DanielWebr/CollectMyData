package com.webrdaniel.collectmydata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

public class AboutAppActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
        TextView github = findViewById(R.id.textView5);
        Spanned Text = Html.fromHtml(getString(R.string.app_info1)+" <a href='https://github.com/DanielWebr/CollectMyData'>GitHub</a> "+getString(R.string.app_info2));
        github.setMovementMethod(LinkMovementMethod.getInstance());
        github.setText(Text);
        getSupportActionBar().setTitle(getString(R.string.about_app));
    }
}
