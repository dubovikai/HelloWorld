package MyFirstApp;

import MyFirstApp.AppMessages.Root;
import MyFirstApp.Beans.TransBean;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Properties;

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

	@Bean
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource ret = new DriverManagerDataSource();
		ret.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		ret.setUrl(env.getProperty("jdbc.url"));
		ret.setUsername(env.getProperty("jdbc.username"));
		ret.setUsername(env.getProperty("jdbc.password"));
		return ret;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		LocalSessionFactoryBean ret = new LocalSessionFactoryBean();
		ret.setDataSource(dataSource());
		ret.setAnnotatedClasses(Root.class);
		Properties hibernateProps = new Properties();
		hibernateProps.setProperty("hibernate.dialect",env.getProperty("hibernate.dialect"));
		hibernateProps.setProperty("hibernate.show_sql",env.getProperty("hibernate.show_sql"));
		ret.setHibernateProperties(hibernateProps);
		return ret;
	}

	@Bean
	public HibernateTransactionManager transactionManager(){
		HibernateTransactionManager ret = new HibernateTransactionManager();
		ret.setSessionFactory((SessionFactory) sessionFactory());
		return ret;
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
