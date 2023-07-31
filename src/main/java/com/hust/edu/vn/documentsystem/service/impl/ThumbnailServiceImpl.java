package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.Dimensions;
import co.elastic.thumbnails4j.core.Thumbnailer;
import co.elastic.thumbnails4j.core.ThumbnailingException;
import co.elastic.thumbnails4j.docx.DOCXThumbnailer;
import co.elastic.thumbnails4j.image.ImageThumbnailer;
import co.elastic.thumbnails4j.pdf.PDFThumbnailer;
import co.elastic.thumbnails4j.xls.XLSThumbnailer;
import com.aspose.cells.*;
import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;
import com.hust.edu.vn.documentsystem.service.ThumbnailService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import jakarta.validation.constraints.NotNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Objects;

@Service
public class ThumbnailServiceImpl implements ThumbnailService {
    @Override
    public byte[] generateThumbnail(@NotNull MultipartFile file) throws Exception {

        switch (Objects.requireNonNull(file.getContentType())) {
            case MediaType.APPLICATION_PDF_VALUE -> {
                return generateThumbnailUseItextPdf(file);
            }
            case MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/jpg" -> {
                return generateThumbnailForImage(file);
            }
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" , "application/msword" -> {
                return generateThumbnailForDoc(file);
            }
            case "text/plain" -> {
                return generateThumbnailForTxt(file);
            }
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/vnd.ms-powerpoint" -> {
                return generateThumbnailForPptx(file);
            }
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel" -> {
                return generateThumbnailForXlsx(file);
            }
            default -> {
                return new byte[0];
            }
        }
    }

    public byte[] generateThumbnailForImage(MultipartFile image) throws ThumbnailingException, IOException {
        String filename = image.getOriginalFilename();
        if (filename == null)
            return new byte[0];
        Thumbnailer thumbnailImage = new ImageThumbnailer(filename.substring(filename.lastIndexOf(".") + 1));
        return generateThumbnailUseThumbnails4j(thumbnailImage, image);
    }

    @Override
    public byte[] generateThumbnailForPDF(MultipartFile pdf) throws ThumbnailingException, IOException {
        Thumbnailer thumbnailPdf = new PDFThumbnailer();
        return generateThumbnailUseThumbnails4j(thumbnailPdf, pdf);
    }

    @Override
    public byte[] generateThumbnailForDoc(MultipartFile doc) throws Exception {
        com.aspose.words.Document document = new com.aspose.words.Document(doc.getInputStream());
        float SCALE = 1f;
        Dimension thumbSize = document.getPageInfo(0).getSizeInPixels(SCALE, 96);
        int imgWidth = (int) (thumbSize.getWidth());
        int imgHeight = (int) (thumbSize.getHeight() * 1);
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = img.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gr.setColor(java.awt.Color.white);
        gr.fillRect(0, 0, imgWidth, imgHeight);
        Point2D.Float size = document.renderToScale(0, gr, 0, 0, SCALE);
        gr.setColor(Color.black);
        gr.drawRect(0, 0, (int) size.getX(), (int) size.getY());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", baos);
        return baos.toByteArray();
    }

    @Override
    public byte[] generateThumbnailForDocx(MultipartFile docx) throws ThumbnailingException, IOException {
        Thumbnailer thumbnailDocx = new DOCXThumbnailer();
        return generateThumbnailUseThumbnails4j(thumbnailDocx, docx);
    }


    @Override
    public byte[] generateThumbnailForXls(MultipartFile xls) throws ThumbnailingException, IOException {
        Thumbnailer thumbnailer = new XLSThumbnailer();
        return generateThumbnailUseThumbnails4j(thumbnailer, xls);
    }

    @Override
    public byte[] generateThumbnailForXlsx(MultipartFile xlsx) throws Exception {
        Workbook book = new Workbook(xlsx.getInputStream());

        ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
        imgOptions.setVerticalResolution(200);
        imgOptions.setHorizontalResolution(200);
        imgOptions.setImageType(ImageType.PNG);
        imgOptions.setOnePagePerSheet(true);

        Worksheet sheet = book.getWorksheets().get(0);
        SheetRender sr = new SheetRender(sheet, imgOptions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sr.toImage(0, baos);
        return baos.toByteArray();
    }

    @Override
    public byte[] generateThumbnailForPptx(MultipartFile pptx) throws IOException {
        Presentation presentation = new Presentation(pptx.getInputStream());
        int desiredX = 794;
        int desiredY = 1123;
        float ScaleX = (float) (1.0 / presentation.getSlideSize().getSize().getWidth()) * desiredX;
        float ScaleY = (float) (1.0 / presentation.getSlideSize().getSize().getHeight()) * desiredY;
        ISlide iSlide = presentation.getSlides().get_Item(0);
        BufferedImage bi = iSlide.getThumbnail(ScaleX, ScaleY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return baos.toByteArray();
    }

    @Override
    public byte[] generateThumbnailForTxt(MultipartFile txt) throws DocumentException, IOException, ThumbnailingException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        String content = new String(txt.getBytes());
        Paragraph paragraph = new Paragraph(content);
        document.add(paragraph);
        document.close();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Thumbnailer thumbnailer = new PDFThumbnailer();
        Dimensions outputDimensions = new Dimensions(200, 200);
        BufferedImage thumbnail = thumbnailer.getThumbnails(inputStream, List.of(outputDimensions)).get(0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "png", baos);
        return baos.toByteArray();
    }

    public byte[] generateThumbnailUseItextPdf(MultipartFile file) throws IOException {
        PDDocument reader = PDDocument.load(file.getInputStream());
        PDFRenderer pdfRenderer = new PDFRenderer(reader);
        BufferedImage image = pdfRenderer.renderImage(0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private byte[] generateThumbnailUseThumbnails4j(Thumbnailer thumbnailer, MultipartFile document) throws IOException, ThumbnailingException {
        Dimensions outputDimensions = new Dimensions(794, 1123);
        BufferedImage thumbnail = thumbnailer.getThumbnails(document.getInputStream(), List.of(outputDimensions)).get(0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "png", baos);
        return baos.toByteArray();
    }
}
