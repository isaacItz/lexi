package modelo;

public class Nodo {
	private int elemento;
	private Token token;
	private int padre;
	private int marca;
	private String tipo;
	
	public Nodo() {
		// TODO Auto-generated constructor stub
	}

	public int getElemento() {
		return elemento;
	}

	public void setElemento(int elemento) {
		this.elemento = elemento;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public int getPadre() {
		return padre;
	}

	public void setPadre(int padre) {
		this.padre = padre;
	}
	
	public int getMarca() {
		return marca;
	}

	public void setMarca(int marca) {
		this.marca = marca;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Nodo [elemento=" + elemento + ", token=" + token + ", padre=" + padre + ", marca=" + marca + ", tipo="
				+ tipo + "]";
	}

	
	
	
	
	
	

}
