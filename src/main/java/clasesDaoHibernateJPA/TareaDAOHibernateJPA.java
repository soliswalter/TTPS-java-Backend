package clasesDaoHibernateJPA;

import java.io.Serializable;


import org.springframework.stereotype.Repository;

import interfacesDao.TareaDAO;
import modelo.Lista;
import modelo.Tarea;
import modelo.Usuario;

@Repository
public class TareaDAOHibernateJPA extends GenericDAOHibernateJPA<Tarea> implements TareaDAO  {
	
	public TareaDAOHibernateJPA() {
		super(Tarea.class);
	}

	/**
	 * esté método es a modo de ejemplo, se agregaría para cosas particulares de la
	 * entidad Persona
	 **/
	@Override
	public Tarea recuperarTarea(Serializable id) {
		Tarea tarea = this.recuperar(id);
		tarea.getMiembros().size();	//truco para que se carguen los miembros que estaban en lazy
		tarea.getComentarios().size();
		tarea.getMiembros().size();
		return tarea;
	}
	
	

}
