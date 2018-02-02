package com.jremoter.core.app.biz.impl;

import com.jremoter.core.annotation.Service;
import com.jremoter.core.app.biz.IMemberBiz;

@Service
public class UserMemberBizImpl implements IMemberBiz{

	@Override
	public String getName(){
		return "UserMemberBizImpl";
	}
	
}