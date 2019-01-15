package application.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Usuario {
	// Atributos
	private int codigo; // Necesario para rescatar un usuario del servidor de persistencia
	private String login;
	private String password;
	private String nombre;
	private String apellidos;
	private LocalDate fechaNac;
	private String email;
	private boolean premium;
	private Filtro filtroPremium;
	private Map<String, ListaVideos> listas;
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
		this.premium = false;
		
		listas = new HashMap<String, ListaVideos>();
		listaRecientes = new ListaVideos("Recientes");
		this.filtroPremium = new NoFiltro();
	}

	public Usuario(String login, String password) {
		this(login, password, "", "", null, "");
	}

	// Mï¿½todos de consulta y modificado
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
		return premium;
	}

	// Hacemos premium al usuario asignï¿½ndole un rol premium
	public void setPremium(boolean valor) {
		this.premium = valor;
	}

	public String getLogin() {
		return login;
	}

	public Filtro getFiltro() {
		if (!premium)
			return new NoFiltro();
		return filtroPremium;
	}
	
	public boolean setFiltro(Filtro filtro) {
		if (!premium) return false;
	
		filtroPremium = filtro;
		return true;
	}


	public List<ListaVideos> getListas() {
		return new LinkedList<ListaVideos>(listas.values());
	}
	
	public ListaVideos getListaVideos(String titulo) {
		return listas.get(titulo.toLowerCase());
	}

	public ListaVideos getListaRecientes() {
		return listaRecientes;
	}

	public void setListaRecientes(ListaVideos listaRecientes) {
		this.listaRecientes = listaRecientes;
	}

	
	// Funcionalidad
	public boolean addListaVideos(ListaVideos listaVideos) {
		if (listaRecientes.getNombre().toLowerCase().equals(listaVideos.getNombre().toLowerCase()))
			return false;
		
		return listas.put(listaVideos.getNombre().toLowerCase(), listaVideos) == null;
	}

	public boolean borrarListaVideos(String titulo) {
		return listas.remove(titulo.toLowerCase()) != null;
	}
	
	public boolean addVideoALista(Video video, String tituloLista) {
		ListaVideos lista = getListaVideos(tituloLista);
		if (lista == null) return false;
		return lista.addVideo(video);	
	}
	
	public boolean removeVideoDeLista(Video video, String tituloLista) {
		ListaVideos lista = getListaVideos(tituloLista);
		if (lista == null) return false;
		return lista.addVideo(video);	
	}

	public void reproducirVideo(Video video) {
		video.reproducir();
		addVideoReciente(video);
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
		for (ListaVideos i : listas.values()) {
			buffer.append(i.toString() + "\n");
		}
		
		return buffer.toString();
	}
	
	private boolean addVideoReciente(Video video) {
		while (listaRecientes.removeVideo(video)) {};
		listaRecientes.addVideo(0, video);
		
		// Si tras añadir hay más de 5 videos, quitamos el ultimo (posicion 5)
		if (listaRecientes.getNumVideos() > 5) {
			listaRecientes.removeVideo(5);
		}
		return true;
	}
	

}
