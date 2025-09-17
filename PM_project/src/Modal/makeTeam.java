package Modal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import VO.Project;
import VO.Student;
import VO.Team;

/**
 * 팀 생성/삭제 모달 패널 (생성/삭제 차수 분리, 미배정 기반) - 상단: 수업 / (생성) 차수/팀명 / (삭제) 차수/팀 - 중앙: 미배정(좌) ↔ 선택 인원(우) 이동 - 하단: 저장 / 팀 삭제
 */
public class makeTeam extends JPanel {

	// ─────────────────────────────────────────────────────────────
	// 1) UI 필드
	// ─────────────────────────────────────────────────────────────
	private final JComboBox<Project> slpj = new JComboBox<>();

	// 생성 전용
	private final JComboBox<Integer> sldgCreate = new JComboBox<>();
	private final JTextField tfTeamName = new JTextField(20);

	// 삭제 전용
	private final JComboBox<Integer> sldgDelete = new JComboBox<>();
	private final JComboBox<String> sltmDelete = new JComboBox<>();

	private final DefaultListModel<Student> availModel = new DefaultListModel<>(); // 미배정
	private final DefaultListModel<Student> selModel = new DefaultListModel<>(); // 선택 인원
	private final JList<Student> availableList = new JList<>(availModel);
	private final JList<Student> selectedList = new JList<>(selModel);

	// ─────────────────────────────────────────────────────────────
	// 2) 결과 전달용 DTO
	// ─────────────────────────────────────────────────────────────
	public static class Result {
		public final Project project;
		public final int degree; // 생성용 차수
		public final String teamName; // 생성할 팀명
		public final List<Student> members; // 선택한 멤버

		public Result(Project project, int degree, String teamName, List<Student> members) {
			this.project = project;
			this.degree = degree;
			this.teamName = teamName;
			this.members = members;
		}
	}

	// ─────────────────────────────────────────────────────────────
	// 3) 생성자
	// ─────────────────────────────────────────────────────────────
	/**
	 * @param projects   수업 목록
	 * @param current    기본 선택 수업 (null 가능)
	 * @param currentDeg 기본 선택 차수 (생성/삭제 모두에 초깃값으로 시도)
	 * @param onSave     저장 콜백 (Result 전달)
	 */
	public makeTeam(List<Project> projects, Project current, Integer currentDeg, Consumer<Result> onSave) {
		setLayout(new BorderLayout(8, 8));

		// 임시로 창크기 고정 0912 승민추가 코드
		setPreferredSize(new Dimension(350, 400));
		setMinimumSize(new Dimension(350, 400));
		setMaximumSize(new Dimension(350, 400));

		buildTop();
		buildCenter();
		buildBottom(onSave);
		configureLists();

		fillProjects(projects, current);
		fillDegreesForCreate(currentDeg);
		fillDegreesForDelete(currentDeg);
		fillTeamsForDelete();
		fillAvailableFromUnassigned(); // 좌측 리스트: 미배정

		bindListeners(onSave);
	}

