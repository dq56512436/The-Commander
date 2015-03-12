package com.venky.connection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.controller.R;
import com.venky.controller.MainActivity;

public class ConnectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		final EditText ip = (EditText) findViewById(R.id.editText1);
		Button connect = (Button) findViewById(R.id.mute);

		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SocketConnect(ConnectionActivity.this.getBaseContext(), ip.getText()
						.toString()).execute();
				
				Intent intent = new Intent(ConnectionActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
