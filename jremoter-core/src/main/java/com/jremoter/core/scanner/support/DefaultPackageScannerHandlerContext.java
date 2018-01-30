package com.jremoter.core.scanner.support;

import com.jremoter.core.handler.HandlerChain;
import com.jremoter.core.handler.support.AbstractHandlerContext;
import com.jremoter.core.scanner.PackageScannerHandler;
import com.jremoter.core.scanner.PackageScannerHandlerContext;

public class DefaultPackageScannerHandlerContext extends AbstractHandlerContext<PackageScannerHandler> implements PackageScannerHandlerContext{

	public DefaultPackageScannerHandlerContext(HandlerChain<PackageScannerHandler> chain, String name,PackageScannerHandler handler) {
		super(chain, name, handler);
	}
	
}