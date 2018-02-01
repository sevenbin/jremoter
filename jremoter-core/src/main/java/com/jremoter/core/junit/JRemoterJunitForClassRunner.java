package com.jremoter.core.junit;

import java.io.IOException;

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
	protected Statement withBeforeClasses(Statement statement) {
		this.applicationContext.refresh();
		return super.withBeforeClasses(statement);
	}

	@Override
	protected Statement withAfterClasses(Statement statement){
		try{
			this.applicationContext.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return super.withAfterClasses(statement);
	}

}