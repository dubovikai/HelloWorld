package MyFirstApp;

import MyFirstApp.Beans.TransBean;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan
public class AppConfig extends CamelConfiguration{

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
	public List<RouteBuilder> routes(){
		return Arrays.asList(route);
	}
}
