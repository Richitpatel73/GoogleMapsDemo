package com.example.mapsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ContactMe extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_me);
        Intent receiveIntent = getIntent();
	}
}
