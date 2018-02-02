package com.jremoter.core.app.biz.impl;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.InitialMethod;
import com.jremoter.core.annotation.Service;
import com.jremoter.core.app.biz.IMemberBiz;

@Service
public class DemoMemberBizImpl implements IMemberBiz{
	
	@Autowired("userMemberBizImpl")
	private IMemberBiz memberBiz;
	
	@Override
	public String getName(){
		return "DemoMemberBizImpl";
	}
	
	@InitialMethod
	public void initial(){
		try{
			Thread.sleep(10000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
}