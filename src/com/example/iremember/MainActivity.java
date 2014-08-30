package com.example.iremember;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String FIRST_VALUE_ID = "first_value_id";
	public static final String IMAGE_KEY = "second_value_id";
	public static final String AUDIO_KEY = "third_value_id";

	private static final int GET_VALUES_VIDEO_ID = 1;
	private static final int GET_VALUES_IMAGE_ID = 2;
	public static final int GET_VALUES_AUDIO_ID = 3;
	EditText edtTittle, edtImageName, edtBody;
	ImageView btnAddVideo, btnAddAudio, btnAddLocation, btnAddPhoto, btnBack;
	TextView tvTime, tvLatitude, btnCreate;
	private MySQLiteOpenHelper dataHelper;
	private Cursor cusor;
	private SimpleAdapter adapter;
	LocationManager locationManager;
	Location location;
	String videoPath, imagePath, audioPath;
	GPSTracker gps;
	double latitude, longitude;
	String data[] = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_activity);

		createDirectory();
		initComponent();
		addVideo();
		addAudio();
		addPhoto();
		getLocation();
		Intent i = getIntent();
		final int id = i.getIntExtra("id", -1);
		if(id!=-1){
			update(id);
		}else CreateNew();
		back();
	}
// create folder contains files
	private void createDirectory() {
		File newVideoFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/IRemember3/Video");
		if (newVideoFolder.exists() == false) {
			if (newVideoFolder.mkdirs())
				Toast.makeText(getApplicationContext(), "Successful",
						Toast.LENGTH_SHORT).show();
		}
		File newAudioFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/IRemember3/Audio");
		if (newAudioFolder.exists() == false) {
			if (newAudioFolder.mkdirs())
				Toast.makeText(getApplicationContext(), "Successful",
						Toast.LENGTH_SHORT).show();
		}
		File newPictureFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/IRemember3/Picture");
		if (newPictureFolder.exists() == false) {
			if (newPictureFolder.mkdirs())
				Toast.makeText(getApplicationContext(), "Successful",
						Toast.LENGTH_SHORT).show();
		}
	}

	private void initComponent() {
		dataHelper = new MySQLiteOpenHelper(MainActivity.this);
		edtTittle = (EditText) findViewById(R.id.edtTittle);
		edtTittle.setText("");
		edtBody = (EditText) findViewById(R.id.edtBody);
		edtBody.setText("");
		btnAddAudio = (ImageView) findViewById(R.id.btnAddAudio);
		btnAddVideo = (ImageView) findViewById(R.id.btnAddVideo);
		btnAddPhoto = (ImageView) findViewById(R.id.btnAddPhoto);
		btnCreate = (TextView) findViewById(R.id.btnCreate);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		tvTime = (TextView) findViewById(R.id.tvTime);
		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnAddLocation = (ImageView) findViewById(R.id.btnGetLocation);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		setTimeText();
	}
// back to first Screen
	public void back() {
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				Intent intent = new Intent(getApplicationContext(),
						FirstScreen.class);
				startActivity(intent);

			}
		});
	}
// get time and display system's time
	public void setTimeText() {
		Calendar c = Calendar.getInstance();
		String time = c.get(Calendar.HOUR) + ": " + c.get(Calendar.MINUTE)
				+ "   " + c.get(Calendar.DATE) + " - " + c.get(Calendar.MONTH)
				+ " - " + c.get(Calendar.YEAR);
		tvTime.setText(time);
	}
//update 
	public void update(int id) {
		Intent i = getIntent();
			btnCreate.setText("Update");
			data = i.getStringArrayExtra("data");
			tvTime.setText(data[2]);
			edtTittle.setText(data[0]);
			edtBody.setText(data[1]);
			final int updateId=id;
			btnCreate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//v.startAnimation(AnimationUtils.loadAnimation(
					//		getApplicationContext(), R.anim.anim_click));
					String uaudioPath, uvideoPath, uimagePath, utittle, ubody;
					uaudioPath = data[3];
					uvideoPath = data[4];
					uimagePath = data[5];
					Intent i = getIntent();
					if(audioPath!=null){
						uaudioPath=audioPath;
					}
					if(videoPath!=null){
						uvideoPath=videoPath;
					}
					if(imagePath!=null){
						uimagePath=imagePath;
					}
					utittle = edtTittle.getText().toString();
					ubody = edtBody.getText().toString();
					String utime = tvTime.getText().toString();
					String ulocation = "Latitude: " + latitude + " longitude: "
							+ longitude;
					if (!utittle.equals("")) {
						Record r = new Record(utittle,ubody, utime, uaudioPath,
								uvideoPath, uimagePath, ulocation);
						dataHelper.UPDATE_Record(r, updateId);
						dataHelper.close();
						Toast.makeText(MainActivity.this,
								"updated successfuly", 30000).show();
						Intent mIntent = new Intent(getApplicationContext(),
								FirstScreen.class);
						startActivity(mIntent);
					} else {
						AlertDialog alertdialog = new AlertDialog.Builder(
								MainActivity.this).create();
						alertdialog.setTitle("iRemember");
						alertdialog.setMessage("tittle should be filled");
						alertdialog.setButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						alertdialog.show();
					}

				}
			});
		
	}
