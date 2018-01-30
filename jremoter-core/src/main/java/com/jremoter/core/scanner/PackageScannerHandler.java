package com.jremoter.core.scanner;

import com.jremoter.core.handler.Handler;

public interface PackageScannerHandler extends Handler{
	
	public boolean onClassFound(Class<?> type);
	
}