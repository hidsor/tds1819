package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RolPremium {
	// ATRIBUTOS
	private int codigo;
	private Filtro filtroPremium;
	
	
	// CONSTRUCTOR
	public RolPremium(Filtro filtroPremium) {
		setCodigo(0);
		this.filtroPremium = filtroPremium;
	}
	
	public RolPremium() {
		this(new NoFiltro());
	}


	// M�TODO DE OBTENCI�N
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Filtro getFiltro() {
		return filtroPremium;
	}
	
	public void setFiltro(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}

	
	// FUNCIONALIDAD
}
