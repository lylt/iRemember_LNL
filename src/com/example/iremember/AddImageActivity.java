package com.example.iremember;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AddImageActivity extends Activity {

	ImageView image;
	ImageButton btnChooseImage, btnTakePicture, btnBack;
	Button btnSave;
	static final int REQUEST_IMAGE_CAPTURE = 200;
	private static final int RESULT_LOAD_IMAGE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	String path;
	static File mediaFile;
	String mPath;
	static boolean isChoose = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.addimage);
		initComponent();
		setBtnTakePicture();
		setBtnChooseImage();
		setBtnSave();
		setBtnBack();
	}

	private void setBtnTakePicture() {
		btnTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				takePicture();
			}
		});
	}

	private void setBtnChooseImage() {
		btnChooseImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseImage();
			}
		});
	}

	private void setBtnSave() {
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				Intent main = new Intent();
				if(isChoose){
					main.putExtra(MainActivity.IMAGE_KEY, path);
				}else {
					main.putExtra(MainActivity.IMAGE_KEY, mPath);
				}
				setResult(Activity.RESULT_OK, main);
				finish();
			}
		});
	}

	private void initComponent() {
		btnBack = (ImageButton) findViewById(R.id.btnBACK);
		image = (ImageView) findViewById(R.id.imgImage);
		btnChooseImage = (ImageButton) findViewById(R.id.btnChooseImage);
		btnTakePicture = (ImageButton) findViewById(R.id.btnTakePicture);
		btnSave = (Button) findViewById(R.id.btnBack);
	}

	private void setBtnBack() {
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.anim_click));
				Intent mIntent = new Intent(getApplicationContext(),
						MainActivity.class);
				finish();
			}
		});
	}

	// using camera to take a picture
	public void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri outputFileUri = Uri.fromFile( getOutputImageFile(MEDIA_TYPE_IMAGE) );
		intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);
		path = getOutputImageFile(MEDIA_TYPE_IMAGE).getAbsolutePath();
		mPath = outputFileUri.getPath();
		startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
	}

	// pick an image from gallery.
	public void chooseImage() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inSampleSize = 4;
		    	
		    Bitmap bitmap = BitmapFactory.decodeFile( mPath, options );
		    image.setImageBitmap(bitmap);

		}

		else if (requestCode == RESULT_LOAD_IMAGE) {
			if (resultCode == RESULT_OK) {
				Uri _uri = data.getData();
				// User had pick an image.
				Cursor cursor = getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				// Link to the image
				String photoPath = cursor.getString(0);
				cursor.close();
				path = photoPath;
				Bitmap bit = BitmapFactory.decodeFile(photoPath);
				image.setImageBitmap(bit);
				Toast.makeText(this, "OKe", Toast.LENGTH_SHORT).show();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private static File getOutputImageFile(int type) {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		String videoName = "JPEG_" + timeStamp ;
		File mediaFileDir = new File(Environment.getExternalStorageDirectory() + "/IRemember3/Picture/");

		if (type == MEDIA_TYPE_IMAGE) {
			try {
				mediaFile = File
						.createTempFile(videoName, ".jpg", mediaFileDir);
				isChoose = false;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.addimage, container,
					false);
			return rootView;
		}
	}

}
