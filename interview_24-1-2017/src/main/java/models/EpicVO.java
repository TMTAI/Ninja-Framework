package models;

import java.util.List;
import java.util.Map;

/**
 * Created by nnthat on 3/3/17.
 */
public class EpicVO {
    private String project;
    private String key;
    private String summary;
    private String type;
    private String priority;
    private String status;
    private String resolution;
    private String security;
    private String assignee;
    private String reporter;
    private String created;
    private String updated;
    private String resolved;
    private String votes;
    private String watches;
    private String component;
    private String epicPriority;
    private String fo;
    private String ia;
    private String numberOfUS;
    private String product;
    private String storyPoints;
    private String wikiEstimate;
    private String targetWikiReviewedDate;
    private List<String> userStoryList;
    private List<UserStoryVO> userStoryVOS;
    private Map<String, Object> userStoryCounter;

    public EpicVO() {
    }

    public EpicVO(EpicVO epicVO) {
        this.project = epicVO.getProject();
        this.key = epicVO.getKey();
        this.summary = epicVO.getSummary();
        this.type = epicVO.getType();
        this.priority = epicVO.getPriority();
        this.status = epicVO.getStatus();
        this.assignee = epicVO.getAssignee();
        this.reporter = epicVO.getReporter();
        this.component = epicVO.getComponent();
        this.epicPriority = epicVO.getEpicPriority();
        this.fo = epicVO.getFo();
        this.ia = epicVO.getIa();
        this.numberOfUS = epicVO.getNumberOfUS();
        this.product = epicVO.getProduct();
        this.storyPoints = epicVO.getStoryPoints();
        this.wikiEstimate = epicVO.getWikiEstimate();
        this.targetWikiReviewedDate = epicVO.getTargetWikiReviewedDate();
        this.userStoryList = epicVO.getUserStoryList();
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getEpicPriority() {
        return epicPriority;
    }

    public void setEpicPriority(String epicPriority) {
        this.epicPriority = epicPriority;
    }

    public String getFo() {
        return fo;
    }

    public void setFo(String fo) {
        this.fo = fo;
    }

    public String getIa() {
        return ia;
    }

    public void setIa(String ia) {
        this.ia = ia;
    }

    public String getNumberOfUS() {
        return numberOfUS;
    }

    public void setNumberOfUS(String numberOfUS) {
        this.numberOfUS = numberOfUS;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(String storyPoints) {
        this.storyPoints = storyPoints;
    }

    public List<String> getUserStoryList() {
        return userStoryList;
    }

    public void setUserStoryList(List<String> userStoryList) {
        this.userStoryList = userStoryList;
    }

    public String getWikiEstimate() {
        return wikiEstimate;
    }

    public void setWikiEstimate(String wikiEstimate) {
        this.wikiEstimate = wikiEstimate;
    }

    public String getTargetWikiReviewedDate() {
        return targetWikiReviewedDate;
    }

    public void setTargetWikiReviewedDate(String targetWikiReviewedDate) {
        this.targetWikiReviewedDate = targetWikiReviewedDate;
    }

    public List<UserStoryVO> getUserStoryVOS() {
        return userStoryVOS;
    }

    public void setUserStoryVOS(List<UserStoryVO> userStoryVOS) {
        this.userStoryVOS = userStoryVOS;
    }

    public Map<String, Object> getUserStoryCounter() {
        return userStoryCounter;
    }

    public void setUserStoryCounter(Map<String, Object> userStoryCounter) {
        this.userStoryCounter = userStoryCounter;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getWatches() {
        return watches;
    }

    public void setWatches(String watches) {
        this.watches = watches;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }
}
