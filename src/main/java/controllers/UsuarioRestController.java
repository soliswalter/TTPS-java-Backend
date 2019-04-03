package controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import clases.Respuesta;
import modelo.Proyecto;
import modelo.Usuario;
import interfacesDao.UsuarioDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UsuarioRestController {
	
	@Autowired
	UsuarioDAO userDAO;
	
	//DEVUELVE UN USUARIO CON EL ID PASADO POR PARÁMETRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> getUser(@PathVariable("id") long id) {
		System.out.println("Obteniendo usuario con id " + id);
		Usuario user = userDAO.recuperar(id);
		if (user == null) {
			System.out.println("Usuario con id " + id + " no encontrado");
			return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
	}
	
	//DEVUELVE EL USUARIO QUE ESTA LOGUEADO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/user", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> getUsuario(@RequestHeader("Authorization") String token) {
		
		String id = Jwts.parser().setSigningKey("mi_clave").parseClaimsJws(token).getBody().get("id").toString();
		long aux_id = Long.parseLong(id);
		System.out.println("Obteniendo usuario con id " + id);
		Usuario user = userDAO.recuperar(aux_id);
		if (user == null) {
			System.out.println("Usuario con id " + id + " no encontrado");
			return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
	}

	//RECUPERA LOS PROYECTOS QUE LE PERTENECEN AL USUARIO CON ID PASADO POR PARAMETRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users/{id}/proyectos", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Proyecto>> getProyectosUsuario(@PathVariable("id") long id) {
		System.out.println("Obteniendo proyectos de usuario con id " + id);
		Usuario user = userDAO.recuperarUsuario(id);
		if (user == null) {
			System.out.println("Usuario con id " + id + " no encontrado");
			return new ResponseEntity<List<Proyecto>>(HttpStatus.NOT_FOUND);
		}
		List<Proyecto> proyectos = user.getProyectos();
		return new ResponseEntity<List<Proyecto>>(proyectos, HttpStatus.OK);
	}
	
	//DEVUELVO LOS PROYECTOS DE LOS QUE EL USUARIO ES MIEMBRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users/{id}/proyectosParticipa", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Proyecto>> getProyectosParticipaUsuario(@PathVariable("id") long id) {
		System.out.println("Obteniendo proyectos de usuario con id " + id);
		Usuario user = userDAO.recuperarUsuario(id);
		if (user == null) {
			System.out.println("Usuario con id " + id + " no encontrado");
			return new ResponseEntity<List<Proyecto>>(HttpStatus.NOT_FOUND);
		}
		List<Proyecto> proyectosParticipa = user.getProyectoMiembro();
		return new ResponseEntity<List<Proyecto>>(proyectosParticipa, HttpStatus.OK);
	}
	
	
	
	//Agrego un usuario
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createUser(@RequestBody Usuario user, UriComponentsBuilder ucBuilder){
		System.out.println("Creando el usuario " + user.getNombreUsuario());
		if (userDAO.existeUsuario(user.getNombreUsuario())) {
			System.out.println("Ya existe un usuario con nombre " + user.getNombreUsuario());
			return new ResponseEntity<String>(HttpStatus.CONFLICT); //Código de respuesta 409
		}
		userDAO.persistir(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//ACTUALIZO USUARIO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> updateUser(@PathVariable("id") long id, @RequestBody Usuario user) {
		System.out.println("Verificando autorización..");
		Usuario currentUser = userDAO.recuperar(id);
		if (currentUser==null) {
			System.out.println("Usuario con id " + id + " no encontrado");
			return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND); //Error 404
		}
		System.out.println("Actualizando el usuario " + id);
		currentUser.setNombreUsuario(user.getNombreUsuario());
		currentUser.setEmail(user.getEmail());
		currentUser.setPassword(user.getPassword());
		userDAO.actualizar(currentUser);
		return new ResponseEntity<Usuario>(currentUser, HttpStatus.OK);
	}
	
	
	//AUTENTICO USUARIO
		@CrossOrigin(origins = "*")
	    @RequestMapping(value = "/autenticacion", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE,
				consumes=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Respuesta> authenticateUser(@RequestBody Usuario usuario){
			String nombreUsuario = usuario.getNombreUsuario();
			String clave = usuario.getPassword();
			Usuario user = userDAO.autenticarUsuario(nombreUsuario, clave);//usuario con todos sus datos
			if( user == null) {
				System.out.println("Nombre de usuario o contraseña incorrecta");
				return new ResponseEntity<Respuesta>(HttpStatus.UNAUTHORIZED); //Error 401
			}
			String key= "mi_clave";	//esto deberia ir en una property
			long tiempo = System.currentTimeMillis();//hora actual
			Respuesta token = new Respuesta();
			
			token.setToken(Jwts.builder().setSubject(user.getNombreUsuario()).claim("id", user.getId())
							//.setExpiration(new Date(tiempo + 900000))//tiempo de expiración de 15 minutos
							.signWith(SignatureAlgorithm.HS512, "mi_clave")
							.compact());
			
			System.out.println("Usuario autenticado");
			token.setIdUsuario(user.getId());
			token.setNombre(user.getNombreUsuario());
			return new ResponseEntity<Respuesta>(token, HttpStatus.OK);
		}

}
