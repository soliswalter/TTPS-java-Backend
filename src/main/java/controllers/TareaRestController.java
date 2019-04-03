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

import interfacesDao.ListaDAO;
import interfacesDao.TareaDAO;
import interfacesDao.UsuarioDAO;
import modelo.Lista;
import modelo.Proyecto;
import modelo.Tarea;
import modelo.Usuario;

@RestController
public class TareaRestController {
	
	@Autowired
	TareaDAO tareaDAO;
	
	@Autowired
	ListaDAO listaDAO;
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	//CREA UNA TAREA PARA LA LISTA CON ID PASADO POR PARAMETRO
			@CrossOrigin(origins = "*")
			@RequestMapping(value = "/tareas/lista/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<String> createTarea(@PathVariable("id") long id, @RequestHeader("Authorization") String token,@RequestBody Tarea tarea){
				//tendria que buscar el usuario a traves del token y luego asignarlo a proy con proy.setProyecto	
				System.out.println("Obteniendo la lista con id " + id);
				if(!listaDAO.existe(id)) {
					return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
				}//no se si tendria que controlar que el proyecto no tenga una lista con ese mismo nombre
				Lista lista = listaDAO.recuperarLista(id);
				
				System.out.println("Creando la tarea: "+tarea.getNombre());
				
				tarea.setLista(lista);
				tareaDAO.persistir(tarea);
				return new ResponseEntity<String>(HttpStatus.CREATED);
			}
			
			//RETORNO LA TAREA CON ID PASADO POR PARÁMETRO
			@CrossOrigin(origins = "*")
			@RequestMapping(value = "/tarea/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<Tarea> getTarea(@PathVariable("id") long id) {
				System.out.println("Obteniendo tarea con id " + id);
				Tarea tarea = tareaDAO.recuperarTarea(id);
				if (tarea == null) {
					System.out.println("Tarea con id " + id + " no encontrado");
					return new ResponseEntity<Tarea>(HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity<Tarea>(tarea, HttpStatus.OK);
			}
	
			//AGREGA PARA UNA TAREA CON ID PASADO POR PARAMETRO UN USUARIO COMO MIEMBRO PASADO POR PARAMETRO
			@CrossOrigin(origins = "*")
			@RequestMapping(value = "/tarea/{id_tarea}/miembros/{id_usuario}", method = RequestMethod.POST)
			public ResponseEntity<String> setMiembro(@PathVariable("id_tarea") long id_t, @PathVariable("id_usuario") long id_u, @RequestHeader("Authorization") String token) {
				System.out.println("Obteniendo tarea con id " + id_t);
				Tarea tarea = tareaDAO.recuperarTarea(id_t);
				System.out.println("Obteniendo usuario con id " + id_u);
				Usuario usuario = usuarioDAO.recuperarUsuario(id_u);
				if (tarea == null) {
					System.out.println("Tarea con id " + id_t + " no encontrado");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				if (usuario == null) {
					System.out.println("Usuario con id " + id_u + " no encontrado");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				tarea.agregarMiembro(usuario);
				tareaDAO.actualizar(tarea);
				return new ResponseEntity<String>(HttpStatus.CREATED);
			}
	
	

}
