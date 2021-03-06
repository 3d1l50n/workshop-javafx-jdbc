package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC ( Connection conn) {
		this.conn = conn;
	}


	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(name, email, birthdate, basesalary, departamentid) "
					+ "VALUES"
					+ "(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			
			st.setString(1, obj.getName());
			st.setString(2,  obj.getEmail());
			st.setDate(3,  new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartament().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected! ");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

		
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET name = ?, email = ?, birthdate = ?, basesalary = ?, departamentid = ? "
				    + "WHERE id = ?");
			
			
			st.setString(1, obj.getName());
			st.setString(2,  obj.getEmail());
			st.setDate(3,  new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartament().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"DELETE FROM seller where id = ?");
			
			st.setInt(1, id);
			int rows = st.executeUpdate();
			
			if(rows == 0) {
				throw new DbException("id n??o encontrado !");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.* , departament.name as DepName "
					+ "from seller INNER JOIN departament"
				    + " ON  seller.departamentid = departament.id "
				    + "where seller.id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Departament dep = instantiateDepartament(rs);
				
				Seller obj = instantiateSeller(rs, dep);
				
				return obj;

			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	private Seller instantiateSeller(ResultSet rs, Departament dep) throws SQLException {
		Seller obj  = new Seller();
		obj.setId(rs.getInt("id"));
		obj.setName(rs.getString("name"));
		obj.setEmail(rs.getString("email"));
		obj.setBaseSalary(rs.getDouble("basesalary"));
		obj.setBirthDate(new java.util.Date(rs.getTimestamp("birthdate").getTime()));
		obj.setDepartament(dep);
		
		return obj;
	}


	private Departament instantiateDepartament(ResultSet rs) throws SQLException {
		Departament dep = new Departament();
		dep.setId(rs.getInt("departamentid"));
		dep.setName(rs.getString("depname"));
		return dep;
	}


	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, departament.Name as DepName "
					+ "FROM seller INNER JOIN departament "
					+ "ON seller.departamentid = departament.id "
					+ "ORDER BY name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Departament> map = new HashMap<>();
			
			while (rs.next()) {
				
				// verifica se o map j??  possui um departmentid
				Departament dep = map.get(rs.getInt("departamentid"));
				
				if (dep ==null ) {
				
					dep = instantiateDepartament(rs);
					map.put(rs.getInt("departamentid"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				
				list.add(obj);

			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	
		
	}


	@Override
	public List<Seller> findByDepartament(Departament departament) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, departament.Name as DepName "
					+ "FROM seller INNER JOIN departament "
					+ "ON seller.departamentid = departament.id "
					+ "WHERE departamentid = ? "
					+ "ORDER BY name");
			
			st.setInt(1, departament.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Departament> map = new HashMap<>();
			
			while (rs.next()) {
				
				// verifica se o map j??  possui um departmentid
				Departament dep = map.get(rs.getInt("departamentid"));
				
				if (dep ==null ) {
				
					dep = instantiateDepartament(rs);
					map.put(rs.getInt("departamentid"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				
				list.add(obj);

			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	

	

}
