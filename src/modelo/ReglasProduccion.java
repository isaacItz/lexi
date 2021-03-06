package modelo;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReglasProduccion {

	String errores;
	List<Token> tokens;
	int contador, numeroE = 0, aux = 0;;
	int linea = 1;
	boolean bandera;
	Stack<Token> pila;
	List<Nodo> arbol;
	List<Reglas> reglas;
	List<Token> elementos;

	public ReglasProduccion(List<Token> lTokens) {
		tokens = lTokens;
		errores = "";
		contador = 0;
		bandera = false;
		pila = new Stack<>();
		arbol = new ArrayList<Nodo>();
		reglas = setReglas();
		elementos = new ArrayList<Token>();
	}

	public List<Reglas> setReglas() {
		List<Reglas> reglas = new ArrayList<Reglas>();

		String[] inicio = { "PALABRA_RESERVADA_BEGIN", "SIMBOLO_{" };
		Reglas reglaInicio = new Reglas("INICIO", inicio);
		reglas.add(reglaInicio);

		String[] declaracionEntero = { "ENTERO", "SIMBOLO_;" };
		Reglas reglasDeclaracion = new Reglas("DECLARACION", declaracionEntero);
		reglas.add(reglasDeclaracion);

		String[] declaracionReal = { "REAL", "SIMBOLO_;" };
		Reglas reglasDeclaracionReal = new Reglas("DECLARACION", declaracionReal);
		reglas.add(reglasDeclaracionReal);

		String[] entero = { "PALABRA_RESERVADA_INTEGER", "IDENTIFICADOR_TK" };
		Reglas reglasEntero = new Reglas("ENTERO", entero);
		reglas.add(reglasEntero);

		String[] entero2 = { "PALABRA_RESERVADA_INTEGER", "ASIGNACION" };
		Reglas reglasEntero2 = new Reglas("ENTERO", entero2);
		reglas.add(reglasEntero2);

		String[] entero3 = { "PALABRA_RESERVADA_INTEGER", "IDENTIFICADOR_TK", "MULTIDECLARACION" };
		Reglas reglasEntero3 = new Reglas("ENTERO", entero3);
		reglas.add(reglasEntero3);

		String[] real = { "PALABRA_RESERVADA_REAL", "IDENTIFICADOR_TK" };
		Reglas reglasReal = new Reglas("REAL", real);
		reglas.add(reglasReal);

		String[] real2 = { "PALABRA_RESERVADA_REAL", "ASIGNACION" };
		Reglas reglasReal2 = new Reglas("REAL", real2);
		reglas.add(reglasReal2);

		String[] real3 = { "PALABRA_RESERVADA_REAL", "IDENTIFICADOR_TK", "MULTIDECLARACION" };
		Reglas reglasReal3 = new Reglas("REAL", real3);
		reglas.add(reglasReal3);

		String[] multidelaracion = { "SIMBOLO_,", "IDENTIFICADOR_TK" };
		Reglas reglasMulti = new Reglas("MULTIDECLARACION", multidelaracion);
		reglas.add(reglasMulti);

		String[] multideclaracion2 = { "SIMBOLO_,", "ASIGNACION" };
		Reglas reglasMulti2 = new Reglas("MULTIDECLARACION", multideclaracion2);
		reglas.add(reglasMulti2);

		String[] multideclaracion3 = { "SIMBOLO_,", "IDENTIFICADOR_TK", "MULTIDECLARACION" };
		Reglas reglasMulti3 = new Reglas("MULTIDECLARACION", multideclaracion3);
		reglas.add(reglasMulti3);

		String[] multideclaracion4 = { "SIMBOLO_,", "ASIGNACION", "MULTIDECLARACION" };
		Reglas reglasMulti4 = new Reglas("MULTIDECLARACION", multideclaracion4);
		reglas.add(reglasMulti4);

		String[] asignacion = { "IDENTIFICADOR_TK", "SIMBOLO_=", "NUMERO_ENTERO_TK" };
		Reglas reglasAsignacion = new Reglas("ASIGNACION", asignacion);
		reglas.add(reglasAsignacion);

		String[] asignacion2 = { "IDENTIFICADOR_TK", "SIMBOLO_=", "NUMERO_REAL_TK" };
		Reglas reglasAsignacion2 = new Reglas("ASIGNACION", asignacion2);
		reglas.add(reglasAsignacion2);

		String[] asignacion3 = { "IDENTIFICADOR_TK", "SIMBOLO_=", "OPGERARQUICA" };
		Reglas reglasAsignacion3 = new Reglas("ASIGNACION", asignacion3);
		reglas.add(reglasAsignacion3);

		String[] leer = { "PALABRA_RESERVADA_READ", "PARENTESIS", "SIMBOLO_;" };
		Reglas reglasLeer = new Reglas("LEER", leer);
		reglas.add(reglasLeer);

		String[] escribir = { "PALABRA_RESERVADA_WRITE", "PARENTESIS", "SIMBOLO_;" };
		Reglas reglasEscribir = new Reglas("ESCRIBIR", escribir);
		reglas.add(reglasEscribir);

		String[] opGerarquica = { "PALABRA_RESERVADA_ADD", "PARENTESIS" };
		Reglas reglasOpGerarquica = new Reglas("OPGERARQUICA", opGerarquica);
		reglas.add(reglasOpGerarquica);

		String[] opGerarquica2 = { "PALABRA_RESERVADA_SUB", "PARENTESIS" };
		Reglas reglasOpGerarquica2 = new Reglas("OPGERARQUICA", opGerarquica2);
		reglas.add(reglasOpGerarquica2);

		String[] opGerarquica3 = { "PALABRA_RESERVADA_MUL", "PARENTESIS" };
		Reglas reglasOpGerarquica3 = new Reglas("OPGERARQUICA", opGerarquica3);
		reglas.add(reglasOpGerarquica3);

		String[] opGerarquica4 = { "PALABRA_RESERVADA_DIV", "PARENTESIS" };
		Reglas reglasOpGerarquica4 = new Reglas("OPGERARQUICA", opGerarquica4);
		reglas.add(reglasOpGerarquica4);

		String[] parentesis = { "SIMBOLO_(", "AGRUPACION", "SIMBOLO_)" };
		Reglas reglasParentesis = new Reglas("PARENTESIS", parentesis);
		reglas.add(reglasParentesis);

		String[] parentesis2 = { "SIMBOLO_(", "IDENTIFICADOR_TK", "SIMBOLO_)" };
		Reglas reglasParentesis2 = new Reglas("PARENTESIS", parentesis2);
		reglas.add(reglasParentesis2);

		String[] agrupacion = { "ELEMENTO", "SIMBOLO_,", "ELEMENTO" };
		Reglas reglasAgrupacion = new Reglas("AGRUPACION", agrupacion);
		reglas.add(reglasAgrupacion);

		String[] elemento = { "IDENTIFICADOR_TK" };
		Reglas reglasElemento = new Reglas("ELEMENTO", elemento);
		reglas.add(reglasElemento);

		String[] elemento2 = { "OPGERARQUICA" };
		Reglas reglasElemento2 = new Reglas("ELEMENTO", elemento2);
		reglas.add(reglasElemento2);

		String[] elemento3 = { "NUMERO_REAL_TK" };
		Reglas reglasElemento3 = new Reglas("ELEMENTO", elemento3);
		reglas.add(reglasElemento3);

		String[] elemento4 = { "NUMERO_ENTERO_TK" };
		Reglas reglasElemento4 = new Reglas("ELEMENTO", elemento4);
		reglas.add(reglasElemento4);

		String[] darValor = { "ASIGNACION", "SIMBOLO_;" };
		Reglas reglasDarValor = new Reglas("DARVALOR", darValor);
		reglas.add(reglasDarValor);

		String[] fin = { "SIMBOLO_}", "PALABRA_RESERVADA_END" };
		Reglas reglasFin = new Reglas("FIN", fin);
		reglas.add(reglasFin);

		String[] cuerpo = { "DECLARACION" };
		Reglas reglasCuerpo = new Reglas("CUERPO", cuerpo);
		reglas.add(reglasCuerpo);

		String[] cuerpo2 = { "LEER" };
		Reglas reglasCuerpo2 = new Reglas("CUERPO", cuerpo2);
		reglas.add(reglasCuerpo2);

		String[] cuerpo3 = { "ESCRIBIR" };
		Reglas reglasCuerpo3 = new Reglas("CUERPO", cuerpo3);
		reglas.add(reglasCuerpo3);

		String[] cuerpo4 = { "DARVALOR" };
		Reglas reglasCuerpo4 = new Reglas("CUERPO", cuerpo4);
		reglas.add(reglasCuerpo4);
		return reglas;

	}

	public void inicio(int p) {
		if (tokens.get(0).getTipo().compareTo("PALABRA_RESERVADA_BEGIN") == 0) {
			pila.push(tokens.get(0));
			contador++;
			if (tokens.get(contador).getTipo().compareTo("SIMBOLO_{") == 0) {
				pila.push(tokens.get(contador));
				contador++;
				if (tokens.get(contador).getTipo().compareTo("SALTO") == 0) {
					contador--;
					String[] elementos = { "SIMBOLO_{", "PALABRA_RESERVADA_BEGIN" };
					aplicarRegla(elementos, "INICIO");
					// pila.push(new Token("INICIO", "INICIO"));
					return;
				} else {
					errores += "\nError de sintaxis en la linea " + linea;
				}

			} else {
				errores += "\nError de sintaxis en la linea " + linea;
				return;
			}
		} else {
			errores += "\nError de sintaxis en la linea " + linea;
			if (tokens.get(contador).getTipo().compareTo("SIMBOLO_{") == 0) {
				contador++;
				return;
			} else {
				return;
			}
		}
	}

	private void cuerpo(int p) {
		for (int i = contador; i < tokens.size(); i++) {
			bandera = false;
			switch (tokens.get(i).getTipo()) {
			case "PALABRA_RESERVADA_INTEGER":
				contador = i;
				declaracion(3);
				if (bandera) {
					volcarAPila();
					System.out.println("Construir el arbol entero");

				} else {
					errores += "\nError de sintaxis en la linea INTEGER " + linea;
					contador = terminarLinea(contador);
					elementos.clear();
				}
				i = contador - 1;
				break;

			case "PALABRA_RESERVADA_REAL":
				contador = i;
				declaracion(3);
				if (bandera) {
					volcarAPila();
					System.out.println("Construir el arbol real");
				} else {
					errores += "\nError de sintaxis en la linea REAL " + linea;
					contador = terminarLinea(contador);
					elementos.clear();
				}
				i = contador - 1;
				break;
			case "PALABRA_RESERVADA_READ":
				contador = i;
				leer(3);
				if (bandera) {
					volcarAPila();
					System.out.println("Construir arbol leer");
				} else {
					errores += "\nError de sintaxis en la linea READ " + linea;
					contador = terminarLinea(contador);
					elementos.clear();
				}
				i = contador - 1;
				break;

			case "PALABRA_RESERVADA_WRITE":
				contador = i;
				escribir(3);
				if (bandera) {
					volcarAPila();
					System.out.println("Construir arbol escribir");
				} else {
					errores += "\nError de sintaxis en la linea WRITE " + linea;
					contador = terminarLinea(contador);
					elementos.clear();
				}
				i = contador - 1;
				break;

			case "IDENTIFICADOR_TK":
				contador = i;
				darValor(3);
				if (bandera) {
					volcarAPila();
					System.out.println("Construir arbol darValor");
				} else {
					System.out.println(elementos);
					errores += "\nError de sintaxis en la linea ID " + linea;
					contador = terminarLinea(contador);
					elementos.clear();
				}
				i = contador - 1;
				break;
			case "SALTO":
				System.out.println("Nueva linea");
				linea++;
				break;

			default:
				break;
			}
			if (i == tokens.size() - 3) {
				volcarAPila();
				String[] elementos = { "DECLARACION", "LEER", "ESCRIBIR", "DARVALOR" };
				reglaCuerpo(elementos, "CUERPO");

				return;
			}
			contador++;
		}
	}

	private void declaracion(int p) {
		int v = 0;
		if (verificar(contador, "PALABRA_RESERVADA_INTEGER")) {
			v = 1;
			elementos.add(tokens.get(contador));
			entero(4);
			contador--;

		} else {
			if (verificar(contador, "PALABRA_RESERVADA_REAL")) {
				v = 2;
				elementos.add(tokens.get(contador));
				real(4);
			}
		}
		if (verificar(contador, "SIMBOLO_;")) {
			bandera = true;
			if (v == 1) {
				volcarAPila();
				String[] elementos = { "SIMBOLO_;", "ENTERO" };
				aplicarRegla(elementos, "DECLARACION");
				// elementos.add(new Token("DECLARACION", "DECLARACION"));
			} else {
				volcarAPila();
				String[] elementos = { "SIMBOLO_;", "REAL" };
				aplicarRegla(elementos, "DECLARACION");
				// elementos.add(new Token("DECLARACION", "DECLARACION"));
			}
			return;
		}
	}

	private void entero(int p) {

		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "IDENTIFICADOR_TK")) {
			contador++;
			elementos.add(tokens.get(contador));
			switch (tokens.get(contador).getTipo()) {
			case "SIMBOLO_;":
				contador++;
				volcarAPila();
				String[] elementos = { "IDENTIFICADOR_TK", "PALABRA_RESERVADA_INTEGER" };
				aplicarRegla(elementos, "ENTERO");
				// elementos.add(new Token("ENTERO", "ENTERO"));
				return;
			case "SIMBOLO_=":
				asignacion(8);
				volcarAPila();
				String[] elementos2 = { "ASIGNACION", "PALABRA_RESERVADA_INTEGER" };
				aplicarRegla(elementos2, "ENTERO");
				// elementos.add(new Token("ENTERO", "ENTERO"));
				return;
			case "SIMBOLO_,":
				// contador--;
				multideclaracion(false);
				volcarAPila();
				String[] elementos3 = { "MULTIDECLARACION", "IDENTIFICADOR_TK", "PALABRA_RESERVADA_INTEGER" };
				aplicarRegla(elementos3, "ENTERO");
				// elementos.add(new Token("ENTERO", "ENTERO"));
				break;
			default:
				break;
			}
		}
	}

	private void real(int p) {
		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "IDENTIFICADOR_TK")) {
			contador++;
			elementos.add(tokens.get(contador));
			switch (tokens.get(contador).getTipo()) {
			case "SIMBOLO_;":
				contador++;
				volcarAPila();
				String[] elementos = { "IDENTIFICADOR_TK", "PALABRA_RESERVADA_REAL" };
				aplicarRegla(elementos, "REAL");
				// elementos.add(new Token("REAL", "REAL"));
				return;
			case "SIMBOLO_=":
				asignacion(9);
				volcarAPila();
				String[] elementos2 = { "ASIGNACION", "PALABRA_RESERVADA_REAL" };
				aplicarRegla(elementos2, "REAL");
				// elementos.add(new Token("REAL", "REAL"));
				return;
			case "SIMBOLO_,":
				// contador--;
				multideclaracion(false);
				volcarAPila();
				String[] elementos3 = { "MULTIDECLARACION", "IDENTIFICADOR_TK", "PALABRA_RESERVADA_REAL" };
				aplicarRegla(elementos3, "REAL");
				// elementos.add(new Token("REAL", "REAL"));
				break;
			default:
				break;
			}
		}
	}

	private void asignacion(int p) {
		contador++;
		elementos.add(tokens.get(contador));
		switch (tokens.get(contador).getTipo()) {
		case "NUMERO_ENTERO_TK":
			contador++;
			elementos.add(tokens.get(contador));
			if (verificar(contador, "PALABRA_RESERVADA_ADD") || verificar(contador, "PALABRA_RESERVADA_SUB")
					|| verificar(contador, "PALABRA_RESERVADA_MUL") || verificar(contador, "PALABRA_RESERVADA_DIV")) {
				// contador--;
				opGerarquica(10);
				volcarAPila();
				String[] elementos1 = { "NUMERO_ENTERO_TK", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos1, "ASIGNACION");
				// elementos.add(new Token("ASIGNACION", "ASIGNACION"));
			} else if (verificar(contador, "SIMBOLO_,")) {
				multideclaracion(false);
				volcarAPila();
				String[] elementos1 = { "NUMERO_ENTERO_TK", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos1, "ASIGNACION");
				// elementos.add(new Token("ASINACION", "ASIGNACION"));
			} else {
				volcarAPila();
				String[] elementos1 = { "NUMERO_ENTERO_TK", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos1, "ASIGNACION");
				// elementos.add(new Token("ASIGNACION", "ASIGNACION"));
			}
			return;
		case "NUMERO_REAL_TK":
			contador++;
			elementos.add(tokens.get(contador));
			if (verificar(contador, "PALABRA_RESERVADA_ADD") || verificar(contador, "PALABRA_RESERVADA_SUB")
					|| verificar(contador, "PALABRA_RESERVADA_MUL") || verificar(contador, "PALABRA_RESERVADA_DIV")) {
				opGerarquica(10);
				volcarAPila();
				String[] elementos1 = { "NUMERO_REAL_TK", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos1, "ASIGNACION");
				// elementos.add(new Token("ASIGNACION", "ASIGNACION"));
			} else if (verificar(contador, "SIMBOLO_,")) {
				multideclaracion(false);
				volcarAPila();
				String[] elementos1 = { "NUMERO_REAL_TK", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos1, "ASIGNACION");
				// elementos.add(new Token("ASINACION", "ASIGNACION"));
			} else {
				volcarAPila();
				String[] elementos1 = { "NUMERO_REAL_TK", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos1, "ASIGNACION");
				// elementos.add(new Token("ASIGNACION", "ASIGNACION"));
			}
			return;
		default:
			if (verificar(contador, "PALABRA_RESERVADA_ADD") || verificar(contador, "PALABRA_RESERVADA_SUB")
					|| verificar(contador, "PALABRA_RESERVADA_MUL") || verificar(contador, "PALABRA_RESERVADA_DIV")) {
				opGerarquica(10);
				volcarAPila();
				String[] elementos3 = { "OPGERARQUICA", "SIMBOLO_=", "IDENTIFICADOR_TK" };
				aplicarRegla(elementos3, "ASIGNACION");
				// elementos.add(new Token("ASIGNACION", "ASIGNACION"));
			}
			break;
		}
	}

	private void leer(int p) {
		elementos.add(tokens.get(contador));
		parentesis(6);
		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "SIMBOLO_;")) {
			bandera = true;
			volcarAPila();
			String[] elementos3 = { "SIMBOLO_;", "PARENTESIS", "PALABRA_RESERVADA_READ" };
			aplicarRegla(elementos3, "LEER");
			// elementos.add(new Token("LEER", "LEER"));
		}
	}

	private void escribir(int p) {
		elementos.add(tokens.get(contador));
		parentesis(5);
		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "SIMBOLO_;")) {
			bandera = true;
			volcarAPila();
			String[] elementos3 = { "SIMBOLO_;", "PARENTESIS", "PALABRA_RESERVADA_WRITE" };
			aplicarRegla(elementos3, "ESCRIBIR");
			// elementos.add(new Token("ESCRIBIR", "ESCRIBIR"));
		}
	}

	private void multideclaracion(boolean b) {
		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "IDENTIFICADOR_TK")) {
			contador++;
			elementos.add(tokens.get(contador));
			switch (tokens.get(contador).getTipo()) {
			case "SIMBOLO_=":
				asignacion(11);
				volcarAPila();
				String[] elementos1 = { "ASIGNACION", "SIMBOLO_," };
				aplicarRegla(elementos1, "MULTIDECLARACION");
				// elementos.add(new Token("MULTIDECLARACION", "MULTIDECLARACION"));
				break;
			case "SIMBOLO_,":
				multideclaracion(false);
				volcarAPila();
				String[] elementos3 = { "MULTIDECLARACION", "IDENTIFICADOR_TK", "SIMBOLO_," };
				aplicarRegla(elementos3, "MULTIDECLARACION");
				// elementos.add(new Token("MULTIDECLARACION", "MULTIDECLARACION"));
				break;
			default:
				contador++;
				elementos.add(tokens.get(contador));
				volcarAPila();
				String[] elementos4 = { "IDENTIFICADOR_TK", "SIMBOLO_," };
				aplicarRegla(elementos4, "MULTIDECLARACION");
				// elementos.add(new Token("MULTIDECLARACION", "MULTIDECLARACION"));
				return;
			}
		}
	}

	private void opGerarquica(int p) {

		switch (tokens.get(contador).getTipo()) {
		case "PALABRA_RESERVADA_ADD":
			volcarAPila();
			String[] elementos = { "PARENTESIS", "PALABRA_RESERVADA_ADD" };
			aplicarRegla(elementos, "OPGERARQUICA");
			// elementos.add(new Token("OPGERARQUICA", "OPGERARQUICA"));
			break;
		case "PALABRA_RESERVADA_SUB":
			volcarAPila();
			String[] elementos2 = { "PARENTESIS", "PALABRA_RESERVADA_SUB" };
			aplicarRegla(elementos2, "OPGERARQUICA");
			// elementos.add(new Token("OPGERARQUICA", "OPGERARQUICA"));
			break;
		case "PALABRA_RESERVADA_MUL":
			volcarAPila();
			String[] elementos3 = { "PARENTESIS", "PALABRA_RESERVADA_MUL" };
			aplicarRegla(elementos3, "OPGERARQUICA");
			// elementos.add(new Token("OPGERARQUICA", "OPGERARQUICA"));
			break;
		case "PALABRA_RESERVADA_DIV":
			volcarAPila();
			String[] elementos4 = { "PARENTESIS", "PALABRA_RESERVADA_DIV" };
			aplicarRegla(elementos4, "OPGERARQUICA");
			// elementos.add(new Token("OPGERARQUICA", "OPGERARQUICA"));
			break;

		default:
			break;
		}
		parentesis(12);

	}

	private void parentesis(int p) {
		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "SIMBOLO_(")) {
			contador++;
			elementos.add(tokens.get(contador));
			switch (tokens.get(contador).getTipo()) {
			case "IDENTIFICADOR_TK":
				contador++;
				elementos.add(tokens.get(contador));
				switch (tokens.get(contador).getTipo()) {
				case "SIMBOLO_)":
					volcarAPila();
					String[] elementos1 = { "SIMBOLO_)", "IDENTIFICADOR_TK", "SIMBOLO_(" };
					aplicarRegla(elementos1, "PARENTESIS");
					// elementos.add(new Token("PARENTESIS", "PARENTESIS"));
					return;
				case "SIMBOLO_,":
					elementos.remove(elementos.size() - 1);
					contador--;
					agrupacion(13, false);
					elementos.add(new Token("SIMBOLO_,", ","));
					contador += 2;
					elementos.add(tokens.get(contador));
					agrupacion(13, true);
					contador++;
					if (verificar(contador, "SIMBOLO_)")) {
						volcarAPila();
						String[] elementos2 = { "SIMBOLO_)", "AGRUPACION", "SIMBOLO_(" };
						aplicarRegla(elementos2, "PARENTESIS");
						// elementos.add(new Token("PARENTESIS", "PARENTESIS"));
						return;
					}
					break;
				default:

					break;
				}
				break;
			case "NUMERO_ENTERO_TK":
				contador++;
				elementos.add(tokens.get(contador));
				if (verificar(contador, "SIMBOLO_,")) {
					contador--;
					agrupacion(13, false);
					contador += 2;
					elementos.add(tokens.get(contador));
					agrupacion(13, true);
					contador++;
				}
				if (verificar(contador, "SIMBOLO_)")) {
					volcarAPila();
					String[] elementos1 = { "SIMBOLO_)", "AGRUPACION", "SIMBOLO_(" };
					aplicarRegla(elementos1, "PARENTESIS");
					// elementos.add(new Token("PARENTESIS", "PARENTESIS"));
					return;
				}
				break;
			case "NUMERO_REAL_TK":
				contador++;
				elementos.add(tokens.get(contador));
				if (verificar(contador, "SIMBOLO_,")) {
					contador--;
					agrupacion(13, false);
					contador += 2;
					agrupacion(13, true);
					elementos.add(tokens.get(contador));
					if (verificar(contador, "SIMBOLO_,")) {
						contador--;
						agrupacion(13, false);
						contador += 2;
						elementos.add(tokens.get(contador));
						agrupacion(13, true);
						contador++;
					}
					if (verificar(contador, "SIMBOLO_)")) {
						volcarAPila();
						String[] elementos1 = { "SIMBOLO_)", "AGRUPACION", "SIMBOLO_(" };
						aplicarRegla(elementos1, "PARENTESIS");
						// elementos.add(new Token("PARENTESIS", "PARENTESIS"));
						return;
					}

				}
				break;
			default:
				if (verificar(contador, "PALABRA_RESERVADA_ADD") || verificar(contador, "PALABRA_RESERVADA_SUB")
						|| verificar(contador, "PALABRA_RESERVADA_MUL")
						|| verificar(contador, "PALABRA_RESERVADA_DIV")) {
					opGerarquica(13);
					contador++;
					elementos.add(tokens.get(contador));
					if (verificar(contador, "SIMBOLO_)")) {
						volcarAPila();
						elementos.add(tokens.get(contador));
						String[] elementos1 = { "SIMBOLO_)", "AGRUPACION", "SIMBOLO_(" };
						aplicarRegla(elementos1, "PARENTESIS");
						// elementos.add(new Token("PARENTESIS", "PARENTESIS"));
						return;
					} else {
						if (verificar(contador, "SIMBOLO_,")) {
							contador--;
							agrupacion(13, true);
							elementos.add(tokens.get(contador));
							contador += 2;
							contador++;
							if (verificar(contador, "SIMBOLO_)")) {
								elementos.add(tokens.get(contador));
								String[] elementos1 = { "SIMBOLO_)", "AGRUPACION", "SIMBOLO_(" };
								aplicarRegla(elementos1, "PARENTESIS");
								// elementos.add(new Token("PARENTESIS", "PARENTESIS"));
								return;
							}
						}
					}
				}
				break;
			}
		}
	}

	private void agrupacion(int p, boolean b) {
		elemento(14);

		if (b) {
			volcarAPila();
			String[] elementos = { "ELEMENTO", "SIMBOLO_,", "ELEMENTO" };
			aplicarRegla(elementos, "AGRUPACION");
			// elementos.add(new Token("AGRUPACION", "AGRUPACION"));
		}

		return;
	}

	private void elemento(int p) {
		switch (tokens.get(contador).getTipo()) {
		case "IDENTIFICADOR_TK":
			volcarAPila();
			String[] elementos = { "IDENTIFICADOR_TK" };
			aplicarRegla(elementos, "ELEMENTO");
			// elementos.add(new Token("ELEMENTO", "ELEMENTO"));
			return;
		case "NUMERO_REAL_TK":
			volcarAPila();
			String[] elementos2 = { "NUMERO_REAL_TK" };
			aplicarRegla(elementos2, "ELEMENTO");
			// elementos.add(new Token("ELEMENTO", "ELEMENTO"));
			return;
		case "NUMERO_ENTERO_TK":
			volcarAPila();
			String[] elementos3 = { "NUMERO_ENTERO_TK" };
			aplicarRegla(elementos3, "ELEMENTO");
			// elementos.add(new Token("ELEMENTO", "ELEMENTO"));
			return;
		default:
			if (verificar(contador, "PALABRA_RESERVADA_ADD") || verificar(contador, "PALABRA_RESERVADA_SUB")
					|| verificar(contador, "PALABRA_RESERVADA_MUL") || verificar(contador, "PALABRA_RESERVADA_DIV")) {
				opGerarquica(15);
				volcarAPila();
				String[] elementos4 = { "OPGERARQUICA" };
				aplicarRegla(elementos4, "ELEMENTO");
				// elementos.add(new Token("ELEMENTO", "ELEMENTO"));
			}
			break;
		}
	}

	private void darValor(int p) {
		elementos.add(tokens.get(contador));
		contador++;
		elementos.add(tokens.get(contador));
		if (verificar(contador, "SIMBOLO_=")) {
			asignacion(7);
		}
		if (!verificar(contador, "SIMBOLO_;")) {
			contador++;
		}
		elementos.add(tokens.get(contador));
		if (verificar(contador, "SIMBOLO_;")) {
			bandera = true;
			volcarAPila();
			String[] elementos = { "SIMBOLO_;", "ASIGNACION" };
			aplicarRegla(elementos, "DARVALOR");
			// elementos.add(new Token("DARVALOR", "DARVALOR"));
		}
	}

	private void fin(int p) {
		if (verificar(tokens.size() - 3, "SIMBOLO_}")) {
			elementos.add(tokens.get(tokens.size() - 3));
			if (verificar(tokens.size() - 2, "PALABRA_RESERVADA_END")) {
				elementos.add(tokens.get(tokens.size() - 2));
				volcarAPila();
				String[] elementos = { "PALABRA_RESERVADA_END", "SIMBOLO_}" };
				aplicarRegla(elementos, "FIN");
				// elementos.add(new Token("FIN", "FIN"));
				return;
			} else {
				errores += "\nError de sintaxis en la linea " + linea;
			}
		} else {
			if (verificar(tokens.size() - 2, "PALABRA_RESERVADA_END")) {
				errores += "\nError de sintaxis en la linea " + linea;
			} else {
				errores += "\nError de sintaxis. Faltan simbolos de cierre";
			}
		}
	}

	public void codigo() {
		inicio(1);
		cuerpo(1);
		fin(1);
		// elementos.add(new Token("CODIGO", "CODIGO"));
		volcarAPila();
		String[] elementos = { "FIN", "CUERPO", "INICIO" };
		aplicarRegla(elementos, "CODIGO");

		for (int i = 0; i < arbol.size(); i++) {
			System.out.println(arbol.get(i));
		}

		for (int i = 0; i < pila.size(); i++) {
			System.out.println(pila.get(i));
		}

		// indexar();
		generar();
	}

	private void volcarAPila() {
		for (int i = 0; i < elementos.size(); i++) {
			Token token = new Token(elementos.get(i).getTipo(), elementos.get(i).getValor());
			pila.push(token);
		}
		elementos.clear();
	}

	private void verificarPila() {
		for (int i = 0; i < pila.size(); i++) {
			if (pila.get(i).getTipo().compareTo("SALTO") == 0) {
				pila.get(i).setTipo("SIMBOLO_;");
				pila.get(i).setValor(";");
			}
		}
	}

	private void aplicarRegla(String[] elementos, String padre) {
		verificarPila();
		List<Token> tokens = new ArrayList<Token>();
		Stack<Token> aux = new Stack<Token>();
		for (int i = 0; i < elementos.length; i++) {
			for (int j = pila.size() - 1; j >= 0; j--) {
				Token e = pila.pop();
				if (e.getTipo().compareTo(elementos[i]) == 0) {
					tokens.add(e);
					break;
				} else {
					aux.push(e);
				}
			}
			for (int K = aux.size() - 1; K >= 0; K--) {
				pila.push(aux.pop());
			}
		}
		Token padreT = new Token(padre, padre);
		pila.push(padreT);
		crearNodo(tokens);
	}

	private void reglaCuerpo(String[] elementos, String padre) {
		List<Token> tokens = new ArrayList<Token>();
		Stack<Token> aux = new Stack<Token>();
		for (int i = 0; i < elementos.length; i++) {
			for (int j = pila.size() - 1; j >= 0; j--) {
				Token e = pila.pop();
				if (e.getTipo().compareTo(elementos[i]) == 0) {
					tokens.add(e);
				} else {
					aux.push(e);
				}
			}
			for (int K = aux.size() - 1; K >= 0; K--) {
				pila.push(aux.pop());
			}
		}
		Token padreT = new Token(padre, padre);
		pila.push(padreT);
		crearNodo(tokens);
	}

	private void crearNodo(List<Token> tokens) {
		List<Reglas> pReglas = new ArrayList<Reglas>();
		for (int i = 0; i < tokens.size(); i++) {
			Nodo nodo = new Nodo();
			nodo.setElemento(arbol.size() + 1);
			nodo.setToken(tokens.get(i));
			arbol.add(nodo);
			for (int j = 0; j < reglas.size(); j++) {
				if (tokens.get(i).getTipo().compareTo(reglas.get(j).getPadre()) == 0) {
					switch (tokens.get(i).getTipo()) {
					case "INICIO":
						padreInicio(arbol);
						break;

					case "CUERPO":
						padreCuerpo(arbol);
						break;

					case "DARVALOR":
						padreDarValor(arbol);
						System.out.println("padre de dar valor");
						break;

					default:
						padres(arbol);
						break;
					}
				}
			}
		}
		// setPadre(pReglas);
	}

	private void padres(List<Nodo> arbol) {
		int padre = arbol.get(arbol.size() - 1).getElemento();
		for (int i = arbol.size() - 2; i >= 0; i--) {
			if (arbol.get(i).getPadre() == 0) {
				if (arbol.get(i).getToken().getTipo().compareTo("PALABRA_RESERVADA_READ") == 0
						&& arbol.get(i - 1).getToken().getTipo().compareTo("PARENTESIS") == 0
						&& arbol.get(i - 2).getToken().getTipo().compareTo("SIMBOLO_,") == 0) {
					arbol.get(i).setPadre(0);
					arbol.get(i - 1).setPadre(0);
					arbol.get(i - 2).setPadre(0);
				} else {
					arbol.get(i).setPadre(padre);
				}
			}
		}
	}

	private void padreInicio(List<Nodo> arbol) {
		int padre = arbol.get(arbol.size() - 1).getElemento();
		for (int i = arbol.size() - 1; i >= 0; i--) {
			if (arbol.get(i).getToken().getTipo().compareTo("PALABRA_RESERVADA_BEGIN") == 0
					|| arbol.get(i).getToken().getTipo().compareTo("SIMBOLO_{") == 0) {
				arbol.get(i).setPadre(padre);
			}
		}
	}

	private void padreCuerpo(List<Nodo> arbol) {
		int padre = arbol.get(arbol.size() - 1).getElemento();
		for (int i = arbol.size() - 1; i >= 0; i--) {
			if (arbol.get(i).getToken().getTipo().compareTo("DARVALOR") == 0
					|| arbol.get(i).getToken().getTipo().compareTo("ESCRIBIR") == 0
					|| arbol.get(i).getToken().getTipo().compareTo("LEER") == 0
					|| arbol.get(i).getToken().getTipo().compareTo("DECLARACION") == 0) {
				arbol.get(i).setPadre(padre);
			}
		}
	}

	private void padreDarValor(List<Nodo> arbol) {
		int padre = arbol.get(arbol.size() - 1).getElemento();
		for (int i = arbol.size() - 1; i >= 0; i--) {
			if (arbol.get(i).getToken().getTipo().compareTo("ASIGNACION") == 0) {
				if (arbol.get(i - 1).getToken().getTipo().compareTo("SIMBOLO_;") == 0) {
					arbol.get(i).setPadre(padre);
					arbol.get(i - 1).setPadre(padre);
					return;

				}

			}
		}
	}

	private void setPadre(List<Reglas> reglas) {
		int posicion = 0;
		List<Integer> hijos = new ArrayList<Integer>();
		boolean b = false, isFin = false;
		String[] e = getRegla(0, reglas);
		if (e != null) {
			String elemento = getElemento(posicion, e);
			for (int i = 0; i < reglas.size(); i++) {
				e = getRegla(0, reglas);
				int padre = buscar(reglas.get(i).getPadre());
				for (int j = padre; j >= 0; j--) {
					System.out.println("antes " + elemento + " " + arbol.get(j).getToken() + " regla " + elemento);
					if (arbol.get(j).getToken().getTipo().compareTo(elemento) == 0) {
						System.out.println(elemento + " " + arbol.get(j).getToken());
						arbol.get(j).setPadre(padre + 1);
						b = true;
						hijos.add(j);
						posicion++;
						if (posicion < e.length) {
							elemento = getElemento(posicion, e);
						} else {
							isFin = true;
							return;
						}
					} else {
						if (b) {
							quitarPadre(hijos);
							break;
						}

					}
				}
				if (isFin) {
					break;
				}
			}
		}
	}

	private void quitarPadre(List<Integer> hijos) {
		for (int i = 0; i < hijos.size(); i++) {
			arbol.get(hijos.get(i)).setPadre(0);
		}
	}

	private String getElemento(int p, String[] elementos) {
		return elementos[p];
	}

	private String[] getRegla(int posicion, List<Reglas> reglas) {
		if (posicion < reglas.size()) {
			return reglas.get(posicion).getElementos();
		}
		return null;
	}

	private int buscar(String padre) {
		for (int i = arbol.size() - 1; i >= 0; i--) {
			if (arbol.get(i).getToken().getTipo().compareTo(padre) == 0 && arbol.get(i).getPadre() == 0) {
				return i;
			}
		}
		return 0;
	}

	private int terminarLinea(int posicion) {
		for (int i = posicion; i < tokens.size(); i++) {
			if (tokens.get(i).getTipo().compareTo("SALTO") == 0) {
				return i;
			}
		}
		return posicion;
	}

	public boolean verificar(int c, String id) {
		if (tokens.get(c).getTipo().compareTo(id) == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void generar() {
		Document document = new Document();
		File aux = new File("C:\\temp\\Analizador_Lexico\\arboles");
		aux.mkdirs();
		String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre para el arbol", "Arbol",
				JOptionPane.QUESTION_MESSAGE);
		File fileID = new File("C:\\temp\\Analizador_Lexico\\arboles\\" + nombre + ".pdf");
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileID));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.open();
		try {
			document.add(new Paragraph("Arbol de Tokens", FontFactory.getFont("arial")));
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PdfPTable tablaPDF = new PdfPTable(3);
		tablaPDF.setWidthPercentage(100f);
		tablaPDF.setHorizontalAlignment(Element.ALIGN_LEFT);
		tablaPDF.addCell("Elemento");
		tablaPDF.addCell("Token");
		tablaPDF.addCell("Padre");
		List<Nodo> identificadores = arbol;
		for (Nodo nodo : identificadores) {
			tablaPDF.addCell(nodo.getElemento() + "");
			tablaPDF.addCell(nodo.getToken().getTipo());
			tablaPDF.addCell(nodo.getPadre() + "");
		}

		try {
			document.add(tablaPDF);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.close();
		if (JOptionPane.showConfirmDialog(null, "???Desea visualizar los resultados?", "Pregunta",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			try {
				Desktop.getDesktop().open(fileID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getErrores() {
		return errores;
	}

	public void setErrores(String errores) {
		this.errores = errores;
	}

	public List<Nodo> getArbol() {
		return arbol;
	}

}
