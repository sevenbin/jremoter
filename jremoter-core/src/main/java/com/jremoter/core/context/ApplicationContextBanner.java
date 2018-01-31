package com.jremoter.core.context;

import java.io.PrintStream;

import com.jremoter.core.toolkit.Extension;

@Extension
public interface ApplicationContextBanner {
	
	public void write(PrintStream printStream);
	
}