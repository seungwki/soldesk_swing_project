package testment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class seperatorNonprintableChanger {
	public static void main(String[] args) throws Exception {
		String path = "resource\\data\\SessionList";
//		String path = "resource\\data\\LectureList";
		String extension = ".txt";
		File file = new File(path + extension);
		FileReader fr = new FileReader(file);

		String content;
		int data;
		StringBuilder sb = new StringBuilder();

		while ((data = fr.read()) != -1) {
			if (data == ',') {
				data = 3;
			}
			if (data == '&') {
				data = 4;
			}
			System.out.print((char) data);
			sb.append((char) data);
		} // while
		fr.close();
		// 닫아주고 실행하지 않으면 입출력 통로가 꼬여서 제대로 출력이 안 됨...
		FileWriter fw = new FileWriter(file);
		content = sb.toString();
		for (int i = 0; i < content.length(); i++) {
			fw.write(content.charAt(i));
		} // for
		fw.close();
	}
}
