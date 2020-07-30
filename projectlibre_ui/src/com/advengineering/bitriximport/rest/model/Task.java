package com.advengineering.bitriximport.rest.model;

import java.util.Date;
import java.util.List;

public class Task {
	
	private Integer id;
	
	private String title;
	
	private String decription;
	
	private Date createDate;
	
	private Date startDatePlan;
	
	private Date endDatePlan;
	
	private String deadLine;
	
	private List<Integer> accomplices;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDatePlan() {
		return startDatePlan;
	}

	public void setStartDatePlan(Date startDatePlan) {
		this.startDatePlan = startDatePlan;
	}

	public Date getEndDatePlan() {
		return endDatePlan;
	}

	public void setEndDatePlan(Date endDatePlan) {
		this.endDatePlan = endDatePlan;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public List<Integer> getAccomplices() {
		return accomplices;
	}

	public void setAccomplices(List<Integer> accomplices) {
		this.accomplices = accomplices;
	}

}
