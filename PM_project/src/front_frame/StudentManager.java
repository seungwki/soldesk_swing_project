package front_frame;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import front_ui.TopBar;

public class StudentManager extends BasePage {
	public StudentManager() {
		
		super(new TopBar.OnMenuClick() {
			@Override
			public void onClass() {
				DefaultFrame.getInstance(new ClassManager());
			}

			@Override
			public void onStudent() {
				DefaultFrame.getInstance(new StudentManager());
			}

			@Override
			public void onTag() {
				DefaultFrame.getInstance(new TagManager());
			}
		});
		
		setScrollBarVisible(false);

		getTopBar().selectOnly("student");

		// ✅ BottomPanel에서 가져온 UI 추가
		addBottomUI(getContentPanel());

		// ✅ ClassTable 추가
		addClassTable(getContentPanel());

		refreshScroll();
	}

	// ----------------------------
	// BottomPanel에서 뽑아온 UI 코드
	// ----------------------------
	private void addBottomUI(JPanel parent) {
		parent.setLayout(null); // 절대 배치 사용

		JLabel lblCls = new JLabel("수업", SwingConstants.CENTER);
		lblCls.setBounds(10, 15, 60, 30);
		lblCls.setOpaque(true);
		lblCls.setBackground(Color.WHITE);
		lblCls.setForeground(Color.BLACK);
		parent.add(lblCls);

		JLabel lblOrder = new JLabel("차수", SwingConstants.CENTER);
		lblOrder.setBounds(215, 15, 60, 30);
		lblOrder.setOpaque(true);
		lblOrder.setBackground(Color.WHITE);
		lblOrder.setForeground(Color.BLACK);
		parent.add(lblOrder);

		JLabel lblTeam = new JLabel("조", SwingConstants.CENTER);
		lblTeam.setBounds(420, 15, 60, 30);
		lblTeam.setOpaque(true);
		lblTeam.setBackground(Color.WHITE);
		lblTeam.setForeground(Color.BLACK);
		parent.add(lblTeam);

		JComboBox<String> regionCombo = new JComboBox<>(new String[] { "수업 이름(호실)", "501호", "502호", "503호" });
		regionCombo.setBounds(75, 15, 133, 30);
		parent.add(regionCombo);

		JComboBox<String> orderCombo = new JComboBox<>(new String[] { "1차", "2차", "3차" });
		orderCombo.setBounds(280, 15, 133, 30);
		parent.add(orderCombo);

		JComboBox<String> teamCombo = new JComboBox<>(new String[] { "1조", "2조", "3조" });
		teamCombo.setBounds(485, 15, 133, 30);
		parent.add(teamCombo);

		JButton btnPlus = new JButton(new ImageIcon("btnPlusImage1-1.png"));
		btnPlus.setBounds(621, 10, 70, 40);
		parent.add(btnPlus);

		JButton btnTeamPlus = new JButton(new ImageIcon("btnTeamPlus1Image.png"));
		btnTeamPlus.setBounds(698, 10, 70, 40);
		parent.add(btnTeamPlus);

		// 첫번째 이미지 버튼 클릭 시 팝업화면 실행코드
		btnPlus.addActionListener(e -> {
		    JDialog dialog = new JDialog(
		        (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
		        " ",
		        true
		    );
		    dialog.setSize(400, 400);
		    dialog.setLocationRelativeTo(this);

		    // 원래 내용 패널
		    StudentPopup popupPanel = new StudentPopup();

		    // ✅ 전체 패널을 스크롤 가능하게 감쌈
		    JScrollPane scrollPane = new JScrollPane(popupPanel);
		    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 👈 가로 스크롤 제거

		    dialog.add(scrollPane);
		    dialog.setVisible(true);
		});
		// 두 번째 버튼 클릭 시 팝업 띄우기
		btnTeamPlus.addActionListener(e -> {
		    JDialog dialog = new JDialog(
		        (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
		        " ",
		        true
		    );
		    dialog.setSize(400, 400);
		    dialog.setLocationRelativeTo(this);

		    // ✅ 팀 정보를 표시할 패널 (StudentPopup처럼 별도 클래스 추천)
		    TeamPopup teamPanel = new TeamPopup();

		    JScrollPane scrollPane = new JScrollPane(teamPanel);
		    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		    dialog.add(scrollPane);
		    dialog.setVisible(true);
		});



		// 이벤트 처리
		regionCombo.addActionListener(e -> System.out.println("선택한 항목: " + regionCombo.getSelectedItem()));
		orderCombo.addActionListener(e -> System.out.println("차수 선택: " + orderCombo.getSelectedItem()));
		teamCombo.addActionListener(e -> System.out.println("조 선택: " + teamCombo.getSelectedItem()));
	}

	private void addClassTable(JPanel parent) {
		StuTableClass tablePanel = new StuTableClass(760, 670); // 크기 전달
		tablePanel.setBounds(10, 60, 760, 670);
		parent.add(tablePanel);
	}

}
