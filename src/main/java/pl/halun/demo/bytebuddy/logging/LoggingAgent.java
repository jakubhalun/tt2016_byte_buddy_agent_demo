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

	/**
	 * Allows installation of java agent from command line.
	 *
	 * @param agentArguments
	 *            arguments for agent
	 * @param instrumentation
	 *            instrumentation instance
	 */
	public static void premain(String agentArguments,
			Instrumentation instrumentation) {
		createAgent(RestController.class, "greeting")
				.installOn(instrumentation);
		createAgent(RestController.class, "showUserAgent").installOn(
				instrumentation);
	}

	/**
	 * Allows installation of java agent with Attach API.
	 *
	 * @param agentArguments
	 *            arguments for agent
	 * @param instrumentation
	 *            instrumentation instance
	 */
	public static void agentmain(String agentArguments,
			Instrumentation instrumentation) {
		createAgent(RestController.class, "greeting")
				.installOn(instrumentation);
		createAgent(RestController.class, "showUserAgent").installOn(
				instrumentation);
	}

	private static AgentBuilder createAgent(
			Class<? extends Annotation> annotationType, String methodName) {
		return new AgentBuilder.Default().type(
				ElementMatchers.isAnnotatedWith(annotationType)).transform(
				new AgentBuilder.Transformer() {
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
				});
	}

}
