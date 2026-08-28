package in.master.moneymanager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Sends a professional HTML activation email.
     */
    public void sendHtmlEmail(
            String to,
            String subject,
            String activationUrl) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String html = """
                    <!DOCTYPE html>
                    <html lang="en">

                    <head>

                        <meta charset="UTF-8">

                        <meta name="viewport"
                              content="width=device-width, initial-scale=1.0">

                        <meta name="x-apple-disable-message-reformatting">

                        <title>Verify your Money Manager account</title>

                        <style>

                            body {
                                margin: 0;
                                padding: 0;
                                background-color: #f4f6f8;
                                font-family:
                                    -apple-system,
                                    BlinkMacSystemFont,
                                    "Segoe UI",
                                    Roboto,
                                    Helvetica,
                                    Arial,
                                    sans-serif;
                            }

                            table {
                                border-spacing: 0;
                            }

                            .wrapper {
                                width: 100%%;
                                background-color: #f4f6f8;
                                padding: 48px 16px;
                            }

                            .container {
                                width: 100%%;
                                max-width: 560px;
                                margin: 0 auto;
                                background-color: #ffffff;
                                border-radius: 16px;
                                overflow: hidden;
                                border: 1px solid #e6e9ed;
                            }

                            .header {
                                padding: 28px 36px;
                                border-bottom: 1px solid #eef0f2;
                            }

                            .brand {
                                font-size: 18px;
                                font-weight: 700;
                                letter-spacing: -0.3px;
                                color: #111827;
                            }

                            .brand-mark {
                                display: inline-block;
                                width: 8px;
                                height: 8px;
                                margin-right: 8px;
                                border-radius: 50%%;
                                background-color: #2563eb;
                            }

                            .content {
                                padding: 48px 44px;
                            }

                            .verification {
                                position: relative;
                                width: 58px;
                                height: 58px;
                                margin: 0 auto 30px auto;
                            }

                            .verification-ring {
                                width: 58px;
                                height: 58px;
                                border: 2px solid #dbeafe;
                                border-radius: 50%%;
                                box-sizing: border-box;

                                /*
                                 * Supported email clients will show
                                 * a subtle pulse animation.
                                 *
                                 * Unsupported clients simply show
                                 * the static ring.
                                 */
                                animation: pulse 2.4s ease-in-out infinite;
                            }

                            .verification-dot {
                                position: absolute;
                                width: 12px;
                                height: 12px;
                                top: 21px;
                                left: 21px;
                                border-radius: 50%%;
                                background-color: #2563eb;
                            }

                            .title {
                                margin: 0;
                                text-align: center;
                                font-size: 30px;
                                line-height: 1.2;
                                letter-spacing: -0.8px;
                                font-weight: 700;
                                color: #111827;
                            }

                            .description {
                                max-width: 430px;
                                margin: 18px auto 0 auto;
                                text-align: center;
                                font-size: 16px;
                                line-height: 1.7;
                                color: #667085;
                            }

                            .button-container {
                                text-align: center;
                                padding: 34px 0 30px 0;
                            }

                            .button {
                                display: inline-block;
                                padding: 15px 30px;
                                border-radius: 9px;
                                background-color: #2563eb;
                                color: #ffffff !important;
                                font-size: 15px;
                                font-weight: 600;
                                text-decoration: none;
                            }

                            .security {
                                margin-top: 8px;
                                padding: 18px 20px;
                                background-color: #f8fafc;
                                border: 1px solid #edf0f3;
                                border-radius: 10px;
                            }

                            .security-title {
                                margin: 0 0 5px 0;
                                font-size: 13px;
                                font-weight: 600;
                                color: #344054;
                            }

                            .security-text {
                                margin: 0;
                                font-size: 12px;
                                line-height: 1.6;
                                color: #667085;
                            }

                            .fallback {
                                margin-top: 26px;
                                text-align: center;
                            }

                            .fallback-label {
                                margin: 0 0 7px 0;
                                font-size: 11px;
                                color: #98a2b3;
                            }

                            .fallback-link {
                                margin: 0;
                                font-size: 11px;
                                line-height: 1.5;
                                word-break: break-all;
                            }

                            .fallback-link a {
                                color: #2563eb;
                                text-decoration: none;
                            }

                            .footer {
                                padding: 24px 30px;
                                background-color: #fafbfc;
                                border-top: 1px solid #eef0f2;
                                text-align: center;
                            }

                            .footer-brand {
                                margin: 0;
                                font-size: 12px;
                                font-weight: 600;
                                color: #667085;
                            }

                            .footer-text {
                                margin: 6px 0 0 0;
                                font-size: 11px;
                                color: #98a2b3;
                            }

                            @keyframes pulse {

                                0%% {
                                    transform: scale(1);
                                    opacity: 1;
                                }

                                50%% {
                                    transform: scale(1.08);
                                    opacity: 0.65;
                                }

                                100%% {
                                    transform: scale(1);
                                    opacity: 1;
                                }

                            }

                            @media only screen and (max-width: 600px) {

                                .wrapper {
                                    padding: 24px 10px;
                                }

                                .content {
                                    padding: 38px 24px;
                                }

                                .header {
                                    padding: 24px;
                                }

                                .title {
                                    font-size: 26px;
                                }

                                .description {
                                    font-size: 15px;
                                }

                                .button {
                                    display: block;
                                }

                            }

                        </style>

                    </head>

                    <body>

                        <table
                            class="wrapper"
                            width="100%%"
                            cellpadding="0"
                            cellspacing="0">

                            <tr>

                                <td align="center">

                                    <table
                                        class="container"
                                        width="100%%"
                                        cellpadding="0"
                                        cellspacing="0">

                                        <!-- HEADER -->

                                        <tr>

                                            <td class="header">

                                                <div class="brand">

                                                    <span
                                                        class="brand-mark">
                                                    </span>

                                                    MONEY MANAGER

                                                </div>

                                            </td>

                                        </tr>

                                        <!-- CONTENT -->

                                        <tr>

                                            <td class="content">

                                                <!-- SUBTLE ANIMATION -->

                                                <div class="verification">

                                                    <div
                                                        class="verification-ring">
                                                    </div>

                                                    <div
                                                        class="verification-dot">
                                                    </div>

                                                </div>

                                                <h1 class="title">
                                                    Verify your email
                                                </h1>

                                                <p class="description">
                                                    Your Money Manager account
                                                    is almost ready. Confirm
                                                    your email address to
                                                    activate your account.
                                                </p>

                                                <!-- CTA -->

                                                <div
                                                    class="button-container">

                                                    <a
                                                        href="%s"
                                                        class="button">

                                                        Activate account

                                                    </a>

                                                </div>

                                                <!-- SECURITY -->

                                                <div class="security">

                                                    <p class="security-title">
                                                        Account security
                                                    </p>

                                                    <p class="security-text">
                                                        This verification link
                                                        was generated specifically
                                                        for your account. If you
                                                        did not create a Money
                                                        Manager account, you can
                                                        safely ignore this email.
                                                    </p>

                                                </div>

                                                <!-- FALLBACK LINK -->

                                                <div class="fallback">

                                                    <p class="fallback-label">
                                                        If the button doesn't
                                                        work, use this link:
                                                    </p>

                                                    <p class="fallback-link">

                                                        <a href="%s">
                                                            %s
                                                        </a>

                                                    </p>

                                                </div>

                                            </td>

                                        </tr>

                                        <!-- FOOTER -->

                                        <tr>

                                            <td class="footer">

                                                <p class="footer-brand">
                                                    MONEY MANAGER
                                                </p>

                                                <p class="footer-text">
                                                    Personal finance,
                                                    simplified.
                                                </p>

                                            </td>

                                        </tr>

                                    </table>

                                </td>

                            </tr>

                        </table>

                    </body>

                    </html>
                    """.formatted(
                            activationUrl,
                            activationUrl,
                            activationUrl
                    );

            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }
    }

    /**
     * Sends an email with an attachment.
     */
    public void sendEmailWithAttachment(
            String to,
            String subject,
            String body,
            byte[] attachment,
            String filename) throws MessagingException {

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        helper.addAttachment(
                filename,
                new ByteArrayResource(attachment)
        );

        mailSender.send(message);
    }
}