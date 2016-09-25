package pl.halun.demo.bytebuddy.logging;

import java.lang.reflect.Method;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingInterceptor {
	public static void intercept(@AllArguments Object[] allArguments,
			@Origin Method method) {

		Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());

		logger.info("Method {} of class {} called", method.getName(), method
				.getDeclaringClass().getSimpleName());

		for (Object argument : allArguments) {
			logger.info("Method {}, parameter type {}, value={}",
					method.getName(), argument.getClass().getSimpleName(),
					argument.toString());
		}
	}

}
