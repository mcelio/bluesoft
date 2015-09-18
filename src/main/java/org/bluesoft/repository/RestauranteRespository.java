package org.bluesoft.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bluesoft.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RestauranteRespository {

	// persistence unit
	@PersistenceUnit(name = "jpa-persistence")
	private EntityManagerFactory entityManagerFactory;
	// Console logger

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	static final Logger logger = LogManager.getLogger(RestauranteRespository.class.getName());
	
	/**
	 * Finds all restaurants.
	 * 
	 */
	public List<Restaurant> getAll() throws Exception {
		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			String sql = "SELECT r FROM Restaurant r";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Finds by id.
	 * 
	 */
	public Restaurant get(Long id) throws Exception {
		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			return em.find(Restaurant.class, id);
		} finally {
			em.close();
		}
	}
	
	/**
	 * Finds by id.
	 * 
	 */
	public void update(Long id) throws Exception {
		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			Restaurant o = em.find(Restaurant.class, id);
			Integer votes = o.getVoteNumber();
			if(votes == null){
				o.setVoteNumber(1);
			} else {
				o.setVoteNumber(votes + 1);
			}
			em.persist(o);
			em.flush();
		} finally {
			em.close();
		}
	}

}
