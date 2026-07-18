<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Intranet - Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
        .progress-bar { background: #00babc; height: 8px; border-radius: 4px; }
        .card { background: #1a1d24; border: 1px solid #262a33; border-radius: 12px; }
        .badge { border-radius: 9999px; padding: 2px 10px; font-size: 12px; font-weight: 600; }
        .badge-completed { background: #14532d; color: #4ade80; }
        .badge-failed { background: #450a0a; color: #f87171; }
        .badge-progress { background: #1e3a3a; color: #2dd4bf; }
    </style>
</head>
<body class="text-gray-200 font-sans">
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
    <main class="max-w-7xl mx-auto mt-6 p-4">
        <div class="flex flex-wrap justify-between items-center mb-6">
            <div>
                <h2 class="text-2xl font-semibold text-white">${user.username}</h2>
                <p class="text-gray-500">${user.email}</p>
                <p class="text-sm text-gray-500">Niveau : ${user.currentLevel} - Campus : ${user.campusLocation}</p>
            </div>
            <div class="bg-teal-500 text-white text-2xl font-bold px-6 py-2 rounded-full">
                ${percent}%
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div class="card p-4">
                <h3 class="text-gray-500 text-sm uppercase tracking-wider">Common Core ETA</h3>
                <p class="text-2xl font-bold text-white">
                    <fmt:formatDate value="${user.etaDate}" pattern="dd/MM/yyyy" />
                </p>
                <div class="mt-2 flex gap-2 flex-wrap">
                    <c:forEach var="project" items="${projects}" varStatus="status">
                        <span class="bg-[#262a33] text-gray-300 px-3 py-1 rounded-full text-sm">M.${status.index + 1}</span>
                    </c:forEach>
                </div>
            </div>
            <div class="card p-4">
                <h3 class="text-gray-500 text-sm uppercase tracking-wider">Progression</h3>
                <div class="w-full bg-[#262a33] rounded-full h-2.5 mt-2">
                    <div class="progress-bar" style="width: ${percent}%"></div>
                </div>
                <p class="mt-2 text-sm text-gray-500">${completedCount} / ${totalProjects} projets terminés</p>
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div class="card p-4">
                <h3 class="text-gray-500 text-sm uppercase tracking-wider">AGENDA</h3>
                <ul class="mt-2 divide-y divide-[#262a33]">
                    <c:forEach var="evt" items="${events}" varStatus="i" end="1">
                        <li class="py-2 flex justify-between text-sm">
                            <span><fmt:formatDate value="${evt.eventDate}" pattern="EEE d MMM" /> ${evt.title}</span>
                            <span class="text-gray-500">${evt.location}</span>
                        </li>
                    </c:forEach>
                </ul>
                <a href="${pageContext.request.contextPath}/calendar" class="text-teal-400 text-sm mt-2 inline-block">Voir tout →</a>
            </div>
            <div class="card p-4">
                <h3 class="text-gray-500 text-sm uppercase tracking-wider">ALL EVENTS</h3>
                <ul class="mt-2 divide-y divide-[#262a33]">
                    <c:forEach var="evt" items="${events}" varStatus="i" end="2">
                        <li class="py-2 flex justify-between text-sm">
                            <span><fmt:formatDate value="${evt.eventDate}" pattern="HH:mm" /> ${evt.title}</span>
                            <span class="text-gray-500">${evt.location}</span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div class="card p-4">
                <h3 class="text-gray-500 text-sm uppercase tracking-wider">ÉVALUATIONS EN ATTENTE</h3>
                <c:choose>
                    <c:when test="${not empty pendingEvals}">
                        <ul>
                            <c:forEach var="eval" items="${pendingEvals}">
                                <li class="py-1 text-sm">${eval.projectId} - <fmt:formatDate value="${eval.scheduledDate}" pattern="dd/MM/yyyy HH:mm"/></li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p class="text-gray-500 text-sm mt-2">Aucune évaluation en attente</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card p-4">
                <h3 class="text-gray-500 text-sm uppercase tracking-wider">LOGTIME (mois en cours)</h3>
                <p class="text-lg font-bold text-white"><fmt:formatNumber value="${totalHours}" pattern="#0.00"/> / ${maxHours}</p>
                <div class="w-full bg-[#262a33] rounded-full h-2.5 mt-1">
                    <div class="progress-bar" style="width: ${(totalHours/maxHours)*100}%"></div>
                </div>
                <a href="${pageContext.request.contextPath}/logtime" class="text-teal-400 text-sm mt-2 inline-block">Voir détail →</a>
            </div>
        </div>

        <div class="card p-4">
            <h3 class="text-gray-500 text-sm uppercase tracking-wider">MES PROJETS EN COURS</h3>
            <c:choose>
                <c:when test="${not empty myProgress}">
                    <ul class="mt-2 divide-y divide-[#262a33]">
                        <c:forEach var="pr" items="${myProgress}">
                            <li class="py-2 flex justify-between items-center text-sm">
                                <span>${pr.projectName} <span class="text-gray-500">(tentative #${pr.attemptNumber})</span></span>
                                <span class="badge
                                    <c:choose>
                                        <c:when test="${pr.status == 'COMPLETED'}">badge-completed</c:when>
                                        <c:when test="${pr.status == 'FAILED'}">badge-failed</c:when>
                                        <c:otherwise>badge-progress</c:otherwise>
                                    </c:choose>">${pr.status}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-sm mt-2">Aucun projet en cours pour le moment.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</body>
</html>