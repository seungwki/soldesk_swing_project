package main;

import java.util.ArrayList;
import java.util.Comparator;

import VO.Data;
import VO.Project;
import VO.Team;

public class Test {
	public static void main(String[] args) {
		ArrayList<Project> data = Data.projectData;
		Data dataClass = new Data();
		for (int i = 0; i < data.size(); i++) {
			data.get(i).getTeams().sort(new Comparator<Team>() {
				@Override
				public int compare(Team o1, Team o2) {
					return o1.getDegree() - o2.getDegree();
				}
			});
		}
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(i).getTeams().size(); j++) {
				System.out.println(data.get(i).getName() + " : (" + data.get(i).getTeams().get(j).getDegree() + ")" + data.get(i).getTeams().get(j).getTName());
			}
		}
	}
}