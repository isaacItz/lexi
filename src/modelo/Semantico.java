package modelo;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Semantico {
	List<Identificador> identificadores;
	List<Nodo> arbol;
	List<Integer> idDeclaracion = new ArrayList<Integer>();
	String errores = "";

	public Semantico(List<Identificador> identificadores, List<Nodo> arbol) {
		this.identificadores = identificadores;
		this.arbol = arbol;
	}

	public void analizar() {
		for (int i = 0; i < arbol.size(); i++) {
			if (arbol.get(i).getToken().getTipo().compareTo("PALABRA_RESERVADA_INTEGER") == 0) {
				declaracion("ENTERO", i - 1);
			} else if (arbol.get(i).getToken().getTipo().compareTo("PALABRA_RESERVADA_REAL") == 0) {
				declaracion("REAL", i - 1);
			}
		}

		for (int i = 0; i < arbol.size(); i++) {
			if (arbol.get(i).getToken().getTipo().compareTo("IDENTIFICADOR_TK") == 0) {
				if (!isIdDeclaracion(i)) {
					if (existeIdentificador(arbol.get(i).getToken().getValor())) {
						if (arbol.get(i - 1).getToken().getTipo().compareTo("SIMBOLO_=") == 0) {
							if (arbol.get(i - 2).getToken().getTipo().compareTo("NUMERO_ENTERO_TK") == 0) {
								if (validarAsignacion("ENTERO", arbol.get(i - 2).getToken().getValor(),
										arbol.get(i).getToken().getValor())) {
									decoracionSimple(i, "ENTERO");
								} else {
									errores += "Incompatibilidad de tipo: No puede asignar un valor ENTERO al identificador REAL "
											+ arbol.get(i).getToken().getValor() + "\n";
								}
							} else {
								if (arbol.get(i - 2).getToken().getTipo().compareTo("NUMERO_REAL_TK") == 0) {
									if (validarAsignacion("REAL", arbol.get(i - 2).getToken().getValor(),
											arbol.get(i).getToken().getValor())) {
										decoracionSimple(i, "REAL");
									} else {
										errores += "Incompatibilidad de tipo: No puede asignar un valor REAL al identificador ENTERO "
												+ arbol.get(i).getToken().getValor() + "\n";
									}
								} else {
									if (arbol.get(i - 2).getToken().getTipo().compareTo("OPGERARQUICA") == 0) {
										validarOperacion(i, sacarTipo(arbol.get(i).getToken().getValor()));
									}
								}
							}
						}
					} else {
						errores += "Identificador " + arbol.get(i).getToken().getValor() + " no ha sido declarado \n";
						System.out.println(errores);
					}
				}

			}
		}

		generar();
		generarArbol();

		System.out.println(errores);
	}

	private boolean isIdDeclaracion(int i) {
		for (int j = 0; j < idDeclaracion.size(); j++) {
			System.out.println(idDeclaracion.get(j));
			if (idDeclaracion.get(j) == i) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void validarOperacion(int posicion, String tipo) {
		for (int i = posicion; i >= 0; i--) {
			if (arbol.get(i).getToken().getTipo().compareTo("SIMBOLO_;") == 0) {
				return;
			} else {
				if (tipo == "ENTERO") {
					if (arbol.get(i).getToken().getTipo().compareTo("IDENTIFICADOR_TK") == 0) {
						String validar = sacarTipo(arbol.get(i).getToken().getValor());
						if (validar == tipo || arbol.get(i).getToken().getTipo().compareTo("NUMERO_ENTERO_TK") == 0) {

						} else {
							errores += "El tipo de identificador " + arbol.get(i).getToken().getValor()
									+ " no coincide en la operacion\n";
							System.out.println(errores);
							return;
						}
					} else {
						if (arbol.get(i).getToken().getTipo().compareTo("NUMERO_REAL_TK") == 0) {
							errores += "Tipo de numero erroneo: " + arbol.get(i).getToken().getValor() + "\n";
							System.out.println(errores);
							return;
						}
					}
				} else {
					if (tipo == "REAL") {
						if (arbol.get(i).getToken().getTipo().compareTo("IDENTIFICADOR_TK") == 0) {
							String validar = sacarTipo(arbol.get(i).getToken().getValor());
							if (validar == tipo || arbol.get(i).getToken().getTipo().compareTo("NUMERO_REAL_TK") == 0) {

							} else {
								errores += "El tipo de identificador " + arbol.get(i).getToken().getValor()
										+ " no coincide en la operacion\n";
								System.out.println(errores);
								return;
							}
						} else {
							if (arbol.get(i).getToken().getTipo().compareTo("NUMERO_ENTERO_TK") == 0) {
								errores += "Tipo de numero erroneo: " + arbol.get(i).getToken().getValor() + "\n";
								System.out.println(errores);
								return;
							}
						}
					}
				}
			}
		}
	}

	private String sacarTipo(String id) {
		for (int i = 0; i < identificadores.size(); i++) {
			if (identificadores.get(i).getId().compareTo(id) == 0) {
				return identificadores.get(i).getTipo();
			}
		}
		return "";
	}

	private boolean validarAsignacion(String tipo, String valor, String id) {
		for (int i = 0; i < identificadores.size(); i++) {
			if (identificadores.get(i).getId().compareTo(id) == 0) {
				if (identificadores.get(i).getTipo().compareTo(tipo) == 0) {
					identificadores.get(i).setValor(valor + "");
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	private void decoracionSimple(int posicion, String tipo) {
		arbol.get(posicion).setTipo(tipo);
		arbol.get(posicion - 2).setTipo(tipo);
		arbol.get(arbol.get(posicion).getPadre() - 1).setTipo(tipo);

	}

	private boolean existeIdentificador(String id) {
		for (int i = 0; i < identificadores.size(); i++) {
			if (identificadores.get(i).getId().compareTo(id) == 0 && identificadores.get(i).getTipo() != null) {
				return true;
			}
		}
		return false;
	}

	private void declaracion(String tipo, int lugar) {
		for (int i = lugar; i >= 0; i--) {
			if (arbol.get(i).getToken().getTipo().compareTo("PALABRA_RESERVADA_INTEGER") != 0
					&& arbol.get(i).getToken().getTipo().compareTo("PALABRA_RESERVADA_REAL") != 0) {
				if (arbol.get(i).getToken().getTipo().compareTo("IDENTIFICADOR_TK") == 0) {
					idDeclaracion.add(i);
					if (arbol.get(i - 1).getToken().getTipo().compareTo("SIMBOLO_=") == 0) {
						if (tipo.compareTo("ENTERO") == 0) {
							if (arbol.get(i - 2).getToken().getTipo().compareTo("NUMERO_ENTERO_TK") == 0) {
								int valorE = Integer.parseInt(arbol.get(i - 2).getToken().getValor());
								setDatosEntero(tipo, valorE, arbol.get(i).getToken().getValor());
								decoracionSimple(i, "ENTERO");
							} else {
								errores += "Incompatibilidad de tipo: No puede asignar un valor REAL al identificador ENTERO "
										+ arbol.get(i).getToken().getValor() + "\n";
								setDatosEntero(tipo, 0, arbol.get(i).getToken().getValor());
							}

						} else {
							if (arbol.get(i - 2).getToken().getTipo().compareTo("NUMERO_REAL_TK") == 0) {
								float valorF = Float.parseFloat(arbol.get(i - 2).getToken().getValor());
								setDatosFloat(tipo, valorF, arbol.get(i).getToken().getValor());
								decoracionSimple(i, "REAL");
							} else {
								errores += "Incompatibilidad de tipo: No puede asignar un valor ENTERO al identificador REAL "
										+ arbol.get(i).getToken().getValor() + "\n";
								setDatosFloat(tipo, 0, arbol.get(i).getToken().getValor());
							}

						}
					} else {
						int valorC = 0;
						if (tipo.compareTo("ENTERO") == 0) {
							setDatosEntero(tipo, valorC, arbol.get(i).getToken().getValor());
						} else {
							setDatosFloat(tipo, valorC, arbol.get(i).getToken().getValor());
						}
					}
				}
			} else {
				i = -1;
			}
		}
	}

	private void setDatosEntero(String tipo, int valor, String id) {
		for (int i = 0; i < identificadores.size(); i++) {
			Identificador identifIcador = identificadores.get(i);
			if (identifIcador.getId().compareTo(id) == 0) {
				if (identifIcador.getTipo() == null && identifIcador.getValor() == null) {
					identificadores.get(i).setTipo(tipo);
					identificadores.get(i).setValor(valor + "");
				} else {
					errores += "Doble declaraci�n. El identificador: " + id + " ya ha sido declarado\n";
				}
			}
		}

	}

	private void setDatosFloat(String tipo, float valor, String id) {
		for (int i = 0; i < identificadores.size(); i++) {
			Identificador identifIcador = identificadores.get(i);
			if (identifIcador.getId().compareTo(id) == 0) {
				if (identifIcador.getTipo() == null && identifIcador.getValor() == null) {
					identificadores.get(i).setTipo(tipo);
					identificadores.get(i).setValor(valor + "");
				} else {
					errores += "Doble declaracion. El identificador: " + id + " ya ha sido declarado\n";
				}
			}
		}
	}

	private void generar() {
		Document document = new Document();
		File aux = new File("C:\\temp\\Analizador_Lexico\\tablasId");
		aux.mkdirs();
		String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre para la tabla de id modificiada", "Tabla",
				JOptionPane.QUESTION_MESSAGE);
		File fileID = new File("C:\\temp\\Analizador_Lexico\\tablasId\\" + nombre + ".pdf");
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
			document.add(new Paragraph("Tabla de ID", FontFactory.getFont("arial")));
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PdfPTable tablaPDF = new PdfPTable(3);
		tablaPDF.setWidthPercentage(100f);
		tablaPDF.setHorizontalAlignment(Element.ALIGN_LEFT);
		tablaPDF.addCell("ID");
		tablaPDF.addCell("TIPO");
		tablaPDF.addCell("VALOR");
		for (Identificador identificador : identificadores) {
			System.out.println(identificador.toString());
			tablaPDF.addCell(identificador.getId() + "");
			tablaPDF.addCell(identificador.getTipo());
			tablaPDF.addCell(identificador.getValor());
		}

		try {
			document.add(tablaPDF);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.close();
		if (JOptionPane.showConfirmDialog(null, "�Desea visualizar los resultados?", "Pregunta",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			try {
				Desktop.getDesktop().open(fileID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void generarArbol() {
		Document document = new Document();
		File aux = new File("C:\\temp\\Analizador_Lexico\\");
		aux.mkdirs();
		String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre para el arbol adornado", "Tabla",
				JOptionPane.QUESTION_MESSAGE);
		File fileID = new File("C:\\temp\\Analizador_Lexico\\tablasId\\" + nombre + "_decorado.pdf");
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
			document.add(new Paragraph("Arbol Decorado", FontFactory.getFont("arial")));
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PdfPTable tablaPDF = new PdfPTable(5);
		tablaPDF.setWidthPercentage(100f);
		tablaPDF.setHorizontalAlignment(Element.ALIGN_LEFT);
		tablaPDF.addCell("ELEMENTO");
		tablaPDF.addCell("TOKEN");
		tablaPDF.addCell("PADRE");
		tablaPDF.addCell("VALOR");
		tablaPDF.addCell("TIPO");
		for (Nodo nodo : arbol) {
			tablaPDF.addCell(nodo.getElemento() + "");
			tablaPDF.addCell(nodo.getToken().getTipo());
			tablaPDF.addCell(nodo.getPadre() + "");
			if (nodo.getToken().getTipo().equals("IDENTIFICADOR_TK")
					|| nodo.getToken().getTipo().equals("NUMERO_ENTERO_TK")
					|| nodo.getToken().getTipo().equals("NUMERO_REAL_TK")) {
				tablaPDF.addCell(nodo.getToken().getValor());
			} else {
				tablaPDF.addCell(" ");
			}
			tablaPDF.addCell(nodo.getTipo());
		}

		try {
			document.add(tablaPDF);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.close();
		if (JOptionPane.showConfirmDialog(null, "�Desea visualizar los resultados?", "Pregunta",
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

}
