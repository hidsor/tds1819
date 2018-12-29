package application.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Usuario {

	// Atributos
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

	// Constructores
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

	// M�todos de consulta y modificado
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

	// Hacemos premium al usuario asign�ndole un rol premium
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
	
	public ListaVideos getListaVideos(String titulo) {
		for (ListaVideos lista : listas) {
			if (lista.getNombre().equals(titulo)) return lista;
		}
		return null;
	}

	public ListaVideos getListaRecientes() {
		return listaRecientes;
	}

	public void setListaRecientes(ListaVideos listaRecientes) {
		this.listaRecientes = listaRecientes;
	}

	// Funcionalidad
	public void obtenerPremium() {
		premium = new RolPremium();
	}

	public boolean addListaVideos(ListaVideos listaVideos) {
		return listas.add(listaVideos);
	}

	public boolean removeListaVideos(String titulo) {
		ListaVideos lista = getListaVideos(titulo);
		return listas.remove(lista);
	}

	public boolean addVideoReciente(Video video) {
		// Si hay 5 o m�s videos, quitamos el primero (el que hace m�s tiempo que reprodujimos)
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
	
	
	public String infoListasVideos() {
		StringBuffer buffer = new StringBuffer("");
		for (ListaVideos i : listas) {
			buffer.append(i.toString() + "\n");
		}
		
		return buffer.toString();
	}
}
