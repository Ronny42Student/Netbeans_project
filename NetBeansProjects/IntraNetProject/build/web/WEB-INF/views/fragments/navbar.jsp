<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav style="background:#0f1115;border-bottom:1px solid #262a33;" class="px-6 py-3">
    <div class="max-w-7xl mx-auto flex items-center justify-between">
        <a href="${pageContext.request.contextPath}/dashboard" class="text-white font-bold text-lg tracking-wider">
            42<span class="text-teal-400">.</span>
        </a>
        <div class="flex gap-6 text-sm text-gray-400">
            <a href="${pageContext.request.contextPath}/dashboard" class="hover:text-teal-400">Dashboard</a>
            <a href="${pageContext.request.contextPath}/projects" class="hover:text-teal-400">Projets</a>
            <a href="${pageContext.request.contextPath}/logtime" class="hover:text-teal-400">Logtime</a>
            <a href="${pageContext.request.contextPath}/calendar" class="hover:text-teal-400">Agenda</a>
            <a href="${pageContext.request.contextPath}/evaluations" class="hover:text-teal-400">Évaluations</a>
            <a href="${pageContext.request.contextPath}/profile" class="hover:text-teal-400">Profil</a>
            <c:if test="${sessionScope.loggedUser.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin" class="text-yellow-400 hover:text-yellow-300">Admin</a>
            </c:if>
        </div>
        <div class="flex items-center gap-4">
            <span class="text-gray-300 text-sm">${sessionScope.loggedUser.username}</span>
            <a href="${pageContext.request.contextPath}/logout" class="text-red-400 hover:text-red-300 text-sm">Déconnexion</a>
        </div>
    </div>
</nav>