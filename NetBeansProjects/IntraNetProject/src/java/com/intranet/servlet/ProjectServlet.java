package com.intranet.servlet;

import com.intranet.dao.ProjectDAO;
import com.intranet.dao.ProgressDAO;
import com.intranet.model.Project;
import com.intranet.model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Project> projects = projectDAO.getAllProjects();
        req.setAttribute("projects", projects);

        List<com.intranet.model.Progress> myProgress = progressDAO.getProgressByUser(user.getId());
        java.util.Set<Integer> enrolledProjectIds = new java.util.HashSet<>();
        for (com.intranet.model.Progress p : myProgress) {
            enrolledProjectIds.add(p.getProjectId());
        }
        req.setAttribute("enrolledProjectIds", enrolledProjectIds);

        req.getRequestDispatcher("/WEB-INF/views/projects.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Inscription à un projet
        String action = req.getParameter("action");
        if ("enroll".equals(action)) {
            int projectId = Integer.parseInt(req.getParameter("projectId"));
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("loggedUser");
            if (user != null) {
                com.intranet.dao.ProjectDAO pdao = new com.intranet.dao.ProjectDAO();
                com.intranet.model.Project proj = pdao.getProjectById(projectId);
                int deadlineDays = proj != null ? proj.getDeadlineDays() : 15;
                boolean success = progressDAO.enrollUser(user.getId(), projectId, deadlineDays);
                if (success) {
                    req.getSession().setAttribute("message", "Inscription au projet réussie !");
                } else {
                    req.getSession().setAttribute("error", "Vous êtes déjà inscrit à ce projet.");
                }
            }
            resp.sendRedirect(req.getContextPath() + "/projects");
        }
        else if ("requestEvaluation".equals(action)) {
            int projectId = Integer.parseInt(req.getParameter("projectId"));
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("loggedUser");
            if (user != null) {
                com.intranet.model.Evaluation eval = new com.intranet.model.Evaluation();
                eval.setProjectId(projectId);
                eval.setUserId(user.getId());
                eval.setStatus("PENDING");
                eval.setScheduledDate(new java.util.Date());
                boolean success = new com.intranet.dao.EvaluationDAO().addEvaluation(eval);
                session.setAttribute(success ? "message" : "error",
                    success ? "Demande d'évaluation envoyée !" : "Erreur lors de la demande.");
            }
            resp.sendRedirect(req.getContextPath() + "/projects");
        }
        else if ("unenroll".equals(action)) {
            int projectId = Integer.parseInt(req.getParameter("projectId"));
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("loggedUser");
            if (user != null) {
                boolean success = new com.intranet.dao.ProgressDAO().removeEnrollment(user.getId(), projectId);
                session.setAttribute(success ? "message" : "error",
                    success ? "Désinscription effectuée." : "Erreur lors de la désinscription.");
            }
            resp.sendRedirect(req.getContextPath() + "/projects");
        }
    }
}