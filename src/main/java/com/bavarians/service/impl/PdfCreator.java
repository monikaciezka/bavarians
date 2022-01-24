package com.bavarians.service.impl;

import com.bavarians.graphql.model.Oferta;
import com.bavarians.graphql.model.Pojazd;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PdfCreator {

    public static final int FIXED_HEIGHT = 20;
    public static final String STATIC_IMG_BAVARIANS_LOGO_PNG = "static/img/bavarians-logo.png";
    public static final String BAVARIANS_ADRIAN_DABROWSKI = "Bavarians Adrian Dąbrowski";
    public static final String UL_NOWOWIEJSKA_88 = "ul. Nowowiejska 88";
    public static final String POGROSZEW_KOLONIA = "05-850 Pogroszew-Kolonia";
    public static final String NIP_1182073903 = "NIP: 1182073903";
    private Font font;
    private Font fontHeader;

    public void createOfferPdf(String filename, Oferta oferta) throws IOException, DocumentException, URISyntaxException {
        BaseFont baseFont = BaseFont.createFont("static/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        font = new Font(baseFont, 12);
        fontHeader = new Font(baseFont, 16);
        Document document = new Document();
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        document.add(getLogo());
        document.add(getParagraphRightAlignment(createChunkWithBaseFont(getDate())));
        document.add(getParagraphRightAlignment(createChunkWithBaseFont(BAVARIANS_ADRIAN_DABROWSKI)));
        document.add(getParagraphRightAlignment(createChunkWithBaseFont(UL_NOWOWIEJSKA_88)));
        document.add(getParagraphRightAlignment(createChunkWithBaseFont(POGROSZEW_KOLONIA)));
        document.add(getParagraphRightAlignment(createChunkWithBaseFont(NIP_1182073903)));

        document.add(getParagraphCenterAlignment(createChunkWithFont("Kosztorys naprawy", fontHeader)));
        document.add(getParagraphLeftAlignment(createChunkWithBaseFont("Samochód")));
        document.add(getParagraphLeftAlignment(createChunkWithBaseFont(getPojazdInfo(oferta))));
        document.add(getParagraphLeftAlignment(createChunkWithBaseFont(getPojazdNrRej(oferta))));
        document.add(getParagraphLeftAlignment(createChunkWithBaseFont(getPojazdVin(oferta))));
        document.add(getParagraphLeftAlignment(createChunkWithBaseFont(getPojazdPrzebieg(oferta))));

        PdfPTable table = new PdfPTable(new float[]{60, 20, 20});

        table.setSpacingBefore(50);
        table.setSpacingAfter(50);
        table.setWidthPercentage(100);
        addTableHeader(table);
        oferta.getElementySerwisowe().forEach(element -> addRows(table, element));
        dodajPodsumowanieOferty(table, oferta);
        document.add(table);

        document.add(getParagraphRightAlignment(createChunkWithBaseFont("Suma netto " + getFormattedCena(oferta.getSumaBezVat()))));
        document.add(getParagraphRightAlignment(createChunkWithBaseFont("Suma brutto " + getFormattedCena(oferta.getSuma()))));

        document.close();
    }

    @NotNull
    private Paragraph getParagraphLeftAlignment(Chunk chunk) {
        return new Paragraph(chunk);
    }

    @NotNull
    private Chunk createChunkWithBaseFont(String text) {
        return createChunkWithFont(text, font);
    }

    @NotNull
    private Chunk createChunkWithFont(String text, Font font) {
        return new Chunk(text, font);
    }

    @NotNull
    private Phrase createPhrase(String columnTitle) {
        return new Phrase(createChunkWithBaseFont(columnTitle));
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Wykaz czynności i elementów", "Robocizna", "Części")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setFixedHeight(FIXED_HEIGHT);
                    header.setBorder(0);
                    header.setBorderWidthBottom(2);
                    header.setBorderColorBottom(BaseColor.GRAY);
                    header.setPhrase(createPhrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, com.bavarians.graphql.model.Element element) {
        table.addCell(getStandardCell(element.getNazwa()));
        table.addCell(getStandardCell(getFormattedCena(element.getCenaRobociznyNetto())));
        table.addCell(getStandardCell(getFormattedCena(element.getCenaCzesciBrutto())));
    }

    @NotNull
    private PdfPCell getStandardCell(String text) {
        PdfPCell pdfPCell = new PdfPCell(createPhrase(text));
        pdfPCell.setBorder(0);
        pdfPCell.setBorderWidthTop(1);
        pdfPCell.setBorderColorTop(BaseColor.LIGHT_GRAY);
        pdfPCell.setFixedHeight(FIXED_HEIGHT);
        return pdfPCell;
    }

    private void dodajPodsumowanieOferty(PdfPTable table, Oferta oferta) {
        table.addCell(createPdfCell(null));
        table.addCell(createPdfCell(oferta.getSumaRobociznaNetto()));
        table.addCell(createPdfCell(oferta.getSumaCzesciBrutto()));
    }

    @NotNull
    private PdfPCell createPdfCell(BigDecimal bigDecimal) {
        PdfPCell cell = getStandardCell(getFormattedCena(bigDecimal));
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setPaddingTop(5);
        cell.setFixedHeight(FIXED_HEIGHT);
        cell.setBorderColorTop(BaseColor.DARK_GRAY);
        return cell;
    }

    private String getFormattedCena(BigDecimal val) {
        return String.format("%s", Optional.ofNullable(val).map(d -> d + " zł").orElse(""));
    }

    private String getPojazdInfo(Oferta oferta) {
        return getPojazd(oferta).map(pojazd -> "Marka/model " + pojazd.getMarka() + " " + pojazd.getModel()).orElse("");
    }

    private String getPojazdNrRej(Oferta oferta) {
        return getPojazd(oferta).map(pojazd -> "Numer rejestracyjny " + pojazd.getNumerRejestracyjny()).orElse("");
    }

    private Optional<Pojazd> getPojazd(Oferta oferta) {
        return Optional.ofNullable(oferta.getPojazd());
    }

    private String getPojazdVin(Oferta oferta) {
        return getPojazd(oferta).map(pojazd -> "VIN " + pojazd.getVin()).orElse("");
    }

    private String getPojazdPrzebieg(Oferta oferta) {
        return getPojazd(oferta).map(pojazd -> "Przebieg " + pojazd.getPrzebieg()).orElse("");
    }

    @NotNull
    private Paragraph getParagraphRightAlignment(Chunk date) {
        Paragraph e1 = getParagraphLeftAlignment(date);
        e1.setAlignment(Element.ALIGN_RIGHT);
        return e1;
    }

    @NotNull
    private Paragraph getParagraphCenterAlignment(Chunk chunk) {
        Paragraph paragraph = getParagraphLeftAlignment(chunk);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(30);
        paragraph.setSpacingBefore(20);
        return paragraph;
    }

    @NotNull
    private String getDate() {
        LocalDate date = LocalDate.now();
        return date.toString();
    }

    private Image getLogo() throws BadElementException, IOException {
        String absolutePath = ResourceUtils.getFile("classpath:" + STATIC_IMG_BAVARIANS_LOGO_PNG).getAbsolutePath();
        return Image.getInstance(absolutePath);
    }
}

