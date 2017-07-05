package models;

/**
 * Created by nnthat on 3/4/17.
 */
public class SubTaskVO {
    private String key;
    private String status;
    private String parent;
    private String component;

    public SubTaskVO() {
    }

    public SubTaskVO(SubTaskVO subTaskVO) {
        this.key = subTaskVO.getKey();
        this.status = subTaskVO.getStatus();
        this.parent = subTaskVO.getParent();
        this.component = subTaskVO.getComponent();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
