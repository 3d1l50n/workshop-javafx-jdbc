package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentDao;
import model.entities.Departament;

public class DepartamentService {

		
		private DepartamentDao dao = DaoFactory.createDepartamentDao();
		
		// Mock para teste
		// List<Department> list = new ArrayList<>();
		// list.add(new Department(1, "Books"));
		// list.add(new Department(2, "Coputers"));
		// list.add(new Department(3, "Eletronics"));
		
		// return list;
		
		public List<Departament> findAll() {
			return dao.findAll();
		}

		public void saveOrUpdate(Departament obj) {
			if(obj.getId() == null) {
				dao.insert(obj);
			} else {
				dao.update(obj);
			}
		}
		
		public void remove(Departament obj) {
			dao.deleteById(obj.getId());
		}
		
		
	
		
	

}
