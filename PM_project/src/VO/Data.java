package VO;

import java.util.ArrayList;

public class Data {
	public static ArrayList<Project> projectData = new ArrayList<>();

	public Data() {
		//반 추가
		projectData.add(new Project("[Microsoft] 인공지능 sw 아카데미 5", "종로 501호"));//5
		projectData.add(new Project("언리얼엔진5 게임 프로그래밍 부트캠프 8", "종로 502호"));//8
		projectData.add(new Project("데이터분석&AI엔지니어링 마스터 과정 6", "종로 503호"));//6
		projectData.add(new Project("Java 풀스택 아카데미 2", "종로 504호"));//2
		projectData.add(new Project("생성형 ai 활용 msa기반 풀스택 교육 7", "종로 505호"));//7
		projectData.add(new Project("AWS Cloud School 1", "강남 602호"));//1
		projectData.add(new Project("Java&Spring 기반 풀스택 개발자 양성 과정 3", "강남 603호"));//3
		projectData.add(new Project("Node.js 백엔드 엔지니어 트랙 4", "강남 604호"));//4
		//팀 추가
		//		/*
		projectData.get(0).addTeam(new Team("BitByBit", 2));
		projectData.get(0).addTeam(new Team("Canvas.dev", 1));
		projectData.get(0).addTeam(new Team("CodeBloom", 2));
		projectData.get(0).addTeam(new Team("Codyssey", 2));
		projectData.get(0).addTeam(new Team("DevVerse", 4));
		projectData.get(0).addTeam(new Team("NeonCoders", 1));
		projectData.get(0).addTeam(new Team("PixelPushers", 3));
		projectData.get(0).addTeam(new Team("Studio404", 1));
		projectData.get(0).addTeam(new Team("VibeCoders", 4));
		projectData.get(0).addTeam(new Team("레벨업코더", 3));
		projectData.get(0).addTeam(new Team("스타개발단", 2));
		projectData.get(0).addTeam(new Team("코드앤소울", 3));
		projectData.get(0).addTeam(new Team("코드워즈", 3));
		projectData.get(0).addTeam(new Team("코딩몬스터", 1));
		projectData.get(0).addTeam(new Team("파이널디버그", 4));
		projectData.get(1).addTeam(new Team("너의코드를믿어", 2));
		projectData.get(1).addTeam(new Team("디지털사무라이", 2));
		projectData.get(1).addTeam(new Team("롤백엔더스", 1));
		projectData.get(1).addTeam(new Team("마감요정들", 1));
		projectData.get(1).addTeam(new Team("멘붕개발단", 1));
		projectData.get(1).addTeam(new Team("버그버스터즈", 3));
		projectData.get(1).addTeam(new Team("안자고코딩해", 3));
		projectData.get(1).addTeam(new Team("야근장인들", 2));
		projectData.get(1).addTeam(new Team("어몽코더스", 4));
		projectData.get(1).addTeam(new Team("오류는내운명", 4));
		projectData.get(1).addTeam(new Team("이노코드", 3));
		projectData.get(1).addTeam(new Team("코드맛집", 4));
		projectData.get(1).addTeam(new Team("코드장인들", 2));
		projectData.get(1).addTeam(new Team("코드토끼", 1));
		projectData.get(1).addTeam(new Team("코딩의민족", 3));
		projectData.get(1).addTeam(new Team("함수가족", 4));
		projectData.get(2).addTeam(new Team("DevUp", 3));
		projectData.get(2).addTeam(new Team("HTML하마들", 1));
		projectData.get(2).addTeam(new Team("NEXTjenerasion", 3));
		projectData.get(2).addTeam(new Team("개발자도감", 2));
		projectData.get(2).addTeam(new Team("데브챌린저스", 4));
		projectData.get(2).addTeam(new Team("도전하는개발자들", 2));
		projectData.get(2).addTeam(new Team("무한루프러버즈", 3));
		projectData.get(2).addTeam(new Team("세그폴트세대", 4));
		projectData.get(2).addTeam(new Team("쌓고또쌓고", 1));
		projectData.get(2).addTeam(new Team("알고리듬베이커리", 1));
		projectData.get(2).addTeam(new Team("오늘도성장", 1));
		projectData.get(2).addTeam(new Team("코드온더라이즈", 4));
		projectData.get(2).addTeam(new Team("코딩하루한줄", 2));
		projectData.get(2).addTeam(new Team("키보드캣츠", 4));
		projectData.get(2).addTeam(new Team("포트폴리오빌더즈", 3));
		projectData.get(2).addTeam(new Team("해커톤히어로즈", 2));
		projectData.get(3).addTeam(new Team("CodeCore", 3));
		projectData.get(3).addTeam(new Team("Codetelligence", 4));
		projectData.get(3).addTeam(new Team("Command+Team", 2));
		projectData.get(3).addTeam(new Team("DevStorm", 1));
		projectData.get(3).addTeam(new Team("HackForge", 4));
		projectData.get(3).addTeam(new Team("Infinity Coders", 2));
		projectData.get(3).addTeam(new Team("IronStack", 1));
		projectData.get(3).addTeam(new Team("Project Phoenix", 3));
		projectData.get(3).addTeam(new Team("Reboot Rebels", 1));
		projectData.get(3).addTeam(new Team("SmartStack", 3));
		projectData.get(3).addTeam(new Team("ThinkCode", 3));
		projectData.get(3).addTeam(new Team("나노코더스", 4));
		projectData.get(3).addTeam(new Team("블랙핵커즈", 4));
		projectData.get(3).addTeam(new Team("알고리듬스쿼드", 1));
		projectData.get(3).addTeam(new Team("창조적파괴자들", 2));
		projectData.get(3).addTeam(new Team("코드파이터즈", 2));
		projectData.get(4).addTeam(new Team("DevDynamos", 1));
		projectData.get(4).addTeam(new Team("MindBuilders", 4));
		projectData.get(4).addTeam(new Team("개발?우리가하조", 4));
		projectData.get(4).addTeam(new Team("구글링장인들", 3));
		projectData.get(4).addTeam(new Team("디버그생활", 3));
		projectData.get(4).addTeam(new Team("버그는우리친구", 1));
		projectData.get(4).addTeam(new Team("불타는코더들", 3));
		projectData.get(4).addTeam(new Team("시간없조", 2));
		projectData.get(4).addTeam(new Team("야근은싫어요", 4));
		projectData.get(4).addTeam(new Team("어디서든코딩", 2));
		projectData.get(4).addTeam(new Team("이게되네팀", 1));
		projectData.get(4).addTeam(new Team("일단만들고보자", 3));
		projectData.get(4).addTeam(new Team("잘될지도몰라", 4));
		projectData.get(4).addTeam(new Team("코딩고민상담소", 1));
		projectData.get(4).addTeam(new Team("키보드워리어즈", 2));
		projectData.get(4).addTeam(new Team("페이지404", 2));
		projectData.get(5).addTeam(new Team("TMI Devs", 2));
		projectData.get(5).addTeam(new Team("개발자되조", 2));
		projectData.get(5).addTeam(new Team("괜찮아커밋했어", 1));
		projectData.get(5).addTeam(new Team("깃충들", 1));
		projectData.get(5).addTeam(new Team("디버깅은내운명", 4));
		projectData.get(5).addTeam(new Team("마감이곧동기부여", 1));
		projectData.get(5).addTeam(new Team("수면보다개발", 4));
		projectData.get(5).addTeam(new Team("우리팀프진짜함", 3));
		projectData.get(5).addTeam(new Team("일단돌려보자", 3));
		projectData.get(5).addTeam(new Team("커피앤코드", 1));
		projectData.get(5).addTeam(new Team("컴파일안돼조", 4));
		projectData.get(5).addTeam(new Team("코드국민", 3));
		projectData.get(5).addTeam(new Team("코딩이제그만", 3));
		projectData.get(5).addTeam(new Team("코딩좀치는사람들", 2));
		projectData.get(5).addTeam(new Team("풀스택맛집", 2));
		projectData.get(5).addTeam(new Team("함수형사건", 4));
		projectData.get(6).addTeam(new Team("개발의민족", 3));
		projectData.get(6).addTeam(new Team("끝내고싶조", 1));
		projectData.get(6).addTeam(new Team("배운대로했조", 2));
		projectData.get(6).addTeam(new Team("배포는다음생에", 4));
		projectData.get(6).addTeam(new Team("버튼하나안눌러져조", 1));
		projectData.get(6).addTeam(new Team("비동기개발단", 3));
		projectData.get(6).addTeam(new Team("새벽두시커밋", 2));
		projectData.get(6).addTeam(new Team("이게왜돼조", 2));
		projectData.get(6).addTeam(new Team("제출5분전", 4));
		projectData.get(6).addTeam(new Team("취업하조", 4));
		projectData.get(6).addTeam(new Team("코드엔딩", 1));
		projectData.get(6).addTeam(new Team("코딩지옥에서온그대", 4));
		projectData.get(6).addTeam(new Team("터미널과나", 3));
		projectData.get(6).addTeam(new Team("팀이름뭐하지", 3));
		projectData.get(6).addTeam(new Team("포트폴리오조차없조", 1));
		projectData.get(6).addTeam(new Team("프론트에서끝낸다", 2));
		projectData.get(7).addTeam(new Team("내코드왜안돼", 4));
		projectData.get(7).addTeam(new Team("다같이에러내기", 3));
		projectData.get(7).addTeam(new Team("렌더링지옥", 2));
		projectData.get(7).addTeam(new Team("스코프밖에서놀자", 2));
		projectData.get(7).addTeam(new Team("스택쌓는중", 3));
		projectData.get(7).addTeam(new Team("앗.앱이.멈췄어요", 1));
		projectData.get(7).addTeam(new Team("이벤트리스너들", 1));
		projectData.get(7).addTeam(new Team("조건문달인들", 2));
		projectData.get(7).addTeam(new Team("카페인조", 4));
		projectData.get(7).addTeam(new Team("컴포넌트괴물들", 1));
		projectData.get(7).addTeam(new Team("코드한줄에밤샘", 3));
		projectData.get(7).addTeam(new Team("코딩왕초보단", 3));
		projectData.get(7).addTeam(new Team("콘솔로그맛집", 1));
		projectData.get(7).addTeam(new Team("콜백지옥주민들", 2));
		projectData.get(7).addTeam(new Team("클래스보다함수", 4));
		projectData.get(7).addTeam(new Team("프젝해치우자", 4));
		//학생 데이터 추가하기

		//		*/
		//학생 추가
		//		projectData.get(0).getTeams().get(0).addMember(new Student("name","tel"));
	}//생성자
}