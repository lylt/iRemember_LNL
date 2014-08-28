package com.example.iremember;

import java.io.Serializable;

public class Record	implements Serializable {
	int id;
	public Record(int id, String tittle, String body, String time,
			String audioPath, String videoPath, String imagePath,
			String location) {
		super();
		this.id = id;
		this.tittle = tittle;
		this.body = body;
		Time = time;
		this.audioPath = audioPath;
		this.videoPath = videoPath;
		this.imagePath = imagePath;
		this.location = location;
	}
	private String tittle;
	private String body;
	private String Time;
	String audioPath,videoPath,imagePath,location;
	public String getLocation() {
		return location;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Record(String tittle, String body, String time, String audioPath,
			String videoPath, String imagePath) {
		super();
		this.tittle = tittle;
		this.body = body;
		Time = time;
		this.audioPath = audioPath;
		this.videoPath = videoPath;
		this.imagePath = imagePath;
	}
	public String getAudioPath() {
		return audioPath;
	}
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Record(){
		tittle="";
		body="";
		
	}
	public Record(String tittle, String body, String time, String audioPath,
			String videoPath, String imagePath, String location) {
		super();
		this.tittle = tittle;
		this.body = body;
		Time = time;
		this.audioPath = audioPath;
		this.videoPath = videoPath;
		this.imagePath = imagePath;
		this.location = location;
	}
	public Record(String t,String b){
		tittle=t;
		body=b;
	}
	public String getTittle() {
		return tittle;
	}
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	public Record(String tittle, String body, String time) {
		super();
		this.tittle = tittle;
		this.body = body;
		Time = time;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
}
