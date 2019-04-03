package clasesDaoHibernateJPA;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import interfacesDao.GenericDAO;

@Transactional
public class GenericDAOHibernateJPA<T> implements GenericDAO<T> {
    
	private EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected Class<T> persistentClass;
	
	GenericDAOHibernateJPA(Class<T> t){ //ver como hacer el constructor
		this.setPersistentClass(t);
	}
	
	@Override
	public T persistir(T entity) {
		this.getEntityManager().persist(entity); 
		return entity;
	}

	@Override
	public T actualizar(T entity) {
		T entityNew = this.getEntityManager().merge(entity); 
		return entityNew;
	}
	
	@Override
	public void borrar(T entity) {

		//this.getEntityManager().remove(entity);
		// em.remove(em.merge(entity)); //Si agrego merge funciona
		this.getEntityManager().remove(this.getEntityManager().merge(entity));
	}

	@Override
	public T borrar(Serializable id) {
		T entity = this.getEntityManager().find(this.getPersistentClass(), id);
		if (entity != null) {
			this.borrar(entity);
		} 
		return entity;
	}
	
	public List<T> recuperarTodos() {
		Query consulta = this.getEntityManager().createQuery("select e from " 
				+ getPersistentClass().getSimpleName() + " e");
		
		List<T> resultado = (List<T>) consulta.getResultList();
		
		return resultado;
	}
	
	@Override
	public boolean existe(Serializable id) {
		T entity = this.getEntityManager().find(this.getPersistentClass(), id);
		
		return (entity != null);
	}
	
	@Override
	public T recuperar(Serializable id) {
		T entity = this.getEntityManager().find(this.getPersistentClass(), id);
		
		return entity;
	}
	
	public Class<T> getPersistentClass() {
		return this.persistentClass;
	}
	
	public void setPersistentClass(Class<T> T) {
		this.persistentClass = T;
	}
			
}
