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
		projectData.get(0).addTeam(new Team("NeonCoders", 2));
		projectData.get(0).addTeam(new Team("BitByBit", 3));
		projectData.get(0).addTeam(new Team("코드앤소울", 4));
		projectData.get(0).addTeam(new Team("DevVerse", 1));
		projectData.get(0).addTeam(new Team("Studio404", 2));
		projectData.get(0).addTeam(new Team("Codyssey", 3));
		projectData.get(0).addTeam(new Team("PixelPushers", 4));
		projectData.get(0).addTeam(new Team("VibeCoders", 1));
		projectData.get(0).addTeam(new Team("Canvas.dev", 2));
		projectData.get(0).addTeam(new Team("CodeBloom", 3));
		projectData.get(0).addTeam(new Team("레벨업코더", 4));
		projectData.get(0).addTeam(new Team("파이널디버그", 1));
		projectData.get(0).addTeam(new Team("코딩몬스터", 2));
		projectData.get(0).addTeam(new Team("스타개발단", 3));
		projectData.get(0).addTeam(new Team("코드워즈", 4));
		projectData.get(0).addTeam(new Team("어몽코더스", 1));
		projectData.get(0).addTeam(new Team("롤백엔더스", 2));
		projectData.get(1).addTeam(new Team("너의코드를믿어", 3));
		projectData.get(1).addTeam(new Team("이노코드", 4));
		projectData.get(1).addTeam(new Team("코드맛집", 1));
		projectData.get(1).addTeam(new Team("멘붕개발단", 2));
		projectData.get(1).addTeam(new Team("야근장인들", 3));
		projectData.get(1).addTeam(new Team("코딩의민족", 4));
		projectData.get(1).addTeam(new Team("함수가족", 1));
		projectData.get(1).addTeam(new Team("마감요정들", 2));
		projectData.get(1).addTeam(new Team("코드장인들", 3));
		projectData.get(1).addTeam(new Team("안자고코딩해", 4));
		projectData.get(1).addTeam(new Team("오류는내운명", 1));
		projectData.get(1).addTeam(new Team("코드토끼", 2));
		projectData.get(1).addTeam(new Team("디지털사무라이", 3));
		projectData.get(1).addTeam(new Team("버그버스터즈", 4));
		projectData.get(1).addTeam(new Team("키보드캣츠", 1));
		projectData.get(1).addTeam(new Team("알고리듬베이커리", 2));
		projectData.get(1).addTeam(new Team("해커톤히어로즈", 3));
		projectData.get(1).addTeam(new Team("무한루프러버즈", 4));
		projectData.get(2).addTeam(new Team("세그폴트세대", 1));
		projectData.get(2).addTeam(new Team("HTML하마들", 2));
		projectData.get(2).addTeam(new Team("개발자도감", 3));
		projectData.get(2).addTeam(new Team("포트폴리오빌더즈", 4));
		projectData.get(2).addTeam(new Team("데브챌린저스", 1));
		projectData.get(2).addTeam(new Team("오늘도성장", 2));
		projectData.get(2).addTeam(new Team("코딩하루한줄", 3));
		projectData.get(2).addTeam(new Team("DevUp", 4));
		projectData.get(2).addTeam(new Team("코드온더라이즈", 1));
		projectData.get(2).addTeam(new Team("쌓고또쌓고", 2));
		projectData.get(2).addTeam(new Team("도전하는개발자들", 3));
		projectData.get(2).addTeam(new Team("NEXTjenerasion", 4));
		projectData.get(2).addTeam(new Team("나노코더스", 1));
		projectData.get(2).addTeam(new Team("DevStorm", 2));
		projectData.get(2).addTeam(new Team("코드파이터즈", 3));
		projectData.get(2).addTeam(new Team("Project Phoenix", 4));
		projectData.get(2).addTeam(new Team("블랙핵커즈", 1));
		projectData.get(2).addTeam(new Team("Reboot Rebels", 2));
		projectData.get(3).addTeam(new Team("Infinity Coders", 3));
		projectData.get(3).addTeam(new Team("CodeCore", 4));
		projectData.get(3).addTeam(new Team("HackForge", 1));
		projectData.get(3).addTeam(new Team("IronStack", 2));
		projectData.get(3).addTeam(new Team("Command+Team", 3));
		projectData.get(3).addTeam(new Team("ThinkCode", 4));
		projectData.get(3).addTeam(new Team("Codetelligence", 1));
		projectData.get(3).addTeam(new Team("알고리듬스쿼드", 2));
		projectData.get(3).addTeam(new Team("창조적파괴자들", 3));
		projectData.get(3).addTeam(new Team("SmartStack", 4));
		projectData.get(3).addTeam(new Team("MindBuilders", 1));
		projectData.get(3).addTeam(new Team("DevDynamos", 2));
		projectData.get(3).addTeam(new Team("어디서든코딩", 3));
		projectData.get(3).addTeam(new Team("불타는코더들", 4));
		projectData.get(3).addTeam(new Team("야근은싫어요", 1));
		projectData.get(3).addTeam(new Team("이게되네팀", 2));
		projectData.get(3).addTeam(new Team("페이지404", 3));
		projectData.get(3).addTeam(new Team("일단만들고보자", 4));
		projectData.get(4).addTeam(new Team("잘될지도몰라", 1));
		projectData.get(4).addTeam(new Team("코딩고민상담소", 2));
		projectData.get(4).addTeam(new Team("시간없조", 3));
		projectData.get(4).addTeam(new Team("구글링장인들", 4));
		projectData.get(4).addTeam(new Team("개발?우리가하조", 1));
		projectData.get(4).addTeam(new Team("버그는우리친구", 2));
		projectData.get(4).addTeam(new Team("키보드워리어즈", 3));
		projectData.get(4).addTeam(new Team("디버그생활", 4));
		projectData.get(4).addTeam(new Team("수면보다개발", 1));
		projectData.get(4).addTeam(new Team("커피앤코드", 2));
		projectData.get(4).addTeam(new Team("TMI Devs", 3));
		projectData.get(4).addTeam(new Team("코드국민", 4));
		projectData.get(4).addTeam(new Team("디버깅은내운명", 1));
		projectData.get(4).addTeam(new Team("괜찮아커밋했어", 2));
		projectData.get(4).addTeam(new Team("풀스택맛집", 3));
		projectData.get(4).addTeam(new Team("일단돌려보자", 4));
		projectData.get(4).addTeam(new Team("함수형사건", 1));
		projectData.get(4).addTeam(new Team("마감이곧동기부여", 2));
		projectData.get(5).addTeam(new Team("코딩좀치는사람들", 3));
		projectData.get(5).addTeam(new Team("우리팀프진짜함", 4));
		projectData.get(5).addTeam(new Team("컴파일안돼조", 1));
		projectData.get(5).addTeam(new Team("깃충들", 2));
		projectData.get(5).addTeam(new Team("개발자되조", 3));
		projectData.get(5).addTeam(new Team("코딩이제그만", 4));
		projectData.get(5).addTeam(new Team("제출5분전", 1));
		projectData.get(5).addTeam(new Team("버튼하나안눌러져조", 2));
		projectData.get(5).addTeam(new Team("이게왜돼조", 3));
		projectData.get(5).addTeam(new Team("비동기개발단", 4));
		projectData.get(5).addTeam(new Team("배포는다음생에", 1));
		projectData.get(5).addTeam(new Team("코드엔딩", 2));
		projectData.get(5).addTeam(new Team("프론트에서끝낸다", 3));
		projectData.get(5).addTeam(new Team("개발의민족", 4));
		projectData.get(5).addTeam(new Team("취업하조", 1));
		projectData.get(5).addTeam(new Team("끝내고싶조", 2));
		projectData.get(5).addTeam(new Team("배운대로했조", 3));
		projectData.get(5).addTeam(new Team("터미널과나", 4));
		projectData.get(6).addTeam(new Team("코딩지옥에서온그대", 1));
		projectData.get(6).addTeam(new Team("포트폴리오조차없조", 2));
		projectData.get(6).addTeam(new Team("새벽두시커밋", 3));
		projectData.get(6).addTeam(new Team("팀이름뭐하지", 4));
		projectData.get(6).addTeam(new Team("카페인조", 1));
		projectData.get(6).addTeam(new Team("콘솔로그맛집", 2));
		projectData.get(6).addTeam(new Team("조건문달인들", 3));
		projectData.get(6).addTeam(new Team("스택쌓는중", 4));
		projectData.get(6).addTeam(new Team("내코드왜안돼", 1));
		projectData.get(6).addTeam(new Team("컴포넌트괴물들", 2));
		projectData.get(6).addTeam(new Team("스코프밖에서놀자", 3));
		projectData.get(6).addTeam(new Team("다같이에러내기", 4));
		projectData.get(6).addTeam(new Team("클래스보다함수", 1));
		projectData.get(6).addTeam(new Team("이벤트리스너들", 2));
		projectData.get(6).addTeam(new Team("콜백지옥주민들", 3));
		projectData.get(6).addTeam(new Team("코딩왕초보단", 4));
		projectData.get(6).addTeam(new Team("프젝해치우자", 1));
		projectData.get(6).addTeam(new Team("앗.앱이.멈췄어요", 2));
		projectData.get(7).addTeam(new Team("렌더링지옥", 3));
		projectData.get(7).addTeam(new Team("코드한줄에밤샘", 4));
		projectData.get(7).addTeam(new Team("우리는팀이다()", 1));
		projectData.get(7).addTeam(new Team("이걸왜우리가", 2));
		projectData.get(7).addTeam(new Team("CSS수련회", 3));
		projectData.get(7).addTeam(new Team("F12정령들", 4));
		projectData.get(7).addTeam(new Team("다음줄에서에러남", 1));
		projectData.get(7).addTeam(new Team("에러로그수집단", 2));
		projectData.get(7).addTeam(new Team("비주얼스튜던트", 3));
		projectData.get(7).addTeam(new Team("이력서에쓸거임", 4));
		projectData.get(7).addTeam(new Team("오늘도렌더링", 1));
		projectData.get(7).addTeam(new Team("뇌를갈아넣는중", 2));
		projectData.get(7).addTeam(new Team("키보드뽀개조", 3));
		projectData.get(7).addTeam(new Team("나도개발자되조", 4));
		projectData.get(7).addTeam(new Team("로그인안돼조", 1));
		projectData.get(7).addTeam(new Team("쿼리파이터즈", 2));
		//		*/
		//학생 추가
//		projectData.get(0).getTeams().get(0).addMember(new Student("name","tel"));
	}//생성자
}