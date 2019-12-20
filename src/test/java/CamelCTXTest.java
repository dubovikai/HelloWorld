
import MyFirstApp.AppMessages.Root;
import MyFirstApp.Beans.TransBean;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=MyFirstApp.AppConfig.class)
public class CamelCTXTest extends CamelTestSupport {
	@Autowired
	ApplicationContext context;

	@Autowired
	TransBean tb;

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:start").routeId("MyTestRoute")
						//.to("activemq:queue:queA")
						.log("Received Message is ${body} and Headers are ${headers}")
						.process(tb)
						.log("Received Message is ${body} and Headers are ${headers}")
						//.to("activemq:queue:queB")
						.to("mock:result");
			}
		};
	}

	@Test
	public void testCTX() throws InterruptedException, JAXBException {
		Root objSend = new Root();
		objSend.setA("TestMessage");

		Root objReceive = new Root();
		objReceive.setA("TestMessage - Дичь");

		String TestMsgSend = tb.ConvertRootToStringXML(objSend);

		String TestMsgReceive = tb.ConvertRootToStringXML(objReceive);

		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedBodiesReceived(TestMsgReceive);
		template.sendBody("direct:start",TestMsgSend);
		assertMockEndpointsSatisfied();
	}
}
