<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mes évaluations</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
        .card { background: #1a1d24; border: 1px solid #262a33; border-radius: 12px; }
        .input-dark { background: #12141a; border: 1px solid #2a2e38; color: #e5e7eb; border-radius: 6px; padding: 6px 10px; }
        .btn-teal { background: #00babc; color: white; border-radius: 6px; padding: 6px 14px; font-size: 14px; font-weight: 600; }
        .btn-teal:hover { background: #009da0; }
    </style>
</head>
<body class="text-gray-200 font-sans">
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
    <main class="max-w-7xl mx-auto mt-6 p-4">
        <c:if test="${not empty sessionScope.message}">
            <div class="bg-green-900/40 text-green-400 border border-green-800 p-3 rounded-lg mb-4 text-sm">${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>

        <h2 class="text-2xl font-semibold mb-4 text-white">Évaluations en attente (à passer)</h2>
        <div class="card p-4 mb-8">
            <c:choose>
                <c:when test="${not empty pendingEvaluations}">
                    <ul class="divide-y divide-[#262a33]">
                        <c:forEach var="eval" items="${pendingEvaluations}">
                            <li class="py-2 flex justify-between text-sm">
                                <span>Projet #${eval.projectId}</span>
                                <span class="text-gray-500"><fmt:formatDate value="${eval.scheduledDate}" pattern="dd/MM/yyyy HH:mm"/></span>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-sm">Aucune évaluation en attente.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <h2 class="text-2xl font-semibold mb-4 text-white">Évaluations que je dois donner</h2>
        <div class="card p-4">
            <c:choose>
                <c:when test="${not empty toEvaluate}">
                    <c:forEach var="ev" items="${toEvaluate}">
                        <form action="${pageContext.request.contextPath}/evaluations" method="post" class="flex flex-wrap gap-2 items-center py-3 border-b border-[#262a33]">
                            <input type="hidden" name="evalId" value="${ev.id}" />
                            <span class="text-sm w-40">Étudiant #${ev.userId} — Projet #${ev.projectId}</span>
                            <input class="input-dark w-20" type="number" name="finalGrade" placeholder="Note" min="0" max="100" required />
                            <label class="text-xs flex items-center gap-1"><input type="checkbox" name="passed" /> Réussi</label>
                            <input class="input-dark flex-1" type="text" name="comment" placeholder="Commentaire" />
                            <button type="submit" class="btn-teal">Soumettre</button>
                        </form>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-sm">Aucune évaluation à donner pour le moment.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</body>
</html>