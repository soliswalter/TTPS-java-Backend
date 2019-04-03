package clasesDaoHibernateJPA;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import interfacesDao.ComentarioDAO;
import modelo.Comentario;

@Repository
public class ComentarioDAOHibernateJPA extends GenericDAOHibernateJPA<Comentario> implements ComentarioDAO{

	public ComentarioDAOHibernateJPA() {
		super(Comentario.class);
	}

	/**
	 * esté método es a modo de ejemplo, se agregaría para cosas particulares de la
	 * entidad Persona
	 **/
	@Override
	public Comentario recuperarComentario(Integer id) {
		/*Query consulta = EMF.getEMF().createEntityManager().
				createQuery("select p from Usuario p where p.nombreUsuario =?");// no sabria de donde importarlo
		consulta.setParameter(1, nombreUsuario);
		Usuario resultado = (Usuario) consulta.getSingleResult();
		return resultado;*/ return null;
	}
}
