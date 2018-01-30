package com.jremoter.core.scanner.support;

import com.jremoter.core.handler.HandlerContext;
import com.jremoter.core.handler.support.AbstractHandlerChain;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.scanner.PackageScannerHandler;
import com.jremoter.core.scanner.PackageScannerHandlerChain;

public class DefaultPackageScannerHandlerChain extends AbstractHandlerChain<PackageScannerHandler> implements PackageScannerHandlerChain{
	
	protected final PackageScanner packageScanner;
	
	public DefaultPackageScannerHandlerChain(PackageScanner packageScanner){
		this.packageScanner = packageScanner;
	}
	
	@Override
	public PackageScanner getPackageScanner() {
		return this.packageScanner;
	}

	@Override
	public boolean onClassFound(Class<?> type) {
		boolean result = true;
		HandlerContext<PackageScannerHandler> context = this.head.getNext();
		while(context != this.foot){
			result = context.getHandler().onClassFound(type);
			context = context.getNext();
		}
		return result;
	}

	@Override
	protected HandlerContext<PackageScannerHandler> createHeadHandlerContext() {
		return new DefaultPackageScannerHandlerHead(this);
	}
	
	@Override
	protected HandlerContext<PackageScannerHandler> createFootHandlerContext() {
		return new DefaultPackageScannerHandlerFoot(this);
	}
	
	@Override
	protected HandlerContext<PackageScannerHandler> createHandlerContext(String name,PackageScannerHandler handler){
		return new DefaultPackageScannerHandlerContext(this,name,handler);
	}
	
}