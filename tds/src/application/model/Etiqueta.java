package application.model;

public class Etiqueta {

	// Atributos
	private String nombre;

	// Constructor
	public Etiqueta(String nombre) {
		this.nombre = nombre;
	}

	// Métodos de consulta
	public String getNombre() {
		return nombre;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Etiqueta otro = (Etiqueta) obj;
		return nombre.equals(otro.nombre);
	}
	
	@Override
	public int hashCode() {
		return nombre.hashCode();
	}
	
	@Override
	public String toString() {
		return nombre.toString();
	}

}
