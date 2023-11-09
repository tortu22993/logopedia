package crm.logopedia.data.session.model.pdf;



import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import crm.logopedia.data.session.model.dto.SessionDetailDto;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;

public class SessionPdfExporter {

    private SessionDetailDto session;

    // Constructor sin la sesión
    public SessionPdfExporter(SessionDetailDto session) {
        this.session= session;
    }

    // Método para establecer la sesión después de la creación del objeto
    public void setSession(SessionDetailDto session) {
        this.session = session;
    }

    private void writeSessionInfo(Document document, SessionDetailDto session) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(12);

        // Nombre del paciente
        Paragraph patientParagraph = new Paragraph("Paciente: " + session.getPatientName(), font);
        document.add(patientParagraph);

        // Terapeuta
        Paragraph therapistParagraph = new Paragraph("Terapeuta: " + session.getOwnerFullName(), font);
        document.add(therapistParagraph);

        // Asunto
        Paragraph subjectParagraph = new Paragraph("Asunto: " + session.getSubject(), font);
        document.add(subjectParagraph);

        // Fecha
        Paragraph dateParagraph = new Paragraph("Fecha: " + session.getSessionDate().toString(), font);
        document.add(dateParagraph);

        // Observaciones
        Paragraph observationsParagraph = new Paragraph("Observaciones: " + session.getDetails(), font);
        document.add(observationsParagraph);

        // Separador entre sesiones
        document.add(new Paragraph("------------------------------------------------------------------------"));


    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        titleFont.setColor(Color.BLUE);

        Paragraph title = new Paragraph("Informe de Sesiones", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(title);

        writeSessionInfo(document, session);


        document.close();
    }

}
