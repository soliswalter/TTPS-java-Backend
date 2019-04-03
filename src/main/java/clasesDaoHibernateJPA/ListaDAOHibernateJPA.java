package clasesDaoHibernateJPA;

import java.io.Serializable;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import interfacesDao.ListaDAO;
import modelo.Lista;
import modelo.Proyecto;
@Repository
public class ListaDAOHibernateJPA extends GenericDAOHibernateJPA<Lista> implements ListaDAO {

	public ListaDAOHibernateJPA() {
		super(Lista.class);
	}

	@Override
	public Lista recuperarLista(Serializable id) {
		Lista lista = this.recuperar(id);
		lista.getTareas().size();	//truco para que se carguen los proyectos que estaban en lazy
		return lista;
	}
	
	@Override
	public boolean existeLista(String nombreLista) {
		Lista resultado;
		Query consulta = this.getEntityManager().createQuery("select p from Lista p where p.nombre = :name");
		consulta.setParameter("name", nombreLista);
		try {
			resultado = (Lista) consulta.getSingleResult();
		} catch (Exception e) {
			resultado = null;
		}
		return (resultado != null);
	}
	

}
