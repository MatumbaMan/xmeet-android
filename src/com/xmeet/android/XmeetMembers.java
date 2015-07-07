package com.xmeet.android;

import java.util.ArrayList;
import java.util.List;

class XmeetMembers {

	private List<XmeetUserInfo> members = new ArrayList<XmeetUserInfo>();

	public XmeetUserInfo queryMember(String id) {
		XmeetUserInfo result = new XmeetUserInfo();
		result.id = id;
		
		for (XmeetUserInfo user : members) {
			if (user.id.equals(id)) {
				
				result.nickname = user.nickname;
				result.isSelf = user.isSelf;
				break;
			}
		}
		
		return result;
	}

	public void addMembers(List<XmeetUserInfo> members) {
		for (XmeetUserInfo user : members) {
			addMember(user);
		}
	}
	
	public void addMember(XmeetUserInfo member) {
		this.members.add(member);
	}
	
	public String removeMember(String id) {
		XmeetUserInfo user = queryMember(id);
		this.members.remove(user);
		return user.nickname;
	}
	
	public void setNewName(String name, String id) {
		for (XmeetUserInfo user : members) {
			if (user.id.equals(id)) {
				user.nickname = name;
				break;
			}
		}
	}
}
