package com.example.iremember;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

public class AddVideoActivity extends Activity {

	private static final int VIDEO_CAPTURE = 101;
	static final int REQUEST_IMAGE_CAPTURE = 200;
	private Uri fileUri;
	static File mediaFile;
	VideoView v;
	ImageButton btnBackToMain;
	Button btnSave, btnCaptureVideo, btnChooseVideo;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private static final int RESULT_LOAD_IMAGE = 100;
	protected static final int RESULT_LOAD_VIDEO = 50;

	private boolean bVideoIsBeingTouched = false;
	String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.add_video);
		initComponent();
		setOnTouchVideo();
		setBtnBack();
		setBtnSave();
		setBtnCaptureVideo();
		setBtnChooseVideo();
	}

	public void initComponent() {
		v = (VideoView) findViewById(R.id.vdvVideo);
		btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);
		btnSave = (Button) findViewById(R.id.btnBack);
		btnCaptureVideo = (Button) findViewById(R.id.btnCaptureVideo);
		btnChooseVideo = (Button) findViewById(R.id.btnChooseVideo);
	}

	private void setOnTouchVideo() {

		v.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (!bVideoIsBeingTouched) {
					playVideo(path);
					bVideoIsBeingTouched = true;
				} else {
					pauseVideo();
					bVideoIsBeingTouched = false;
				}

				return false;
			}
		});
	}

	// back to MainActivity
	private void setBtnBack() {

		btnBackToMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				finish();

			}
		});
	}

	// click on button to save data
	private void setBtnSave() {

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				Intent main = new Intent();
				main.putExtra(MainActivity.FIRST_VALUE_ID, path);
				setResult(Activity.RESULT_OK, main);
				finish();
			}
		});
	}

	private void setBtnCaptureVideo() {
		btnCaptureVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				// TODO Auto-generated method stub
				captureVideo();
			}
		});
	}

	// set on click for btnChooseVideo
	private void setBtnChooseVideo() {
		btnChooseVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				selectVideo();
			}
		});
	}

	// capture video by using camera
	public void captureVideo() {
		v.setVisibility(View.VISIBLE);
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, VIDEO_CAPTURE);
		path = fileUri.getPath();
	}

	// select video from external file
	public void selectVideo() {
		v.setVisibility(View.VISIBLE);
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_VIDEO);
	}

	// play video
	public void playVideo(String path) {

		v.setVideoPath(path);
		v.start();
	}

	// pause video
	public void pauseVideo() {
		v.pause();
	}

	// override onActivityResult
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VIDEO_CAPTURE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Video saved to:\n" + data.getData(),
						Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Video recording cancelled.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed to record video",
						Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == RESULT_LOAD_VIDEO) {
			if (resultCode == RESULT_OK) {
				Uri _uri = data.getData();
				// User had pick an image.
				Cursor cursor = getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Video.VideoColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				// Link to the image
				String photoPath = cursor.getString(0);
				cursor.close();
				path = photoPath;

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Video recording cancelled.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed to record video",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	// create folder contains media files and get path of these file
	private static File getOutputMediaFile(int type) {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		String videoName = timeStamp + "_";
		File mediaFileDir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/IRemember3/Video/");

		if (type == MEDIA_TYPE_VIDEO) {
			try {
				mediaFile = File
						.createTempFile(videoName, ".mp4", mediaFileDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// mediaFile = new File(mediaStorageDir.getPath());
		} else {
			return null;
		}
		return mediaFile;

	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

}