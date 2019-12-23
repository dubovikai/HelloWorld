package MyFirstApp.AppMessages;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 * Message class. Path Root.a
 */
@XmlRootElement
@Entity
@Table(name = "ROOT_TAB")
public class Root {


	public Integer getId() {
		return id;
	}

	@XmlTransient
	public void setId(Integer id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "TIME")
	private String time = new Date().toString();

	@Column(name = "A")
	private String a;

	public String getA() {
		return a;
	}
	@XmlElement
	public void setA(String a) {
		this.a = a;
	}
}