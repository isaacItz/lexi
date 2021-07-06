package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.Templates;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jtattoo.plaf.BaseTreeUI;

import modelo.Archivo;
import modelo.Identificador;
import modelo.Lex;
import modelo.ReglasProduccion;
import modelo.Semantico;
import modelo.Token;

public class VentanaAnalizador extends JFrame {

	private JPanel contentPane;
	private String path = "";
	private JTextArea areaCodigo;
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane;
	private JButton btnNuevo;
	private JTextArea areaConsola;
	private List<Archivo> archivos = new ArrayList<Archivo>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
					VentanaAnalizador frame = new VentanaAnalizador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaAnalizador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 654, 650);
		setTitle("Compilador");
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		java.awt.Image icon = new ImageIcon(getClass().getResource("/recursos/compilador.png")).getImage();
		setIconImage(icon);
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 618, 27);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnAbrirProyecto = new JButton("Abrir Proyecto");
		btnAbrirProyecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File aux = new File("C:\\temp\\Analizador_Lexico\\codigos");
				aux.mkdirs();
				JFileChooser JFC = new JFileChooser("C:\\temp\\Analizador_Lexico\\codigos");
				JFC.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
				int abrir = JFC.showDialog(null, "Abrir");
				if (abrir == JFileChooser.APPROVE_OPTION) {
					FileReader fr = null;
					BufferedReader br = null;
					try {
						File archivo = JFC.getSelectedFile();
						String PATH = JFC.getSelectedFile().getAbsolutePath();
						if (isAbierto(archivo.getName()) == -1) {
							if (PATH.endsWith(".txt") || PATH.endsWith(".TXT")) {
								fr = new FileReader(archivo);
								br = new BufferedReader(fr);
								String linea;
								if (path.compareTo(archivo.getAbsolutePath()) == 0) {
									JOptionPane.showMessageDialog(rootPane, "¡Archivo abierto!", "ERROR",
											JOptionPane.ERROR_MESSAGE, null);
								} else {
									path = archivo.getAbsolutePath();
									Archivo archivo2 = new Archivo(archivo.getName(), PATH);
									archivos.add(archivo2);
									JScrollPane scrollPane1 = new JScrollPane();
									JTextArea textArea = new JTextArea();
									textArea = new JTextArea();
									textArea.setBackground(Color.BLACK);
									textArea.setForeground(Color.WHITE);
									scrollPane1 = new JScrollPane(textArea);
									tabbedPane.addTab(archivo.getName(), scrollPane1);
									int index = tabbedPane.indexOfTab(archivo.getName());
									JPanel pnlTab = new JPanel(new GridBagLayout());
									pnlTab.setOpaque(false);
									JLabel lblTitulo = new JLabel(archivo.getName());
									JButton btnClose = new JButton("X");
									btnClose.setForeground(Color.RED);
									btnClose.setContentAreaFilled(false);
									btnClose.setBorderPainted(false);
									GridBagConstraints gbc = new GridBagConstraints();
									gbc.gridx = 0;
									gbc.gridy = 0;
									gbc.weightx = 1;
									pnlTab.add(lblTitulo, gbc);
									gbc.gridx++;
									gbc.weightx = 0;
									pnlTab.add(btnClose, gbc);
									tabbedPane.setTabComponentAt(index, pnlTab);
									btnClose.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent arg0) {
											int selected = tabbedPane.indexOfTab(archivo.getName());
											if (selected > -1) {
												tabbedPane.remove(selected);
												cerrar(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
												path = "";
											}

										}
									});
									tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
									Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
									((JTextArea) ((JScrollPane) c).getViewport().getComponent(0)).setText(null);
									while ((linea = br.readLine()) != null) {
										((JTextArea) ((JScrollPane) c).getViewport().getComponent(0))
												.append(linea + "\n");
									}
								}
							} else {
								JOptionPane.showMessageDialog(rootPane, "Archivo no soportado", "ERROR",
										JOptionPane.ERROR_MESSAGE, null);
								btnAbrirProyecto.doClick();
							}
						} else {
							tabbedPane.setSelectedIndex(isAbierto(archivo.getName()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnAbrirProyecto.setBounds(0, 0, 118, 27);
		btnAbrirProyecto.setContentAreaFilled(false);
		btnAbrirProyecto.setBorderPainted(false);
		btnAbrirProyecto.setForeground(Color.WHITE);
		panel.add(btnAbrirProyecto);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File aux = new File("C:\\temp\\Analizador_Lexico\\codigos");
				aux.mkdirs();
				if (tabbedPane.indexOfTab("Bienvenid@") != tabbedPane.getSelectedIndex()) {
					if (!estaAbierto(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()))) {
						JFileChooser fileChooser = new JFileChooser("C:\\temp\\Analizador_Lexico\\codigos");
						fileChooser
								.addChoosableFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt", "TXT"));
						int seleccion = fileChooser.showSaveDialog(null);
						try {

							if (seleccion == JFileChooser.APPROVE_OPTION) {
								File JFC = fileChooser.getSelectedFile();
								if (!JFC.exists()) {
									String PATH = JFC.getAbsolutePath();
									PrintWriter printWriter = new PrintWriter(JFC);
									Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
									printWriter.write(
											((JTextArea) ((JScrollPane) c).getViewport().getComponent(0)).getText());
									printWriter.close();
									if (!PATH.endsWith("txt")) {
										File temp = new File(PATH + ".txt");
										JFC.renameTo(temp);
										((JLabel) (((JPanel) tabbedPane
												.getTabComponentAt(tabbedPane.getSelectedIndex())).getComponent(0)))
														.setText(temp.getName());
										Archivo archivo = new Archivo(temp.getName(), PATH);
										archivos.add(archivo);
									} else {
										JOptionPane.showMessageDialog(null, "Guardado con exito!", "EXITO",
												JOptionPane.INFORMATION_MESSAGE, null);
										((JLabel) (((JPanel) tabbedPane
												.getTabComponentAt(tabbedPane.getSelectedIndex())).getComponent(0)))
														.setText(JFC.getName());
										Archivo archivo = new Archivo(JFC.getName(), PATH);
										archivos.add(archivo);
									}
								} else {
									JOptionPane.showMessageDialog(rootPane, "El archivo ya existe", "ERROR",
											JOptionPane.ERROR_MESSAGE, null);
									btnGuardar.doClick();
								}

							}
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "¡Error al guardar el archivo!", "ERROR",
									JOptionPane.ERROR_MESSAGE, null);
						}

					} else {
						String URL = getPath(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
						File file = new File(URL);
						try {
							PrintWriter printWriter = new PrintWriter(file);
							Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
							printWriter.write(((JTextArea) ((JScrollPane) c).getViewport().getComponent(0)).getText());
							printWriter.close();
							JOptionPane.showMessageDialog(null, "Guardado con exito!", "EXITO",
									JOptionPane.INFORMATION_MESSAGE, null);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} else {
					JOptionPane.showMessageDialog(rootPane, "El saludo no se puede guardar", "ADVERTENCIA",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}
		});
		btnGuardar.setBounds(129, 0, 118, 27);
		btnGuardar.setContentAreaFilled(false);
		btnGuardar.setBorderPainted(false);
		btnGuardar.setForeground(Color.WHITE);
		panel.add(btnGuardar);

		btnNuevo = new JButton("Nuevo Proyecto");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isAbierto("Nuevo Proyecto") == -1) {
					JScrollPane scrollPane1 = new JScrollPane();
					JTextArea textArea = new JTextArea();
					textArea = new JTextArea();
					textArea.setBackground(Color.BLACK);
					textArea.setForeground(Color.WHITE);
					scrollPane1 = new JScrollPane(textArea);
					tabbedPane.addTab("Nuevo Proyecto", scrollPane1);
					int index = tabbedPane.indexOfTab("Nuevo Proyecto");
					JPanel pnlTab = new JPanel(new GridBagLayout());
					pnlTab.setOpaque(false);
					JLabel lblTitulo = new JLabel("Nuevo Proyecto");
					JButton btnClose = new JButton("X");
					btnClose.setForeground(Color.RED);
					btnClose.setContentAreaFilled(false);
					btnClose.setBorderPainted(false);
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridx = 0;
					gbc.gridy = 0;
					gbc.weightx = 1;
					pnlTab.add(lblTitulo, gbc);
					gbc.gridx++;
					gbc.weightx = 0;
					pnlTab.add(btnClose, gbc);
					tabbedPane.setTabComponentAt(index, pnlTab);
					tabbedPane.setSelectedIndex(index);

					btnClose.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							int selected = tabbedPane.indexOfTab("Nuevo Proyecto");
							if (selected > -1) {
								tabbedPane.remove(selected);
							}

						}
					});

				} else {
					tabbedPane.setSelectedIndex(isAbierto("Nuevo Proyecto"));
				}
			}
		});
		btnNuevo.setContentAreaFilled(false);
		btnNuevo.setBorderPainted(false);
		btnNuevo.setForeground(Color.WHITE);
		btnNuevo.setBounds(257, 0, 110, 27);
		panel.add(btnNuevo);

		JButton btnAnalizar = new JButton("Analizar");
		btnAnalizar.setForeground(Color.WHITE);
		btnAnalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = new File("C:\\temp\\Analizador_Lexico\\tokens");
				file.mkdirs();
				File aux = new File("C:\\temp\\Analizador_Lexico\\identificadores");
				aux.mkdirs();
				Lex lex = new Lex();
				if (tabbedPane.indexOfTab("Bienvenid@") != tabbedPane.getSelectedIndex()) {
					if (JOptionPane.showConfirmDialog(rootPane, "¡Desea guardar los cambios?", "Pregunta",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == JOptionPane.YES_OPTION) {
						btnGuardar.doClick();
					}
					JFileChooser fileChooser = new JFileChooser("C:\\temp\\Analizador_Lexico\\tokens");
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt", "TXT"));
					int seleccion = fileChooser.showDialog(null, "Guardar tokens");
					try {

						if (seleccion == JFileChooser.APPROVE_OPTION) {
							File JFC = fileChooser.getSelectedFile();
							if (!JFC.exists()) {
								String PATH = JFC.getAbsolutePath();
								PrintWriter printWriter = new PrintWriter(JFC);

								Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
								List<Token> tokens = lex.getTokens(
										((JTextArea) ((JScrollPane) c).getViewport().getComponent(0)).getText());

								areaConsola.setText("");
								areaConsola.append(lex.getErrores());
								for (Token token : tokens) {
									printWriter.write("(" + token.getValor() + ", " + token.getTipo() + ")\n");
								}

								printWriter.close();

								Archivo archivo = new Archivo(JFC.getName(), PATH);
								archivos.add(archivo);
								if (!PATH.endsWith("txt")) {
									File temp = new File(PATH + ".txt");
									JFC.renameTo(temp);
									if (JOptionPane.showConfirmDialog(null, "¡Desea visualizar los resultados?",
											"Pregunta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
										try {
											Desktop.getDesktop().open(temp);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								} else {
									JOptionPane.showMessageDialog(null, "Guardado con exito!", "EXITO",
											JOptionPane.INFORMATION_MESSAGE, null);
									if (JOptionPane.showConfirmDialog(null, "¡Desea visualizar los resultados?",
											"Pregunta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
										try {
											Desktop.getDesktop().open(JFC);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								}
								String nombre = JOptionPane.showInputDialog(rootPane,
										"Ingrese el nombre para el archivo de los identificadores",
										"Nombre identificadores", JOptionPane.QUESTION_MESSAGE);
								String rutaTxt = "C:\\temp\\Analizador_Lexico\\identificadores\\" + nombre + ".txt";

								try {
									File archivoTxt = new File(rutaTxt);
									if (!archivoTxt.exists()) {
										archivoTxt.createNewFile();
									}

									FileWriter w = new FileWriter(archivoTxt);
									BufferedWriter bf = new BufferedWriter(w);
									List<String> identificadores = lex.getIdentificadores();
									bf.write("IDENTFICADOR\n");
									for (String string : identificadores) {
										bf.write(string + "\n");
									}
									bf.close();
								} catch (Exception e) {
									e.printStackTrace();
								}

								/*
								 * Document document = new Document(); String nombre =
								 * JOptionPane.showInputDialog(rootPane,
								 * "Ingrese el nombre para el archivo de los identificadores",
								 * "Nombre identificadores", JOptionPane.QUESTION_MESSAGE); File fileID = new
								 * File("C:\\temp\\Analizador_Lexico\\identificadores\\"+nombre+".pdf");
								 * PdfWriter.getInstance(document, new FileOutputStream(fileID));
								 * document.open(); document.add(new Paragraph("Tabla de Simbolos",
								 * FontFactory.getFont("arial"))); PdfPTable tablaPDF = new PdfPTable(2);
								 * tablaPDF.setWidthPercentage(100f);
								 * tablaPDF.setHorizontalAlignment(Element.ALIGN_CENTER);
								 * tablaPDF.addCell("Identificador"); tablaPDF.addCell("Tipo");
								 * tablaPDF.addCell("Valor"); List<String> identificadores =
								 * lex.getIdentificadores(); for (String string : identificadores) {
								 * tablaPDF.addCell(string); tablaPDF.addCell(""); tablaPDF.addCell(""); }
								 * document.add(tablaPDF); document.close();
								 * if(JOptionPane.showConfirmDialog(null, "ñDesea visualizar los resultados?",
								 * "Pregunta", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION ) { try {
								 * Desktop.getDesktop().open(fileID); } catch (IOException e) { // TODO
								 * Auto-generated catch block e.printStackTrace(); } }
								 */

								String elemento;
								File lista = new File(JFC.getAbsolutePath() + ".txt");
								FileReader fr = new FileReader(lista);
								BufferedReader br = new BufferedReader(fr);
								List<Token> listaTokens = new ArrayList<Token>();
								while ((elemento = br.readLine()) != null) {
									char[] linea = elemento.toCharArray();
									String tk = "", nombreTK = "";
									int a = 0;
									for (int i = 1; i < linea.length; i++) {
										if (linea[i] == ',') {
											a = i;
											a++;
											if (linea[a] == ',') {
												tk += linea[i];
												for (int j = a + 2; j < linea.length - 1; j++) {
													nombreTK += linea[j];
												}
												break;
											} else {
												for (int j = i + 2; j < linea.length - 1; j++) {
													nombreTK += linea[j];
												}
												break;
											}
										} else {
											tk += linea[i];
										}
									}
									Token token = new Token(nombreTK, tk);
									listaTokens.add(token);
								}
								ReglasProduccion reglasProduccion = new ReglasProduccion(listaTokens);
								reglasProduccion.codigo();
								String errores = reglasProduccion.getErrores();
								areaConsola.append(errores);

								String linea;
								System.out.println("Ruta " + rutaTxt);
								File id = new File(rutaTxt);
								FileReader frId = new FileReader(id);
								BufferedReader brId = new BufferedReader(frId);
								List<Identificador> identificadores = new ArrayList<Identificador>();
								int contador = 1;
								while ((linea = brId.readLine()) != null) {
									if (contador != 1) {
										Identificador identifcador = new Identificador(linea);
										identificadores.add(identifcador);
									}
									contador++;
								}
								brId.close();
								Semantico semantico = new Semantico(identificadores, reglasProduccion.getArbol());
								semantico.analizar();
								String erroresSemanticos = semantico.getErrores();
								areaConsola.append(erroresSemanticos);
							} else {
								JOptionPane.showMessageDialog(rootPane, "El archivo ya existe", "ERROR",
										JOptionPane.ERROR_MESSAGE, null);
								btnAnalizar.doClick();
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "¡Error al guardar el archivo!", "ERROR",
								JOptionPane.ERROR_MESSAGE, null);
					}

				} else {
					JOptionPane.showMessageDialog(rootPane, "El saludo no puede ser analizado", "ADVERTENCIA",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}
		});
		btnAnalizar.setContentAreaFilled(false);
		btnAnalizar.setBorderPainted(false);
		btnAnalizar.setForeground(Color.WHITE);
		btnAnalizar.setBounds(377, 0, 104, 27);
		panel.add(btnAnalizar);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 49, 618, 378);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 618, 378);
		panel_1.add(tabbedPane);
		areaCodigo = new JTextArea();
		areaCodigo.setBackground(Color.BLACK);
		areaCodigo.setForeground(Color.WHITE);
		areaCodigo.setEditable(false);
		areaCodigo.setText("COMPILADOR\nHOLA BIENVENID@\n\nEste es un programa diseñado por:\n"
				+ "- Luis Alberto Colin Zuñiga\t\t17650016\n" + "- Edwin Jesus Silva Escutia\t\t17650079\n"
				+ "- Jose Daniel Torres Victoria\t\t17650082\n\n" + "Grupo: 7M\n\n"
				+ "Cree o Abra un archivo para poder analizarlo :)");

		scrollPane = new JScrollPane(areaCodigo);
		tabbedPane.addTab("Bienvenid@", null, scrollPane, null);

		JPanel panelConsola = new JPanel();
		panelConsola.setBorder(new TitledBorder(null, "Consola", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelConsola.setBounds(10, 438, 618, 162);
		contentPane.add(panelConsola);
		panelConsola.setLayout(null);

		areaConsola = new JTextArea();
		areaConsola.setBackground(Color.DARK_GRAY);
		areaConsola.setForeground(Color.WHITE);
		areaConsola.setEditable(false);

		JScrollPane scrollPaneConsola = new JScrollPane(areaConsola);
		scrollPaneConsola.setBounds(10, 21, 598, 130);
		panelConsola.add(scrollPaneConsola);

		int index = tabbedPane.indexOfTab("Bienvenid@");
		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitulo = new JLabel("Bienvenid@");
		JButton btnClose = new JButton("X");
		btnClose.setForeground(Color.RED);
		btnClose.setContentAreaFilled(false);
		btnClose.setBorderPainted(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		pnlTab.add(lblTitulo, gbc);
		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClose, gbc);
		tabbedPane.setTabComponentAt(index, pnlTab);
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selected = tabbedPane.indexOfTab("Bienvenid@");
				if (selected > -1) {
					tabbedPane.remove(selected);
				}

			}
		});

	}

	public int isAbierto(String name) {
		int aux = -1;
		aux = tabbedPane.indexOfTab(name);
		return aux;

	}

	public boolean estaAbierto(String pestaña) {
		for (Archivo archivo : archivos) {
			if (archivo.getArchivo().compareTo(pestaña) == 0) {
				return true;
			}
		}
		return false;
	}

	public void cerrar(String pestaña) {
		for (int i = 0; i < archivos.size(); i++) {
			if (archivos.get(i).getArchivo().compareTo(pestaña) == 0) {
				remove(i);
			}
		}
	}

	public String getPath(String pestaña) {
		for (Archivo archivo : archivos) {
			if (archivo.getArchivo().compareTo(pestaña) == 0) {
				return archivo.getRuta();
			}
		}
		return null;
	}

}