// create new record
	public void CreateNew() {
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				String tittle = "";
				String body = "";
				tittle = edtTittle.getText().toString();
				body = edtBody.getText().toString();
				String time = tvTime.getText().toString();
				String location = "Latitude: " + latitude + " longitude: "
						+ longitude;
				if (!tittle.equals("")) {
					Record r = new Record(tittle, body, time, audioPath,
							videoPath, imagePath, location);
					dataHelper.INSERT_RECORD(r);
					dataHelper.close();
					Toast.makeText(MainActivity.this, "created successfuly",
							30000).show();
					Intent mIntent = new Intent(getApplicationContext(),
							FirstScreen.class);
					startActivity(mIntent);
				} else {
					AlertDialog alertdialog = new AlertDialog.Builder(
							MainActivity.this).create();
					alertdialog.setTitle("iRemember");
					alertdialog.setMessage("tittle should be filled");
					alertdialog.setButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					alertdialog.show();
				}

			}
		});

	}
// add video
	public void addVideo() {
		btnAddVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent addVideoIntent = new Intent(getApplicationContext(),
						AddVideoActivity.class);
				startActivityForResult(addVideoIntent, GET_VALUES_VIDEO_ID);
			}
		});
	}
// add audio
	public void addAudio() {
		btnAddAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				Intent addAudioIntent = new Intent(getApplicationContext(),
						AddAudioActivity.class);
				startActivityForResult(addAudioIntent, GET_VALUES_AUDIO_ID);
			}
		});
	}
// add photos
	public void addPhoto() {
		btnAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent addPhotoIntent = new Intent(getApplicationContext(),
						AddImageActivity.class);
				startActivityForResult(addPhotoIntent, GET_VALUES_IMAGE_ID);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_VALUES_VIDEO_ID: {
			if (Activity.RESULT_OK == resultCode) {
				videoPath = data.getStringExtra(FIRST_VALUE_ID);
			}
			break;
		}
		case GET_VALUES_IMAGE_ID: {
			if (Activity.RESULT_OK == resultCode) {
				imagePath = data.getStringExtra(IMAGE_KEY);
			}
			break;
		}

		case GET_VALUES_AUDIO_ID: {
			if (Activity.RESULT_OK == resultCode) {
				audioPath = data.getStringExtra(AUDIO_KEY);
			}
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
// get longitude and latitude of current location
	public void getLocation() {
		btnAddLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gps = new GPSTracker(MainActivity.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					String mapPath = "http://maps.google.com/maps?q=+"+latitude+","+longitude+"&t=m&z=7";
					Intent mIntent=new Intent(getApplicationContext(),Activity_Location.class);
					mIntent.putExtra("mapPath",mapPath);
					startActivity(mIntent);
				} else {
					gps.showSettingsAlert();
				}

			}
		});


	}
// get name of current location
	public void getAddress() {
		try {
			Geocoder geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
			double x = latitude;
			double y = longitude;
			List<Address> addresses = geocoder.getFromLocation(x, y, 1);
			StringBuilder str = new StringBuilder();
			if (geocoder.isPresent()) {
				Toast.makeText(getApplicationContext(), "geocoder present",
						Toast.LENGTH_SHORT).show();
				Address returnAddress = addresses.get(0);

				String localityString = returnAddress.getLocality();
				String city = returnAddress.getCountryName();
				String region_code = returnAddress.getCountryCode();
				String zipcode = returnAddress.getPostalCode();

				str.append(localityString + "");
				str.append(city + "" + region_code + "");
				str.append(zipcode + "");
				Toast.makeText(getApplicationContext(), city,
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(), "geocoder not present",
						Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			Log.e("tag", e.getMessage());
		}
	}
}
