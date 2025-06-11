package com.shubham.authProject.service;

import com.lowagie.text.DocumentException;
import com.shubham.authProject.model.ResumeData;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import jakarta.annotation.PostConstruct; // Add this line



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class ResumePdfService {

    @Value("${pdf.storage.directory:/tmp/resumes}")
    private String pdfStorageDirectory;

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(pdfStorageDirectory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public String generateResumePdf(ResumeData resumeData) {
        String htmlContent = buildHtmlContent(resumeData);
        String uniqueFileName = generateUniqueFilename(resumeData.getName());

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);

            Path filePath = Paths.get(pdfStorageDirectory, uniqueFileName);
            Files.write(filePath, os.toByteArray());
            
            return uniqueFileName;
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failed to generate resume PDF", e);
        }
    }

    private String buildHtmlContent(ResumeData resumeData) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
            .append("<html lang=\"en\">")
            .append("<head>")
            .append("<meta charset=\"UTF-8\" />")
            .append("<title>").append(escape(resumeData.getName())).append(" - Resume</title>")
            .append("<style>").append(getCssForTemplate(resumeData.getTemplate())).append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class=\"container\">")
            .append("<div class=\"header\">")
            .append("<h1>").append(escape(resumeData.getName())).append("</h1>")
            .append("<p>Email: ").append(escape(resumeData.getEmail()))
            .append(" | Phone: ").append(escape(resumeData.getPhone())).append("</p>");

        if (hasValue(resumeData.getAddress())) {
            html.append("<p>Address: ").append(escape(resumeData.getAddress())).append("</p>");
        }
        if (hasValue(resumeData.getPermanentAddress())) {
            html.append("<p>Permanent Address: ").append(escape(resumeData.getPermanentAddress())).append("</p>");
        }
        if (hasValue(resumeData.getDateOfBirth())) {
            html.append("<p>Date of Birth: ").append(escape(resumeData.getDateOfBirth())).append("</p>");
        }
        if (hasValue(resumeData.getLinkedin())) {
            html.append("<p>LinkedIn: <a href=\"").append(escape(resumeData.getLinkedin())).append("\">")
               .append(escape(resumeData.getLinkedin())).append("</a></p>");
        }
        html.append("</div>");

        // Add all sections (objective, education, etc.)
        addSection(html, "Objective", resumeData.getObjective());
        addSection(html, "Education", resumeData.getEducation());
        addOptionalSection(html, "Work Experience", resumeData.getExperience());
        addSection(html, "Skills", resumeData.getSkills());
        addOptionalSection(html, "Projects", resumeData.getProjects());
        addOptionalSection(html, "Languages Known", resumeData.getLanguages());
        addOptionalSection(html, "Hobbies/Interests", resumeData.getHobbies());
        addSection(html, "Declaration", resumeData.getDeclaration());

        html.append("</div></body></html>");
        return html.toString();
    }

    private void addSection(StringBuilder html, String title, String content) {
        html.append("<div class=\"section\">")
            .append("<h2>").append(title).append("</h2>")
            .append("<p>").append(escape(content).replace("\n", "<br/>")).append("</p>")
            .append("</div>");
    }

    private void addOptionalSection(StringBuilder html, String title, String content) {
        if (hasValue(content)) {
            addSection(html, title, content);
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String escape(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

    private String generateUniqueFilename(String name) {
        String safeName = name.replaceAll("[^a-zA-Z0-9.-]", "_");
        return safeName + "_" + UUID.randomUUID() + ".pdf";
    }

    public ByteArrayResource loadPdfAsResource(String filename) throws IOException {
        Path filePath = Paths.get(pdfStorageDirectory).resolve(filename).normalize();
        if (!filePath.startsWith(Paths.get(pdfStorageDirectory))) {
            throw new IOException("Attempted to access file outside of designated directory");
        }
        return new ByteArrayResource(Files.readAllBytes(filePath));
    }

    private String getCssForTemplate(String template) {
        switch (template) {
            case "classic":
                return "body { font-family: 'Times New Roman', serif; margin: 20px; font-size: 12pt; }" +
                       ".container { width: 100%; margin: 0 auto; }" +
                       ".header { text-align: center; margin-bottom: 20px; }" +
                       ".header h1 { font-size: 24pt; margin-bottom: 5px; }" +
                       ".header p { margin: 0; font-size: 10pt; }" +
                       ".section { margin-bottom: 15px; border-bottom: 1px solid #ccc; padding-bottom: 10px; }" +
                       ".section h2 { font-size: 16pt; color: #333; margin-top: 0; margin-bottom: 10px; }";
            case "modern":
                return "body { font-family: Arial, sans-serif; margin: 25px; font-size: 11pt; }" +
                       ".container { width: 90%; margin: 0 auto; }" +
                       ".header { background-color: #f0f0f0; padding: 15px; text-align: center; margin-bottom: 20px; border-radius: 5px; }" +
                       ".header h1 { color: #0056b3; font-size: 22pt; margin-bottom: 5px; }" +
                       ".header p { margin: 0; font-size: 10pt; color: #555; }" +
                       ".section { margin-bottom: 20px; }" +
                       ".section h2 { font-size: 15pt; color: #0056b3; margin-top: 0; margin-bottom: 10px; border-left: 4px solid #0056b3; padding-left: 10px; }";
            case "creative":
                return "body { font-family: 'Georgia', serif; margin: 30px; font-size: 12pt; background-color: #fffaf0; }" +
                       ".container { width: 85%; margin: 0 auto; border: 1px solid #ddd; padding: 20px; box-shadow: 0 0 10px rgba(0,0,0,0.05); }" +
                       ".header { text-align: left; margin-bottom: 25px; background-color: #e0e0e0; padding: 15px; border-radius: 8px; }" +
                       ".header h1 { font-size: 26pt; color: #333; margin-bottom: 5px; }" +
                       ".header p { margin: 0; font-size: 11pt; color: #666; }" +
                       ".section { margin-bottom: 25px; position: relative; padding-left: 20px; }" +
                       ".section h2 { font-size: 18pt; color: #a0522d; margin-top: 0; margin-bottom: 10px; border-bottom: 1px dashed #a0522d; padding-bottom: 5px; }" +
                       ".section::before { content: '•'; position: absolute; left: 0; color: #a0522d; font-size: 1.5em; line-height: 1; }";
            default:
                return "";
        }
    }
}