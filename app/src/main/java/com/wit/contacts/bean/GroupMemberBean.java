package com.wit.contacts.bean;

public class GroupMemberBean {

	private SystemContacts systemContacts;  //��ʾ������
	private String sortLetters;             //��ʾ����ƴ��������ĸ

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
