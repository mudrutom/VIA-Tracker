package cz.cvut.via.tracker.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

	public Long idIssue;

	public String title;

	public String description;

	public Integer priority;

	public Long createdByUser;

	public Integer state;

	public Long getIdIssue() {
		return idIssue;
	}

	public void setIdIssue(Long idIssue) {
		this.idIssue = idIssue;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Issue issue = (Issue) o;

		if (createdByUser != null ? !createdByUser.equals(issue.createdByUser) : issue.createdByUser != null) return false;
		if (description != null ? !description.equals(issue.description) : issue.description != null) return false;
		if (idIssue != null ? !idIssue.equals(issue.idIssue) : issue.idIssue != null) return false;
		if (priority != null ? !priority.equals(issue.priority) : issue.priority != null) return false;
		if (state != null ? !state.equals(issue.state) : issue.state != null) return false;
		if (title != null ? !title.equals(issue.title) : issue.title != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = idIssue != null ? idIssue.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (priority != null ? priority.hashCode() : 0);
		result = 31 * result + (createdByUser != null ? createdByUser.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Issue{");
		sb.append("idIssue=").append(idIssue);
		sb.append(", title='").append(title).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", priority=").append(priority);
		sb.append(", createdByUser=").append(createdByUser);
		sb.append(", state=").append(state);
		sb.append('}');
		return sb.toString();
	}
}
