package Modal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import VO.Project;
import VO.Student;
import VO.Tag;
import VO.TagRepository;
import VO.Team;

public class makeStudent extends JPanel {

	private final JTextField tfName = new JTextField();
	private final JTextField tfPhone = new JTextField();
	private final JTextArea taMemo = new JTextArea(5, 20);

	private final JComboBox<Project> slpj = new JComboBox<>();
	private final JComboBox<Integer> sldg = new JComboBox<>(); // 차수 (Integer)
	private final JComboBox<String> sltm = new JComboBox<>(); // 팀 (String)

	private final JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
	private final List<Tag> selectedTags = new ArrayList<>();

	private final JCheckBox cbNoTeam = new JCheckBox("팀/차수 없이 추가");
	private final JButton btnSave = new JButton("저장");
	private final JButton csvBtn = new JButton("CSV로 추가");
	private final JButton btnAddTag = new JButton("+");

	// --- 파일 브라우저 필드(목록 불러오기용) ---
	private final DefaultListModel<File> model = new DefaultListModel<>();

	/** 저장 시 호출자에게 돌려줄 결과 */
	public static class Result {
		public final Project project;
		public final Integer degree;
		public final String teamName;
		public final Student student;

		public Result(Project project, Integer degree, String teamName, Student student) {
			this.project = project;
			this.degree = degree;
			this.teamName = teamName;
			this.student = student;
		}
	}

	/**
	 * @param projects    수업 목록 (콤보 채움)
	 * @param current     기본 선택 수업 (null이면 첫 항목)
	 * @param currentDeg  기본 선택 차수 (없으면 첫 항목)
	 * @param currentTeam 기본 선택 팀명 (없으면 첫 항목)
	 * @param existing    수정 모드일 때 기존 학생 (null = 추가 모드)
	 * @param onSave      저장 콜백 (선택된 위치 + 학생 객체 반환)
	 */
	public makeStudent(List<Project> projects, Project current, Integer currentDeg, String currentTeam, Student existing, Consumer<Result> onSave) {

		// 임시로 창크기 고정
		setPreferredSize(new Dimension(350, 400));
		setMinimumSize(new Dimension(350, 400));
		setMaximumSize(new Dimension(350, 400));

		// ====== BorderLayout → GridBagLayout 으로 교체 ======
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // 여백
		//gbc.fill = GridBagConstraints.NONE; // 기본적으로 가로 확장
		gbc.anchor = GridBagConstraints.WEST;

		// ====== CSV 버튼 + 이름 ======
		Dimension fieldSize = new Dimension(130, 25);

		tfName.setPreferredSize(fieldSize);
		tfName.setMinimumSize(fieldSize);
		tfName.setMaximumSize(fieldSize);

		tfPhone.setPreferredSize(fieldSize);
		tfPhone.setMinimumSize(fieldSize);
		tfPhone.setMaximumSize(fieldSize);

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("이름"), gbc);

		gbc.gridx = 1;
		add(tfName, gbc);

		gbc.gridx = 2;
		add(csvBtn, gbc);

