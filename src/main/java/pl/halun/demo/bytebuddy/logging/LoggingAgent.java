package pl.halun.demo.bytebuddy.logging;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

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
		install(instrumentation);
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
		install(instrumentation);
	}

	private static void install(Instrumentation instrumentation) {
		createAgent(RestController.class, "greeting")
				.installOn(instrumentation);
		createAgent(RestController.class, "showUserAgent").installOn(
				instrumentation);
	}

	private static AgentBuilder createAgent(
			Class<? extends Annotation> annotationType, String methodName) {
		return new AgentBuilder.Default().disableClassFormatChanges()
				.disableBootstrapInjection()
				.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
				.type(isAnnotatedWith(annotationType))
				.transform(new AgentBuilder.Transformer() {
					@Override
					public DynamicType.Builder<?> transform(
							DynamicType.Builder<?> builder,
							TypeDescription typeDescription,
							ClassLoader classLoader) {
						return builder.visit(Advice.to(LoggingAdvice.class).on(
								named(methodName)));
					}
				});
	}

}
