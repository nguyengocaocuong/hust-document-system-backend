package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.Dimensions;
import co.elastic.thumbnails4j.core.Thumbnailer;
import co.elastic.thumbnails4j.core.ThumbnailingException;
import co.elastic.thumbnails4j.doc.DOCThumbnailer;
import co.elastic.thumbnails4j.docx.DOCXThumbnailer;
import co.elastic.thumbnails4j.image.ImageThumbnailer;
import co.elastic.thumbnails4j.pdf.PDFThumbnailer;
import co.elastic.thumbnails4j.pptx.PPTXThumbnailer;
import co.elastic.thumbnails4j.xls.XLSThumbnailer;
import co.elastic.thumbnails4j.xlsx.XLSXThumbnailer;
import com.hust.edu.vn.documentsystem.service.ThumbnailService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.SlideShow;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ThumbnailServiceImpl implements ThumbnailService {
    @Override
    public byte[] generateThumbnail(@NotNull  MultipartFile file) throws ThumbnailingException, IOException, DocumentException {

        switch (Objects.requireNonNull(file.getContentType())) {
            case MediaType.APPLICATION_PDF_VALUE -> {
                return generateThumbnailUseItextPdf(file);
            }
            case MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/jpg" -> {
                return generateThumbnailForImage(file);
            }
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> {
                return generateThumbnailForDocx(file);
            }
            case "application/msword" ->{
                return generateThumbnailForDoc(file);
            }
            case "text/plain"->{
                return generateThumbnailForTxt(file);
            }
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> {
                return generateThumbnailForPptx(file);
            }
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> {
                return generateThumbnailForXlsx(file);
            }
            case "application/vnd.ms-excel" -> {
                return generateThumbnailForXls(file);
            }
            default -> {
                return null;
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
    public byte[] generateThumbnailForDoc(MultipartFile doc) throws ThumbnailingException, IOException {
        Thumbnailer thumbnailDoc = new DOCThumbnailer();
        return generateThumbnailUseThumbnails4j(thumbnailDoc, doc);
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
    public byte[] generateThumbnailForXlsx(MultipartFile xlsx) throws ThumbnailingException, IOException {
        Thumbnailer thumbnailer = new XLSXThumbnailer();
        return generateThumbnailUseThumbnails4j(thumbnailer, xlsx);
    }

    @Override
    public byte[] generateThumbnailForPptx(MultipartFile pptx) throws ThumbnailingException, IOException {
        Thumbnailer thumbnailer = new PPTXThumbnailer();
        return generateThumbnailUseThumbnails4j(thumbnailer, pptx);
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
