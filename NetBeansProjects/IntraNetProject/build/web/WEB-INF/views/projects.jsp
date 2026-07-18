<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mes Projets</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
        .card { background: #1a1d24; border: 1px solid #262a33; border-radius: 12px; }
        .badge { border-radius: 9999px; padding: 2px 10px; font-size: 12px; font-weight: 600; }
        .badge-progress { background: #1e3a3a; color: #2dd4bf; }
        .badge-completed { background: #14532d; color: #4ade80; }
        .badge-failed { background: #450a0a; color: #f87171; }
        .btn-teal { background: #00babc; color: white; border-radius: 6px; padding: 6px 14px; font-size: 14px; font-weight: 600; }
        .btn-teal:hover { background: #009da0; }
        .btn-outline { background: transparent; border: 1px solid #2a2e38; color: #9ca3af; border-radius: 6px; padding: 6px 14px; font-size: 14px; font-weight: 600; }
        .btn-red { background: red; color: white; border-radius: 6px; padding: 6px 14px; font-size: 14px; font-weight: 600; }
    </style>
</head>
<body class="text-gray-200 font-sans">
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
    <main class="max-w-7xl mx-auto mt-6 p-4">
        <h2 class="text-2xl font-semibold mb-4 text-white">Liste des projets</h2>
        <c:if test="${not empty sessionScope.message}">
            <div class="bg-green-900/40 text-green-400 border border-green-800 p-2 rounded-lg mb-4 text-sm">${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="bg-red-900/40 text-red-400 border border-red-800 p-2 rounded-lg mb-4 text-sm">${sessionScope.error}</div>
            <% session.removeAttribute("error"); %>
        </c:if>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <c:forEach var="proj" items="${projects}">
                <div class="card p-4">
                    <div class="flex justify-between items-start mb-2">
                        <h3 class="text-lg font-bold text-white">${proj.name}</h3>
                        <span class="badge badge-progress">M.min ${proj.minGrade}</span>
                    </div>
                    <p class="text-sm text-gray-500 mb-1">Slug : ${proj.slug}</p>
                    <p class="text-sm text-gray-400 mb-2">${proj.description}</p>
                    <p class="text-xs text-gray-500 mb-3">Délai : ${proj.deadlineDays} jours · Tentatives max : ${proj.maxRetries}</p>
                    <c:choose>
                        <c:when test="${enrolledProjectIds.contains(proj.id)}">
                            <div class="flex gap-2">
                                <form action="${pageContext.request.contextPath}/projects" method="post" class="flex-1">
                                    <input type="hidden" name="action" value="requestEvaluation" />
                                    <input type="hidden" name="projectId" value="${proj.id}" />
                                    <button type="submit" class="btn-outline w-full">Demander éval.</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/projects" method="post" class="flex-1" onsubmit="return confirm('Se désinscrire ?');">
                                    <input type="hidden" name="action" value="unenroll" />
                                    <input type="hidden" name="projectId" value="${proj.id}" />
                                    <button type="submit" class="btn-red w-full">Se désinscrire</button>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/projects" method="post">
                                <input type="hidden" name="action" value="enroll" />
                                <input type="hidden" name="projectId" value="${proj.id}" />
                                <button type="submit" class="btn-teal w-full">S'inscrire</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </main>
</body>
</html>