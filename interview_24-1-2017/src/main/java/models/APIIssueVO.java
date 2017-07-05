package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import models.JQLIssuetypeVO.Type;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIIssueVO {
    private String key;
    private String self;
    private String summary;
    private PriorityVO priority;
    private Type type;

    public APIIssueVO() {
    }

    public APIIssueVO(String key, String self) {
        this.key = key;
        this.self = self;
    }

    public APIIssueVO(String key, String self, String summary) {
        this.key = key;
        this.self = self;
        this.summary = summary;
    }

    public PriorityVO getPriority() {
        return priority;
    }

    public void setPriority(PriorityVO priority) {
        this.priority = priority;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result
				+ ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((self == null) ? 0 : self.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		APIIssueVO other = (APIIssueVO) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (self == null) {
			if (other.self != null)
				return false;
		} else if (!self.equals(other.self))
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
