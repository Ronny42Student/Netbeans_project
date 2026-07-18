package com.intranet.servlet;

import com.intranet.dao.EvaluationDAO;
import com.intranet.dao.ProgressDAO;
import com.intranet.dao.ProjectDAO;
import com.intranet.dao.UserDAO;
import com.intranet.model.Evaluation;
import com.intranet.model.Progress;
import com.intranet.model.Project;
import com.intranet.model.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final EvaluationDAO evaluationDAO = new EvaluationDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Project> projects = projectDAO.getAllProjectsAdmin();
        List<Progress> allProgress = progressDAO.getAllProgress();
        List<Evaluation> allEvals = evaluationDAO.getAllEvaluations();
        List<User> allUsers = userDAO.getAllUsers();

        req.setAttribute("projects", projects);
        req.setAttribute("allProgress", allProgress);
        req.setAttribute("allEvals", allEvals);
        req.setAttribute("allUsers", allUsers);

        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        HttpSession session = req.getSession(false);

        try {
            switch (action) {
                case "createProject": {
                    Project p = new Project();
                    p.setName(req.getParameter("name"));
                    p.setSlug(req.getParameter("slug"));
                    p.setDescription(req.getParameter("description"));
                    p.setDeadlineDays(Integer.parseInt(req.getParameter("deadlineDays")));
                    p.setMaxRetries(Integer.parseInt(req.getParameter("maxRetries")));
                    p.setMinGrade(Integer.parseInt(req.getParameter("minGrade")));
                    p.setActive(true);
                    boolean ok = projectDAO.createProject(p);
                    session.setAttribute(ok ? "message" : "error", ok ? "Projet créé." : "Erreur création projet.");
                    break;
                }
                case "updateProject": {
                    Project p = new Project();
                    p.setId(Integer.parseInt(req.getParameter("projectId")));
                    p.setName(req.getParameter("name"));
                    p.setSlug(req.getParameter("slug"));
                    p.setDescription(req.getParameter("description"));
                    p.setDeadlineDays(Integer.parseInt(req.getParameter("deadlineDays")));
                    p.setMaxRetries(Integer.parseInt(req.getParameter("maxRetries")));
                    p.setMinGrade(Integer.parseInt(req.getParameter("minGrade")));
                    p.setActive(req.getParameter("active") != null);
                    boolean ok = projectDAO.updateProject(p);
                    session.setAttribute(ok ? "message" : "error", ok ? "Projet modifié." : "Erreur modification.");
                    break;
                }
                case "deactivateProject": {
                    int id = Integer.parseInt(req.getParameter("projectId"));
                    boolean ok = projectDAO.deactivateProject(id);
                    session.setAttribute(ok ? "message" : "error", ok ? "Projet désactivé." : "Erreur.");
                    break;
                }
                case "deleteProject": {
                    int id = Integer.parseInt(req.getParameter("projectId"));
                    boolean ok = projectDAO.deleteProjectPermanently(id);
                    session.setAttribute(ok ? "message" : "error", ok ? "Projet supprimé." : "Suppression impossible (progressions liées).");
                    break;
                }
                case "markFailed": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    int projectId = Integer.parseInt(req.getParameter("projectId"));
                    double penalty = Double.parseDouble(req.getParameter("penaltyPercent"));
                    boolean ok = progressDAO.markFailed(userId, projectId, penalty);
                    session.setAttribute(ok ? "message" : "error", ok ? "Échec enregistré." : "Erreur.");
                    break;
                }
                case "retryProject": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    int projectId = Integer.parseInt(req.getParameter("projectId"));
                    Project proj = projectDAO.getProjectById(projectId);
                    int deadlineDays = proj != null ? proj.getDeadlineDays() : 15;
                    boolean ok = progressDAO.retryProject(userId, projectId, deadlineDays);
                    session.setAttribute(ok ? "message" : "error", ok ? "Nouvelle tentative accordée." : "Erreur.");
                    break;
                }
                case "completeProject": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    int projectId = Integer.parseInt(req.getParameter("projectId"));
                    int grade = Integer.parseInt(req.getParameter("grade"));
                    boolean ok = progressDAO.completeProject(userId, projectId, grade);
                    session.setAttribute(ok ? "message" : "error", ok ? "Projet validé." : "Erreur.");
                    break;
                }
                case "removeEnrollment": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    int projectId = Integer.parseInt(req.getParameter("projectId"));
                    boolean ok = progressDAO.removeEnrollment(userId, projectId);
                    session.setAttribute(ok ? "message" : "error", ok ? "Inscription retirée." : "Erreur.");
                    break;
                }
                case "scheduleEvaluation": {
                    Evaluation eval = new Evaluation();
                    eval.setProjectId(Integer.parseInt(req.getParameter("projectId")));
                    eval.setUserId(Integer.parseInt(req.getParameter("userId")));
                    String evaluatorStr = req.getParameter("evaluatorId");
                    if (evaluatorStr != null && !evaluatorStr.isEmpty()) {
                        eval.setEvaluatorId(Integer.parseInt(evaluatorStr));
                    }
                    eval.setStatus("PENDING");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    Date scheduled = sdf.parse(req.getParameter("scheduledDate"));
                    eval.setScheduledDate(scheduled);
                    boolean ok = evaluationDAO.addEvaluation(eval);
                    session.setAttribute(ok ? "message" : "error", ok ? "Évaluation planifiée." : "Erreur.");
                    break;
                }
                case "submitEvaluationResult": {
                    int evalId = Integer.parseInt(req.getParameter("evalId"));
                    int finalGrade = Integer.parseInt(req.getParameter("finalGrade"));
                    boolean passed = req.getParameter("passed") != null;
                    String comments = req.getParameter("comments");
                    Evaluation eval = evaluationDAO.getEvaluationById(evalId);
                    boolean ok = evaluationDAO.submitEvaluationResult(evalId, finalGrade, passed, comments);
                    if (ok && eval != null) {
                        if (passed) {
                            progressDAO.completeProject(eval.getUserId(), eval.getProjectId(), finalGrade);
                        } else {
                            Project proj = projectDAO.getProjectById(eval.getProjectId());
                            int deadlineDays = proj != null ? proj.getDeadlineDays() : 15;
                            progressDAO.markFailed(eval.getUserId(), eval.getProjectId(), 0);
                            progressDAO.retryProject(eval.getUserId(), eval.getProjectId(), deadlineDays);
                        }
                    }
                    session.setAttribute(ok ? "message" : "error", ok ? "Résultat enregistré." : "Erreur.");
                    break;
                }
                case "deleteEvaluation": {
                    int evalId = Integer.parseInt(req.getParameter("evalId"));
                    boolean ok = evaluationDAO.deleteEvaluation(evalId);
                    session.setAttribute(ok ? "message" : "error", ok ? "Évaluation supprimée." : "Erreur.");
                    break;
                }
                case "updateUserRole": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    String role = req.getParameter("role");
                    boolean ok = userDAO.updateUserRole(userId, role);
                    session.setAttribute(ok ? "message" : "error", ok ? "Rôle mis à jour." : "Erreur.");
                    break;
                }
                case "updateUserLevel": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    int level = Integer.parseInt(req.getParameter("level"));
                    boolean ok = userDAO.updateUserLevel(userId, level);
                    session.setAttribute(ok ? "message" : "error", ok ? "Niveau mis à jour." : "Erreur.");
                    break;
                }
                case "deleteUser": {
                    int userId = Integer.parseInt(req.getParameter("userId"));
                    boolean ok = userDAO.deactivateUser(userId);
                    session.setAttribute(ok ? "message" : "error", ok ? "Utilisateur supprimé." : "Erreur.");
                    break;
                }
                default:
                    session.setAttribute("error", "Action inconnue.");
            }
        } catch (ParseException | NumberFormatException e) {
            session.setAttribute("error", "Données invalides : " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin");
    }
}