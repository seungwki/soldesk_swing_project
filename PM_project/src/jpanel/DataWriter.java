package jpanel;

import java.io.File;
import java.io.FileWriter;

import VO.Lecture;

public class DataWriter {
	public void lectureWrite(Lecture lecture) {
		String filePath;
		String fileExtension;
		filePath = "resource\\data\\LectureList";
		fileExtension = ".txt";
		File file = new File(filePath + fileExtension);
		try (FileWriter fw = new FileWriter(file, true)) {
			// 이름 장소 인덱스 삭제(0) 북마크(0)
			fw.write(lecture.getlName());// 이름
			fw.write((char) 3);// 구분자
			fw.write(lecture.getlPlace());// 장소
			fw.write((char) 3);// 구분자
			fw.write(String.valueOf(Lecture.getLastIndex()));// 인덱스
			fw.write((char) 3);// 구분자
			fw.write("0");// 삭제
			fw.write((char) 3);// 구분자
			fw.write("0");// 북마크
			fw.write('\n');// 종결자
			System.out.println("writeComplete");
		} catch (Exception e) {
		}
	}
}
