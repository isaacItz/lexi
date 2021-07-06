package modelo;

public class Token {
	 private String tipo;
	 private String valor;
	 private int Linea;
	 
	 
	    
	    
	  public Token(String tipo, String valor) {
		super();
		this.tipo = tipo;
		this.valor = valor;
	}

	public String getTipo() {
	        return tipo;
	    }
	 
	    public void setTipo(String string) {
	        this.tipo = string;
	    }
	 
	    public String getValor() {
	        return valor;
	    }
	 
	    public void setValor(String valor) {
	        this.valor = valor;
	    }

		@Override
		public String toString() {
			return "Token [tipo=" + tipo + ", valor=" + valor + "]";
		}
	    
	    
	    
}
