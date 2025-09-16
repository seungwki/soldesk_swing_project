package front_frame;

import java.awt.*;
import javax.swing.*;
import VO.User;

public class FrameMyInfo extends JPanel {

    private final JLabel lblPhoto = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblNameV = new JLabel();
    private final JLabel lblEmailV = new JLabel();

    private final JRadioButton rbM = new JRadioButton("남");
    private final JRadioButton rbF = new JRadioButton("여");
    private final JRadioButton rbN = new JRadioButton("비공개");
    private final ButtonGroup bg = new ButtonGroup();

    public FrameMyInfo() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        JLabel title = new JLabel("내 정보");
        title.setFont(new Font("Dialog", Font.BOLD, 24));
        title.setBounds(340, 30, 200, 40);
        add(title);

        // 프로필 사진
        lblPhoto.setBounds(80, 110, 160, 160);
        lblPhoto.setOpaque(true);
        lblPhoto.setBackground(Color.WHITE);
        lblPhoto.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        add(lblPhoto);

        // 이름
        JLabel lblName = new JLabel("이름:");
        lblName.setBounds(280, 120, 80, 30);
        add(lblName);
        lblNameV.setBounds(360, 120, 200, 30);
        add(lblNameV);

        // 이메일
        JLabel lblEmail = new JLabel("이메일:");
        lblEmail.setBounds(280, 165, 80, 30);
        add(lblEmail);
        lblEmailV.setBounds(360, 165, 260, 30);
        add(lblEmailV);

        // 성별
        JLabel lblGender = new JLabel("성별:");
        lblGender.setBounds(280, 210, 80, 30);
        add(lblGender);

        rbM.setBounds(360, 210, 60, 30);
        rbF.setBounds(430, 210, 60, 30);
        rbN.setBounds(500, 210, 80, 30);

        bg.add(rbM); bg.add(rbF); bg.add(rbN);
        add(rbM); add(rbF); add(rbN);

        JButton btnSave = new JButton("저장");
        btnSave.setBounds(360, 270, 100, 35);
        add(btnSave);

        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(480, 270, 100, 35);
        add(btnBack);

        // 데이터 바인딩
        User me = UserManager.getInstance().getCurrentUser();
        if (me != null) {
            lblNameV.setText(me.getName() == null ? "" : me.getName());
            lblEmailV.setText(me.getEmail() == null ? "" : me.getEmail());

            String g = me.getGender();
            if ("남".equals(g)) rbM.setSelected(true);
            else if ("여".equals(g)) rbF.setSelected(true);
            else rbN.setSelected(true);

            updatePhoto(currentGender());
        } else {
            // 세션이 없을 때 기본 상태
            rbN.setSelected(true);
            updatePhoto("비공개");
        }

        // 성별 변경 시 사진 즉시 반영
        java.awt.event.ActionListener onGender = e -> updatePhoto(currentGender());
        rbM.addActionListener(onGender);
        rbF.addActionListener(onGender);
        rbN.addActionListener(onGender);

        // 저장
        btnSave.addActionListener(e -> {
        	
            User u = UserManager.getInstance().getCurrentUser();
            if (u == null) {
                JOptionPane.showMessageDialog(this, "로그인/회원정보를 찾을 수 없습니다.");
                return;
            }
            String g = currentGender();
            u.setGender(g);  // ← 실제로 성별 업데이트
            UserManager.getInstance().notifyProfileChanged(); // ← TopBar에 갱신 알림
            JOptionPane.showMessageDialog(this, "저장되었습니다.");
            DefaultFrame.getInstance(new ClassManager());
            
        });

        btnBack.addActionListener(e -> DefaultFrame.getInstance(new ClassManager()));
    }

    private String currentGender() {
        if (rbM.isSelected()) return "남";
        if (rbF.isSelected()) return "여";
        return "비공개";
    }

    private void updatePhoto(String gender) {
        String file;
        switch (gender) {
            case "남": file = "profile_male.png"; break;
            case "여": file = "profile_female.png"; break;
            default:   file = "profile_secret.png"; break;
        }
        lblPhoto.setIcon(loadIconFromFS(file, 160, 160));
        lblPhoto.setText(lblPhoto.getIcon() == null ? "이미지 없음" : "");
    }

    // 2) 헬퍼 추가 (프로젝트 루트 및 ./img 둘 다 시도)
    private ImageIcon loadIconFromFS(String filename, int w, int h) {
        java.nio.file.Path p = java.nio.file.Paths.get(System.getProperty("user.dir"), filename);
        if (!java.nio.file.Files.exists(p)) {
            p = java.nio.file.Paths.get(System.getProperty("user.dir"), "img", filename);
        }
        if (!java.nio.file.Files.exists(p)) {
            System.out.println("이미지 못 찾음: " + p.toAbsolutePath());
            return null;
        }
        ImageIcon icon = new ImageIcon(p.toString());
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

}
