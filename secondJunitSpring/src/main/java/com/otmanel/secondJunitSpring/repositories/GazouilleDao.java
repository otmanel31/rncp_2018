package com.otmanel.secondJunitSpring.repositories;

import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.otmanel.secondJunitSpring.metier.Gazouille;

// rowmapper va mapper une ligne a une objet .... 
public class GazouilleDao implements RowMapper<Gazouille>, IGazouilleDao {
	
	public static final String FIND_ALL_SQL = "select id, titre, corps from gazouille";
	public static final String FIND_BY_ID_SQL = "select  titre, corps from gazouille where id = ?";
	public static final String UPDATE_ONE_SQL = "UPDATE  gazouille set titre=?, corps=? where id=?";
	public static final String INSERT_ONE_SQL = "INSERT INTO gazouille(titre, corps) VALUES(?,?)";
	
	private JdbcTemplate jdbcTempl;

	public JdbcTemplate getJdbcTempl() {return jdbcTempl;}
	public void setJdbcTempl(JdbcTemplate jdbcTempl) {this.jdbcTempl = jdbcTempl;}
	
	/* (non-Javadoc)
	 * @see com.otmanel.secondJunitSpring.repositories.IGazouilleDao#findAll()
	 */
	@Override
	public List<Gazouille> findAll(){
		// this => appele meth impl mapRow
		return jdbcTempl.query(FIND_ALL_SQL, this);
	}
	
	/* (non-Javadoc)
	 * @see com.otmanel.secondJunitSpring.repositories.IGazouilleDao#findById(int)
	 */
	@Override
	public Gazouille findById(int id) {
		return jdbcTempl.queryForObject(FIND_BY_ID_SQL, new Object[] {id}, this);
	}
	
	/* (non-Javadoc)
	 * @see com.otmanel.secondJunitSpring.repositories.IGazouilleDao#save(com.otmanel.secondJunitSpring.metier.Gazouille)
	 */
	@Override
	public int save(Gazouille g) {
		if (g.getId() == 0) return jdbcTempl.update(INSERT_ONE_SQL, g.getTitre(), g.getCorps());
		else return jdbcTempl.update(UPDATE_ONE_SQL, g.getTitre(), g.getCorps(), g.getId());
	}
	
	@Override
	public Gazouille mapRow(ResultSet rs, int arg1) throws SQLException {
		return new Gazouille(rs.getInt("id"), rs.getString("titre"), rs.getString("corps"));
	}
}
