package pl.halun.demo.bytebuddy.logging;

import java.lang.reflect.Method;

import net.bytebuddy.asm.Advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAdvice {
	@Advice.OnMethodEnter
	public static void intercept(@Advice.BoxedArguments Object[] allArguments,
			@Advice.Origin Method method) {
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
