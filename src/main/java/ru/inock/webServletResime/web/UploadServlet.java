/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  5 дек. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.web;

import ru.inock.webServletResime.config.Config;
import ru.inock.webServletResime.model.Resume;
import ru.inock.webServletResime.storage.FileIoStorage;
import ru.inock.webServletResime.storage.SqlStorage;
import ru.inock.webServletResime.storage.storageInterfaces.Storage;
import ru.inock.webServletResime.storage.storageInterfaces.StreamSerializable;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.nio.file.Paths;

import ru.inock.webServletResime.storage.XmlJaxbParser;
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private StreamSerializable streamSerializable = new XmlJaxbParser();
    final SqlStorage SQL_STORAGE = (SqlStorage) Config.getInstance().getStorage();
    final Storage FILE_STORAGE = new FileIoStorage(Config.getInstance().getStorageDir().getCanonicalPath(), new XmlJaxbParser());
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.


    Resume r;

    public UploadServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");

        if (uuid != null) {
            r = SQL_STORAGE.get(uuid);
            String fileResumeName = String.valueOf(Paths.get(Config.getInstance().getStorageDir() + "/" + uuid + streamSerializable.getFileExc()));
            File fileResume = new File(fileResumeName);
            String contentType = getServletContext().getMimeType(fileResume.getName());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            if (fileResume.exists()) {
                fileResume.delete();
            }

            FILE_STORAGE.save(r);

            // Init servlet response.
            response.reset();
            response.setBufferSize(DEFAULT_BUFFER_SIZE);
            response.setContentType(contentType);
            response.setHeader("Content-Length", String.valueOf(fileResume.length()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileResume.getName() + "\"");
            // Prepare streams.
            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                // Open streams.
                input = new BufferedInputStream(new FileInputStream(fileResume), DEFAULT_BUFFER_SIZE);
                output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

                // Write file contents to response.
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            } finally {
                // Gently close streams.

                output.close();
                input.close();
                fileResume.delete();
            }
        }



        }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String description = request.getParameter("description");
        //Retrieves <input type="text" name="description">
        Part filePart = request.getPart("fileResume");
        // Retrieves <input type="file" name="file">
        //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        r = streamSerializable.doReade(fileContent);
        SQL_STORAGE.save(r);
        response.sendRedirect("resume?uuid=" + r.getUuid());
    }


}
