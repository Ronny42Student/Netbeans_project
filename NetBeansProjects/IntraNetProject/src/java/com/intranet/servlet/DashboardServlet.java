package com.intranet.servlet;

import com.intranet.dao.EventDAO;
import com.intranet.dao.EvaluationDAO;
import com.intranet.dao.LogtimeDAO;
import com.intranet.dao.ProgressDAO;
import com.intranet.dao.ProjectDAO;
import com.intranet.model.Event;
import com.intranet.model.Evaluation;
import com.intranet.model.Project;
import com.intranet.model.User;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final EventDAO eventDAO = new EventDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final LogtimeDAO logtimeDAO = new LogtimeDAO();
    private final EvaluationDAO evaluationDAO = new EvaluationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Liste des projets pour les milestones
        List<Project> projects = projectDAO.getAllProjects();
        int totalProjects = projects.size();

        // Progression
        int completed = progressDAO.getCompletedCountForUser(user.getId());

        // Pourcentage
        double percent = totalProjects == 0 ? 0 : (double) completed / totalProjects * 100;

        // Logtime du mois
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        double totalHours = logtimeDAO.getTotalHoursForMonth(user.getId(), month, year);
        double maxHours = 16.0;

        // Événements à venir
        List<Event> upcomingEvents = eventDAO.getUpcomingEvents(5);

        // Évaluations en attente
        List<Evaluation> pendingEvals = evaluationDAO.getPendingEvaluationsForUser(user.getId());

        List<com.intranet.model.Progress> myProgress = progressDAO.getProgressByUser(user.getId());

        // Mise en attributs
        req.setAttribute("projects", projects);
        req.setAttribute("percent", Math.round(percent));
        req.setAttribute("completedCount", completed);
        req.setAttribute("totalProjects", totalProjects);
        req.setAttribute("totalHours", totalHours);
        req.setAttribute("maxHours", maxHours);
        req.setAttribute("events", upcomingEvents);
        req.setAttribute("pendingEvals", pendingEvals);
        req.setAttribute("user", user);
        req.setAttribute("myProgress", myProgress);

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}