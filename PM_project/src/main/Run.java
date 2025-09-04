package main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import frame.BasicFrame;
import frame.LoginFrame;

public class Run {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {// 작업시간에 따른 충돌을 막기 위해 멀티스레드 환경으로 만드는 어쩌고
			public void run() {
				try {
					JFrame frame = BasicFrame.getInstance(new LoginFrame());

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // tryCatch
			}
		});// invokeLater
	}// main
}