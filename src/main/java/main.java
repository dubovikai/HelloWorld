import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//FIXME  Drop main class
public class main {
	public static void main(String[] args){
		ApplicationContext context = new AnnotationConfigApplicationContext(MyFirstApp.AppConfig.class);
	}
}