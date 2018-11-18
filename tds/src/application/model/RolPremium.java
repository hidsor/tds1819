package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RolPremium {
	// ATRIBUTOS
	private Filtro filtroPremium;
	
	
	// CONSTRUCTOR
	public RolPremium(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}
	
	public RolPremium() {
		this(new NoFiltro());
	}


	// MÉTODO DE OBTENCIÓN
	public Filtro getFiltro() {
		return filtroPremium;
	}
	
	public void setFiltro(Filtro filtroPremium) {
		this.filtroPremium = filtroPremium;
	}

	
	// FUNCIONALIDAD
}
