package models;

import java.util.List;
import java.util.Map;

/**
 * Created by nnthat on 3/3/17.
 */
public class UserStoryVO extends EpicVO {
    private String epicLink;
    private String wikiReviewedDate;
    private List<String> listSubTask;
    private String sprint;
    private Map<String, Object> subTasksCounter;


    public UserStoryVO() {
    }

    public UserStoryVO(UserStoryVO userStoryVO) {
        super(userStoryVO);
        this.epicLink = userStoryVO.getEpicLink();
        this.wikiReviewedDate = userStoryVO.getWikiReviewedDate();
        this.listSubTask = userStoryVO.getListSubTask();
        this.sprint = userStoryVO.getSprint();
    }

    public String getEpicLink() {
        return epicLink;
    }

    public void setEpicLink(String epicLink) {
        this.epicLink = epicLink;
    }

    public String getWikiReviewedDate() {
        return wikiReviewedDate;
    }

    public void setWikiReviewedDate(String wikiReviewedDate) {
        this.wikiReviewedDate = wikiReviewedDate;
    }

    public List<String> getListSubTask() {
        return listSubTask;
    }

    public void setListSubTask(List<String> listSubTask) {
        this.listSubTask = listSubTask;
    }

    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }

    public Map<String, Object> getSubTasksCounter() {
        return subTasksCounter;
    }

    public void setSubTasksCounter(Map<String, Object> subTasksCounter) {
        this.subTasksCounter = subTasksCounter;
    }
}
