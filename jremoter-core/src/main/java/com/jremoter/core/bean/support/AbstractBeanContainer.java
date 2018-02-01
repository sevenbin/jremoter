package com.jremoter.core.bean.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jremoter.core.Constant;
import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.Service;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerHandlerChain;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.BeanDefinitionFactory;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.bean.inject.FieldBeanInject;
import com.jremoter.core.bean.inject.MethodBeanInject;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.exception.BeanCircleReferenceException;
import com.jremoter.core.exception.BeanDefinitionExistException;
import com.jremoter.core.exception.BeanDefinitionMultipleException;
import com.jremoter.core.exception.BeanDefinitionNotFoundException;
import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;
import com.jremoter.core.option.Configuration;
import com.jremoter.core.option.support.AbstractConfiguration;
import com.jremoter.core.proxy.ProxyFactory;
import com.jremoter.core.toolkit.DirectedGraph;
import com.jremoter.core.toolkit.DirectedGraphUtil;
import com.jremoter.core.toolkit.ExtensionLoader;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.ClassUtil;
import com.jremoter.core.util.ReflectionUtil;
import com.jremoter.core.util.StringUtil;

public abstract class AbstractBeanContainer implements BeanContainer{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractBeanContainer.class);
	
	protected final ApplicationContext applicationContext;
	protected final BeanContainerHandlerChain beanContainerHandlerChain;
	protected final Configuration configuration;
	protected final ProxyFactory proxyFactory;
	protected final ConcurrentHashMap<String,BeanDefinition> beanDefinitions;
	protected final BeanDefinitionFactory beanDefinitionFactory;
	
	private Set<BeanDefinition> beanDefinitionSet;
	
	private volatile boolean initial = false;
	
	public AbstractBeanContainer(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		this.beanContainerHandlerChain = new DefaultBeanContainerHandlerChain(this);
		this.beanContainerHandlerChain.addLast("inject_field",new FieldBeanInject());
		this.beanContainerHandlerChain.addLast("inject_method",new MethodBeanInject());
		this.configuration = AbstractConfiguration.getConfiguration();
		this.proxyFactory = ExtensionLoader.getService(ProxyFactory.class,this.configuration.getOption(Constant.O_PROXY_FACTORY));
		this.beanDefinitionFactory = ExtensionLoader.getService(BeanDefinitionFactory.class,this.configuration.getOption(Constant.O_BEAN_DEFINITION_FACTORY));
		this.beanDefinitions = new ConcurrentHashMap<String,BeanDefinition>();
		
	}
	
	@Override
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}

	@Override
	public BeanContainerHandlerChain getBeanContainerHandlerChain(){
		return this.beanContainerHandlerChain;
	}
	
	@Override
	public void initial(){
		if(this.initial){
			return;
		}
		
		//初始化有向图
		DirectedGraph<BeanDefinition> directedGraph = new DirectedGraph<BeanDefinition>();
		DirectedGraphUtil<BeanDefinition> directedGraphUtil = new DirectedGraphUtil<BeanDefinition>(directedGraph);
		for(BeanDefinition beanDefinition : this.beanDefinitions.values()){
			this.processDirectedGraph(beanDefinition,directedGraph);
		}
		
		//循环引用检测
		if(directedGraphUtil.hasCycle()){
			throw new BeanCircleReferenceException();
		}
		
		//依赖排序 按照依赖关系的顺序
		//存储列表,保证按照初始化的顺序进行销毁
		this.beanDefinitionSet = directedGraphUtil.getSort();
		
		//初始化
		for(BeanDefinition beanDefinition : this.beanDefinitionSet){
			beanDefinition.initial();
		}
		
		//创建
		for(BeanDefinition beanDefinition : this.beanDefinitionSet){
			beanDefinition.create();
		}
		
		//注入
		for(BeanDefinition beanDefinition : this.beanDefinitionSet){
			beanDefinition.inject();
		}
		
		this.initial = true;
	}

	@Override
	public void destory(){
		if(!this.initial){
			return;
		}
		//销毁前
		for(BeanDefinition beanDefinition : this.beanDefinitionSet){
			beanDefinition.beforeDestory();
		}
		//销毁
		for(BeanDefinition beanDefinition : this.beanDefinitionSet){
			beanDefinition.destory();
		}
		this.beanDefinitionSet.clear();
		this.initial = false;
	}

	@Override
	public BeanDefinition attachBean(Class<?> requireType,BeanScope requireScope,String beanName){
		if(this.initial){
			return null;
		}
		if(null == requireType){
			throw new NullPointerException("requireType");
		}
		String tempBeanName = beanName;
		if(StringUtil.isBlank(tempBeanName)){
			tempBeanName = ClassUtil.getCamelClassName(requireType);
		}
		String key = String.format("%s#%s",requireType.getName(),tempBeanName);
		if(this.beanDefinitions.containsKey(key)){
			throw new BeanDefinitionExistException(key);
		}
		BeanDefinition beanDefinition = this.beanDefinitionFactory.createBeanDefinition(this, requireType, requireScope, tempBeanName);
		this.beanDefinitions.put(key,beanDefinition);//存储bean定义
		log.debug("create bean definition success [{}] -> {}",beanDefinition.getClass(),beanDefinition);
		
		this.resolveMethodDefineBean(requireType,beanDefinition);
		
		return beanDefinition;
	}

	@Override
	public BeanDefinition attachBean(Class<?> requireType, String beanName,Object beanInstance){
		if(null == beanInstance){
			throw new NullPointerException("instance");
		}
		if(null == requireType){
			requireType = beanInstance.getClass();
		}
		if(StringUtil.isBlank(beanName)){
			beanName = ClassUtil.getCamelClassName(requireType);
		}
		String key = String.format("%s#%s",requireType,beanName);
		if(this.beanDefinitions.containsKey(key)){
			throw new BeanDefinitionExistException(key);
		}
		BeanDefinition beanDefinition = this.beanDefinitionFactory.createBeanDefinition(this, requireType, beanName, beanInstance);
		this.beanDefinitions.put(key,beanDefinition);//存储bean定义
		log.debug("create bean definition success [{}] -> {}",beanDefinition.getClass(),beanDefinition);
		
		this.resolveMethodDefineBean(beanInstance.getClass(),beanDefinition);
		
		return beanDefinition;
	}
	
	@Override
	public Map<String,BeanDefinition> getBeanDefinitions(){
		return this.beanDefinitions;
	}

	@Override
	public Map<String,BeanDefinition> getBeanDefinitions(Class<?> requireType){
		Map<String,BeanDefinition> result = new HashMap<String,BeanDefinition>();
		if(null == requireType){
			return result;
		}
		for(BeanDefinition beanDefinition : this.beanDefinitions.values()){
			if(beanDefinition.matchType(requireType)){
				result.put(beanDefinition.getBeanName(),beanDefinition);
			}
		}
		return result;
	}

	@Override
	public BeanDefinition getBeanDefinition(Class<?> requireType,String beanName){
		try{
			return this.getBeanDefinitionWithException(requireType,beanName);
		}catch(Exception e){
			log.warn(e);
		}
		return null;
	}
	
	@Override
	public ProxyFactory getProxyFactory() {
		return this.proxyFactory;
	}
	
	protected void resolveMethodDefineBean(Class<?> requireType,BeanDefinition beanDefinition){
		
		for(Method method : ReflectionUtil.getDeclaredMethods(requireType)){
			if(!AnnotationUtil.hasAnnotation(method,Service.class)){
				continue;
			}
			if(method.getReturnType().equals(void.class)){
				log.warn("method return type is void {}.{}",requireType,method.getName());
				continue;
			}
			Service service = AnnotationUtil.getAnnotation(method,Service.class);
			String methodBeanName = method.getName();
			if(StringUtil.isNotBlank(service.value())){
				methodBeanName = service.value();
			}
			String methodKey = String.format("%s#%s",method.getReturnType(),methodBeanName);
			BeanDefinition methodBeanDefinition = this.beanDefinitionFactory.createBeanDefinition(this, method.getReturnType(),service.scope(), methodBeanName, beanDefinition, method);
			this.beanDefinitions.put(methodKey,methodBeanDefinition);
			log.debug("create bean definition success [{}] -> {}",methodBeanDefinition.getClass(),methodBeanDefinition);
		}
		
	}

	//添加对象到有向图中
	protected BeanDefinition processDirectedGraph(BeanDefinition beanDefinition,DirectedGraph<BeanDefinition> directedGraph){
		
		if(beanDefinition instanceof DefaultBeanDefinitionPrototypeFromMethod || beanDefinition instanceof DefaultBeanDefinitionSingletonFromMethod){
			Method method = null;
			BeanDefinition methodBeanDefinition = null;
			if(beanDefinition instanceof DefaultBeanDefinitionPrototypeFromMethod){
				DefaultBeanDefinitionPrototypeFromMethod defaultBeanDefinitionPrototypeFromMethod = (DefaultBeanDefinitionPrototypeFromMethod)beanDefinition;
				method = defaultBeanDefinitionPrototypeFromMethod.getMethod();
				methodBeanDefinition = defaultBeanDefinitionPrototypeFromMethod.getMethodBeanDefinition();
			}else{
				DefaultBeanDefinitionSingletonFromMethod defaultBeanDefinitionSingletonFromMethod = (DefaultBeanDefinitionSingletonFromMethod)beanDefinition;
				method = defaultBeanDefinitionSingletonFromMethod.getMethod();
				methodBeanDefinition = defaultBeanDefinitionSingletonFromMethod.getMethodBeanDefinition();
			}
			directedGraph.addEdge(beanDefinition,methodBeanDefinition);
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(null == parameterTypes || parameterTypes.length == 0){
				directedGraph.addNode(beanDefinition);
				return beanDefinition;
			}
			Annotation[] annotations = AnnotationUtil.getAnnotationFromParameter(method,Autowired.class);
			for(int i=0;i<parameterTypes.length;i++){
				if(null == annotations[i]){
					continue;
				}
				Class<?> parameterType = parameterTypes[i];
				Autowired autowired = (Autowired)annotations[i];
				String beanName = autowired.value();
				if(StringUtil.isBlank(beanName)){
					beanName = ClassUtil.getCamelClassName(parameterType);
				}
				BeanDefinition tempMethodBeanDefinition = this.getBeanDefinitionWithException(parameterType,beanName);
				directedGraph.addEdge(beanDefinition, tempMethodBeanDefinition);
			}
		}else if(beanDefinition instanceof DefaultBeanDefinitionSingletonInstance){
			directedGraph.addNode(beanDefinition);
		}else{
			Constructor<?> constructor = beanDefinition.getConstructor();
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if(null == parameterTypes || parameterTypes.length == 0){
				directedGraph.addNode(beanDefinition);
				return beanDefinition;
			}
			Annotation[] annotations = AnnotationUtil.getAnnotationFromParameter(constructor,Autowired.class);
			for(int i=0;i<parameterTypes.length;i++){
				if(null == annotations[i]){
					continue;
				}
				Class<?> parameterType = parameterTypes[i];
				Autowired autowired = (Autowired)annotations[i];
				String beanName = autowired.value();
				if(StringUtil.isBlank(beanName)){
					beanName = ClassUtil.getCamelClassName(parameterType);
				}
				BeanDefinition methodBeanDefinition = this.getBeanDefinitionWithException(parameterType,beanName);
				directedGraph.addEdge(beanDefinition, methodBeanDefinition);
			}
		}
		return beanDefinition;
	}
	
	protected BeanDefinition getBeanDefinitionWithException(Class<?> requireType,String beanName){
		if(null == requireType){
			throw new NullPointerException("requireType");
		}
		Map<String,BeanDefinition> beanDefinitionMap = this.getBeanDefinitions(requireType);
		if(null == beanDefinitionMap || beanDefinitionMap.size() == 0){
			throw new BeanDefinitionNotFoundException(String.format("%s",requireType.getName()));
		}
		if(StringUtil.isBlank(beanName)){
			if(beanDefinitionMap.size() == 1){//未指定名称,且当前类型定义只有一个则返回
				return beanDefinitionMap.values().iterator().next();
			}else{//未指定名称,且当前类型定义存在多个,抛出异常,需要指定名称
				throw new BeanDefinitionMultipleException(String.format("%s",requireType.getName()));
			}
		}else{
			if(beanDefinitionMap.containsKey(beanName)){//指定名称,且当前类型定义存在,则返回
				return beanDefinitionMap.get(beanName);
			}else{
				//指定名称,且当前类型定义不存在,则抛出异常
				throw new BeanDefinitionNotFoundException(String.format("%s",requireType.getName()));
			}
		}
	}
	
}