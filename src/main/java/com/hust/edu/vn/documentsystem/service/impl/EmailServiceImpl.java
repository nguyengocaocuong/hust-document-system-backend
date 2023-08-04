package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailMailSender;


    public EmailServiceImpl(JavaMailSender emailMailSender) {
        this.emailMailSender = emailMailSender;
    }

    @Override
    public boolean sendSimpleMessage(User user, String subject, String text) {
        MimeMessage message = emailMailSender.createMimeMessage();
        MimeMessageHelper helper;
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("""
                                
                <!doctype html>
                   <html>
                   <head>
                     <meta name="viewport" content="width=device-width" />
                     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                     <title>Hust Document System</title>
                   </head>
                   <style>
                     /* -------------------------------------
                       GLOBAL RESETS
                   ------------------------------------- */
                                       
                     /*All the styling goes here*/
                                       
                     img {
                       border: none;
                       -ms-interpolation-mode: bicubic;
                       max-width: 100%;
                     }
                                       
                     body {
                       background-color: #eaebed;
                       font-family: sans-serif;
                       -webkit-font-smoothing: antialiased;
                       font-size: 14px;
                       line-height: 1.4;
                       margin: 0;
                       padding: 0;
                       -ms-text-size-adjust: 100%;
                       -webkit-text-size-adjust: 100%;
                     }
                                       
                     table {
                       border-collapse: separate;
                       mso-table-lspace: 0pt;
                       mso-table-rspace: 0pt;
                       min-width: 100%;
                       width: 100%;
                     }
                                       
                     table td {
                       font-family: sans-serif;
                       font-size: 14px;
                       vertical-align: top;
                     }
                                       
                     /* -------------------------------------
                       BODY & CONTAINER
                   ------------------------------------- */
                                       
                     .body {
                       background-color: #eaebed;
                       width: 100%;
                     }
                                       
                     /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */
                     .container {
                       display: block;
                       Margin: 0 auto !important;
                       /* makes it centered */
                       max-width: 580px;
                       padding: 10px;
                       width: 580px;
                     }
                                       
                     /* This should also be a block element, so that it will fill 100% of the .container */
                     .content {
                       box-sizing: border-box;
                       display: block;
                       Margin: 0 auto;
                       max-width: 580px;
                       padding: 10px;
                     }
                                       
                     /* -------------------------------------
                       HEADER, FOOTER, MAIN
                   ------------------------------------- */
                     .main {
                       background: #ffffff;
                       border-radius: 3px;
                       width: 100%;
                     }
                                       
                     .header {
                       padding: 20px 0;
                     }
                                       
                     .wrapper {
                       box-sizing: border-box;
                       padding: 20px;
                     }
                                       
                     .content-block {
                       padding-bottom: 10px;
                       padding-top: 10px;
                     }
                                       
                     .footer {
                       clear: both;
                       Margin-top: 10px;
                       text-align: center;
                       width: 100%;
                     }
                                       
                     .footer td,
                     .footer p,
                     .footer span,
                     .footer a {
                       color: #9a9ea6;
                       font-size: 12px;
                       text-align: center;
                     }
                                       
                     /* -------------------------------------
                       TYPOGRAPHY
                   ------------------------------------- */
                     h1,
                     h2,
                     h3,
                     h4 {
                       color: #06090f;
                       font-family: sans-serif;
                       font-weight: 400;
                       line-height: 1.4;
                       margin: 0;
                       margin-bottom: 30px;
                     }
                                       
                     h1 {
                       font-size: 35px;
                       font-weight: 300;
                       text-align: center;
                       text-transform: capitalize;
                     }
                                       
                     p,
                     ul,
                     ol {
                       font-family: sans-serif;
                       font-size: 14px;
                       font-weight: normal;
                       margin: 0;
                       margin-bottom: 15px;
                     }
                                       
                     p li,
                     ul li,
                     ol li {
                       list-style-position: inside;
                       margin-left: 5px;
                     }
                                       
                     a {
                       color: #ec0867;
                       text-decoration: underline;
                     }
                                       
                     /* -------------------------------------
                       BUTTONS
                   ------------------------------------- */
                     .btn {
                       box-sizing: border-box;
                       width: 100%;
                     }
                                       
                     .btn>tbody>tr>td {
                       padding-bottom: 15px;
                     }
                                       
                     .btn table {
                       min-width: auto;
                       width: auto;
                     }
                                       
                     .btn table td {
                       background-color: #ffffff;
                       border-radius: 5px;
                       text-align: center;
                     }
                                       
                     .btn a {
                       background-color: #ffffff;
                       border: solid 1px #ec0867;
                       border-radius: 5px;
                       box-sizing: border-box;
                       color: #ec0867;
                       cursor: pointer;
                       display: inline-block;
                       font-size: 14px;
                       font-weight: bold;
                       margin: 0;
                       padding: 12px 25px;
                       text-decoration: none;
                       text-transform: capitalize;
                     }
                                       
                     .btn-primary table td {
                       background-color: #ec0867;
                     }
                                       
                     .btn-primary a {
                       background-color: #ec0867;
                       border-color: #ec0867;
                       color: #ffffff;
                     }
                                       
                     /* -------------------------------------
                       OTHER STYLES THAT MIGHT BE USEFUL
                   ------------------------------------- */
                     .last {
                       margin-bottom: 0;
                     }
                                       
                     .first {
                       margin-top: 0;
                     }
                                       
                     .align-center {
                       text-align: center;
                     }
                                       
                     .align-right {
                       text-align: right;
                     }
                                       
                     .align-left {
                       text-align: left;
                     }
                                       
                     .clear {
                       clear: both;
                     }
                                       
                     .mt0 {
                       margin-top: 0;
                     }
                                       
                     .mb0 {
                       margin-bottom: 0;
                     }
                                       
                     .preheader {
                       color: transparent;
                       display: none;
                       height: 0;
                       max-height: 0;
                       max-width: 0;
                       opacity: 0;
                       overflow: hidden;
                       mso-hide: all;
                       visibility: hidden;
                       width: 0;
                     }
                                       
                     .powered-by a {
                       text-decoration: none;
                     }
                                       
                     hr {
                       border: 0;
                       border-bottom: 1px solid #f6f6f6;
                       Margin: 20px 0;
                     }
                                       
                     /* -------------------------------------
                       RESPONSIVE AND MOBILE FRIENDLY STYLES
                   ------------------------------------- */
                     @media only screen and (max-width: 620px) {
                       table[class=body] h1 {
                         font-size: 28px !important;
                         margin-bottom: 10px !important;
                       }
                                       
                       table[class=body] p,
                       table[class=body] ul,
                       table[class=body] ol,
                       table[class=body] td,
                       table[class=body] span,
                       table[class=body] a {
                         font-size: 16px !important;
                       }
                                       
                       table[class=body] .wrapper,
                       table[class=body] .article {
                         padding: 10px !important;
                       }
                                       
                       table[class=body] .content {
                         padding: 0 !important;
                       }
                                       
                       table[class=body] .container {
                         padding: 0 !important;
                         width: 100% !important;
                       }
                                       
                       table[class=body] .main {
                         border-left-width: 0 !important;
                         border-radius: 0 !important;
                         border-right-width: 0 !important;
                       }
                                       
                       table[class=body] .btn table {
                         width: 100% !important;
                       }
                                       
                       table[class=body] .btn a {
                         width: 100% !important;
                       }
                                       
                       table[class=body] .img-responsive {
                         height: auto !important;
                         max-width: 100% !important;
                         width: auto !important;
                       }
                     }
                                       
                     /* -------------------------------------
                       PRESERVE THESE STYLES IN THE HEAD
                   ------------------------------------- */
                     @media all {
                       .ExternalClass {
                         width: 100%;
                       }
                                       
                       .ExternalClass,
                       .ExternalClass p,
                       .ExternalClass span,
                       .ExternalClass font,
                       .ExternalClass td,
                       .ExternalClass div {
                         line-height: 100%;
                       }
                                       
                       .apple-link a {
                         color: inherit !important;
                         font-family: inherit !important;
                         font-size: inherit !important;
                         font-weight: inherit !important;
                         line-height: inherit !important;
                         text-decoration: none !important;
                       }
                                       
                       .btn-primary table td:hover {
                         background-color: #d5075d !important;
                       }
                                       
                       .btn-primary a:hover {
                         background-color: #d5075d !important;
                         border-color: #d5075d !important;
                       }
                     }
                   </style>
                                       
                   <body class="">
                     <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="body">
                       <tr>
                         <td>&nbsp;</td>
                         <td class="container">
                           <div class="header">
                             <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%">
                               <tr>
                                 <td class="align-center" width="100%">
                                   <a
                                     href="
                                     """);
        htmlContent.append(System.getenv("FRONTEND_URL"));
        htmlContent.append("""
                                 "><img src="https://storage.googleapis.com/hust-document-storage/logo/logo.png" height="40" alt="Hust document system"></a>
                                 </td>
                               </tr>
                             </table>
                           </div>
                           <div class="content">
                                       
                             <!-- START CENTERED WHITE CONTAINER -->
                             <span class="preheader">Email kích hoạt tài khoản hệ thống Hust Document System</span>
                             <table role="presentation" class="main">
                                       
                               <!-- START MAIN CONTENT AREA -->
                               <tr>
                                 <td class="wrapper">
                                   <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                     <tr>
                                       <td>
                                         <p>👋&nbsp; Xin chào <strong><i>
                """);
        htmlContent.append(user.getFirstName() + " " + user.getLastName());
        htmlContent.append("""
                </i></strong>. Đây là email kích hoạt tài khoản
                                            của bạn.</p>
                                          <p>✨&nbsp; Hệ thống này hứa hẹn sẽ giúp ích cho bạn trong việc học tập của bạn, tại trường đại học
                                            <strong>Bách Khoa Hà Nội</strong></p>
                                          <p>⬇️&nbsp; Hi vọng bạn sẽ có trải nghiệm tốt khi sử dụng hệ thống.</p>
                                          <p>📬&nbsp; Truy cập link phía dưới để bắt đầu sử dụng hệ thống nhé.</p>
                                          <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="btn btn-primary">
                                            <tbody>
                                              <tr>
                                                <td align="center">
                                                  <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                                    <tbody>
                                                      <tr>
                                                        <td> <a href="
                """);
        htmlContent.append(text);
        htmlContent.append("""
                " target="_blank">Kích hoạt tài khoản</a> </td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                                </td>
                                              </tr>
                                            </tbody>
                                          </table>
                                        </td>
                                      </tr>
                                    </table>
                                  </td>
                                </tr>
                                        
                                <!-- END MAIN CONTENT AREA -->
                              </table>
                                        
                              <!-- START FOOTER -->
                              <div class="footer">
                                <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                  <tr>
                                    <td class="content-block">
                                      <span class="apple-link">Đừng quên kích hoạt tài khoản để đăng nhập và sử dụng </span>
                                      <br> <a href="
                """);
        htmlContent.append(System.getenv("FRONTEND_URL"));
        htmlContent.append("""
                ">Hust Document System</a> Tại đây.
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="content-block powered-by">
                                      Chủ sở hữu <strong>Nguyễn Ngô Cao Cường</a>.
                                    </td>
                                  </tr>
                                </table>
                              </div>
                              <!-- END FOOTER -->
                                        
                              <!-- END CENTERED WHITE CONTAINER -->
                            </div>
                          </td>
                          <td>&nbsp;</td>
                        </tr>
                      </table>
                    </body>
                                        
                    </html>
                """);

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent.toString(), true);
            emailMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendHtmlMessage(String to, String subject, String htmlContent) {
        MimeMessage message = emailMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean sendEmailWithAttachment(String to, String subject, String text, MultipartFile attachmentFilePath) {
        MimeMessage message = emailMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text);
            multipart.addBodyPart(textPart);
            MimeBodyPart attachmentPart = new MimeBodyPart();
            ByteArrayDataSource attachmentDataSource = new ByteArrayDataSource(attachmentFilePath.getBytes(), attachmentFilePath.getContentType());
            attachmentPart.setDataHandler(new DataHandler(attachmentDataSource));
            attachmentPart.setFileName(attachmentFilePath.getOriginalFilename());
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            emailMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
