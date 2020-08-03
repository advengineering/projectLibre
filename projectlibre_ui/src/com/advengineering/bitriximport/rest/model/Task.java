package com.advengineering.bitriximport.rest.model;

import java.util.Date;
import java.util.List;

public class Task {
	
	private Integer id;
	
	private String title;
	
	private String decription;
	
	private Date createdDate;
	
	private Date startDatePlan;
	
	private Date endDatePlan;
	
	private String deadline;
	
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public List<Integer> getAccomplices() {
		return accomplices;
	}

	public void setAccomplices(List<Integer> accomplices) {
		this.accomplices = accomplices;
	}

	//TODO - to remove
	@Override
	public String toString() {
		return "Задача id=" + id + ", title=" + title;
	}
	
	

}
