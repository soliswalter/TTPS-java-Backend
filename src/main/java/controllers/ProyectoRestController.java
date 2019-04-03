package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import interfacesDao.ListaDAO;
import interfacesDao.ProyectoDAO;
import interfacesDao.UsuarioDAO;
import io.jsonwebtoken.Jwts;
import modelo.Lista;
import modelo.Proyecto;
import modelo.Usuario;

@RestController
public class ProyectoRestController {
	
	@Autowired
	ProyectoDAO proyectoDAO;
	
	@Autowired
	UsuarioDAO userDAO;
	
	@Autowired
	ListaDAO listaDAO;
	
	
	//CREO UN PROYECTO PARA EL USUARIO QUE YA ESTA LOGUEADO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/proyectos", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createProyecto(@RequestHeader("Authorization") String token, @RequestBody Proyecto proy){
		//tendria que buscar el usuario a traves del token y luego asignarlo a proy con proy.setProyecto
		String id = Jwts.parser().setSigningKey("mi_clave").parseClaimsJws(token).getBody().get("id").toString();
		long aux_id = Long.parseLong(id);
		System.out.println("Obteniendo usuario con id " + id);
		Usuario user = userDAO.recuperar(aux_id);
		
		System.out.println("Creando el proyecto: "+proy.getNombre());
		if (proyectoDAO.existeProyecto(proy.getNombre())) {
			System.out.println("Ya existe un proyecto con nombre " + proy.getNombre());
			return new ResponseEntity<String>(HttpStatus.CONFLICT); //Código de respuesta 409
		}
		proy.setPropietario(user);
		proyectoDAO.persistir(proy);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	
	//CREA UN PROYECTO PARA EL USUARIO CON ID PASADO POR PARAMETRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/proyectos/user/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createProject(@PathVariable("id") long id, @RequestHeader("Authorization") String token,@RequestBody Proyecto proy){
		//tendria que buscar el usuario a traves del token y luego asignarlo a proy con proy.setProyecto	
		System.out.println("Obteniendo usuario con id " + id);
		Usuario user = userDAO.recuperar(id);
		
		System.out.println("Creando el proyecto: "+proy.getNombre());
		if (proyectoDAO.existeProyecto(proy.getNombre())) {
			System.out.println("Ya existe un proyecto con nombre " + proy.getNombre());
			return new ResponseEntity<String>(HttpStatus.CONFLICT); //Código de respuesta 409
		}
		proy.setPropietario(user);
		proyectoDAO.persistir(proy);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	//AGREGA PARA UN PROYECTO CON ID PASADO POR PARAMETRO UN USUARIO COMO MIEMBRO PASADO POR PARAMETRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/proyecto/{id_proy}/miembros/{id_usuario}", method = RequestMethod.POST)
	public ResponseEntity<String> setMiembro(@PathVariable("id_proy") long id_p, @PathVariable("id_usuario") long id_u, @RequestHeader("Authorization") String token) {
		System.out.println("Obteniendo proyecto con id " + id_p);
		Proyecto proy = proyectoDAO.recuperarProyecto(id_p);
		System.out.println("Obteniendo usuario con id " + id_u);
		Usuario user = userDAO.recuperarUsuario(id_u);
		if (proy == null) {
			System.out.println("Proyecto con id " + id_p + " no encontrado");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (user == null) {
			System.out.println("Usuario con id " + id_u + " no encontrado");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.agregarProyectoAparticipar(proy);
		proy.agregarMiembro(user);
		proyectoDAO.actualizar(proy);
		userDAO.actualizar(user);
		System.out.println("holaaa2");
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	//DEVUELVE LOS MIEMBROS DE UN PROYECTO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/proyecto/{id}/miembros", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Usuario>> getMiembros(@PathVariable("id") long id) {
		System.out.println("Obteniendo miembros de proyecto con id " + id);
		Proyecto proy = proyectoDAO.recuperarProyecto(id);
		if (proy == null) {
			System.out.println("Proyecto con id " + id + " no encontrado");
			return new ResponseEntity<List<Usuario>>(HttpStatus.NOT_FOUND);
		}
		List<Usuario> miembros = proy.getMiembros();
		return new ResponseEntity<List<Usuario>>(miembros, HttpStatus.OK);
	}
	
	//Devuelve listas de un proyecto
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/proyecto/{id}/listas", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Lista>> getListas(@PathVariable("id") long id) {
		System.out.println("Obteniendo listas de proyecto con id " + id);
		Proyecto proy = proyectoDAO.recuperarProyecto(id);
		if (proy == null) {
			System.out.println("Proyecto con id " + id + " no encontrado");
			return new ResponseEntity<List<Lista>>(HttpStatus.NOT_FOUND);
		}
		List<Lista> listas = proy.getListas();
		return new ResponseEntity<List<Lista>>(listas, HttpStatus.OK);
	}
	
	
	
	
	//DEVUELVE UN PROYECTO CON EL ID PASADO POR PARÁMETRO
		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/proyecto/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Proyecto> getProyecto(@PathVariable("id") long id) {
			System.out.println("Obteniendo proyecto con id " + id);
			Proyecto proy = proyectoDAO.recuperar(id);
			if (proy == null) {
				System.out.println("Proyecto con id " + id + " no encontrado");
				return new ResponseEntity<Proyecto>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Proyecto>(proy, HttpStatus.OK);
		}
		
		//ACTUALIZO PROYECTO
		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/proyecto/{id}", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Proyecto> updateProyecto(@PathVariable("id") long id, @RequestBody Proyecto proy) {
			System.out.println("Verificando autorización..");
			Proyecto proyectoActual = proyectoDAO.recuperar(id);
			if (proyectoActual==null) {
				System.out.println("Usuario con id " + id + " no encontrado");
				return new ResponseEntity<Proyecto>(HttpStatus.NOT_FOUND); //Error 404
			}
			System.out.println("Actualizando el proyecto " + id);
			proyectoActual.setNombre(proy.getNombre());
			System.out.println(proy.getFechaInicio());
			proyectoActual.setFechaInicio(proy.getFechaInicio());
			proyectoActual.setFechaFinalizacion(proy.getFechaFinalizacion());
			proyectoDAO.actualizar(proyectoActual);
			return new ResponseEntity<Proyecto>(proyectoActual, HttpStatus.OK);
		}
		
		//BORRO UN PROYECTO
				@CrossOrigin(origins = "*")
				@RequestMapping(value = "/proyecto/{id}", method = RequestMethod.DELETE, consumes=MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<String> deleteProyecto(@PathVariable("id") long id) {
					System.out.println("Verificando autorización..");
					Proyecto proyectoActual = proyectoDAO.recuperarProyecto(id);
					if (proyectoActual==null) {
						System.out.println("Usuario con id " + id + " no encontrado");
						return new ResponseEntity<String>(HttpStatus.NOT_FOUND); //Error 404
					}
					System.out.println("Borrando el proyecto " + id);
					/*for (int i = 0; i < proyectoActual.getListas().size(); i++) {
						listaDAO.borrar(proyectoActual.getListas().get(i));
					}*/
					proyectoDAO.borrar(proyectoActual);
					return new ResponseEntity<String>(HttpStatus.OK);
				}
	
	
	

}
