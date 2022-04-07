package com.fimet;


import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//@PropertySource("classpath:fimet.properties")
@Configuration
@ComponentScan
@SpringBootApplication
public class FimetServerApplication {
	private static Logger logger = LoggerFactory.getLogger(FimetServerApplication.class);
	private volatile static ConfigurableApplicationContext context;
	@SuppressWarnings("unused")
	@Autowired private Manager manager;
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FimetServerApplication.class, args);
		setContext(context);
		logger.info("ApplicationContext starting main "+context);
		//startBroker();
	}
    public static void restart() {
    	restart(getContext());
    }
    public static void restart(final ConfigurableApplicationContext oldContext) {
    	ApplicationArguments args = getContext().getBean(ApplicationArguments.class);
    	
    	Thread thread = new Thread(() -> {
    		sleepFor(1000L);
    		oldContext.close();
    		ConfigurableApplicationContext newContext = SpringApplication.run(FimetServerApplication.class, args.getSourceArgs());
    		logger.info("ApplicationContext starting restart "+newContext);
    		setContext(newContext);
    	});
    	
    	thread.setDaemon(false);
    	thread.start();
    }
	public static void stop() {
		stop(getContext());
	}
	public static void stop(final ConfigurableApplicationContext context) {
		if (context!=null) {
	        Thread thread = new Thread(() -> {
	        	sleepFor(1000L);
	            int exitCode = SpringApplication.exit(context, ()->0);
	            System.exit(exitCode);
	        });
	
	        thread.setDaemon(false);
	        thread.start();
		} else {
			logger.error("ApplicationContext is null in stop");
		}
	}
	private static void sleepFor(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignore) {}
	}
	private static synchronized ConfigurableApplicationContext getContext() {
		return context;
	}
	private static synchronized void setContext(ConfigurableApplicationContext context_) {
		context = context_;
	}
}
