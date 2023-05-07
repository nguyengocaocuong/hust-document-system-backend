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
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class ThumbnailServiceImpl implements ThumbnailService {
    public byte[] generateThumbnailForImage(MultipartFile image) {
        String filename = image.getOriginalFilename();
        if(filename == null)
            return new byte[0];
        Thumbnailer thumbnailImage = new ImageThumbnailer(filename.substring(filename.lastIndexOf(".") + 1));
        return  generateThumbnailUserThubmails4j(thumbnailImage, image);
    }

    @Override
    public byte[] generateThumbnailForPDF(MultipartFile pdf) {
        Thumbnailer thumbnailerPdf = new PDFThumbnailer();
        return  generateThumbnailUserThubmails4j(thumbnailerPdf, pdf);
    }

    @Override
    public byte[] generateThumbnailForDoc(MultipartFile doc) {
        Thumbnailer thumbnailDoc = new DOCThumbnailer();
        return  generateThumbnailUserThubmails4j(thumbnailDoc, doc);
    }

    @Override
    public byte[] generateThumbnailForDocx(MultipartFile docx) {
        Thumbnailer thumbnailerDocx = new DOCXThumbnailer();
        return generateThumbnailUserThubmails4j(thumbnailerDocx, docx);
    }


    @Override
    public byte[] generateThumbnailForXls(MultipartFile xls) {
        Thumbnailer thumbnailer = new XLSThumbnailer();
        return generateThumbnailUserThubmails4j(thumbnailer, xls);
    }

    @Override
    public byte[] generateThumbnailForXlsx(MultipartFile xlsx) {
        Thumbnailer thumbnailer = new XLSXThumbnailer();
        return  generateThumbnailUserThubmails4j(thumbnailer, xlsx);
    }

    @Override
    public byte[] generateThumbnailForPptx(MultipartFile pptx) {
        Thumbnailer thumbnailer = new PPTXThumbnailer();
        return  generateThumbnailUserThubmails4j(thumbnailer, pptx);
    }

    @Override
    public byte[] generateThumbnailForTxt(MultipartFile txt) {
        try {
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
            Dimensions outputDimensions = new Dimensions(200,200);
            BufferedImage thumbnail = thumbnailer.getThumbnails(inputStream, List.of(outputDimensions)).get(0);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            return baos.toByteArray();
        } catch (DocumentException | IOException | ThumbnailingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] generateThumbnailUserThubmails4j(Thumbnailer thumbnailer, MultipartFile document){
        try {
            Dimensions outputDimensions = new Dimensions(200,200);
            BufferedImage thumbnail = thumbnailer.getThumbnails(document.getInputStream(), List.of(outputDimensions)).get(0);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            return baos.toByteArray();

        } catch (IOException | ThumbnailingException e ) {
            throw new RuntimeException(e);
        }
    }
}
