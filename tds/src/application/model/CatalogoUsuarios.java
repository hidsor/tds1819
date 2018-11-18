package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CatalogoUsuarios {
	// ATRIBUTOS
	private Set<Usuario> conjuntoUsuarios;

	
	// CONSTRUCTOR
	public CatalogoUsuarios() {
		conjuntoUsuarios = new HashSet<Usuario>();
	}
	
	// MÉTODOS GET
	public Set<Usuario> getConjuntoUsuarios() {
		return Collections.unmodifiableSet(conjuntoUsuarios);
	}


	//FUNCIONALIDAD
	boolean addUsuario(Usuario usuario) {
		return conjuntoUsuarios.add(usuario);
	}
	
	boolean removeUsuario(Usuario usuario) {
		return conjuntoUsuarios.remove(usuario);
	}
	

}
