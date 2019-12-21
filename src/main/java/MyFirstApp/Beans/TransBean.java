package MyFirstApp.Beans;

import MyFirstApp.AppMessages.Root;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;


/**
 * Класс обработки сообщения в верблюжьем маршруте
 * @author Андрей
 * @version 1.0
 */
@PropertySource("app.properties")
public class TransBean implements Processor {

	@Autowired
	private Environment env;

	/**
	 * Преобразует XML в объект Root
	 * @param txt XML-строка
	 * @return Root-object
	 * @throws JAXBException Какие-то исключения
	 */
	public Root ConvertStringXMLToRoot(String txt) throws JAXBException {
		StringReader reader = new StringReader(txt);
		Root msg = (Root) unmarsh.unmarshal(reader);
		return msg;
	}

	/**
	 * Обратное преобразование к ConvertStringXMLToRoot {@link TransBean#ConvertStringXMLToRoot(String)}
	 * @param msg Объект Root
	 * @return xml-string
	 * @throws JAXBException Исключение
	 * @see TransBean#ConvertStringXMLToRoot(String) 
	 */
	public String ConvertRootToStringXML(Root msg) throws JAXBException {
		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marsh.marshal(msg, sw);
		return sw.toString();
	}

	/**
	 * Контекст парсера-биндера JAXB
	 */
	public JAXBContext jaxbCTX;
	public Unmarshaller unmarsh;
	public Marshaller marsh;

	/**
	 * Инит-метод бина
	 * @throws JAXBException Какое-то исключение
	 */
	public void init() throws JAXBException {
		jaxbCTX = JAXBContext.newInstance(Root.class);
		unmarsh = jaxbCTX.createUnmarshaller();
		marsh = jaxbCTX.createMarshaller();
	}


	@Override
	public void process(Exchange exc) throws Exception{
		Reader custom = exc.getIn().getBody(Reader.class);
		Root msg = (Root) unmarsh.unmarshal(custom);

		msg.setA(msg.getA()+env.getProperty("txttoadd"));

		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marsh.marshal(msg, sw);
		exc.getIn().setBody(sw.toString());
	}
}