package modelo;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Usuario implements java.io.Serializable{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String nombreUsuario;
	private String password;
	@OneToMany(mappedBy="propietario",cascade={CascadeType.REMOVE})
	private List<Proyecto> proyectos;
	@ManyToMany(cascade={CascadeType.REMOVE})
	private List<Proyecto>proyectoMiembro;
	
	public Usuario() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	@JsonProperty(value = "proyectoMiembro")
	public List<Proyecto> getProyectoMiembro() {
		return proyectoMiembro;
	}
	
	@JsonIgnore
	@JsonProperty(value = "proyectos")
	public List<Proyecto> getProyectos() {
		return proyectos;
	}

	public void setProyectos(List<Proyecto> proyecto) {
		this.proyectos = proyecto;
	}

	public void setProyectoMiembro(List<Proyecto> proyectoMiembro) {
		this.proyectoMiembro = proyectoMiembro;
	}
	
	public void agregarProyectoAparticipar(Proyecto proyecto) {
		this.proyectoMiembro.add(proyecto);
	}
	
	
	
}
