package front_frame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StuTableClass extends JPanel {

	private JTable table;
	private DefaultTableModel model;

	public StuTableClass(int panelWidth, int panelHeight) {
		setLayout(null);
		setBounds(0, 0, panelWidth, panelHeight);

		// 1. 흰색 배경 패널
		JPanel whitePanel = new JPanel();
		whitePanel.setBackground(Color.WHITE);
		whitePanel.setBounds(0, 0, panelWidth, panelHeight);
		whitePanel.setLayout(null);
		add(whitePanel);

		// 2. 테이블 생성
		String[] columns = { "번호", "이름", "TEL", "메모" };
		Object[][] data = { { "1", "김자바", "010-1111-2222", "우행" }, { "2", "박자바", "010-2222-3333", "우행" },
				{ "3", "이자바", "010-3333-4444", "우행" }, { "4", "강자바", "010-4444-5555", "우행" },
				{ "5", "최자바", "010-5555-6666", "우행" }, { "6", "도자바", "010-6666-7777", "우행" },
				{ "7", "예자바", "010-7777-8888", "우행" }, { "8", "제갈자바", "010-8888-9999", "우행" },
				{ "9", "김자바", "010-1111-2222", "우행" }, { "10", "박자바", "010-2222-3333", "우행" },
				{ "11", "이자바", "010-3333-4444", "우행" }, { "12", "강자바", "010-4444-5555", "우행" },
				{ "13", "최자바", "010-5555-6666", "우행" }, { "14", "도자바", "010-6666-7777", "우행" },
				{ "15", "예자바", "010-7777-8888", "우행" }, { "16", "제갈자바", "010-8888-9999", "우행" } };

		model = new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(25);
		table.setShowGrid(true);
		table.setGridColor(Color.GRAY);
		table.setBorder(new LineBorder(Color.GRAY));

		// 가운데 정렬
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 10, panelWidth - 20, panelHeight - 20);

		// 아래 두 줄을 숨기기 위해 조정
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

		// 가로 스크롤은 완전히 숨김
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setWheelScrollingEnabled(true); // 기본 활성화 상태

		// 강제 휠 처리 필요 없다면 생략해도 됨
		scrollPane.addMouseWheelListener(e -> {
			JScrollBar bar = scrollPane.getVerticalScrollBar();
			int rotation = e.getWheelRotation();
			bar.setValue(bar.getValue() + rotation * bar.getUnitIncrement(rotation > 0 ? 1 : -1));
		});

		whitePanel.add(scrollPane);
	}
}
