package com.jremoter.core.scanner.support;

import com.jremoter.core.scanner.PackageScannerHandler;

public class DefaultPackageScannerHandler implements PackageScannerHandler{

	@Override
	public boolean onClassFound(Class<?> type){
		return true;
	}
	
}