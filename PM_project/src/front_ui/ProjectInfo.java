// front_ui/ProjectInfo.java
package front_ui;

import java.util.List;

public class ProjectInfo {
    public final String title;
    public final String teamName;
    public final List<String> members;
    public final List<String> files;
    public final List<String> tags;

    public ProjectInfo(String title, String teamName,
                       List<String> members, List<String> files, List<String> tags) {
        this.title = title;
        this.teamName = teamName;
        this.members = members;
        this.files = files;
        this.tags = tags;
    }
}