	/// ─────────────────────────────────────────────────────────────
	// 4) UI 구성 (GridBagLayout 적용 버전)
	//  - 상단: 수업 / 생성 차수 / 팀명 / 삭제 차수 / 삭제 팀
	//  - 중앙: 미배정 인원 리스트 ↔ 추가/제거 버튼 ↔ 선택 인원 리스트
	//  - 하단: 저장 / 팀 삭제 버튼
	//─────────────────────────────────────────────────────────────
	private void buildTop() {
		JPanel top = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;

		Dimension fieldSize = new Dimension(140, 26);
		Dimension degreeSize = new Dimension(55, 26); // 차수 전용
		Dimension lessonSize = new Dimension(250, 26); // 수업 전용 드롭박스 크기
		Dimension teamDeleteSize = new Dimension(100, 26); // 삭제할 팀 드롭박스

		sldgCreate.setPreferredSize(degreeSize);
		sldgCreate.setMinimumSize(degreeSize);
		sldgCreate.setMaximumSize(degreeSize);

		sldgDelete.setPreferredSize(degreeSize);
		sldgDelete.setMinimumSize(degreeSize);
		sldgDelete.setMaximumSize(degreeSize);

		// ============ 수업 ============
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		top.add(new JLabel("수업"), gbc);

		gbc.gridx = 1;
		slpj.setPreferredSize(lessonSize);
		top.add(slpj, gbc);

		// ============ 생성할 차수 ============
		gbc.gridx = 0;
		gbc.gridy = 1;
		top.add(new JLabel("생성할 차수"), gbc);

		gbc.gridx = 1;
		sldgCreate.setPreferredSize(degreeSize);
		top.add(sldgCreate, gbc);

		// ============ 생성할 팀명 ============
		gbc.gridx = 0;
		gbc.gridy = 2;
		top.add(new JLabel("생성할 팀명"), gbc);

		gbc.gridx = 1;
		//9월16일 수정
		this.tfTeamName.setPreferredSize(fieldSize);
		top.add(this.tfTeamName, gbc); // ← (20) 없앰
		tfTeamName.setPreferredSize(fieldSize); // 드롭박스랑 동일 크기
		top.add(tfTeamName, gbc);

		// ============ 삭제할 차수 ============
		gbc.gridx = 0;
		gbc.gridy = 3;
		top.add(new JLabel("삭제할 차수"), gbc);

		gbc.gridx = 1;
		sldgCreate.setPreferredSize(degreeSize);
		top.add(sldgDelete, gbc);

		// ============ 삭제할 팀 ============
		gbc.gridx = 0;
		gbc.gridy = 4;
		top.add(new JLabel("삭제할 팀"), gbc);

		gbc.gridx = 1;
		sltmDelete.setPreferredSize(teamDeleteSize);
		sltmDelete.setMinimumSize(teamDeleteSize);
		sltmDelete.setMaximumSize(teamDeleteSize);
		top.add(sltmDelete, gbc);

		add(top, BorderLayout.NORTH);
		// ============ 오른쪽 빈 공간 ============
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 5; // 5행 전부 차지
		gbc.weightx = 1; // 남는 공간 전부 차지
		gbc.fill = GridBagConstraints.HORIZONTAL;
		top.add(Box.createHorizontalGlue(), gbc);

		add(top, BorderLayout.NORTH);
	}

