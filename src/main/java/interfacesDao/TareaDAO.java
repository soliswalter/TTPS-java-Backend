package interfacesDao;

import java.io.Serializable;

import modelo.Tarea;

public interface TareaDAO extends GenericDAO<Tarea>{
	public Tarea recuperarTarea(Serializable id);

}
