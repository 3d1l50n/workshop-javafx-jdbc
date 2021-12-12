package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

		
		private DepartmentDao dao = DaoFactory.createDepartmentDao();
		
		// Mock para teste
		// List<Department> list = new ArrayList<>();
		// list.add(new Department(1, "Books"));
		// list.add(new Department(2, "Coputers"));
		// list.add(new Department(3, "Eletronics"));
		
		// return list;
		
		public List<Department> findAll() {
			return dao.findAll();
		}

		public void saveOrUpdate(Department obj) {
			if(obj.getId() == null) {
				dao.insert(obj);
			} else {
				dao.update(obj);
			}
		}
		
		
	
		
	

}