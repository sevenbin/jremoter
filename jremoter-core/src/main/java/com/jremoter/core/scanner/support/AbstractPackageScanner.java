package com.jremoter.core.scanner.support;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;
import com.jremoter.core.pattern.PatternMatcher;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.scanner.PackageScannerHandlerChain;
import com.jremoter.core.util.ClassUtil;
import com.jremoter.core.util.ResourceUtil;
import com.jremoter.core.util.ResourceUtil.ResourceMatcher;

public abstract class AbstractPackageScanner implements PackageScanner,ResourceMatcher{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractPackageScanner.class);
	private static final Map<String,Class<?>> cacheClass = new HashMap<String,Class<?>>();
	private static final String PROTOCOL_FILE = "file";
	private static final String PROTOCOL_JAR = "jar";
	
	private static final String MATCHER_SIN_LEVEL = "?";
	private static final String MATCHER_ONE_LEVEL = ".*";
	private static final String MATCHER_MUL_LEVEL = ".**";
	
	private final PackageScannerHandlerChain packageScannerHandlerChain;
	private final Set<String> patterns;
	
	public AbstractPackageScanner(){
		this.patterns = new LinkedHashSet<String>();
		this.packageScannerHandlerChain = new DefaultPackageScannerHandlerChain(this);
	}

	@Override
	public PackageScannerHandlerChain getPackageScannerHandlerChain() {
		return this.packageScannerHandlerChain;
	}

	@Override
	public void addPattern(String... patterns){
		if(null == patterns || patterns.length == 0){
			log.warn("add patterns is blank");
			return;
		}
		for(String pattern : patterns){
			if(!pattern.endsWith(MATCHER_SIN_LEVEL) && !pattern.endsWith(MATCHER_ONE_LEVEL) && !pattern.endsWith(MATCHER_MUL_LEVEL)){
				pattern += MATCHER_MUL_LEVEL;
			}
			this.patterns.add(pattern);
			log.debug("add pattern success [{}]",pattern);
		}
	}

	@Override
	public Set<Class<?>> scan(PatternMatcher patternMatcher){
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		if(null == this.patterns || this.patterns.isEmpty()){
			return classes;
		}
		for(String pattern : this.patterns){
			if(pattern.endsWith(MATCHER_SIN_LEVEL)){
				pattern = pattern.substring(0,pattern.length() - MATCHER_SIN_LEVEL.length());
			}
			if(pattern.endsWith(MATCHER_MUL_LEVEL)){
				pattern = pattern.substring(0,pattern.length() - MATCHER_MUL_LEVEL.length());
			}
			if(pattern.endsWith(MATCHER_ONE_LEVEL)){
				pattern = pattern.substring(0,pattern.length() - MATCHER_ONE_LEVEL.length());
			}
			if(pattern.indexOf(MATCHER_MUL_LEVEL) > 0){
				pattern = pattern.substring(0,pattern.indexOf(MATCHER_MUL_LEVEL));
			}
			if(pattern.indexOf(MATCHER_ONE_LEVEL) > 0){
				pattern = pattern.substring(0,pattern.indexOf(MATCHER_ONE_LEVEL));
			}
			this.doScan(patternMatcher,pattern,classes);
		}
		return classes;
	}
	
	@Override
	public boolean matcher(URL url){
		return true;
	}
	
	protected void doScan(PatternMatcher patternMatcher, String packageName,Set<Class<?>> classes){
		String tempPackageName = packageName;
		String tempPackageDirName = tempPackageName.replace('.','/');
		for(URL url : ResourceUtil.search(this,tempPackageDirName)){
			String protocol = url.getProtocol();
			log.debug("found url {}",url);
			if(PROTOCOL_FILE.equals(protocol)){
				try{
					String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
					this.findAndAddClassesInPackageByFile(patternMatcher,tempPackageName,filePath,classes);
				}catch(Exception e){
					log.warn(e.getMessage(),e);
				}
			}else if(PROTOCOL_JAR.equals(protocol)){
				JarFile jar = null;
				try{
					jar = ((JarURLConnection)url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()){
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if(name.charAt(0) == '/'){
							name = name.substring(1);
						}
						if(name.startsWith(tempPackageDirName)){
							if(name.endsWith(".class") && !entry.isDirectory()){
								name = name.substring(0,name.length() - 6);
								name = name.replace('/','.');
								try{
									Class<?> tempClass = this.classForName(patternMatcher,name);
									if(null != tempClass){
										classes.add(tempClass);
									}
								}catch(Throwable ec){
									log.warn(ec);
								}
							}
						}
					}
				}catch(IOException ex){
					log.warn(ex);
				}
			}
		}
	}
	
	protected Class<?> classForName(PatternMatcher patternMatcher,String className){
		Class<?> result = cacheClass.get(className);
		if(null == result){
			result = ClassUtil.forName(className,null);
			if(null != result){
				cacheClass.put(className,result);
			}
		}
		if(null != result && this.patternMatching(patternMatcher, result) && this.packageScannerHandlerChain.onClassFound(result)){
			return result;
		}
		return null;
	}
	
	protected void findAndAddClassesInPackageByFile(PatternMatcher patternMatcher,String packageName,String filePath,Set<Class<?>> classes){
		File dir = new File(filePath);
		if(dir == null || !dir.exists() || !dir.isDirectory()){
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().endsWith(".class");
			}
		});
		for(File file : dirfiles){
			if(file.isDirectory()){
				findAndAddClassesInPackageByFile(patternMatcher,packageName + "." + file.getName(),file.getAbsolutePath(),classes);
			}else{
				String className = file.getName().substring(0, file.getName().length() - 6);
				Class<?> tempClass = this.classForName(patternMatcher,packageName + "." + className);
				if(null != tempClass){
					classes.add(tempClass);
				}
			}
		}
	}
	
	protected boolean patternMatching(PatternMatcher patternMatcher,Class<?> clazz){
		for(String pattern : this.patterns){
			String packageName = clazz.getName();
			if(patternMatcher.match(pattern,packageName)){
				return true;
			}
		}
		return false;
	}

}