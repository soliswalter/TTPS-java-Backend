package interfacesDao;

import java.io.Serializable;
import java.util.List;

import modelo.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario> {
	public Usuario recuperarUsuario(String usuario);
	public Usuario recuperarUsuario(Serializable id);
	public boolean existeUsuario(String nombreUsuario);
	public Usuario autenticarUsuario(String nombreUsuario, String clave);

}
