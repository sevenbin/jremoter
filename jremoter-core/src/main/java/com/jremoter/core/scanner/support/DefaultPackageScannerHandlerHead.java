package com.jremoter.core.scanner.support;

import com.jremoter.core.handler.HandlerChain;
import com.jremoter.core.scanner.PackageScannerHandler;

public class DefaultPackageScannerHandlerHead extends DefaultPackageScannerHandlerContext implements PackageScannerHandler{
	
	private static final String NAME = "_HEAD";
	
	public DefaultPackageScannerHandlerHead(HandlerChain<PackageScannerHandler> chain){
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