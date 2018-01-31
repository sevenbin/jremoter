package com.jremoter.core.context.support;

import java.io.PrintStream;

import com.jremoter.core.context.ApplicationContextBanner;
import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("default")
public class DefaultApplicationContextBanner implements ApplicationContextBanner{
	
	@Override
	public void write(PrintStream printStream){
		printStream.println("----------------------------------------------------------------------");
		printStream.println("                 _ _____                      _            ");
		printStream.println("                | |  __ \\                    | |           ");
		printStream.println("                | | |__) |___ _ __ ___   ___ | |_ ___ _ __ ");
		printStream.println("            _   | |  _  // _ \\ '_ ` _ \\ / _ \\| __/ _ \\ '__|");
		printStream.println("           | |__| | | \\ \\  __/ | | | | | (_) | ||  __/ |   ");
		printStream.println("            \\____/|_|  \\_\\___|_| |_| |_|\\___/ \\__\\___|_|   ");
		printStream.println("                                                            ");
		printStream.println("----------------------------------------------------------------------");
		printStream.println("                            KoKo  v0.0.5                    ");
		printStream.println("----------------------------------------------------------------------");
	}
	
}