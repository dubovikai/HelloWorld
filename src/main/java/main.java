import org.apache.camel.ProducerTemplate;

import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.component.activemq.ActiveMQConfiguration;
import org.apache.camel.impl.DefaultCamelContext;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;

import java.util.Scanner;

import static org.apache.camel.component.activemq.ActiveMQComponent.activeMQComponent;

public class main {

	public static void main(String[] args){

		DefaultCamelContext CamelCTX;
		CamelCTX = new DefaultCamelContext();

		Scanner in = new Scanner(System.in);
		try {

			ActiveMQComponent compMQ =  activeMQComponent("tcp://localhost:61616");
			compMQ.setUsername("admin");
			compMQ.setPassword("admin");

			CamelCTX.addComponent("activemq", compMQ);
			CamelCTX.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("activemq:queue:queA")
							.process(new trans())
							.to("activemq:queue:queB");
				}
			});

			CamelCTX.start();
			String str = "FirstMessage";

			ProducerTemplate producer = CamelCTX.createProducerTemplate();
			while (true) {
				System.out.print("Введите текст: ");
				str = in.next();

				if (str.equals("exit")) break;

				//javax.xml.parsers
				DocumentBuilderFactory docbuildfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder docbuild = docbuildfac.newDocumentBuilder();

				//org.w3c.dom
				Document doc = docbuild.newDocument();
				Element msg = doc.createElement("root");
				doc.appendChild(msg);
				Element a = doc.createElement("a");
				a.appendChild(doc.createTextNode(str));
				msg.appendChild(a);

				TransformerFactory trfac = TransformerFactory.newInstance();
				Transformer tr = trfac.newTransformer();
				DOMSource doms = new DOMSource(doc);
				StringWriter sw = new StringWriter();
				tr.transform(doms, new StreamResult(sw));

				producer.sendBody("activemq:queue:queA", sw.toString());

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			in.close();
			CamelCTX.stop();
		}

	}
}