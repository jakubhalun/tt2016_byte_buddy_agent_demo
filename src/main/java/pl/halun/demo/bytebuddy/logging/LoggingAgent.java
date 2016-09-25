package pl.halun.demo.bytebuddy.logging;

import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import org.springframework.web.bind.annotation.RestController;

public class LoggingAgent {
	public static void premain(String agentArgument, Instrumentation inst) {
		addLogToMethod(inst, RestController.class, "greeting");
		addLogToMethod(inst, RestController.class, "showUserAgent");
	}

	private static void addLogToMethod(final Instrumentation instrumentation,
			Class<? extends Annotation> annotationType, String methodName) {

		new AgentBuilder.Default()
				.type(ElementMatchers.isAnnotatedWith(annotationType))
				.transform(new AgentBuilder.Transformer() {
					@Override
					public DynamicType.Builder<?> transform(
							DynamicType.Builder<?> builder,
							TypeDescription typeDescription,
							ClassLoader classLoader) {
						return builder
								.method(ElementMatchers.named(methodName))
								.intercept(
										MethodDelegation
												.to(LoggingInterceptor.class)
												.andThen(
														SuperMethodCall.INSTANCE));
					}
				}).installOn(instrumentation);

	}
}
