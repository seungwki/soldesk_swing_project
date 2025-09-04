package VO;

import java.util.ArrayList;

public class Data {
	public static ArrayList<Project> projectData = new ArrayList<>();

	public Data() {
		projectData.add(new Project("[Microsoft] 인공지능 sw 아카데미 5", "종로 501호"));//5
		projectData.add(new Project("언리얼엔진5 게임 프로그래밍 부트캠프 8", "종로 502호"));//8
		projectData.add(new Project("데이터분석&AI엔지니어링 마스터 과정 6", "종로 503호"));//6
		projectData.add(new Project("Java 풀스택 아카데미 2", "종로 504호"));//2
		projectData.add(new Project("생성형 ai 활용 msa기반 풀스택 교육 7", "종로 505호"));//7
		projectData.add(new Project("AWS Cloud School 1", "강남 602호"));//1
		projectData.add(new Project("Java&Spring 기반 풀스택 개발자 양성 과정 3", "강남 603호"));//3
		projectData.add(new Project("Node.js 백엔드 엔지니어 트랙 4", "강남 604호"));//4

		for (int i = 0; i < projectData.size(); i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 4; k++) {
					projectData.get(i).addTeam(new Team("팀" + (i * 5 + j + 1), k + 1));
				}
			}
		}
	}
}