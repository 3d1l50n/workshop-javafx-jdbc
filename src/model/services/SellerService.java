package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

		
		private SellerDao dao = DaoFactory.createSellerDao();
		
		// Mock para teste
		// List<Department> list = new ArrayList<>();
		// list.add(new Department(1, "Books"));
		// list.add(new Department(2, "Coputers"));
		// list.add(new Department(3, "Eletronics"));
		
		// return list;
		
		public List<Seller> findAll() {
			return dao.findAll();
		}

		public void saveOrUpdate(Seller obj) {
			if(obj.getId() == null) {
				dao.insert(obj);
			} else {
				dao.update(obj);
			}
		}
		
		public void remove(Seller obj) {
			dao.deleteById(obj.getId());
		}
		
		
	
		
	

}
