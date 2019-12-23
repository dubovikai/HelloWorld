package MyFirstApp.Util;


import MyFirstApp.AppMessages.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RootDaoImpl implements RootDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = false)
	public void insertPerson(Root root) {
		Session session = sessionFactory.openSession();
		session.save(root);
	}
}
