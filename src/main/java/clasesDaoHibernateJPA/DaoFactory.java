package clasesDaoHibernateJPA;

//import clasesDao.UsuarioDAO;

public class DaoFactory {
	public static UsuarioDAOHibernateJPA getUsuarioDAO() {
		return new UsuarioDAOHibernateJPA();
	}
	public static ProyectoDAOHibernateJPA getProyectoDAO() {
		return new ProyectoDAOHibernateJPA();
	}
	public static TareaDAOHibernateJPA getTareaDAO() {
		return new TareaDAOHibernateJPA();
	}
	public static ListaDAOHibernateJPA getListaDAO() {
		return new ListaDAOHibernateJPA();
	}
	public static ComentarioDAOHibernateJPA getComentarioDAO() {
		return new ComentarioDAOHibernateJPA();
	}
	
	

}
