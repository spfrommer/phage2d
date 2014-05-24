package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
	private static Logger logger = LoggerFactory.getLogger(LoggingTest.class);
	
	public static void main(String[] args) {
		logger.info("Testing logger...");
		logger.info("Hello World!");
		logger.error("This is an error");
		logger.trace("This is a trace...");
		logger.warn("Warning! I don't know what to say!");
		logger.debug("Please debug this application :D");
	}
}