		// ====== TEL ======
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("TEL"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(tfPhone, gbc);
		gbc.gridwidth = 1;

		// ====== 수업 ======
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("수업"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		add(slpj, gbc);
		gbc.gridwidth = 1;

		// ====== 차수 ======
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("차수"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		sldg.setPreferredSize(new Dimension(110, 25));
		add(sldg, gbc);
		gbc.gridwidth = 1;

		// ====== 조 ======
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new JLabel("조"), gbc);

		gbc.gridx = 1;
		sltm.setPreferredSize(new Dimension(110, 25));
		add(sltm, gbc);

		gbc.gridx = 2;
		add(cbNoTeam, gbc);

		// 태그 라벨 + 버튼
		gbc.gridx = 0;
		gbc.gridy = 5; // 저장 버튼보다 아래
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0; // <== 중요! 남는 공간 차지하게 함

		add(new JLabel("태그 :"), gbc);

		// + 버튼 (라벨 오른쪽 같은 행)
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		add(btnAddTag, gbc);

		btnAddTag.addActionListener(e -> {
			List<Tag> availableTags = TagRepository.getStudentTags();

			// 선택창용 ComboBox 생성
			JComboBox<Tag> tagCombo = new JComboBox<>();
			for (Tag tag : availableTags) {
				// 이미 선택된 태그는 제외
				if (!selectedTags.contains(tag)) {
					tagCombo.addItem(tag);
				}
			}

			if (tagCombo.getItemCount() == 0) {
				JOptionPane.showMessageDialog(this, "추가할 수 있는 태그가 없습니다.");
				return;
			}

			// JOptionPane 으로 선택 UI 보여주기
			int result = JOptionPane.showConfirmDialog(this, tagCombo, "태그 선택", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {
				Tag selected = (Tag) tagCombo.getSelectedItem();
				if (selected != null && !selectedTags.contains(selected)) {
					selectedTags.add(selected);
					addTagToPanel(selected);
				}
			}
		});
		// ====== 메모 ======
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		add(new JLabel("메모"), gbc);

		// ==메모 입력칸==
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0;

		Dimension memoSize = new Dimension(300, 110);
		JScrollPane memoScroll = new JScrollPane(taMemo);
		memoScroll.setPreferredSize(memoSize);
		memoScroll.setMinimumSize(memoSize);
		memoScroll.setMaximumSize(memoSize);

		add(memoScroll, gbc);

		// ====== 저장 버튼 ======
		gbc.gridy = 8;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		add(btnSave, gbc);

		// ====== 콤보 데이터 채우기 ======
		// Project 콤보
		for (Project p : projects)
			slpj.addItem(p);
		if (current != null)
			slpj.setSelectedItem(current);
		else if (slpj.getItemCount() > 0)
			slpj.setSelectedIndex(0);

		// 차수/팀 채움 헬퍼
		Runnable fillDegrees = () -> {
			sldg.removeAllItems();
			Project p = (Project) slpj.getSelectedItem();
			if (p == null)
				return;

			// 중복 제거 + 정렬 (★ 1차 이상만 수집)
			java.util.Set<Integer> degrees = new java.util.TreeSet<>();
			boolean hasNonPositive = false;
			for (Team t : p.getTeams()) {
				int deg = t.getDegree();
				if (deg >= 1)
					degrees.add(deg);
				else
					hasNonPositive = true; // 0차/음수 차수 존재 체크(옵션용)
			}

			if (degrees.isEmpty() && hasNonPositive) {
				degrees.add(1);
			}

			for (Integer d : degrees)
				sldg.addItem(d);

			// 기본 선택 (★ currentDeg가 1 이상일 때만 복원)
			if (currentDeg != null && currentDeg >= 1) {
				sldg.setSelectedItem(currentDeg);
			} else if (sldg.getItemCount() > 0) {
				sldg.setSelectedIndex(0);
			}
		};

		Runnable fillTeams = () -> {
			sltm.removeAllItems();
			Project p = (Project) slpj.getSelectedItem();
			Integer d = (Integer) sldg.getSelectedItem(); // 여기서는 null 허용
			if (p == null || d == null)
				return;
			for (Team t : p.getTeams()) {
				if (t.getDegree() == d)
					sltm.addItem(t.getTName());
			}
			if (currentTeam != null)
				sltm.setSelectedItem(currentTeam);
			else if (sltm.getItemCount() > 0)
				sltm.setSelectedIndex(0);
		};
		cbNoTeam.addActionListener(e -> {
			boolean off = cbNoTeam.isSelected();
			sldg.setEnabled(!off);
			sltm.setEnabled(!off);
		});

		// 초기 채움
		fillDegrees.run();
		fillTeams.run();

		// 연동 리스너
		slpj.addActionListener(e -> {
			fillDegrees.run();
			fillTeams.run();
		});
		sldg.addActionListener(e -> {
			fillTeams.run();
		});

		// ====== 기존 학생 데이터 채우기 (수정 모드) ======
		if (existing != null) {
			tfName.setText(existing.getsName());
			tfPhone.setText(existing.getsNum());
			// getMemo()/setMemo() 네이밍에 맞춰 수정
			taMemo.setText(existing.getmemo());
		}

		// ====== 저장 ======
		btnSave.addActionListener(e -> {

			// [추가] ──────────────────────────────────────────────────────────────────
			// 파일 선택 목록(model)에 CSV가 들어있다면, 먼저 배치 임포트를 시도한다.
			if (model != null && model.getSize() > 0) {
				java.util.List<Student> batch = new java.util.ArrayList<>();

				for (int i = 0; i < model.size(); i++) {
					File f = model.get(i);
					// CSV만 대상
					if (f == null || !f.isFile())
						continue;
					String fn = f.getName().toLowerCase(java.util.Locale.ROOT);
					if (!fn.endsWith(".csv"))
						continue;

					try (var br = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(f), java.nio.charset.StandardCharsets.UTF_8))) {

						// 첫 줄(헤더) 읽어 구분자 간단 감지(, ; \t 중 최다)
						String header = br.readLine();
						if (header == null)
							continue;

						int c = 0, s = 0, t = 0;
						for (int k = 0; k < header.length(); k++) {
							char ch = header.charAt(k);
							if (ch == ',')
								c++;
							else if (ch == ';')
								s++;
							else if (ch == '\t')
								t++;
						}
						char delim = ',';
						if (s >= c && s >= t)
							delim = ';';
						else if (t >= c && t >= s)
							delim = '\t';

						// 데이터 라인들 처리(0열=이름, 1열=전화)
						String line;
						while ((line = br.readLine()) != null) {
							if (line.isBlank())
								continue;
							String[] cols = line.split(java.util.regex.Pattern.quote(String.valueOf(delim)), -1);
							if (cols.length < 2)
								continue;

							Student sObj = new Student("", "");
							sObj.setsName(cols[0].trim());
							sObj.setsNum(cols[1].trim());
							sObj.setmemo(taMemo.getText()); // 동일 메모 적용(원하면 지워도 됨)
							batch.add(sObj);
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(this, "[CSV 읽기 실패] " + f.getName() + " : " + ex.getMessage());
					}
				}

				if (!batch.isEmpty()) {
					// 수업 선택 유도
					Project pSel = (Project) slpj.getSelectedItem();
					if (pSel == null) {
						javax.swing.JOptionPane.showMessageDialog(this, String.format("%d명의 학생을 추가합니다.\n수업을 선택하세요.", batch.size()));
						return; // 수업 선택 후 다시 [저장] 누르게 함
					}

					// 차수/팀 처리(체크박스 고려)
					Integer dSel;
					String teamSel;
					if (cbNoTeam.isSelected()) {
						dSel = null;
						teamSel = null;
					} else {
						dSel = (Integer) sldg.getSelectedItem();
						teamSel = (String) sltm.getSelectedItem();
					}

					int ok = javax.swing.JOptionPane.showConfirmDialog(this, String.format("%d명의 학생을 추가합니다.\n계속할까요?", batch.size()), "일괄 추가 확인", javax.swing.JOptionPane.OK_CANCEL_OPTION);
					if (ok != javax.swing.JOptionPane.OK_OPTION)
						return;

					// 일괄 저장(onSave는 기존 단건 저장 때 쓰던 콜백을 재사용)
					for (Student st : batch) {
						if (onSave != null)
							onSave.accept(new Result(pSel, dSel, teamSel, st));
					}
					javax.swing.JOptionPane.showMessageDialog(this, String.format("추가 완료: %d명", batch.size()));

					// 선택 목록 정리 후 단건 로직 실행 방지
					model.clear();
					return;
				}
				// batch가 비었으면 아래 기존 단건 저장 로직으로 자연스럽게 이동
			}
			// [추가] ──────────────────────────────────────────────────────────────────

			// ── 아래는 "기존 단건 저장 로직" 그대로 ────────────────────────────────
			String name = tfName.getText().trim();
			String phone = tfPhone.getText().trim();
			String memo = taMemo.getText();

			if (name.isEmpty() || phone.isEmpty()) {
				JOptionPane.showMessageDialog(this, "이름/전화는 필수입니다.");
				return;
			}

			Project p = (Project) slpj.getSelectedItem();
			if (p == null) {
				JOptionPane.showMessageDialog(this, "수업을 선택하세요.");
				return;
			}

			// 차수/팀은 없어도 OK → 기본값
			Integer d;
			String teamName;

			if (cbNoTeam.isSelected()) {
				// 체크박스 켰을 때는 아예 null 로 강제
				d = null;
				teamName = null;
			} else {
				// 일반 모드에서는 콤보 선택값 사용
				d = (Integer) sldg.getSelectedItem();
				teamName = (String) sltm.getSelectedItem();
			}

			Student s = (existing != null) ? existing : new Student("", "");
			s.setsName(name);
			s.setsNum(phone);
			s.setmemo(memo);

			s.setTags(new ArrayList<>(selectedTags));
			if (onSave != null)
				onSave.accept(new Result(p, d, teamName, s));
			// ────────────────────────────────────────────────────────────────────────
		});

		// ===== 파일 목록 불러오기 =====
		csvBtn.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // ★ 파일
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileFilter(new FileNameExtensionFilter("CSV 파일 (*.csv)", "csv"));
			chooser.setAcceptAllFileFilterUsed(false);

			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File[] picked = chooser.getSelectedFiles();

				for (File f : picked) {
					if (f.isFile()) {
						model.addElement(f);
					} else if (f.isDirectory()) {
						File[] children = f.listFiles();
						if (children != null) {
							for (File c : children) {
								if (c.isFile())
									model.addElement(c);
							}
						}
					}
				}

			}
		});
	}

	// 단순 getter 필요하면 추가
	public Student toStudent() {
		Student s = new Student(tfName.getText().trim(), tfPhone.getText().trim());
		s.setmemo(taMemo.getText());
		return s;
	}

	private void addTagToPanel(Tag tag) {
		JLabel lbl = new JLabel(tag.getName());
		lbl.setOpaque(true);
		lbl.setBackground(tag.getColor());
		lbl.setForeground(Color.WHITE);
		lbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5));

		// 클릭 시 제거 기능 추가 (선택적)
		lbl.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				selectedTags.remove(tag);
				tagPanel.remove(lbl);
				tagPanel.revalidate();
				tagPanel.repaint();
			}
		});

		tagPanel.add(lbl);
		tagPanel.revalidate();
		tagPanel.repaint();
	}

	private static boolean isCsv(File f) {
		if (f == null || !f.isFile())
			return false;
		String name = f.getName();
		int dot = name.lastIndexOf('.');
		return dot > 0 && name.substring(dot + 1).equalsIgnoreCase("csv");
	}

	// --- 폴더에서 파일 목록 읽어오기 ---
	private void loadDirectory(File f) {
		model.clear();

		if (f.isFile()) {
			//  단일 파일 선택 시에도 CSV만 허용
			if (isCsv(f)) {
				model.addElement(f);
			} else {
				JOptionPane.showMessageDialog(this, "CSV 파일만 선택할 수 있습니다.");
			}
		} else if (f.isDirectory()) {
			// 디렉터리일 경우 .csv만 추가
			File[] files = f.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".csv"));
			if (files == null) {
				JOptionPane.showMessageDialog(this, "목록을 불러올 수 없습니다.");
				return;
			}
			for (File child : files) {
				if (child.isFile())
					model.addElement(child);
			}
		}
	}

	// ───────── CSV 파서 (Commons CSV 사용) ─────────
	private static java.util.List<Student> readCsvSmart(File file) throws java.io.IOException {
		java.nio.charset.Charset[] tryCharsets = { java.nio.charset.StandardCharsets.UTF_8, java.nio.charset.Charset.forName("MS949") };
		for (var cs : tryCharsets) {
			try (var fr = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(file), cs))) {

				char delimiter = detectDelimiter(fr); // 첫 줄 보고 , ; \t 중 선택
				// 다시 새로 열기 (readLine을 썼으니 스트림 재생성)
				try (var r = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(file), cs))) {

					var fmt = org.apache.commons.csv.CSVFormat.DEFAULT.builder().setHeader() // 첫 행 헤더로 간주
							.setSkipHeaderRecord(true).setIgnoreEmptyLines(true).setIgnoreSurroundingSpaces(true).setTrim(true).setDelimiter(delimiter).build();

					java.util.List<Student> out = new java.util.ArrayList<>();

					try (var parser = new org.apache.commons.csv.CSVParser(r, fmt)) {
						var nameKeys = java.util.List.of("이름", "성명", "name");
						var phoneKeys = java.util.List.of("전화번호", "연락처", "휴대폰", "phone", "mobile");

						for (var rec : parser) {
							String name = firstMapped(rec, nameKeys);
							String phone = firstMapped(rec, phoneKeys);
							if ((name == null || name.isBlank()) && (phone == null || phone.isBlank()))
								continue;

							Student s = new Student("", "");
							s.setsName(name == null ? "" : name.trim());
							s.setsNum(normalizePhone(phone));
							out.add(s);
						}
					}
					return dedupeByNamePhone(out);
				}
			} catch (java.io.IOException ex) {
				// 다음 인코딩으로 재시도
			}
		}
		throw new java.io.IOException("CSV 읽기 실패: " + file.getName());
	}

	private static char detectDelimiter(java.io.BufferedReader br) throws java.io.IOException {
		br.mark(4096);
		String line = br.readLine();
		br.reset();
		if (line == null)
			return ',';
		int c = count(line, ','), s = count(line, ';'), t = count(line, '\t');
		if (t >= c && t >= s)
			return '\t';
		if (s >= c && s >= t)
			return ';';
		return ',';
	}

	private static int count(String s, char ch) {
		int n = 0;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == ch)
				n++;
		return n;
	}

	private static String firstMapped(org.apache.commons.csv.CSVRecord rec, java.util.List<String> keys) {
		for (String k : keys) {
			if (rec.isMapped(k)) {
				String v = rec.get(k);
				if (v != null && !v.isBlank())
					return v;
			}
		}
		// 헤더명이 특이하면 0,1열 가정(이름,전화)
		try {
			if (rec.size() >= 2) {
				String a = rec.get(0), b = rec.get(1);
				// 이 유틸은 키 리스트 중 어디서 호출되었는지 몰라서 그대로 반환 금지
			}
		} catch (Exception ignore) {
		}
		return null;
	}

	private static String normalizePhone(String raw) {
		if (raw == null)
			return "";
		String digits = raw.replaceAll("\\D+", "");
		if (digits.length() == 11 && digits.startsWith("010")) {
			return "010-" + digits.substring(3, 7) + "-" + digits.substring(7);
		}
		return digits; // 규칙에 안 맞으면 있는 그대로
	}

	private static java.util.List<Student> dedupeByNamePhone(java.util.List<Student> in) {
		java.util.LinkedHashMap<String, Student> map = new java.util.LinkedHashMap<>();
		for (Student s : in) {
			String key = (safe(s.getsName()) + "|" + safe(s.getsNum())).toLowerCase(java.util.Locale.ROOT);
			map.putIfAbsent(key, s);
		}
		return new java.util.ArrayList<>(map.values());
	}

	private static String safe(String x) {
		return x == null ? "" : x.trim();
	}

}
