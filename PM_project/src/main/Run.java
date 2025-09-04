package main;

import javax.swing.JFrame;

import frame.BasicFrame;
import frame.LoginFrame;

public class Run {
	public static void main(String[] args) {
		JFrame frame = BasicFrame.getInstance(new LoginFrame());
		frame.setVisible(true);
	}// main
}