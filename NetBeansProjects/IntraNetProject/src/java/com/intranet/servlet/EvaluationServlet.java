package com.intranet.servlet;

import com.intranet.dao.EvaluationDAO;
import com.intranet.model.Evaluation;
import com.intranet.model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/evaluations", "/evaluations/mine-to-give"})
public class EvaluationServlet extends HttpServlet {
    private final EvaluationDAO evaluationDAO = new EvaluationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        List<Evaluation> pending = evaluationDAO.getPendingEvaluationsForUser(user.getId());
        req.setAttribute("pendingEvaluations", pending);

        List<Evaluation> allEvals = evaluationDAO.getAllEvaluations();
        List<Evaluation> toEvaluate = allEvals.stream()
                .filter(e -> e.getEvaluatorId() != null && e.getEvaluatorId() == user.getId() && "PENDING".equals(e.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        req.setAttribute("toEvaluate", toEvaluate);

        req.getRequestDispatcher("/WEB-INF/views/evaluations.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int evalId = Integer.parseInt(req.getParameter("evalId"));
        int finalGrade = Integer.parseInt(req.getParameter("finalGrade"));
        boolean passed = req.getParameter("passed") != null;
        String comments = req.getParameter("comments");

        Evaluation eval = evaluationDAO.getEvaluationById(evalId);
        boolean ok = evaluationDAO.submitEvaluationResult(evalId, finalGrade, passed, comments);

        if (ok && eval != null) {
            com.intranet.dao.ProgressDAO progressDAO = new com.intranet.dao.ProgressDAO();
            if (passed) {
                progressDAO.completeProject(eval.getUserId(), eval.getProjectId(), finalGrade);
            } else {
                com.intranet.dao.ProjectDAO projectDAO = new com.intranet.dao.ProjectDAO();
                com.intranet.model.Project proj = projectDAO.getProjectById(eval.getProjectId());
                int deadlineDays = proj != null ? proj.getDeadlineDays() : 15;
                progressDAO.markFailed(eval.getUserId(), eval.getProjectId(), 0);
                progressDAO.retryProject(eval.getUserId(), eval.getProjectId(), deadlineDays);
            }
        }

        session.setAttribute(ok ? "message" : "error",
            ok ? (passed ? "Évaluation validée — projet réussi !" : "Évaluation soumise — nouvel essai accordé.") : "Erreur.");
        resp.sendRedirect(req.getContextPath() + "/evaluations");
    }
}