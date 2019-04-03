package interfacesDao;

import modelo.Comentario;

public interface ComentarioDAO extends GenericDAO<Comentario> {
	public Comentario recuperarComentario(Integer id);

}
