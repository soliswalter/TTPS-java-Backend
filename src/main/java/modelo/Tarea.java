package modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Tarea {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String descripcion;
	@ManyToMany
	private List<Usuario> miembroTarea;
	private String fechaAsignacion;
	private String fechaVencimiento;
	@OneToMany(mappedBy="tarea")
	private List<Comentario> comentarios;
	@ManyToOne
	private Lista lista;
	
	public Tarea() {
		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@JsonIgnore
	@JsonProperty(value = "miembroTarea")
	public List<Usuario> getMiembros() {
		return miembroTarea;
	}
	public void setMiembros(List<Usuario> miembros) {
		this.miembroTarea = miembros;
	}
	
	public void agregarMiembro(Usuario miembro) {
		this.miembroTarea.add(miembro);
	}
	
	public String getFechaAsignacion() {
		return fechaAsignacion;
	}
	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	@JsonIgnore
	@JsonProperty(value = "comentarios")
	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	@JsonIgnore
	@JsonProperty(value = "lista")
	public Lista getLista() {
		return lista;
	}

	public void setLista(Lista lista) {
		this.lista = lista;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	

}
