# Byte Buddy Java Agent Demo

This is demo of Byte Buddy Java agent prepared for Motorola Tech Talks 2016 in Kraków.
It instruments [SpringBoot application](https://github.com/jakubhalun/byte_buddy_agent_demo_instrumented_app) changing default log appender to FileAppender and adding logs to RestController.

###Execution of agent from command line

```
java -javaagent:byte-buddy-agent.jar -jar byte-buddy-agent-demo-instrumented-app.jar
```

###Execution of agent with Attach API

Check project [Attach API Agent Loader](https://github.com/jakubhalun/tt2016_attach_api_agent_loader) to load agent with Attach API