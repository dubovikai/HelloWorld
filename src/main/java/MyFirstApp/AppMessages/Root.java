package MyFirstApp.AppMessages;

import javax.xml.bind.annotation.*;

/**
 * Message class. Path Root.a
 */
@XmlRootElement
public class Root {

	private String a;

	public String getA() {
		return a;
	}
	@XmlElement
	public void setA(String a) {
		this.a = a;
	}
}