package com.intranet.servlet;

import com.intranet.dao.LogtimeDAO;
import com.intranet.model.Logtime;
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

@WebServlet("/logtime")
public class LogtimeServlet extends HttpServlet {
    private final LogtimeDAO logtimeDAO = new LogtimeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // Par défaut le mois courant
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        int year = cal.get(java.util.Calendar.YEAR);
        List<Logtime> logtimes = logtimeDAO.getLogtimesByUser(user.getId(), month, year);
        double total = logtimeDAO.getTotalHoursForMonth(user.getId(), month, year);
        req.setAttribute("logtimes", logtimes);
        req.setAttribute("total", total);
        req.setAttribute("month", month);
        req.setAttribute("year", year);
        req.getRequestDispatcher("/WEB-INF/views/logtime.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String dateStr = req.getParameter("logDate");
        String hoursStr = req.getParameter("hours");
        String description = req.getParameter("description");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date logDate = sdf.parse(dateStr);
            double hours = Double.parseDouble(hoursStr);
            Logtime lt = new Logtime();
            lt.setUserId(user.getId());
            lt.setLogDate(logDate);
            lt.setHours(hours);
            lt.setDescription(description);
            boolean success = logtimeDAO.addLogtime(lt);
            if (success) {
                session.setAttribute("message", "Heures ajoutées !");
            } else {
                session.setAttribute("error", "Erreur lors de l'ajout.");
            }
        } catch (ParseException | NumberFormatException e) {
            session.setAttribute("error", "Format de date ou nombre invalide.");
        }
        resp.sendRedirect(req.getContextPath() + "/logtime");
    }
}