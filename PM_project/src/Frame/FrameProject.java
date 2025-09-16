package Frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;





public class FrameProject extends JPanel{
	public FrameProject() {
		 JPanel bottomSet = new JPanel();
	        bottomSet.setBounds(0, 660, 600, 100);
	        bottomSet.setLayout(null);
	        bottomSet.setBackground(new Color(0xFFD700));

	        JButton btn = new JButton("학생관리");
	        btn.setBackground(new Color(0xA6A6A6));
	        btn.setSize(183, 87);
	        btn.setLocation(5, 0);
	        btn.setFont(new Font("굴림", Font.BOLD, 22));
	        bottomSet.add(btn);
	        
	        btn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                FrameBase.getInstance(new FrameTeamM());
	            }
	        });
	        
	        add(btn);
	        
	        
		    
		
	}
}
