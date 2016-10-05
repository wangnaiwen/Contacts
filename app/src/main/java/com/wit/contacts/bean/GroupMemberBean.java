package com.wit.contacts.bean;

public class GroupMemberBean {

	private SystemContacts systemContacts;  //显示的数据
	private String sortLetters;             //显示数据拼音的首字母

	public SystemContacts getSystemContacts() {
		return systemContacts;
	}

	public void setSystemContacts(SystemContacts systemContacts) {
		this.systemContacts = systemContacts;
	}

	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
