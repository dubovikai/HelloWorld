package MyFirstApp.Beans;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

public class Trans implements Processor {

	public Root ConvertStringXMLToRoot(String txt) throws JAXBException {
		StringReader reader = new StringReader(txt);
		Root msg = (Root) unmarsh.unmarshal(reader);
		return msg;
	}

	public String ConvertRootToStringXML(Root msg) throws JAXBException {
		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marsh.marshal(msg, sw);
		return sw.toString();
	}

	public JAXBContext jaxbCTX;
	public Unmarshaller unmarsh;
	public Marshaller marsh;

	public void init() throws JAXBException {
		jaxbCTX = JAXBContext.newInstance(Root.class);
		unmarsh = jaxbCTX.createUnmarshaller();
		marsh = jaxbCTX.createMarshaller();
	}


	@Override
	public void process(Exchange exc) throws Exception{
		Reader custom = exc.getIn().getBody(Reader.class);
		Root msg = (Root) unmarsh.unmarshal(custom);
		msg.setA(msg.getA()+" - Дичь");
		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marsh.marshal(msg, sw);
		exc.getIn().setBody(sw.toString());
	}
}