	private void buildCenter() {
		// 중앙 영역: 좌측 리스트, 가운데 버튼, 우측 리스트
		JPanel center = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(4, 4, 4, 4);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		// 좌측(미배정 인원)
		JScrollPane leftScroll = new JScrollPane(availableList);
		JPanel leftWrap = new JPanel(new BorderLayout(4, 4));
		leftScroll.setPreferredSize(new Dimension(160, 220));
		leftWrap.add(new JLabel("추가 가능 인원 (미배정)"), BorderLayout.NORTH);
		leftWrap.add(leftScroll, BorderLayout.CENTER);

		// 우측(선택 인원)
		JScrollPane rightScroll = new JScrollPane(selectedList);
		JPanel rightWrap = new JPanel(new BorderLayout(4, 4));
		rightWrap.add(new JLabel("선택된 인원"), BorderLayout.NORTH);
		rightWrap.add(rightScroll, BorderLayout.CENTER);

		// 버튼
		JButton btnAdd = new JButton("추가 ▶");
		JButton btnRemove = new JButton("◀ 제거");

		Dimension btnSize = new Dimension(73, 50);

		btnAdd.setPreferredSize(btnSize);
		btnAdd.setMaximumSize(btnSize);
		btnAdd.setMinimumSize(btnSize);

		btnRemove.setPreferredSize(btnSize);
		btnRemove.setMaximumSize(btnSize);
		btnRemove.setMinimumSize(btnSize);

		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel midBtns = new JPanel();
		midBtns.setLayout(new BoxLayout(midBtns, BoxLayout.Y_AXIS));
		midBtns.add(Box.createVerticalGlue()); // 위쪽 공간
		midBtns.add(btnAdd);
		midBtns.add(Box.createVerticalStrut(10)); // 버튼 사이 간격
		midBtns.add(btnRemove);
		midBtns.add(Box.createVerticalGlue()); // 아래쪽 공간

		// 버튼 패널 높이를 리스트와 동일하게 맞추기
		int listHeight = leftScroll.getPreferredSize().height;
		midBtns.setPreferredSize(new Dimension(80, listHeight));

		// GridBag 배치
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		center.add(leftWrap, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		center.add(midBtns, gbc);

		// 우측 리스트
		gbc.gridx = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		center.add(rightWrap, gbc);

		// CENTER 영역에 추가

		add(center, BorderLayout.CENTER);

		// 버튼 동작 (기능 로직 연결)
		btnAdd.addActionListener(e -> moveSelected(availableList, availModel, selModel));
		btnRemove.addActionListener(e -> moveSelected(selectedList, selModel, availModel));
	}

	private void buildBottom(Consumer<Result> onSave) {
		// 하단 버튼 영역
		JPanel southPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(4, 4, 4, 4);

		JButton btnSave = new JButton("팀 저장");
		JButton btnDeleteTeam = new JButton("팀 삭제");

		gbc.gridx = 0;
		gbc.gridy = 0;
		southPanel.add(btnSave, gbc);

		gbc.gridx = 1;
		southPanel.add(btnDeleteTeam, gbc);

		// SOUTH 영역에 추가
		add(southPanel, BorderLayout.SOUTH);

		// 버튼 기능 연결
		btnSave.addActionListener(e -> saveTeam(onSave));
		btnDeleteTeam.addActionListener(e -> deleteTeam());
	}

	private void configureLists() {
		// 리스트 선택/렌더러
		availableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Student) {
					Student s = (Student) value; // instanceof 검사 후 캐스팅
					setText(s.getsName() + "  (" + s.getsNum() + ")");
				}
				return this;
			}
		};
		availableList.setCellRenderer(renderer);
		selectedList.setCellRenderer(renderer);

		// 더블클릭 이동
		availableList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					moveSelected(availableList, availModel, selModel);
			}
		});
		selectedList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					moveSelected(selectedList, selModel, availModel);
			}
		});

		// 드래그&드롭
		availableList.setDragEnabled(true);
		selectedList.setDragEnabled(true);
		availableList.setDropMode(DropMode.INSERT);
		selectedList.setDropMode(DropMode.INSERT);
	}

	// ─────────────────────────────────────────────────────────────
	// 5) 데이터 채우기
	// ─────────────────────────────────────────────────────────────
	private void fillProjects(List<Project> projects, Project current) {
		slpj.removeAllItems();
		if (projects != null) {
			for (Project p : projects)
				slpj.addItem(p);
		}
		if (current != null)
			slpj.setSelectedItem(current);
		else if (slpj.getItemCount() > 0)
			slpj.setSelectedIndex(0);
	}

	// 생성용 차수(1차 이상만)
	private void fillDegreesForCreate(Integer preferDeg) {
		sldgCreate.removeAllItems();
		Project p = (Project) slpj.getSelectedItem();
		if (p == null)
			return;

		Set<Integer> degrees = new TreeSet<>();
		boolean hasZeroOrNegative = false;
		for (Team t : p.getTeams()) {
			int deg = t.getDegree();
			if (deg >= 1)
				degrees.add(deg);
			else
				hasZeroOrNegative = true;
		}
		if (degrees.isEmpty() && hasZeroOrNegative)
			degrees.add(1);

		for (Integer d : degrees)
			sldgCreate.addItem(d);
		if (preferDeg != null && preferDeg >= 1)
			sldgCreate.setSelectedItem(preferDeg);
		else if (sldgCreate.getItemCount() > 0)
			sldgCreate.setSelectedIndex(0);
	}

	// 삭제용 차수(1차 이상만)
	private void fillDegreesForDelete(Integer preferDeg) {
		sldgDelete.removeAllItems();
		Project p = (Project) slpj.getSelectedItem();
		if (p == null)
			return;

		Set<Integer> degrees = new TreeSet<>();
		for (Team t : p.getTeams()) {
			int deg = t.getDegree();
			if (deg >= 1)
				degrees.add(deg);
		}
		for (Integer d : degrees)
			sldgDelete.addItem(d);

		if (preferDeg != null && preferDeg >= 1)
			sldgDelete.setSelectedItem(preferDeg);
		else if (sldgDelete.getItemCount() > 0)
			sldgDelete.setSelectedIndex(0);
	}

	// 삭제용 팀
	private void fillTeamsForDelete() {
		sltmDelete.removeAllItems();
		Project p = (Project) slpj.getSelectedItem();
		Integer d = (Integer) sldgDelete.getSelectedItem();
		if (p == null || d == null)
			return;

		for (Team t : p.getTeams()) {
			if (t.getDegree() == d)
				sltmDelete.addItem(t.getTName());
		}
	}

	/** 좌측 리스트는 '미배정(unassigned)' 학생 목록으로 채운다. */
	private void fillAvailableFromUnassigned() {
		availModel.clear();
		selModel.clear();

		Project p = (Project) slpj.getSelectedItem();
		if (p == null)
			return;

		for (Student s : p.getUnassigned()) {
			availModel.addElement(s);
		}
	}

	// ─────────────────────────────────────────────────────────────
	// 6) 이벤트 바인딩
	// ─────────────────────────────────────────────────────────────
	private void bindListeners(Consumer<Result> onSave) {
		slpj.addActionListener(e -> {
			fillDegreesForCreate(null);
			fillDegreesForDelete(null);
			fillTeamsForDelete();
			fillAvailableFromUnassigned();
		});

		sldgDelete.addActionListener(e -> {
			fillTeamsForDelete();
			// 생성용 차수에는 영향 없음
		});
	}

	// ─────────────────────────────────────────────────────────────
	// 7) 액션 로직 (저장/삭제/이동)
	// ─────────────────────────────────────────────────────────────
	// 저장(팀 생성)은 생성용 차수만 사용
	private void saveTeam(Consumer<Result> onSave) {
		Project p = (Project) slpj.getSelectedItem();
		Integer d = (Integer) sldgCreate.getSelectedItem(); // 생성용
		String teamName = tfTeamName.getText().trim();

		if (p == null || d == null) {
			JOptionPane.showMessageDialog(this, "수업과 (생성할) 차수를 선택하세요.");
			return;
		}
		if (teamName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "팀명을 입력하세요.");
			return;
		}

		java.util.ArrayList<Student> members = new java.util.ArrayList<>();
		for (int i = 0; i < selModel.size(); i++)
			members.add(selModel.getElementAt(i));

		if (members.isEmpty()) {
			int ans = JOptionPane.showConfirmDialog(this, "선택된 인원이 없습니다. 그대로 저장할까요?", "확인", JOptionPane.YES_NO_OPTION);
			if (ans != JOptionPane.YES_OPTION)
				return;
		}

		if (onSave != null)
			onSave.accept(new Result(p, d, teamName, members));
	}

	// 삭제는 삭제용 차수/팀만 사용
	private void deleteTeam() {
		Project p = (Project) slpj.getSelectedItem();
		Integer d = (Integer) sldgDelete.getSelectedItem(); // 삭제용
		String teamName = (String) sltmDelete.getSelectedItem();// 삭제용

		if (p == null || d == null || teamName == null) {
			JOptionPane.showMessageDialog(this, "수업/삭제할 차수/삭제할 팀을 선택하세요.");
			return;
		}

		Team target = null;
		for (Team t : p.getTeams()) {
			if (t.getDegree() == d && t.getTName().equals(teamName)) {
				target = t;
				break;
			}
		}
		if (target == null) {
			JOptionPane.showMessageDialog(this, "팀을 찾을 수 없습니다.");
			return;
		}

		int ans = JOptionPane.showConfirmDialog(this, "팀을 삭제하고 팀원은 '미배정'으로 이동합니다. 진행할까요?", "팀 삭제", JOptionPane.YES_NO_OPTION);
		if (ans != JOptionPane.YES_OPTION)
			return;

		// 멤버를 미배정으로 이동하고 팀 삭제
		p.deleteTeamAndMoveMembersToUnassigned(target.getDegree(), target.getTName());

		// UI 갱신
		fillDegreesForDelete(d);
		fillTeamsForDelete();
		fillAvailableFromUnassigned();
		selModel.clear();

		JOptionPane.showMessageDialog(this, "팀 삭제 완료 (팀원은 미배정으로 이동)");
	}

	private void moveSelected(JList<Student> from, DefaultListModel<Student> src, DefaultListModel<Student> dst) {
		List<Student> selected = from.getSelectedValuesList();
		for (Student s : selected) {
			if (!contains(dst, s))
				dst.addElement(s); // 중복 방지
			src.removeElement(s);
		}
	}

	private boolean contains(DefaultListModel<Student> m, Student s) {
		for (int i = 0; i < m.size(); i++) {
			Student x = m.get(i);
			if (x.getsName().equals(s.getsName()) && x.getsNum().equals(s.getsNum()))
				return true;
		}
		return false;
	}
}
