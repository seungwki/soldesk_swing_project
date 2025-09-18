package front_frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Modal.makeStudent;
import Modal.makeTeam;
import VO.Data;
import VO.Project;
import VO.Student;
import VO.Tag;
import VO.TagRepository;
import VO.Team;
import front_ui.TopBar;

public class StudentManager extends BasePage {
	private static Data sharedData = null;
	private final Data data; // 전체 데이터
	private Project pj; // 현재 선택된 프로젝트
	private final List<Project> projects;

	// 학생 테이블 모델
	private final DefaultTableModel studentModel = new DefaultTableModel(new Object[] { "번호", "이름", "전화", "메모" }, 0) {
		@Override
		public boolean isCellEditable(int r, int c) {
			return c == 1; // "이름"
			// 컬럼만
			// 버튼처럼
			// 클릭                                                                                                                              // 가능
		}
	};

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
		// 싱글톤 패턴으로 데이터 인스턴스 공유
		if (sharedData == null) {
			sharedData = new Data();
		}
		this.data = sharedData;
		//      this.data = new Data();
		projects = data.getProjects();
		if (!projects.isEmpty())
			this.pj = projects.get(0); // 기본 선택
		//         throw new IllegalStateException("데이터가 비어있습니다.");

		getTopBar().selectOnly("student");

