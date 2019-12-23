package MyFirstApp.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DBUtils {

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void initialize(){
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.execute("DROP TABLE IF EXISTS ROOT_TAB");
			statement.executeUpdate(
					"CREATE TABLE ROOT_TAB(" +
							"ID INTEGER Primary key, " +
							"TIME varchar(30) not null, " +
							"A varchar)"
			);
			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}