package front_frame;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import VO.Data;
import VO.Project;

// 수업관리 화면
public class ClassManager extends BasePage {
	// 하드코딩 프로젝트 데이터
	public static ArrayList<Project> projectData = Data.projectData;

	// 전역으로 보관
	private ClassManagerCard addCard;
	private JPanel dummy;

	public ClassManager() {
		super(new front_ui.TopBar.OnMenuClick() {
			@Override
			public void onClass() {DefaultFrame.getInstance(new ClassManager());}
			@Override
			public void onStudent() {DefaultFrame.getInstance(new StudentManager());}
			@Override
			public void onTag() {DefaultFrame.getInstance(new TagManager());}
		});
		sortProjectList();
		getTopBar().selectOnly("class");
		JPanel content = getContentPanel();
		content.setLayout(null);
		// 추가용 카드
		addCard = new ClassManagerCard(null);
		addCard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				projectCreate(null, null, content);
			}//mouseClicked(MouseEvent e)
		});
		// dummy (여백용)
		dummy = new JPanel();
		dummy.setOpaque(false);
		// 첫 렌더링
		layoutCards();
	}

	/**
	 * 전체 카드 (project 카드 + addCard + dummy) 배치
	 */
	private void layoutCards() {
		JPanel content = getContentPanel();
		content.removeAll();
		int cardWidth = 340, cardHeight = 130;
		int gapX = 30, gapY = 20;
		int x = 40, y = 20;
		// 프로젝트 카드들
		for (int i = 0; i < projectData.size(); i++) {
			Project p = projectData.get(i);
			ClassManagerCard card = new ClassManagerCard(p);
			card.setBounds(x, y, cardWidth, cardHeight);
			content.add(card);
			if ((i % 2) == 0) {
				x += cardWidth + gapX;
			} else {
				x = 40;
				y += cardHeight + gapY;
			}
		}
		// addCard
		addCard.setBounds(x, y, cardWidth, cardHeight);
		content.add(addCard);
		// dummy
		dummy.setBounds(0, y + cardHeight + 20, 1, 1);
		content.add(dummy);
		SwingUtilities.invokeLater(() -> {
			refreshScroll();
			content.revalidate();
			content.repaint();
		});//SwingUtilities.invokeLater(
	}//layoutCards()

	// 정렬
	private static void sortProjectList() {
		projectData.sort(new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				if (o1.isBookmark() != o2.isBookmark()) {
					return o1.isBookmark() ? -1 : 1;
				} else {
					if (!o1.getName().equals(o2.getName())) {
						return o1.getName().compareTo(o2.getName());
					} else {
						return o1.getPlace().compareTo(o2.getPlace());
					}
				}
			}
		});
	}//sortProjectList() {

	private void projectCreate(String title, String subtitle, JPanel content) {
		JTextField titleField = new JTextField();
		JTextField subtitleField = new JTextField();
		if (title != null) {
			titleField.setText(title);
		}
		if (subtitle != null) {
			subtitleField.setText(subtitle);
		}
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("이름(필수):"));
		panel.add(subtitleField);
		panel.add(new JLabel("장소:"));
		panel.add(titleField);
		int result = JOptionPane.showConfirmDialog(content, panel, "새 수업 추가", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String newTitle = titleField.getText().trim();
			String newSubtitle = subtitleField.getText().trim();
			if (!newSubtitle.equals("")) {
				Project newProject = new Project(newSubtitle);
				newProject.setPlace(newTitle);
				projectData.add(newProject);
				layoutCards();
			} else {
				JOptionPane.showMessageDialog(content, "제목은 비워둘 수 없습니다.");
				projectCreate(newTitle, newSubtitle, content);
			}
		}
	}//projectCreate
}
