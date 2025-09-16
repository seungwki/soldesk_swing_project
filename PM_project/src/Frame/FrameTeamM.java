package Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Modal.makeStudent;
import Modal.makeTeam;
import VO.Data;
import VO.Project;
import VO.Student;
import VO.Team;

public class FrameTeamM extends JPanel {
	private final Data data; // 전체 데이터
	private Project pj; // 현재 선택된 프로젝트

	// 학생 테이블 모델
	private final DefaultTableModel studentModel = new DefaultTableModel(new Object[] { "번호", "이름", "전화", "메모" }, 0) {

		@Override
		public boolean isCellEditable(int r, int c) {
			return c == 1; // "이름" 컬럼만 버튼처럼 클릭 가능
		}
	};

	public FrameTeamM() {
		this.data = new Data();

		List<Project> projects = data.getProjects();
		if (projects.isEmpty())
			throw new IllegalStateException("데이터가 비어있습니다.");
		this.pj = projects.get(0); // 기본 선택

		// ==== 기본 UI ====
		setPreferredSize(new Dimension(800, 600));
		setBackground(new Color(255, 241, 54));
		setLayout(new BorderLayout());

		// 상단 컨트롤
		JComboBox<Project> regionCombo = new JComboBox<>();
		for (Project p : projects)
			regionCombo.addItem(p);
		regionCombo.setSelectedItem(pj);

		JComboBox<Integer> orderCombo = new JComboBox<>();
		JComboBox<String> teamCombo = new JComboBox<>();

		JButton btnPlus = new JButton("학생 추가");
		JButton btnTeamPlus = new JButton("팀 추가");

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controls.add(new JLabel("수업 :"));
		controls.add(regionCombo);
		controls.add(new JLabel("차수 :"));
		controls.add(orderCombo);
		controls.add(new JLabel("조 :"));
		controls.add(teamCombo);
		controls.add(btnPlus);
		controls.add(btnTeamPlus);
		add(controls, BorderLayout.NORTH);

		// 학생 테이블
		final JTable studentTable = new JTable(studentModel);
		studentTable.setFillsViewportHeight(true);
		studentTable.setRowHeight(24);
		studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentTable.getTableHeader().setReorderingAllowed(false);
		studentTable.getTableHeader().setResizingAllowed(false);
		studentTable.getColumn("이름").setCellRenderer(new ButtonRenderer());
		studentTable.getColumn("이름").setCellEditor(new ButtonEditor(new JCheckBox(), studentTable, studentModel));

		add(new JScrollPane(studentTable), BorderLayout.CENTER);

		// ==== 리스너 (각 콤보당 딱 1개) ====
		regionCombo.addActionListener(e -> {
			Project sel = (Project) regionCombo.getSelectedItem();
			if (sel == null)
				return;
			pj = sel;
			fillDegreesFrom(pj, orderCombo);
			fillTeamsFrom(pj, orderCombo, teamCombo);
			renderStudentsTable(pj, orderCombo, teamCombo);
		});

		orderCombo.addActionListener(e -> {
			fillTeamsFrom(pj, orderCombo, teamCombo);
			renderStudentsTable(pj, orderCombo, teamCombo);
		});

		teamCombo.addActionListener(e -> renderStudentsTable(pj, orderCombo, teamCombo));

		// ==== 버튼 ====
		// 학생추가
		btnPlus.addActionListener(e -> {
			Project current = (Project) regionCombo.getSelectedItem();
			Integer selectedDeg = (Integer) orderCombo.getSelectedItem();
			String selectedTeamName = (String) teamCombo.getSelectedItem();

			JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "학생 추가", Dialog.ModalityType.APPLICATION_MODAL);
			dlg.setLayout(new BorderLayout());

			makeStudent panel = new makeStudent(data.getProjects(), current, selectedDeg, selectedTeamName, null, result -> {

				// === 1) 미배정으로 저장 (팀/차수 없이 추가 체크) ===
				boolean noTeam = (result.degree == null) || (result.teamName == null) || result.teamName.isBlank();

				if (noTeam) {
					// 팀/차수 없이 추가 → 프로젝트 '미배정'으로만 저장
					result.project.addUnassigned(result.student);

					// 현재 화면은 특정 팀 테이블이므로, 테이블 내용은 그대로일 수 있음
					if (result.project == pj) {
						// 콤보/테이블 강제 변경 없이 화면만 안정적으로 갱신하고 싶다면 필요 시 호출
						renderStudentsTable(pj, orderCombo, teamCombo);
					}

					dlg.dispose();
					JOptionPane.showMessageDialog(this, "학생이 '미배정'으로 추가되었습니다.");
					return;
				}

				// === 2) 팀에 배정해서 저장 ===
				int degree = result.degree; // 0차 금지: makeStudent에서 1차 이상만 노출/선택되므로 신뢰
				String teamNm = result.teamName.trim();

				// 팀 찾거나 생성
				Team target = null;
				for (Team t : result.project.getTeams()) {
					if (t.getDegree() == degree && t.getTName().equals(teamNm)) {
						target = t;
						break;
					}
				}
				if (target == null) {
					target = new Team(teamNm, degree);
					result.project.addTeam(target);
				}
				target.addMember(result.student);

				// '미배정'에 남아있을 수 있으니 제거
				result.project.removeUnassigned(result.student);

				// 같은 프로젝트를 보고 있으면 UI 갱신
				if (result.project == pj) {
					// 팀/차수 보장(단, 0차/POOL은 쓰지 않으므로 그대로 사용 가능)
					ensureDegreeAndTeamShown(pj, orderCombo, teamCombo, degree, teamNm);
					renderStudentsTable(pj, orderCombo, teamCombo);
				}

				dlg.dispose();
				JOptionPane.showMessageDialog(this, "학생이 팀에 추가되었습니다: " + teamNm + " (" + degree + "차)");
			});

			dlg.add(panel, BorderLayout.CENTER);
			dlg.pack();
			dlg.setLocationRelativeTo(this);
			dlg.setVisible(true);
		});

