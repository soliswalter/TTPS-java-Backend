package clasesDaoHibernateJPA;

import java.io.Serializable;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import interfacesDao.UsuarioDAO;
import modelo.Usuario;

@Repository
public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario> implements UsuarioDAO {

	public UsuarioDAOHibernateJPA() {
		super(Usuario.class);
	}

	@Override
	public Usuario recuperarUsuario(String usuario) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Usuario recuperarUsuario(Serializable id) {
		Usuario usuario = this.recuperar(id);
		usuario.getProyectos().size();	//truco para que se carguen los proyectos que estaban en lazy
		usuario.getProyectoMiembro().size();
		return usuario;
	}
	

	//@Override
	/*public Usuario recuperarUsuario(String nombreUsuario) {
		Usuario resultado;
		Query consulta = this.getEntityManager().createQuery("select p from Usuario p where p.nombreUsuario = :name");
		consulta.setParameter("name", nombreUsuario);
		try {
			resultado = (Usuario) consulta.getSingleResult();
		} catch (Exception e) {
			resultado = null;
		}
		return resultado;

	}*/
	
	@Override
	public boolean existeUsuario(String nombreUsuario) {
		Usuario resultado;
		Query consulta = this.getEntityManager().createQuery("select p from Usuario p where p.nombreUsuario = :name");
		consulta.setParameter("name", nombreUsuario);
		try {
			resultado = (Usuario) consulta.getSingleResult();
		} catch (Exception e) {
			resultado = null;
		}
		return (resultado != null);
	}
	
	@Override
	public Usuario autenticarUsuario(String nombre, String clave) {//duda: deberian ser transaccionales los metodos
		Usuario resultado = null;
		Query consulta = this.getEntityManager().createQuery("select u from Usuario u where u.nombreUsuario = :nombre "
				+ "and u.password = :clave");
		consulta.setParameter("nombre", nombre);
		consulta.setParameter("clave", clave);
		try {
			resultado = (Usuario) consulta.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		return resultado;
	}

}
