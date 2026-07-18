<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Intranet - Agenda</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
        .card { background: #1a1d24; border: 1px solid #262a33; border-radius: 12px; }
        .btn-teal { background: #00babc; color: white; border-radius: 6px; padding: 4px 12px; font-size: 13px; font-weight: 600; }
        .btn-teal:hover { background: #009da0; }
        .btn-red { background: #7f1d1d; color: #fecaca; border-radius: 6px; padding: 4px 12px; font-size: 13px; }
    </style>
</head>
<body class="text-gray-200 font-sans">
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
    <main class="max-w-7xl mx-auto mt-6 p-4">
        <c:if test="${not empty sessionScope.message}">
            <div class="bg-green-900/40 text-green-400 border border-green-800 p-2 rounded-lg mb-4 text-sm">${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <h2 class="text-2xl font-semibold mb-4 text-white">Tous les événements</h2>
        <div class="card p-4">
        <table class="w-full text-sm">
            <thead class="text-gray-500 text-left border-b border-[#262a33]">
                <tr><th class="pb-2">Titre</th><th class="pb-2">Type</th><th class="pb-2">Date</th><th class="pb-2">Lieu</th><th class="pb-2">Action</th></tr>
            </thead>
            <tbody>
                <c:forEach var="evt" items="${events}">
                    <tr class="border-b border-[#1f232b]">
                        <td class="py-2">${evt.title}</td>
                        <td class="py-2">${evt.eventType}</td>
                        <td class="py-2"><fmt:formatDate value="${evt.eventDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td class="py-2">${evt.location}</td>
                        <td class="py-2">
                            <c:choose>
                                <c:when test="${registeredIds.contains(evt.id)}">
                                    <form action="${pageContext.request.contextPath}/calendar" method="post">
                                        <input type="hidden" name="eventId" value="${evt.id}" />
                                        <input type="hidden" name="action" value="unregister" />
                                        <button type="submit" class="btn-red">Se désinscrire</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/calendar" method="post">
                                        <input type="hidden" name="eventId" value="${evt.id}" />
                                        <input type="hidden" name="action" value="register" />
                                        <button type="submit" class="btn-teal">S'inscrire</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </div>
    </main>
</body>
</html>