package com.jremoter.core.option;

import com.jremoter.core.toolkit.Extension;

@Extension
public interface Configuration {
	
	public <T> T getOption(Option<T> option);
	
	public <T> T getOption(String option);
	
}