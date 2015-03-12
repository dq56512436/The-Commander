package com.venky.media;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.example.controller.R;
import com.venky.controller.MainActivity;

public class OptionsActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options2);

		final ImageButton pause = (ImageButton) findViewById(R.id.imagepause);
		ImageButton volumeup = (ImageButton) findViewById(R.id.imagevolumeup);
		ImageButton volumedown = (ImageButton) findViewById(R.id.imagevolumedown);
		ImageButton next = (ImageButton) findViewById(R.id.imagenext);
		ImageButton previous = (ImageButton) findViewById(R.id.imageprevious);
		ImageButton mute = (ImageButton) findViewById(R.id.imagemute);


		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.DO != null) {
					try {
						MainActivity.DO.write("mediapause".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		volumeup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.DO != null) {
					try {
						MainActivity.DO.write("volumeup".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		volumedown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.DO != null) {
					try {
						MainActivity.DO.write("volumedown".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.DO != null) {
					try {
						MainActivity.DO.write("medianext".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.DO != null) {
					try {
						MainActivity.DO.write("mediaprevious".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		mute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MainActivity.DO != null) {
					try {
						MainActivity.DO.write("volumemute".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
