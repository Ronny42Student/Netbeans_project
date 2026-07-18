package com.intranet.servlet;

import com.intranet.dao.ProgressDAO;
import com.intranet.dao.UserDAO;
import com.intranet.model.User;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/profile")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)
public class ProfileServlet extends HttpServlet {
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final UserDAO userDAO = new UserDAO();
    private static final String UPLOAD_DIR = "avatars";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        List<com.intranet.model.Progress> myProgress = progressDAO.getProgressByUser(user.getId());
        req.setAttribute("myProgress", myProgress);
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Part filePart = req.getPart("avatar");
        if (filePart != null && filePart.getSize() > 0) {
            String submittedName = filePart.getSubmittedFileName();
            String ext = submittedName.contains(".")
                    ? submittedName.substring(submittedName.lastIndexOf('.'))
                    : "";
            if (!ext.matches("(?i)\\.(jpg|jpeg|png|gif|webp)")) {
                session.setAttribute("error", "Format d'image non supporté.");
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            }

            String appPath = req.getServletContext().getRealPath("/");
            String uploadPath = appPath + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = "user_" + user.getId() + "_" + UUID.randomUUID() + ext;
            filePart.write(uploadPath + File.separator + fileName);

            String relativePath = req.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
            boolean ok = userDAO.updateAvatarPath(user.getId(), relativePath);
            if (ok) {
                user.setAvatarPath(relativePath);
                session.setAttribute("loggedUser", user);
                session.setAttribute("message", "Photo de profil mise à jour !");
            } else {
                session.setAttribute("error", "Erreur lors de l'enregistrement.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}