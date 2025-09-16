package main;

import VO.Data;
import front_frame.DefaultFrame;
import front_frame.FrameBegin;

public class TeachCloudMain {
	public static void main(String[] args) {
		Data data = new Data();
		DefaultFrame.getInstance(new FrameBegin());
	}
}
