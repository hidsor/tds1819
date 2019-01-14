package application.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Video {

	// Atributos
	private int codigo;
	private String URL;
	private String titulo;
	private int numReproducciones;
	private Set<Etiqueta> etiquetas;

	// Constructor
	public Video(String URL, String titulo, int numReproducciones) {
		super();
		this.codigo = 0;
		this.URL = URL;
		this.titulo = titulo;
		this.numReproducciones = numReproducciones;
		etiquetas = new HashSet<Etiqueta>();
	}

	// Métodos de consulta
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
		if (etiquetas == null) {
			etiquetas = new HashSet<Etiqueta>();
		}
		return Collections.unmodifiableSet(etiquetas);
	}

	// Funcionalidad
	public boolean addEtiqueta(Etiqueta e) {
		if (etiquetas == null) {
			etiquetas = new HashSet<Etiqueta>();
		}
		return etiquetas.add(e);
	}

	public boolean removeEtiqueta(Etiqueta e) {
		if (etiquetas == null) {
			etiquetas = new HashSet<Etiqueta>();
		}
		return etiquetas.remove(e);
	}
	
	public boolean containsEtiqueta(Etiqueta e) {
		return etiquetas.contains(e);
	}
	
	public boolean containsEtiqueta(String e) {
		return containsEtiqueta(new Etiqueta(e));
	}
	
	public boolean containsAllEtiquetas(Collection<Etiqueta> c) {
		return etiquetas.containsAll(c);
	}

	public void reproducir() {
		// Incrementa el contador de reproducciones en uno
		numReproducciones++;
	}

	public boolean contieneTitulo(String titulo) {
		return this.titulo.toLowerCase().contains(titulo.toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		Video otro = (Video) obj;

		return URL.equals(otro.URL);
	}

	@Override
	public int hashCode() {
		return URL.hashCode();
	}
	
	@Override
	public String toString() {
		return "Titulo: " + titulo + " \t Reproducciones: " + numReproducciones;
	}


	

}
