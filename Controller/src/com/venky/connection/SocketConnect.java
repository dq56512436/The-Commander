package com.venky.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.venky.controller.MainActivity;

public class SocketConnect extends AsyncTask<Void, Void, Void> {

	private String ip;
	private Context context;
	
	public SocketConnect(Context context, String ip) {
		this.context = context;
		this.ip = ip;
	}
	
	@SuppressWarnings("unused")
	@Override
	protected Void doInBackground(Void... params) {
		boolean connected = false;
		System.out.println(ip);
		while (!false) {
			try {
				@SuppressWarnings("resource")
				Socket s = new Socket(ip, 1212);
				connected = true;
				
				MainActivity.DO = new DataOutputStream(s.getOutputStream());

				MainActivity.DO.write(("x" + MainActivity.xres).getBytes());
				MainActivity.DO.write(("y" + MainActivity.yres).getBytes());
			
			} catch (IOException e) {
				System.out.println("waiting for the server to start");
				e.printStackTrace();
			}
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Toast.makeText(context, "Connection Established !!!", Toast.LENGTH_SHORT).show();
	}
}
