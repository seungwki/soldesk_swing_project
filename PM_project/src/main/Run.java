package main;

import VO.Data;
import frame.LoginFrame;
import front_frame.ClassManager;
import front_frame.DefaultFrame;

public class Run {
	public static void main(String[] args) {
		Data data = new Data();
		//		System.out.println(data.projectData.size());
		//		DefaultFrame.getInstance(new ClassManager());//나중엔 로그인 프레임으로 바꿔야됨
		DefaultFrame.getInstance(new LoginFrame());
		//		frame.setVisible(true);
	}// main
}