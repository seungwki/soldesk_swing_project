package front_frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.*;
import javax.swing.*;

public class GcProfile extends JPanel {
    private final int targetW;
    private final int targetH;
    private final JLabel pic;
//    private final String Gcprofile = "profile.png";
    private final String Gcprofile = "resource\\image\\profile.png";
    
// 프로필 크기
    public GcProfile() {
        this(100, 100);
    }

    public GcProfile(int width, int height) {
        this.targetW = width;
        this.targetH = height;

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(targetW, targetH));

        // 이미지 로드 + 고품질 리샘플링 (한 번만)
        ImageIcon icon = new ImageIcon(scaleHighQuality(loadImage(Gcprofile), targetW, targetH));

        pic = new JLabel(icon);
        pic.setBounds(0, 0, targetW, targetH);
        add(pic);
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            // 로드 실패 시 회색 placeholder
            BufferedImage placeholder = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(new Color(230, 230, 230));
            g2.fillRect(0, 0, targetW, targetH);
            g2.setColor(new Color(180, 180, 180));
            g2.drawRect(1, 1, targetW - 3, targetH - 3);
            g2.dispose();
            return placeholder;
        }
    }

    private Image scaleHighQuality(BufferedImage src, int w, int h) {
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dst.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,     RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return dst;
    }
}
