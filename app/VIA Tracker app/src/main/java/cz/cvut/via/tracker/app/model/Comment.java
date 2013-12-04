package cz.cvut.via.tracker.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

	public Long idComment;

	public String text;

	public Long assignedToIssue;

	public Long createdByUser;

	public Long getIdComment() {
		return idComment;
	}

	public void setIdComment(Long idComment) {
		this.idComment = idComment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getAssignedToIssue() {
		return assignedToIssue;
	}

	public void setAssignedToIssue(Long assignedToIssue) {
		this.assignedToIssue = assignedToIssue;
	}

	public Long getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Comment comment = (Comment) o;

		if (assignedToIssue != null ? !assignedToIssue.equals(comment.assignedToIssue) : comment.assignedToIssue != null) return false;
		if (createdByUser != null ? !createdByUser.equals(comment.createdByUser) : comment.createdByUser != null) return false;
		if (idComment != null ? !idComment.equals(comment.idComment) : comment.idComment != null) return false;
		if (text != null ? !text.equals(comment.text) : comment.text != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = idComment != null ? idComment.hashCode() : 0;
		result = 31 * result + (text != null ? text.hashCode() : 0);
		result = 31 * result + (assignedToIssue != null ? assignedToIssue.hashCode() : 0);
		result = 31 * result + (createdByUser != null ? createdByUser.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Comment{");
		sb.append("idComment=").append(idComment);
		sb.append(", text='").append(text).append('\'');
		sb.append(", assignedToIssue=").append(assignedToIssue);
		sb.append(", createdByUser=").append(createdByUser);
		sb.append('}');
		return sb.toString();
	}
}
