package controllers;


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

import interfacesDao.ListaDAO;
import interfacesDao.ProyectoDAO;
import modelo.Lista;
import modelo.Proyecto;
import modelo.Usuario;

@RestController
public class ListaRestController {
	
	@Autowired 
	ListaDAO listaDAO;
	
	@Autowired 
	ProyectoDAO proyectoDAO;
	
	//RETORNO LA LISTA CON ID PASADO POR PARÁMETRO
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/lista/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Lista> getLista(@PathVariable("id") long id) {
		System.out.println("Obteniendo lista con id " + id);
		Lista lista = listaDAO.recuperarLista(id);
		if (lista == null) {
			System.out.println("Lista con id " + id + " no encontrado");
			return new ResponseEntity<Lista>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Lista>(lista, HttpStatus.OK);
	}
	
	//Agrego una lista
		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/listas", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> createUser(@RequestBody Lista lista, UriComponentsBuilder ucBuilder){
			System.out.println("Creando la lista " + lista.getNombre());
			if (listaDAO.existeLista(lista.getNombre())) {//quizas este control esta de más
				System.out.println("Ya existe una lista con nombre " + lista.getNombre());
				return new ResponseEntity<String>(HttpStatus.CONFLICT); //Código de respuesta 409
			}
			listaDAO.persistir(lista);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/lista/{id}").buildAndExpand(lista.getId()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
		
		//CREA UNA LISTA PARA EL PROYECTO CON ID PASADO POR PARAMETRO
		@CrossOrigin(origins = "*")
		@RequestMapping(value = "/listas/proyecto/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> createLista(@PathVariable("id") long id, @RequestHeader("Authorization") String token,@RequestBody Lista lista){
			//tendria que buscar el usuario a traves del token y luego asignarlo a proy con proy.setProyecto	
			System.out.println("Obteniendo proyecto con id " + id);
			if(!proyectoDAO.existe(id)) {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}//no se si tendria que controlar que el proyecto no tenga una lista con ese mismo nombre
			Proyecto proyecto = proyectoDAO.recuperarProyecto(id);
			
			System.out.println("Creando la lista: "+lista.getNombre());
			
			lista.setProyecto(proyecto);
			listaDAO.persistir(lista);
			return new ResponseEntity<String>(HttpStatus.CREATED);
		}
		
		
	
	

}
