package com.jremoter.core.scanner;

import com.jremoter.core.handler.HandlerChain;

public interface PackageScannerHandlerChain extends HandlerChain<PackageScannerHandler>{
	
	public PackageScanner getPackageScanner();
	
	public boolean onClassFound(Class<?> type);
	
}