		// ✅ BottomPanel에서 가져온 UI 추가
		addBottomUI(getContentPanel());
	}

	private ImageIcon resizeImage(String path, int w, int h) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
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

		JComboBox<Project> regionCombo = new JComboBox<>();
		for (Project p : projects)
			regionCombo.addItem(p);
		regionCombo.setSelectedItem(pj);
		regionCombo.setSelectedItem(pj);
		regionCombo.setBounds(75, 15, 133, 30);
		parent.add(regionCombo);

		JComboBox<Integer> orderCombo = new JComboBox<>();
		orderCombo.setBounds(280, 15, 133, 30);
		parent.add(orderCombo);

		JComboBox<String> teamCombo = new JComboBox<>();
		teamCombo.setBounds(485, 15, 133, 30);
		parent.add(teamCombo);

		JButton btnPlus = new JButton(resizeImage("123team1.png", 60, 40));
		btnPlus.setBounds(621, 10, 70, 40);
		btnPlus.setBorderPainted(false); // 테두리 제거 (선택사항)
		btnPlus.setContentAreaFilled(false); // 배경 제거 (선택사항)
		parent.add(btnPlus);

		JButton btnTeamPlus = new JButton(resizeImage("rrteam1.png", 60, 40));
		btnTeamPlus.setBounds(698, 10, 70, 40);
		btnTeamPlus.setBorderPainted(false);
		btnTeamPlus.setContentAreaFilled(false);
		parent.add(btnTeamPlus);

		final JTable studentTable = new JTable(studentModel);
		studentTable.setFillsViewportHeight(true);
		studentTable.setRowHeight(27);
		studentTable.setGridColor(Color.BLACK);
		studentTable.setBorder(new LineBorder(Color.BLACK));
		studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentTable.getTableHeader().setReorderingAllowed(false);
		studentTable.getTableHeader().setResizingAllowed(false);
		studentTable.getTableHeader().setBackground(new Color(78, 116, 222));
		studentTable.getTableHeader().setForeground(Color.WHITE);

		// 이름 버튼 렌더러/에디터
		studentTable.getColumn("이름").setCellRenderer(new ButtonRenderer());
		studentTable.getColumn("이름").setCellEditor(new ButtonEditor(new JCheckBox(), studentTable, studentModel));

		// 가운데 정렬 (이름 제외)
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < studentTable.getColumnCount(); i++) {
			String cname = studentTable.getColumnName(i);
			if (!"이름".equals(cname)) {
				studentTable.getColumnModel().getColumn(i).setCellRenderer(center);
			}
		}

		final int TOP_H = 60; // 상단 컨트롤바 실제 높이와 일치
		final int OUTER = 10; // content와 카드 사이 여백
		final int INNER = 10; // 카드(whitePanel) 내부 패딩
		final int TARGET_W = 760; // StuTableClass의 panelWidth

		JPanel parent1 = getContentPanel();

		// 흰 카드 패널
		final JPanel whitePanel = new JPanel(null);
		whitePanel.setBackground(Color.WHITE);
		parent1.add(whitePanel);

		// 스크롤 + 테이블
		final JScrollPane tableScroll = new JScrollPane(studentTable);
		tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		whitePanel.add(tableScroll);

		// 레이아웃 함수 (초기/리사이즈 공통)
		Runnable doLayout = () -> {
			int w = parent1.getWidth();
			int h = parent1.getHeight();

			// 가로는 760을 기준으로, 화면이 좁으면 그 폭에 맞춰 축소 + 가운데 정렬
			int cardW = Math.min(TARGET_W, w - 2 * OUTER);
			int cardX = (w - cardW) / 2;
			int cardY = TOP_H + OUTER;
			int cardH = Math.max(0, h - cardY - OUTER);

			whitePanel.setBounds(cardX, cardY, cardW, cardH);
			tableScroll.setBounds(INNER, INNER, Math.max(0, cardW - 2 * INNER), Math.max(0, cardH - 2 * INNER));
		};

		// 최초 배치 + 리사이즈 대응
		doLayout.run();
		parent1.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				doLayout.run();
			}
		});

		// 갱신
		parent1.revalidate();
		parent1.repaint();
		refreshScroll();

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

			final java.util.concurrent.atomic.AtomicBoolean once = new java.util.concurrent.atomic.AtomicBoolean(false);

			makeStudent panel = new makeStudent(data.getProjects(), current, selectedDeg, selectedTeamName, null, // existing
					// =
					// null
					// (추가
					// 모드)
					result -> {

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
							showOnce(once, "학생 추가가 완료되었습니다.");
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

			JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "팀 인원 추가", Dialog.ModalityType.APPLICATION_MODAL);
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
					JOptionPane.showMessageDialog(dlg, "해당 차수/팀이 존재하지 않습니다.\n먼저 팀을 생성한 뒤 다시 시도하세요.");
					return; // 생성 금지, 바로 종료
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
				JOptionPane.showMessageDialog(this, "팀 배정 완료: " + result.teamName + " (" + result.degree + "차)");

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

	// 메시지를 단 1번만 띄우기 위한 헬퍼
	private void showOnce(java.util.concurrent.atomic.AtomicBoolean gate, String msg) {
		if (gate.compareAndSet(false, true)) {
			JOptionPane.showMessageDialog(this, msg);
		}
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
				final Student[] matchedStudent = new Student[1];
				matchedStudent[0] = null;

				for (Team tt : pj.getTeams()) {
					for (Student ss : tt.getMembers()) {
						if (name.equals(ss.getsName()) && phone.equals(ss.getsNum())) {
							matchedStudent[0] = ss;
							break;
						}
					}
					if (matchedStudent[0] != null)
						break;
				}

				List<Tag> tags = (matchedStudent[0] != null) ? matchedStudent[0].getTags() : new ArrayList<>();
				// 상단 정보
				JPanel info = new JPanel(new GridLayout(0, 1, 4, 4));
				info.add(new JLabel("이름 : " + name));
				info.add(new JLabel("전화 : " + phone));
				//info.add(new JLabel("태그 : ")); // TODO: 태그 기능 추후 연결

				dlg.add(info, BorderLayout.NORTH);
				dlg.add(new JScrollPane(localBelongTable), BorderLayout.WEST);
				// 태그 전체를 담는 패널 (한 줄로 구성: 태그: + [라벨들...])
				JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));//-----------------------이하 수정
				tagRow.setOpaque(false);

				// "태그 :" 라벨
				JLabel tagTitleLabel = new JLabel("태그 :");//----------- 학생 이름과 번호 값 받아서 검색 출력
				tagRow.add(tagTitleLabel);

				// 태그 추가 버튼 (+)
				JButton addTagBtn = new JButton("+");
				addTagBtn.setPreferredSize(new Dimension(25, 25));
				addTagBtn.setMargin(new Insets(0, 0, 0, 0));
				tagRow.add(addTagBtn);

				// 태그 라벨들이 들어갈 패널
				JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
				tagPanel.setOpaque(false);
				tagRow.add(tagPanel);

				// 초기 태그 라벨들
				for (Tag tag : tags) {
					JLabel tagLabel = new JLabel(tag.getName());
					tagLabel.setOpaque(true);
					tagLabel.setBackground(tag.getColor());
					tagLabel.setForeground(Color.WHITE);
					tagLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

					// 우클릭 삭제 메뉴
					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem deleteItem = new JMenuItem("삭제");
					popupMenu.add(deleteItem);

					tagLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							if (SwingUtilities.isRightMouseButton(e)) {
								popupMenu.show(tagLabel, e.getX(), e.getY());
							}
						}
					});

					deleteItem.addActionListener(ev -> {
						tagPanel.remove(tagLabel);
						tagPanel.revalidate();
						tagPanel.repaint();
						matchedStudent[0].getTags().remove(tag);
					});

					tagPanel.add(tagLabel);
				}

				// ▶ + 버튼 클릭 시 태그 선택 팝업
				addTagBtn.addActionListener(ee -> {
					List<Tag> allTags = TagRepository.getStudentTags();
					String[] tagNames = allTags.stream().map(Tag::getName).toArray(String[]::new);

					String selected = (String) JOptionPane.showInputDialog(dlg, "추가할 태그 선택:", "태그 선택", JOptionPane.PLAIN_MESSAGE, null, tagNames, null);

					if (selected != null) {
						Tag tag = TagRepository.findTagByName(selected);
						if (tag != null && matchedStudent[0] != null && !matchedStudent[0].getTags().contains(tag)) {
							matchedStudent[0].getTags().add(tag);

							JLabel newTagLabel = new JLabel(tag.getName());
							newTagLabel.setOpaque(true);
							newTagLabel.setBackground(tag.getColor());
							newTagLabel.setForeground(Color.WHITE);
							newTagLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

							JPopupMenu popupMenu = new JPopupMenu();
							JMenuItem deleteItem = new JMenuItem("삭제");
							popupMenu.add(deleteItem);

							newTagLabel.addMouseListener(new MouseAdapter() {
								@Override
								public void mousePressed(MouseEvent e) {
									if (SwingUtilities.isRightMouseButton(e)) {
										popupMenu.show(newTagLabel, e.getX(), e.getY());
									}
								}
							});

							deleteItem.addActionListener(ev -> {
								tagPanel.remove(newTagLabel);
								tagPanel.revalidate();
								tagPanel.repaint();
								matchedStudent[0].getTags().remove(tag);
							});

							tagPanel.add(newTagLabel);
							tagPanel.revalidate();
							tagPanel.repaint();
						} else {
							JOptionPane.showMessageDialog(dlg, "이미 추가된 태그입니다.");
						}
					}
				});
				JScrollPane leftScroll = new JScrollPane(localBelongTable);
				leftScroll.setPreferredSize(new Dimension(140, 250)); // << 너비 줄이기

				// 메모 편집 영역 (한 번만 선언!)
				JTextArea memoArea = new JTextArea(8, 40) {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						if (getText().isEmpty() && !isFocusOwner()) {
							Graphics2D g2 = (Graphics2D) g.create();
							g2.setColor(Color.GRAY);
							g2.drawString("(메모를 입력해주세요.)", 5, 15);
							g2.dispose();
						}
					}
				};

				memoArea.setText(memo0 == null ? "" : memo0);

				// 메모 영역
				JScrollPane memoScroll = new JScrollPane(memoArea);

				// SplitPane으로 묶어서 가운데 넣기
				JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, memoScroll);
				split.setDividerLocation(115); // 분할선 위치 (왼쪽 영역 너비)
				dlg.add(split, BorderLayout.CENTER);

				// 다이얼로그 크기 지정
				dlg.setSize(350, 400);
				dlg.setLocationRelativeTo(table);
				dlg.setResizable(true);

				// ▶ 전체 태그 패널을 상단 정보 패널에 추가
				info.add(tagRow);//------------------------------------------------------------까지  태그

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
									//JOptionPane.showMessageDialog(dlg, "이동: " + teamNm + " (" + (degVal < 0 ? "?" : degVal) + "차)");
									// === 실제 상세보기로 이동 ===
									dlg.dispose(); // 상세 다이얼로그 닫기
									java.util.Set<Integer> degreeSet = new java.util.TreeSet<>();
									for (Team tt : pj.getTeams()) {
										int d = tt.getDegree(); // 주의: getdegree() / getDegree() 중 프로젝트에 맞춰 사용
										if (d >= 1)
											degreeSet.add(d);
									}
									java.util.List<Integer> degrees = new java.util.ArrayList<>(degreeSet);
									front_ui.TabSpec[] specs = new front_ui.TabSpec[degrees.size()];
									for (int i = 0; i < degrees.size(); i++) {
										specs[i] = new front_ui.TabSpec(degrees.get(i) + "차", java.awt.Color.WHITE);
										specs[i].setDegree(String.valueOf(degrees.get(i)));
									}

									// 페이지 전환
									BasePage.changePage(new TeamDetailViewer(target, pj, specs));
								} else {
									JOptionPane.showMessageDialog(dlg, "팀을 찾을 수 없습니다: " + cell);
								}
							}
						}
					}
				});

				// 메모 편집
				//JTextArea memoArea = new JTextArea(8, 40);
				memoArea.setText(memo0 == null ? "" : memo0);
				//dlg.add(new JScrollPane(memoArea), BorderLayout.CENTER);

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

				//dlg.pack(); 창크기 변경을 위해 잠시 주석처리
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