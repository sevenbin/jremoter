package com.jremoter.core.junit;

import java.io.IOException;
import java.util.List;

import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.context.support.AnnotationApplicationContext;
import com.jremoter.core.util.ClassUtil;

public class JRemoterJunitForClassRunner extends BlockJUnit4ClassRunner{

	private ApplicationContext applicationContext;
	private BeanContainer beanContainer;
	
	public JRemoterJunitForClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
		this.applicationContext = new AnnotationApplicationContext(klass);
		this.beanContainer = this.applicationContext.getBeanContainer();
		this.beanContainer.attachBean(JRemoterJunitForClassRunner.class,ClassUtil.getCamelClassName(JRemoterJunitForClassRunner.class),this);
	}
	
	@Override
	protected List<MethodRule> rules(Object target) {
		this.beanContainer.attachBean(target.getClass(),ClassUtil.getCamelClassName(target.getClass()),target);
		this.applicationContext.refresh();
		return super.rules(target);
	}
	
	@Override
	protected Statement withAfterClasses(final Statement statement){
		return new Statement(){
			@Override
			public void evaluate() throws Throwable{
				try{
					statement.evaluate();
					applicationContext.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
	}

}