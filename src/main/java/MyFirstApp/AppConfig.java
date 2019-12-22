package MyFirstApp;

import MyFirstApp.Beans.TransBean;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.apache.camel.component.activemq.ActiveMQComponent.activeMQComponent;

//TODO This needs to be documented
@Configuration
@ComponentScan
@PropertySource("app.properties")
public class AppConfig extends CamelConfiguration{

	@Autowired
	private Environment env;

	@Bean(name = {"getTransBean", "TransBean"}, initMethod = "init")
	public MyFirstApp.Beans.TransBean TransBean() {
		return new TransBean();
	}

	@Component
	public class AppCamelRouteBean extends SpringRouteBuilder {
		@Override
		public void configure() throws Exception {
			from("activemq:queue:queA").routeId("MyRoute")
					.process(TransBean())
					.to("activemq:queue:queB");
		}
	}

	@Autowired
	private AppConfig.AppCamelRouteBean route;

	@Override
	protected void setupCamelContext(CamelContext camelContext) throws Exception {
		// setup the ActiveMQ component
		ActiveMQComponent compMQ =  activeMQComponent(env.getProperty("amqhost.url"));
		compMQ.setTestConnectionOnStartup(true);
		compMQ.setUsername(env.getProperty("username"));
		compMQ.setPassword(env.getProperty("password"));
		camelContext.addComponent("activemq", compMQ);
	}
}
