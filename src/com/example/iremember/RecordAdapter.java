package com.example.iremember;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAdapter extends ArrayAdapter {
	int resource;
	ArrayList<Record> arrRecord;
	TextView Tittle;
	TextView Time;
	Context context;
	ImageView imgView;
	Record record;

	public RecordAdapter(Context context, int resource) {
		super(context, resource);

	}

	public RecordAdapter(Context context, int resource, ArrayList<Record> arr) {
		super(context, resource,arr);
		this.context = context;
		this.resource = resource;
		this.arrRecord = arr;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View friendView = convertView;
		if (friendView == null) {
			friendView = new CustomRecord(getContext());

		}
		record = arrRecord.get(position);
		if (record != null) {
			Tittle = ((CustomRecord) friendView).Tittle;
			Time=((CustomRecord) friendView).Time;
			imgView=((CustomRecord) friendView).imgView;
			Tittle.setText(record.getTittle());
			Time.setText(record.getTime());
			Bitmap bit;
			if(record.getImagePath()!=null){
				bit = BitmapFactory.decodeFile(record.getImagePath());
				imgView.setImageBitmap(bit);
			}else{
				imgView.setImageResource(R.drawable.trav);
			}
			
			
		}
		return friendView;
	}

}
