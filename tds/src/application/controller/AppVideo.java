package application.controller;

import java.time.LocalDate;
import java.util.List;

import application.model.*;

public class AppVideo {
	// Atributos
	private CatalogoUsuarios catalogoUsuarios;
	private CatalogoVideos catalogoVideos;
	private ListaVideos topten;
	private List<Etiqueta> listaEtiquetas;
	
	// Patrón singleton
	private static AppVideo unicaInstancia = null;
	
	// Constructor
	private AppVideo() {
		// Cargamos todos los elementos del controlador
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
		catalogoVideos = CatalogoVideos.getUnicaInstancia();
		//topten = ?
		//listaEtiquetas = ?
	}
	
	// Patrón singleton
	public static AppVideo getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppVideo();
			return unicaInstancia;
		} else {
			return unicaInstancia;
		}
	}
	
	// Métodos get
	public CatalogoUsuarios getCatalogoUsuarios() {
		return catalogoUsuarios;
	}
	
	public CatalogoVideos getCatalogoVideos() {
		return catalogoVideos;
	}
	
	public ListaVideos getTopten() {
		return topten;
	}
	
	public List<Etiqueta> getListaEtiquetas() {
		return listaEtiquetas;
	}
	

	// Funcionalidad
	
	boolean verificarUsuario(String login, String password) {
		Usuario usuario = catalogoUsuarios.getUsuario(login);
		if (usuario == null) return false;
		if (usuario.getPassword().equals(password)) return true;
		else return false;
	}

	
	void registrarUsuario(String login, String password, String nombre, String apellidos, LocalDate fechaNac, String email) {
		Usuario usuario = new Usuario(login, password, nombre, apellidos, fechaNac, email);
		catalogoUsuarios.addUsuario(usuario);
	}
	
	//TODO
}
