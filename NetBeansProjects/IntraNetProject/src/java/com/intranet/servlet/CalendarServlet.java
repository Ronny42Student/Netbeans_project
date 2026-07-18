package com.intranet.servlet;

import com.intranet.dao.EventDAO;
import com.intranet.model.Event;
import com.intranet.model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        List<Event> allEvents = eventDAO.getUpcomingEvents(100);
        req.setAttribute("events", allEvents);
        List<Integer> registeredIds = eventDAO.getRegisteredEventIds(user.getId());
        req.setAttribute("registeredIds", registeredIds);
        req.getRequestDispatcher("/WEB-INF/views/calendar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String action = req.getParameter("action");
        int eventId = Integer.parseInt(req.getParameter("eventId"));
        boolean success;
        if ("register".equals(action)) {
            success = eventDAO.registerToEvent(user.getId(), eventId);
            session.setAttribute(success ? "message" : "error", success ? "Inscription à l'événement réussie." : "Déjà inscrit ou erreur.");
        } else {
            success = eventDAO.unregisterFromEvent(user.getId(), eventId);
            session.setAttribute(success ? "message" : "error", success ? "Désinscription effectuée." : "Erreur.");
        }
        resp.sendRedirect(req.getContextPath() + "/calendar");
    }
}