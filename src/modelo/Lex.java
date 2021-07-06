package modelo;

import java.util.ArrayList;

import java.util.List;


public class Lex {
	private String errores = "";
	private List<String> identificadores = new ArrayList<String>();



	public List<Token> getTokens(String input){
		List<Token> tokens = new ArrayList<Token>();
		input = haySalto(input);
		char[] codigo = input.toCharArray();
		String linea = "";
		String token = "";
		boolean letra = false;
		boolean digit = false;
		boolean punto = false;
		boolean end = false;
		int numLinea = 0;
		for (int i = 0; i < codigo.length; i++) {
			if(codigo[i]=='\n') {
				numLinea++;
				char[] aux = linea.toCharArray();
				
				for (int j = 0; j < aux.length; j++) {
					if(Character.isLetter(aux[j])) {
						
						token+=aux[j];
						letra = true;
					}else if(Character.isDigit(aux[j])) {
						
						token+=aux[j];
						digit=true;
					}else if(aux[j] == '.'){
						
						token+=aux[j];
						punto=true;
					}else if(token.length()>= 2 || (digit && token.length()==1)) {
						if(digit) {
							if(digit && letra) {
								if(reglaIndentificador(token)) {
									Token tk = new Token("IDENTIFICADOR_TK", token);
									addIdentificador(token);
									tokens.add(tk);
									token = "";
									digit = false;
									letra = false;
									j--;
								}else {
									errores+="Error Ln"+numLinea+": Indentificador no valido\n";
									token = "";
									digit = false;
									letra = false;
									j--;
								}
							}else if(digit && punto) {
								if(reglaReal(token)) {
								
									Token tk = new Token("NUMERO_REAL_TK", token);
									tokens.add(tk);
									token = "";
									digit = false;
									punto = false;
									j--;
								}else {
									
									errores+="Error Ln"+numLinea+": numero real no valido\n";
									token = "";
									digit = false;
									punto = false;
									j--;
								}
							}else {
								if(reglaEntero(token)) {
									
									Token tk = new Token("NUMERO_ENTERO_TK", token);
									tokens.add(tk);
									token = "";
									digit = false;
									j--;
								}else{
									
									errores+="Error Ln"+numLinea+": numero entero no valido\n";
									token = "";
									digit = false;
									j--;
								}
							}

						}else {
							
							switch (palabraReservada(token)) {
							case 0:
								
								Token tk = new Token("PALABRA_RESERVADA_"+token, token);
								tokens.add(tk);
								token = "";
								letra = false;
								j--;
								break;
							case 1:
								
								errores+="Error Ln"+numLinea+": Error de sintaxis\n";
								token = "";
								letra = false;
								j--;
								break;
							case 2:
								
								errores+="Error Ln"+numLinea+": Palabra no encontrada\n";
								token = "";
								letra = false;
								j--;
								break;
							}
						}

					}else if(!Character.isWhitespace(aux[j])) {
						if(buscarCaracter(aux[j])) {
							Token tk = new Token("SIMBOLO_"+aux[j], aux[j]+"");
							tokens.add(tk);
						}else {
							errores+="Error Ln"+numLinea+": Simbolo no valido\n";
						}
					}
				}
			
				if(token.length() != 0) {
					if(digit) {
						if(letra && digit) {
							if(reglaIndentificador(token)) {
								
								Token tk = new Token("IDENTIFICADOR_TK", token);
								tokens.add(tk);
								addIdentificador(token);
								token = "";
								digit = false;
								letra = false;
							}else {
							
								errores+="Error Ln"+numLinea+": Indentificador no valido\n";
								token = "";
								digit = false;
								letra = false;
							}
						}else if(punto && digit) {
							if(reglaReal(token)) {
								
								Token tk = new Token("NUMERO_REAL_TK", token);
								tokens.add(tk);
								token = "";
								digit = false;
								punto = false;
							}else {
								errores+="Error Ln"+numLinea+": numero real no valido\n";
								token = "";
								digit = false;
								punto = false;
							}
						}else if(digit) {
							if(reglaEntero(token)) {
								Token tk = new Token("NUMERO_ENTERO_TK", token);
								tokens.add(tk);
								token = "";
								digit = false;
							}else{

								errores+="Error Ln"+numLinea+": numero entero no valido\n";
								token = "";
								digit = false;
							}
						}


					}else {
						switch (palabraReservada(token)) {
						case 0:
							Token tk = new Token("PALABRA_RESERVADA_"+token, token);
							tokens.add(tk);
							token = "";
							letra = false;
							break;
						case 1:
							errores+="Error Ln"+numLinea+": Error de sintaxis\n";
							token = "";
							letra = false;
							break;
						case 2:
							errores+="Error Ln"+numLinea+": Palabra no encontrada\n";
							token = "";
							letra = false;
							break;
						}

					}
				}

				linea = "";
				Token salto = new Token("SALTO"," ");
				tokens.add(salto);
			}else {
				linea+=codigo[i];
			}

		}
		return tokens;
	}


	public boolean buscarCaracter(char c) {
		char[] simbolos = {'{',	'}','(',')',';',',','.','='};
		boolean b = false;
		for (int i = 0; i < simbolos.length; i++) {
			if(c == simbolos[i]) {
				b = true;
				break;
			}
		}
		return b;
	}


	public boolean reglaIndentificador(String identificador) {
		int numLetra = 0;
		int numDigit = 0;
		char[] aux = identificador.toCharArray();
		for (int i = 0; i < aux.length; i++) {
			if(Character.isLetter(aux[i])) {
				numLetra++;
			}else if(Character.isDigit(aux[i])) {
				numDigit++;
			}
		}
		if(numLetra == 1 && (numDigit > 0 && numDigit <= 3)) {
			return true;
		}else {
			return false;
		}

	}


	public String haySalto(String codigo) {
		if(codigo.charAt(codigo.length()-1) != '\n') {
			codigo+="\n";
		}
		return codigo;
	}


	public boolean reglaReal(String numReal) {
		String [] numeros = numReal.split("\\.");
		String entero = numeros[0];
		String decimal = numeros[1];
		if((entero.length() > 0  && entero.length() <= 5) && (decimal.length() > 0 && decimal.length() <= 2)) {
			return true;
		}else {
			return false;
		}
	}

	public boolean reglaEntero(String numEnt) {
		if(numEnt.length() > 0 && numEnt.length() <= 5) {
			return true;
		}else {
			return false;
		}



	}


	public int palabraReservada(String palabra) {
		char[] c = palabra.toCharArray();
		String [] palabras = {"BEGIN" , "END", "INTEGER", "REAL", "READ", "WRITE" ,"ADD", "SUB" , "MUL", "DIV"};
		for (int i = 0; i < c.length; i++) {
			if(Character.isLowerCase(c[i])) {
				return 1;
			}
		}
		for (int i = 0; i < palabras.length; i++) {
			if(palabra.compareTo(palabras[i]) == 0) {
				return 0;
			}
		}
		return 2;
	}

	public String getErrores() {
		return errores;
	}


	public void setErrores(String errores) {
		this.errores = errores;
	}


	public List<String> getIdentificadores() {
		return identificadores;
	}


	public void setIdentificadores(List<String> identificadores) {
		this.identificadores = identificadores;
	}

	public void addIdentificador(String identificador) {
		boolean b = true;
		if(identificadores.size() != 0) {
			for (int i = 0; i < identificadores.size(); i++) {
				if(identificadores.get(i).compareTo(identificador)==0) {
					b = false;
				}
			}
			if(b) {
				identificadores.add(identificador);
			}
			
			
		}else {
			identificadores.add(identificador);
		}
	}

}