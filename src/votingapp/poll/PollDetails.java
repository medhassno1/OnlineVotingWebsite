package votingapp.poll
;

import java.sql.Date;

// this class can be used to create an object that stores all details of a poll
public class PollDetails {
	
	private String owner = null;
	private String title = null;
	private String question = null;
	private String option_1 = null;
	private String option_2 = null;
	private String option_3 = null;
	private String option_4 = null;
	private java.sql.Date deadline = null;
	
	public PollDetails(String owner, String title, String question, String option_1, String option_2, String option_3, String option_4, java.sql.Date deadline) {
		this.owner = owner;
		this.title = title;
		this.question = question;
		this.option_1 = option_1;
		this.option_2 = option_2;
		this.option_3 = option_3;
		this.option_4 = option_4;
		this.deadline = deadline;
		
	}

	public String getOwner(){
		return owner;
	}
	
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public void setQuestion(String question){
		this.question = question;
	}
	
	public String getOption1(){
		return option_1;
	}
	
	public void setOption1(String option_1){
		this.option_1 = option_1;
	}
	
	public String getOption2(){
		return option_2;
	}
	
	public void setOption2(String option_2){
		this.option_2 = option_2;
	}
	
	public String getOption3(){
		return option_3;
	}
	
	public void setOption3(String option_3){
		this.option_3 = option_3;
	}
	
	public String getOption4(){
		return option_4;
	}
	
	public void setOption4(String option_4){
		this.option_4 = option_4;
	}

	public java.sql.Date getDeadline(){
		return deadline;
	}
	
	public void setDeadline(Date deadline){
		this.deadline = deadline;
	}
}
