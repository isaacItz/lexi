package modelo;

import com.itextpdf.text.log.SysoLogger;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdfText.PDFText;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			PDFText texto;
			try {
				texto = new PDFText("C:\\temp\\Analizador_Lexico\\identificadores\\ID.pdf", null);
				System.out.println(texto.getVersion());
				java.util.Vector<String> palabras = texto.getWords();
				
				for(int count = 0; count < palabras.size(); count++) {
					  System.out.println("[" + palabras.get(count) + "] ");
					}
			} catch (PDFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}

}
