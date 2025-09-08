package frame;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EdOutput implements ActionListener {
	public final static int EDIT_OUTPUT = (int) 'e';
	public final static int DELETE_OUTPUT = (int) 'd';
	private int input;

	public EdOutput(int input) {
		this.input = input;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//수정
		if (input == 'e') {
			System.out.println("edit");
		}
		//삭제
		//		if (input == 'd') {
		//			System.out.println("delete");
		//			
		//		}
	}
}
