package interfacesDao;

import java.io.Serializable;

import modelo.Proyecto;



public interface ProyectoDAO extends GenericDAO<Proyecto> {
	
	public boolean existeProyecto(String nombreProyecto);
	public Proyecto recuperarProyecto(Serializable id);

}
