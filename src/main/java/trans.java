import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringWriter;

public class trans implements Processor {
	public void process(Exchange exc) throws Exception{
		Reader custom = exc.getIn().getBody(Reader.class);
		JAXBContext jaxbCTX = JAXBContext.newInstance(Root.class);

		Unmarshaller unmarsh = jaxbCTX.createUnmarshaller();
		Root msg = (Root) unmarsh.unmarshal(custom);

		msg.setA(msg.getA()+" - Дичь");

		Marshaller marsh = jaxbCTX.createMarshaller();
		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();

		marsh.marshal(msg, sw);

		exc.getIn().setBody(sw.toString());
	}
}