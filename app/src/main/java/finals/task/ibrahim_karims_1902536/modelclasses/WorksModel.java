//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536.modelclasses;

import java.io.Serializable;

public class WorksModel implements Serializable {
     String title;
    String description;
    String link;
    String publishDate;
    String locPoints;

    public WorksModel(String title, String description, String link, String publishDate, String locPoints) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishDate = publishDate;
        this.locPoints = locPoints;
    }

    public WorksModel() {
    }

    public String getLocPoints() {
        return locPoints;
    }

    public void setLocPoints(String locPoints) {
        this.locPoints = locPoints;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
