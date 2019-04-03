package controllers;

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

import interfacesDao.ComentarioDAO;
import interfacesDao.TareaDAO;
import interfacesDao.UsuarioDAO;
import modelo.Comentario;
import modelo.Lista;
import modelo.Tarea;
import modelo.Usuario;

@RestController
public class ComentarioRestController {
	
	@Autowired
	ComentarioDAO comentarioDAO;
	
	@Autowired
	TareaDAO tareaDAO;
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	//CREA UN COMENTARIO PARA LA TAREA Y USUARIOS PASADOS COMO PARAMETRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/comentarios/tarea/{id}/usuario/{idUser}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createComentario(@PathVariable("id") long id,@PathVariable("idUser") long idUser, @RequestHeader("Authorization") String token,@RequestBody Comentario comentario){
		System.out.println("Obteniendo la tarea con id " + id);
		if(!tareaDAO.existe(id)) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		Tarea tarea = tareaDAO.recuperarTarea(id);//obtengo tarea
		
		System.out.println("Obteniendo el usuario con id " + idUser);
		if(!usuarioDAO.existe(idUser)) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		Usuario usuario = usuarioDAO.recuperarUsuario(idUser);//obtengo usuario
		
		System.out.println("Creando el comentario:");
		comentario.setTarea(tarea);
		comentario.setUsuario(usuario);
		comentarioDAO.persistir(comentario);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	//RETORNO EL COMENTARIO CON ID PASADO POR PARÁMETRO
		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/comentario/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Comentario> getComentario(@PathVariable("id") long id) {
			System.out.println("Obteniendo comentario con id " + id);
			Comentario comentario = comentarioDAO.recuperar(id);
			if (comentario == null) {
				System.out.println("Lista con id " + id + " no encontrado");
				return new ResponseEntity<Comentario>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Comentario>(comentario, HttpStatus.OK);
		}
	 
	
}
