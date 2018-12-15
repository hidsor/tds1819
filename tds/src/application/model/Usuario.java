package application.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
	// ATRIBUTOS
	private int codigo; // Necesario para rescatar un usuario del servidor de persistencia
	private String login;
	private String password;
	private String nombre;
	private String apellidos;
	private LocalDate fechaNac;
	private String email;
	private RolPremium premium;
	private List<ListaVideos> listas;
	private ListaVideos listaRecientes;
	
	
	// CONSTRUCTORES
	public Usuario(String login, String password, String nombre, String apellidos, LocalDate fechaNac, String email) {
		codigo = 0;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNac = fechaNac;
		this.email = email;
		this.premium = null;
		listas = new LinkedList<ListaVideos>();
		listaRecientes = new ListaVideos("Reciente");
	}
	
	public Usuario(String login, String password) {
		this(login, password, "", "", null, "");
	}
	
	
	// MÉTODOS DE CONSULTA Y MODIFICACION
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public LocalDate getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(LocalDate fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isPremium() {
		return premium != null;
	}
	
	// hacemos premium al usuario asignandole un rol premium
	public void setPremium() {
		this.premium = new RolPremium();
	}
	
	public void removePremium() {
		this.premium = null;
	}

	public String getLogin() {
		return login;
	}
	
	public Filtro getFiltro() {
		if (premium == null)
			return new NoFiltro();
		return premium.getFiltro();
	}
	
	
	
	public List<ListaVideos> getListas() {
		return Collections.unmodifiableList(listas);
	}

	public ListaVideos getListaRecientes() {
		return listaRecientes;
	}
	
	public void setListaRecientes(ListaVideos listaRecientes) {
		this.listaRecientes = listaRecientes;
	}
	
	

	// FUNCIONALIDAD
	public void obtenerPremium() {
		premium = new RolPremium();
	}
	
	public boolean addListaVideos(ListaVideos listaVideos) {
		return listas.add(listaVideos);
	}
	
	public boolean removeListaVideos(ListaVideos listaVideos) {
		return listas.remove(listaVideos);
	}
	
	public boolean addVideoReciente(Video video) {
		// Si hay 5 o más videos, quitamos el primero (el que hace más tiempo que reprodujimos)
		if (listaRecientes.getNumVideos() >= 5) {
			listaRecientes.removeVideo(0);
		}		
		return listaRecientes.addVideo(video);
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) 
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		Usuario otro = (Usuario) obj;
		
		return login.equals(otro.login);		
	}
	
	@Override
	public int hashCode() {
		return login.hashCode();
	}
}