		// 팀 추가
		btnTeamPlus.addActionListener(e -> {
			Project current = (Project) regionCombo.getSelectedItem();
			Integer selectedDeg = (Integer) orderCombo.getSelectedItem();
			if (current == null) {
				JOptionPane.showMessageDialog(this, "수업을 먼저 선택하세요.");
				return;
			}

			JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "팀 추가", Dialog.ModalityType.APPLICATION_MODAL);
			dlg.setLayout(new BorderLayout());

			makeTeam panel = new makeTeam(data.getProjects(), current, selectedDeg, result -> {
				// 1) 팀 찾기 또는 생성
				Team target = null;
				for (Team t : result.project.getTeams()) {
					if (t.getTName().equals(result.teamName) && t.getDegree() == result.degree) {
						target = t;
						break;
					}
				}
				if (target == null) {
					target = new Team(result.teamName, result.degree);
					result.project.addTeam(target);
				}

				// 2) 멤버 추가(중복 방지) + 미배정에서 제거
				if (result.members != null) {
					for (Student s : result.members) {
						boolean exists = false;
						for (Student m : target.getMembers()) {
							if (m.getsName().equals(s.getsName()) && m.getsNum().equals(s.getsNum())) {
								exists = true;
								break;
							}
						}
						if (!exists) {
							target.addMember(s);
						}
						// ★ 팀에 배정했으니 '미배정'에서 제거 (POOL에 남지 않도록)
						result.project.removeUnassigned(s);
					}
				}

				// 3) UI 갱신
				if (result.project == pj) {
					ensureDegreeAndTeamShown(pj, orderCombo, teamCombo, result.degree, result.teamName);
					renderStudentsTable(pj, orderCombo, teamCombo);
				}

				dlg.dispose();
				JOptionPane.showMessageDialog(this, "팀 저장 완료: " + result.teamName + " (" + result.degree + "차)");
			});

			dlg.add(panel, BorderLayout.CENTER);
			dlg.pack();
			dlg.setLocationRelativeTo(this);
			dlg.setVisible(true);
		});
		// ==== 초기 렌더 ====
		fillDegreesFrom(pj, orderCombo);
		fillTeamsFrom(pj, orderCombo, teamCombo);
		renderStudentsTable(pj, orderCombo, teamCombo);
	}

	/* 차수 콤보 채우기(정렬 보장: TreeSet) */
	private void fillDegreesFrom(Project pj, JComboBox<Integer> sldg) {
		sldg.removeAllItems();
		java.util.Set<Integer> set = new java.util.TreeSet<>();
		for (Team t : pj.getTeams()) {
			int deg = t.getDegree();
			if (deg >= 1)
				set.add(deg); // ★ 0차 숨김
		}
		for (Integer d : set)
			sldg.addItem(d);
		if (sldg.getItemCount() > 0)
			sldg.setSelectedIndex(0);
	}

	/* 팀 콤보 채우기(선택된 차수 기준) */
	private void fillTeamsFrom(Project pj, JComboBox<Integer> sldg, JComboBox<String> sltm) {
		sltm.removeAllItems();
		Integer d = (Integer) sldg.getSelectedItem();
		if (d == null)
			return;

		// 팀명 수집 후 정렬(Optional)
		List<String> names = new ArrayList<>();
		for (Team t : pj.getTeams())
			if (t.getDegree() == d)
				names.add(t.getTName());
		names.sort(String::compareTo);
		for (String name : names)
			sltm.addItem(name);

		if (sltm.getItemCount() > 0)
			sltm.setSelectedIndex(0);
	}

	/* 학생 테이블 렌더링 */
	private void renderStudentsTable(Project pj, JComboBox<Integer> sldg, JComboBox<String> sltm) {
		studentModel.setRowCount(0);

		Integer selectedDeg = (Integer) sldg.getSelectedItem();
		String selectedTeamName = (String) sltm.getSelectedItem();
		if (pj == null || selectedDeg == null || selectedTeamName == null)
			return;

		for (Team t : pj.getTeams()) {
			if (t.getDegree() == selectedDeg && selectedTeamName.equals(t.getTName())) {
				List<Student> members = t.getMembers();
				for (int i = 0; i < members.size(); i++) {
					Student s = members.get(i);
					int order = i + 1; // 1부터 시작
					String memo = (s.getmemo() == null) ? "" : s.getmemo();
					studentModel.addRow(new Object[] { order, s.getsName(), s.getsNum(), memo });
				}
				break;
			}
		}
	}

	/* 차수/팀 콤보에 특정 값이 보이도록 보장 */
	private void ensureDegreeAndTeamShown(Project pj, JComboBox<Integer> sldg, JComboBox<String> sltm, int degree, String teamName) {
		// 차수 보장
		boolean hasDeg = false;
		for (int i = 0; i < sldg.getItemCount(); i++) {
			Integer d = sldg.getItemAt(i);
			if (d != null && d == degree) {
				hasDeg = true;
				break;
			}
		}
		if (!hasDeg)
			sldg.addItem(degree);
		sldg.setSelectedItem(degree);

		// 팀 보장
		boolean hasTeam = false;
		for (int i = 0; i < sltm.getItemCount(); i++) {
			String tn = sltm.getItemAt(i);
			if (teamName.equals(tn)) {
				hasTeam = true;
				break;
			}
		}
		if (!hasTeam)
			sltm.addItem(teamName);
		sltm.setSelectedItem(teamName);
	}

	/* 이름 컬럼 버튼 렌더러 */
	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText(value == null ? "" : value.toString());
			return this;
		}
	}

	/* 이름 컬럼 버튼 에디터(클릭 시 상세 다이얼로그) */
	class ButtonEditor extends DefaultCellEditor {
		private static final int COL_NO = 0;
		private static final int COL_NAME = 1; // 버튼
		private static final int COL_PHONE = 2;
		private static final int COL_MEMO = 3;

		private final JButton button = new JButton();
		private boolean clicked;
		private int viewRow;

		private final JTable table;
		private final DefaultTableModel model;

		public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel model) {
			super(checkBox);
			this.table = table;
			this.model = model;

			button.setOpaque(true);
			button.addActionListener(e -> {
				if (!clicked)
					return;

				int modelRow = table.convertRowIndexToModel(viewRow);
				String name = String.valueOf(model.getValueAt(modelRow, COL_NAME));
				String phone = String.valueOf(model.getValueAt(modelRow, COL_PHONE));
				String memo0 = String.valueOf(model.getValueAt(modelRow, COL_MEMO) == null ? "" : model.getValueAt(modelRow, COL_MEMO));

				// 학생 상세 다이얼로그
				JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(table), "학생 상세페이지", Dialog.ModalityType.APPLICATION_MODAL);
				dlg.setLayout(new BorderLayout(10, 10));

				// 소속 테이블: 다이얼로그 지역 모델/테이블 (전역 공유 금지)
				DefaultTableModel localBelongModel = new DefaultTableModel(new Object[] { "조, 차수" }, 0) {
					@Override
					public boolean isCellEditable(int r, int c) {
						return false;
					}
				};
				JTable localBelongTable = new JTable(localBelongModel);
				localBelongTable.setFillsViewportHeight(true);
				localBelongTable.setRowHeight(22);
				localBelongTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				localBelongTable.getTableHeader().setReorderingAllowed(false);
				localBelongTable.getTableHeader().setResizingAllowed(false);

				// 이 학생의 모든 소속(팀명 + 차수) 수집 & 채우기
				localBelongModel.setRowCount(0);
				for (Team tt : pj.getTeams()) {
					for (Student ss : tt.getMembers()) {
						if (name.equals(ss.getsName()) && phone.equals(ss.getsNum())) {
							localBelongModel.addRow(new Object[] { tt.getTName() + " " + tt.getDegree() + "차" });
						}
					}
				}

				// 상단 정보
				JPanel info = new JPanel(new GridLayout(0, 1, 4, 4));
				info.add(new JLabel("이름 : " + name));
				info.add(new JLabel("전화 : " + phone));
				info.add(new JLabel("태그 : ")); // TODO: 태그 기능 추후 연결
				info.add(new JLabel("조 :"));
				dlg.add(info, BorderLayout.NORTH);
				dlg.add(new JScrollPane(localBelongTable), BorderLayout.WEST);

				// 더블클릭 시 이동 (예시: 실제 페이지 전환 로직으로 교체)
				localBelongTable.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent e) {
						if (e.getClickCount() == 2) {
							int row = localBelongTable.rowAtPoint(e.getPoint());
							if (row >= 0) {
								String cell = String.valueOf(localBelongModel.getValueAt(row, 0)); // "하하하 2차"
								int sp = cell.lastIndexOf(' ');
								String teamNm = (sp > 0) ? cell.substring(0, sp).trim() : cell.trim();
								int degVal = -1;
								if (sp > 0) {
									String degTxt = cell.substring(sp + 1).trim(); // "2차"
									try {
										degVal = Integer.parseInt(degTxt.replace("차", "").trim());
									} catch (Exception ignore) {
									}
								}
								Team target = null;
								for (Team t : pj.getTeams()) {
									if (t.getTName().equals(teamNm) && (degVal < 0 || t.getDegree() == degVal)) {
										target = t;
										break;
									}
								}
								if (target != null) {
									JOptionPane.showMessageDialog(dlg, "이동: " + teamNm + " (" + (degVal < 0 ? "?" : degVal) + "차)");
									// TODO: 실제 화면 전환 로직 연결
								} else {
									JOptionPane.showMessageDialog(dlg, "팀을 찾을 수 없습니다: " + cell);
								}
							}
						}
					}
				});

				// 메모 편집
				JTextArea memoArea = new JTextArea(8, 40);
				memoArea.setText(memo0 == null ? "" : memo0);
				dlg.add(new JScrollPane(memoArea), BorderLayout.CENTER);

				// 버튼들
				JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				JButton save = new JButton("저장");
				JButton close = new JButton("닫기");
				btns.add(save);
				btns.add(close);
				dlg.add(btns, BorderLayout.SOUTH);

				// 저장: 동일 학생(이름+전화) 모든 객체의 memo 갱신 + 테이블 셀 갱신
				save.addActionListener(ev -> {
					String newMemo = memoArea.getText();
					for (Team tt : pj.getTeams()) {
						for (Student ss : tt.getMembers()) {
							if (name.equals(ss.getsName()) && phone.equals(ss.getsNum())) {
								ss.setmemo(newMemo);
							}
						}
					}
					model.setValueAt(newMemo, modelRow, COL_MEMO);
					dlg.dispose();
				});
				close.addActionListener(ev -> dlg.dispose());

				dlg.pack();
				dlg.setLocationRelativeTo(table);
				dlg.setVisible(true);

				clicked = false;
				fireEditingStopped();
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
			button.setText(value == null ? "" : value.toString());
			this.viewRow = row;
			clicked = true;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return button.getText();
		}

		@Override
		public boolean stopCellEditing() {
			clicked = false;
			return super.stopCellEditing();
		}
	}
}