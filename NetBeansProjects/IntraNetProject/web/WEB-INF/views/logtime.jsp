<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.intranet.model.Logtime" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Logtime</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
        .card { background: #1a1d24; border: 1px solid #262a33; border-radius: 12px; }
        .progress-bar { background: #00babc; height: 8px; border-radius: 4px; }
        .input-dark { background: #12141a; border: 1px solid #2a2e38; color: #e5e7eb; border-radius: 6px; padding: 6px 10px; }
        .input-dark:focus { outline: none; border-color: #00babc; }
        .btn-teal { background: #00babc; color: white; border-radius: 6px; padding: 6px 14px; font-size: 14px; font-weight: 600; }
        .btn-teal:hover { background: #009da0; }
    </style>
</head>
<body class="text-gray-200 font-sans">
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
    <main class="max-w-7xl mx-auto mt-6 p-4">
        <c:if test="${not empty sessionScope.message}">
            <div class="bg-green-900/40 text-green-400 border border-green-800 p-2 rounded-lg mb-4 text-sm">${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="bg-red-900/40 text-red-400 border border-red-800 p-2 rounded-lg mb-4 text-sm">${sessionScope.error}</div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <h2 class="text-2xl font-semibold mb-1 text-white">Logtime – ${month}/${year}</h2>
        <p class="text-gray-500 mb-4">Total : <fmt:formatNumber value="${total}" pattern="#0.00"/> heures</p>

        <!-- Formulaire d'ajout -->
        <div class="card p-4 mb-6">
            <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-3">Ajouter des heures</h3>
            <form action="${pageContext.request.contextPath}/logtime" method="post" class="flex flex-wrap gap-4 items-end">
                <div>
                    <label class="block text-sm text-gray-400 mb-1">Date</label>
                    <input type="date" name="logDate" required class="input-dark" />
                </div>
                <div>
                    <label class="block text-sm text-gray-400 mb-1">Heures</label>
                    <input type="number" step="0.5" name="hours" required class="input-dark w-24" />
                </div>
                <div>
                    <label class="block text-sm text-gray-400 mb-1">Description</label>
                    <input type="text" name="description" class="input-dark w-56" />
                </div>
                <button type="submit" class="btn-teal">Ajouter</button>
            </form>
        </div>

        <!-- Tableau historique -->
        <div class="card p-4">
            <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-3">Historique du mois</h3>
            <table class="w-full text-sm">
                <thead class="text-gray-500 text-left border-b border-[#262a33]">
                    <tr>
                        <th class="pb-2 pr-3">Date</th>
                        <th class="pb-2 pr-3">Heures</th>
                        <th class="pb-2">Description</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="entry" items="${logtimes}">
                        <tr class="border-b border-[#1f232b]">
                            <td class="py-2 pr-3"><fmt:formatDate value="${entry.logDate}" pattern="dd/MM/yyyy"/></td>
                            <td class="py-2 pr-3"><fmt:formatNumber value="${entry.hours}" pattern="#0.00"/></td>
                            <td class="py-2">${entry.description}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty logtimes}">
                        <tr><td colspan="3" class="py-3 text-gray-500">Aucune entrée pour ce mois</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>