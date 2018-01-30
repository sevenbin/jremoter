package com.jremoter.core.scanner.support;

import com.jremoter.core.handler.HandlerChain;
import com.jremoter.core.scanner.PackageScannerHandler;

public class DefaultPackageScannerHandlerFoot extends DefaultPackageScannerHandlerContext implements PackageScannerHandler{
	
	private static final String NAME = "_FOOT";
	
	public DefaultPackageScannerHandlerFoot(HandlerChain<PackageScannerHandler> chain){
		super(chain, NAME, null);
	}

	@Override
	public boolean onClassFound(Class<?> type){
		return false;
	}

	@Override
	public PackageScannerHandler getHandler() {
		return this;
	}
	
}