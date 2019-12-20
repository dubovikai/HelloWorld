package MyFirstApp.AppMessages;

import javax.xml.bind.annotation.*;

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