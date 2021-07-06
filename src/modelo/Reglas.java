package modelo;

import java.util.Arrays;

public class Reglas {
	private String padre;
	private String[] elementos;
	
	public Reglas(String padre, String[] elementos) {
		super();
		this.padre = padre;
		this.elementos = elementos;
	}
	
	public String getPadre() {
		return padre;
	}
	public void setPadre(String padre) {
		this.padre = padre;
	}
	public String[] getElementos() {
		return elementos;
	}
	public void setElementos(String[] elementos) {
		this.elementos = elementos;
	}
	
	@Override
	public String toString() {
		return "ReglasProd [padre=" + padre + ", elementos=" + Arrays.toString(elementos) + "]";
	}
	
	
	

}
