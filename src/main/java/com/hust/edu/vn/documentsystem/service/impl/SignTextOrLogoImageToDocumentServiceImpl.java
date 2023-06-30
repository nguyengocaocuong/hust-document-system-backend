package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.service.SignTextOrLogoImageToDocumentService;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import jakarta.validation.constraints.NotNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.hslf.usermodel.HSLFPictureData;
import org.apache.poi.hslf.usermodel.HSLFPictureShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SignTextOrLogoImageToDocumentServiceImpl implements SignTextOrLogoImageToDocumentService {
    private static final String SIGN_TEXT = "HUST DOCUMENT SYSTEM";
    private static final String PPT_LOGO = "classpath:ppt_logo.png";
    private static final String PDF_LOGO = "classpath:sign.png";
    private final ResourceLoader resourceLoader;

    public SignTextOrLogoImageToDocumentServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public byte[] sign(@NotNull MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        switch (file.getContentType()) {
            case MediaType.APPLICATION_PDF_VALUE -> {
                return signToPDF(inputStream);
            }
            case MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/jpg" -> {
                return signToImage(inputStream);
            }
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> {
                return signToDocx(inputStream);
            }
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> {
                return signToPPTX(inputStream);
            }
            case "application/vnd.ms-powerpoint" -> {
                return signToPPT(inputStream);
            }
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> {
                return signImageToXlsx(inputStream);
            }
            case "application/vnd.ms-excel" -> {
                return signToXls(inputStream);
            }
            default -> {
                return inputStream.readAllBytes();
            }
        }
    }

    @Override
    public byte[] signToPDF(InputStream inputStream) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        int pageCount = document.getNumberOfPages();

        for (int i = 0; i < pageCount; i++) {
            PDPage page = document.getPage(i);
            PDRectangle pageSize = page.getMediaBox();
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.setNonStrokingColor(Color.RED);
                contentStream.newLineAtOffset(pageSize.getWidth() / 2, 20);
                contentStream.showText(SIGN_TEXT);
                contentStream.endText();
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);

        return outputStream.toByteArray();
    }

    @Override
    public byte[] signToImage(InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        Graphics2D graphics2D = image.createGraphics();
        Font font = new Font("Arial", Font.BOLD, 48);
        Color color = Color.RED;
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(SIGN_TEXT);
        int textHeight = fontMetrics.getHeight();
        int x = (image.getWidth() - textWidth) / 2;
        int y = (image.getHeight() + textHeight) / 2;
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        graphics2D.setComposite(alphaComposite);
        graphics2D.drawString(SIGN_TEXT, x, y);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] signToDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);

            XWPFParagraph paragraph = footer.getParagraphArray(0);
            if (paragraph == null) {
                paragraph = footer.createParagraph();
            }

            XWPFRun run = paragraph.createRun();
            run.setText(SIGN_TEXT);

            run.setBold(true);
            run.setFontSize(12);
            run.setColor("FF0000");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public byte[] signToXls(InputStream inputStream) throws IOException {
        Workbook workbook = new Workbook();
        workbook.loadFromStream(inputStream);
        Worksheet sheet = workbook.getWorksheets().get(0);
        BufferedImage backgroundImage = ImageIO.read(resourceLoader.getResource(PDF_LOGO).getFile());
        sheet.getPageSetup().setBackgoundImage(backgroundImage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.saveToStream(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] signToPPTX(InputStream inputStream) throws IOException {
        try (SlideShow ppt = new XMLSlideShow(inputStream)) {

            @SuppressWarnings("unchecked")
            List<XSLFSlide> slides = ppt.getSlides();
            Dimension slideSize = ppt.getPageSize();
            for (XSLFSlide slide : slides) {
                XSLFPictureData logoData = (XSLFPictureData) ppt.addPicture(
                        resourceLoader.getResource(PPT_LOGO).getContentAsByteArray(), PictureData.PictureType.PNG);
                XSLFPictureShape logoShape = slide.createPicture(logoData);
                int logoWidth = 200;
                int logoHeight = 62;
                int margin = 10;
                int logoX = slideSize.width - logoWidth - margin;
                int logoY = slideSize.height - logoHeight - margin;
                logoShape.setAnchor(new Rectangle2D.Double(logoX, logoY, logoWidth, logoHeight));
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ppt.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public byte[] signToPPT(InputStream inputStream) throws IOException {
       try(HSLFSlideShow ppt = new HSLFSlideShow(inputStream)){
        List<HSLFSlide> slides = ppt.getSlides();
        Dimension slideSize = ppt.getPageSize();
        byte[] logoBytes = resourceLoader.getResource(PPT_LOGO).getContentAsByteArray();

        for (HSLFSlide slide : slides) {
            HSLFPictureData logoData = ppt.addPicture(logoBytes, PictureData.PictureType.PNG);
            HSLFPictureShape logoShape = slide.createPicture(logoData);
            int logoWidth = 200;
            int logoHeight = 62;
            int margin = 10;
            int logoX = slideSize.width - logoWidth - margin;
            int logoY = slideSize.height - logoHeight - margin;
            logoShape.setAnchor(new java.awt.Rectangle(logoX, logoY, logoWidth, logoHeight));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ppt.write(outputStream);
        return outputStream.toByteArray();
       }
    }

    @Override
    public byte[] signImageToXlsx(InputStream inputStream) throws IOException {
       try(XSSFWorkbook workbook = new XSSFWorkbook(inputStream)){
        byte[] imgBytes = resourceLoader.getResource(PDF_LOGO).getContentAsByteArray();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            int pictureIdx = workbook.addPicture(imgBytes, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_PNG);
            String rID = sheet.addRelation(null, XSSFRelation.IMAGES, workbook.getAllPictures().get(pictureIdx))
                    .getRelationship().getId();
            sheet.getCTWorksheet().addNewPicture().setId(rID);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
       }
    }
}
