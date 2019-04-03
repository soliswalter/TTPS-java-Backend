package clasesDaoHibernateJPA;

import java.io.Serializable;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import interfacesDao.ProyectoDAO;
import modelo.Proyecto;
import modelo.Usuario;

@Repository
public class ProyectoDAOHibernateJPA extends GenericDAOHibernateJPA<Proyecto> implements ProyectoDAO {

	public ProyectoDAOHibernateJPA() {
		super(Proyecto.class);
	}
	
	/*public Proyecto recuperarProyecto(String proyecto) {
		/*Proyecto resultado;
		Query consulta = EMF.getEMF().createEntityManager().
				createQuery("select p from Proyecto p where p.nombre = :name");
		consulta.setParameter("name", proyecto);
		try {
			resultado = (Proyecto) consulta.getSingleResult();
		}
		catch(NoResultException nre){ //Si no encuentra el proyecto
			resultado = null;
		}
		//return resultado;
		return new Proyecto();
	}*/
	
	@Override
	public Proyecto recuperarProyecto(Serializable id) {
		Proyecto proyecto = this.recuperar(id);
		proyecto.getMiembros().size();	//truco para que se carguen los proyectos que estaban en lazy
		proyecto.getListas().size();
		return proyecto;
	}
	
	public boolean existeProyecto(String nombreProyecto) {
		Proyecto resultado;
		Query consulta = this.getEntityManager().createQuery("select p from Proyecto p where p.nombre = :name");
		consulta.setParameter("name", nombreProyecto);
		try {
			resultado = (Proyecto) consulta.getSingleResult();
		} catch (Exception e) {
			resultado = null;
		}
		return (resultado != null);
	}
}
