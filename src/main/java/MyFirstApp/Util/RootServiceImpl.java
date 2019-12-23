package MyFirstApp.Util;

import MyFirstApp.AppMessages.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RootServiceImpl implements RootService {
	@Autowired
	private RootDAO rootDAO;

	@Override
	public void insertRoot(Root root){
		rootDAO.insertPerson(root);
	}
}
