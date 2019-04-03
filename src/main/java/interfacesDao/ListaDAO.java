package interfacesDao;

import java.io.Serializable;

import modelo.Lista;
import modelo.Proyecto;

public interface ListaDAO extends GenericDAO<Lista> {
	public Lista recuperarLista(Serializable id);
	public boolean existeLista(String nombreLista);
}
