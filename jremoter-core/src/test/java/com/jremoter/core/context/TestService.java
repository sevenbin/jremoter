package com.jremoter.core.context;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.DestoryMethod;
import com.jremoter.core.annotation.InitialMethod;
import com.jremoter.core.annotation.Service;
import com.jremoter.core.annotation.Value;

@Service
public class TestService {
	
	@Value("jremoter.package_scanner")
	private String name;
	@Autowired
	private DemoService demoService;
	
	public String getName(){
		System.out.println(this.name);
		System.out.println("TS -> " + this.demoService.demo());
		return TestService.class.getName();
	}
	
	@InitialMethod(sort=2)
	public void initB(){
		System.out.println("initB");
	}
	
	@InitialMethod(sort=3)
	public void initC(){
		System.out.println("initC");
	}
	
	@InitialMethod(sort=1)
	public void initA(){
		System.out.println("initA");
	}
	
	@InitialMethod(sort=4)
	public void initD(){
		System.out.println("initD");
	}
	
	
	@DestoryMethod(sort=2)
	public void destB(){
		System.out.println("destB");
	}
	
	@DestoryMethod(sort=3)
	public void destC(){
		System.out.println("destC");
	}
	
	@DestoryMethod(sort=1)
	public void destA(){
		System.out.println("destA");
	}
	
	@DestoryMethod(sort=4)
	public void destD(){
		System.out.println("destD");
	}
	
}