package com.example.iremember;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class DisplaySelectedItem extends Activity {
	TextView tvTittle,tvBody,tvTime;
	ImageButton btnBack;
	Button btnPlayAudio,btnEdit;
	MediaPlayer m;
	String audioPath,videoPath,imagePath,time,location;
	VideoView vdvVideo;
	ImageView imageImage;
	boolean bVideoIsBeingTouched=false,isplaying=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.display_selected_item);
		initComponent();
		initData();
		back();
		setUpdate();
	}
	//get data of each record
	private void initData() {
		Intent i=getIntent();
		final String data[]=i.getStringArrayExtra("data");
		tvTittle.setText(data[0]);
		tvBody.setText(data[1]);
		tvTime.setText(data[2]);
		audioPath=data[3];
		videoPath=data[4];
		imagePath=data[5];
		location=data[6];
		if(audioPath!=null)
		playAudio();
		if(videoPath!=null)
		displayVideo();
		if(imagePath!=null){
			displayImage();
		}
	}
	private void initComponent() {
		tvTittle=(TextView) findViewById(R.id.tvTittle);
		tvBody=(TextView) findViewById(R.id.tvBody);
		tvTime=(TextView) findViewById(R.id.TVTime);
		btnPlayAudio=(Button) findViewById(R.id.btnPlayAudio);
		btnBack=(ImageButton) findViewById(R.id.btnBack);
		btnEdit=(Button) findViewById(R.id.btnEdit);
		vdvVideo= (VideoView) findViewById(R.id.vdvVideo);
		imageImage= (ImageView) findViewById(R.id.imageImage);
	}
	//back to first screen
public void back(){
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
				Intent intent= new Intent(getApplicationContext(),FirstScreen.class);
				startActivity(intent);
				
			}
		});
	}
// play audio
public void playAudio(){
	btnPlayAudio.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
			if (isplaying == false) {
				try {
					m = new MediaPlayer();
					m.setDataSource(audioPath);
					m.prepare();
					m.start();
					btnPlayAudio.setText("Stop Audio");
					isplaying = true;
				} catch (Exception e) {
				}
			} else {
				m.stop();
				isplaying = false;
				btnPlayAudio.setText("Play Audio");

			}
		}
	});
}
//load and play video
public void displayVideo(){
	int width = getWindowManager().getDefaultDisplay().getWidth();
	int height = getWindowManager().getDefaultDisplay().getHeight();
	vdvVideo.getLayoutParams().width = width;
	vdvVideo.getLayoutParams().height = height;
	vdvVideo.setOnTouchListener(new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(!bVideoIsBeingTouched){
				vdvVideo.setVideoPath(videoPath);
				vdvVideo.start();
				bVideoIsBeingTouched=true;
			}
			else{
				vdvVideo.stopPlayback();
				bVideoIsBeingTouched=false;
			}
			
			return false;
		}
	});
}
//load and display photo
public void displayImage(){
	Bitmap bit = BitmapFactory.decodeFile(imagePath);
	imageImage.setImageBitmap(bit);
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_selected_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	// update information of record
	public void setUpdate(){
		Intent i=getIntent();
		final int id=i.getIntExtra("id", -1);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
				Intent mIntent=new Intent(getApplicationContext(),MainActivity.class);
				mIntent.putExtra("id", id);
				String data[]={tvTittle.getText().toString(),tvBody.getText().toString(),
						tvTime.getText().toString(),audioPath,videoPath,imagePath};
				mIntent.putExtra("data",data );
				startActivity(mIntent);
			}
		});
	}
}
