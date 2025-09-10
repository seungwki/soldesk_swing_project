package main;

import VO.Data;
import front_frame.ClassManager;
import front_frame.DefaultFrame;

public class Run {
	public static void main(String[] args) {
		Data data = new Data();
//		System.out.println(data.projectData.size());
		DefaultFrame.getInstance(new ClassManager());
		//		JFrame frame = BasicFrame.getInstance(new LoginFrame());
		//		frame.setVisible(true);
	}// main
}