<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Intranet - Profil</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
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
        <div class="card p-6 mb-6">
            <h2 class="text-2xl font-semibold mb-4 text-white">Mon profil</h2>
            <div class="flex items-center gap-4 mb-4">
                <img src="${not empty sessionScope.loggedUser.avatarPath ? sessionScope.loggedUser.avatarPath : pageContext.request.contextPath.concat('/assets/img/images.png')}"
                     class="w-16 h-16 rounded-full object-cover border border-[#262a33]" />
                <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data" class="flex items-center gap-2">
                    <input type="file" name="avatar" accept="image/*" class="text-xs text-gray-400" />
                    <button type="submit" class="btn-teal">Changer</button>
                </form>
            </div>
            <p class="text-sm"><strong class="text-gray-400">Nom d'utilisateur :</strong> ${sessionScope.loggedUser.username}</p>
            <p class="text-sm"><strong class="text-gray-400">Email :</strong> ${sessionScope.loggedUser.email}</p>
            <p class="text-sm"><strong class="text-gray-400">Niveau :</strong> ${sessionScope.loggedUser.currentLevel}</p>
            <p class="text-sm"><strong class="text-gray-400">Campus :</strong> ${sessionScope.loggedUser.campusLocation}</p>
            <p class="text-sm"><strong class="text-gray-400">Rôle :</strong> ${sessionScope.loggedUser.role}</p>
            <p class="text-sm"><strong class="text-gray-400">ETA :</strong> <fmt:formatDate value="${sessionScope.loggedUser.etaDate}" pattern="dd/MM/yyyy"/></p>
        </div>

        <div class="card p-6">
            <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Historique des projets</h3>
            <c:choose>
                <c:when test="${not empty myProgress}">
                    <table class="w-full text-sm">
                        <thead class="text-gray-500 text-left border-b border-[#262a33]">
                            <tr>
                                <th class="pb-2">Projet</th>
                                <th class="pb-2">Statut</th>
                                <th class="pb-2">Tentative</th>
                                <th class="pb-2">Note</th>
                                <th class="pb-2">Pénalité</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="pr" items="${myProgress}">
                                <tr class="border-b border-[#1f232b]">
                                    <td class="py-2">${pr.projectName}</td>
                                    <td class="py-2">
                                        <span class="badge
                                            <c:choose>
                                                <c:when test="${pr.status == 'COMPLETED'}">badge-completed</c:when>
                                                <c:when test="${pr.status == 'FAILED'}">badge-failed</c:when>
                                                <c:otherwise>badge-progress</c:otherwise>
                                            </c:choose>">${pr.status}</span>
                                    </td>
                                    <td class="py-2">#${pr.attemptNumber}</td>
                                    <td class="py-2">${not empty pr.grade ? pr.grade : '—'}</td>
                                    <td class="py-2">${pr.penaltyPercent}%</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-sm">Aucun projet dans l'historique.</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="card p-6 mb-6">
            <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Projets terminés</h3>
            <c:set var="hasCompleted" value="false" />
            <ul class="divide-y divide-[#262a33]">
                <c:forEach var="pr" items="${myProgress}">
                    <c:if test="${pr.status == 'COMPLETED'}">
                        <c:set var="hasCompleted" value="true" />
                        <li class="py-2 flex justify-between text-sm">
                            <span>${pr.projectName}</span>
                            <span class="badge badge-completed">Note : ${pr.grade}</span>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
            <c:if test="${hasCompleted == 'false'}">
                <p class="text-gray-500 text-sm">Aucun projet terminé pour le moment.</p>
            </c:if>
        </div>
    </main>
</body>
</html>