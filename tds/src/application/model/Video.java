package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Video {
	private int codigo;
	private String URL;
	private String titulo;
	private int numReproducciones;
	private Set<Etiqueta> etiquetas;
	
	
	public Video(String URL, String titulo, int numReproducciones) {
		super();
		this.setCodigo(0);
		this.URL = URL;
		this.titulo = titulo;
		this.numReproducciones = numReproducciones;
		etiquetas = new HashSet<Etiqueta>();
	}

	

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getURL() {
		return URL;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getNumReproducciones() {
		return numReproducciones;
	}

	public Set<Etiqueta> getEtiquetas() {
		return Collections.unmodifiableSet(etiquetas);
	}


	// FUNCIONALIDAD
	public boolean addEtiqueta(Etiqueta e) {
		return etiquetas.add(e);
	}

	public boolean removeEtiqueta(Etiqueta e) {
		return etiquetas.remove(e);
	}
	
	
	


}
