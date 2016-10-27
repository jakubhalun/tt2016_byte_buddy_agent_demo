package pl.halun.demo.bytebuddy.logging;

import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

public class LoggingAgent {

	private static final String DEMO_INSTRUMENTED_CLASS_NAME = "pl.halun.demo.bytebuddy.instrumented.app.HelloController";

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
		install(DEMO_INSTRUMENTED_CLASS_NAME, instrumentation);
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
		install(DEMO_INSTRUMENTED_CLASS_NAME, instrumentation);
	}

	private static void install(String className,
			Instrumentation instrumentation) {
		createAgent(className, "greeting").installOn(instrumentation);
		createAgent(className, "showUserAgent").installOn(instrumentation);
	}

	private static AgentBuilder createAgent(String className, String methodName) {
		return new AgentBuilder.Default().disableClassFormatChanges()
				.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
				.type(named(className))
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
