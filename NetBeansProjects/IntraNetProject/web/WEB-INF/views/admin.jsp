<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Administration</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { background: #0f1115; }
        .card { background: #1a1d24; border: 1px solid #262a33; border-radius: 12px; }
        .badge { border-radius: 9999px; padding: 2px 10px; font-size: 12px; font-weight: 600; }
        .badge-progress { background: #1e3a3a; color: #2dd4bf; }
        .badge-completed { background: #14532d; color: #4ade80; }
        .badge-failed { background: #450a0a; color: #f87171; }
        .badge-pending { background: #422006; color: #fbbf24; }
        .tab-btn.active { background: #00babc; color: white; }
        .input-dark { background: #12141a; border: 1px solid #2a2e38; color: #e5e7eb; border-radius: 6px; padding: 6px 10px; }
        .input-dark:focus { outline: none; border-color: #00babc; }
        .btn-teal { background: #00babc; color: white; border-radius: 6px; padding: 6px 14px; font-size: 14px; font-weight: 600; }
        .btn-teal:hover { background: #009da0; }
        .btn-red { background: #7f1d1d; color: #fecaca; border-radius: 6px; padding: 6px 14px; font-size: 14px; }
        .btn-red:hover { background: #991b1b; }
        .btn-outline { background: transparent; border: 1px solid #2a2e38; color: #9ca3af; border-radius: 6px; padding: 6px 14px; font-size: 14px; }
        .btn-outline:hover { border-color: #00babc; color: #00babc; }
    </style>
</head>
<body class="text-gray-200 font-sans">
    <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
    <main class="max-w-7xl mx-auto mt-6 p-4 pb-20">
        <h2 class="text-2xl font-semibold mb-1 text-white">Panneau d'administration</h2>
        <p class="text-gray-500 text-sm mb-6">Gestion des projets, progressions, évaluations et utilisateurs</p>

        <c:if test="${not empty sessionScope.message}">
            <div class="bg-green-900/40 text-green-400 border border-green-800 p-3 rounded-lg mb-4 text-sm">${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="bg-red-900/40 text-red-400 border border-red-800 p-3 rounded-lg mb-4 text-sm">${sessionScope.error}</div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <!-- Tabs -->
        <div class="flex gap-2 mb-6 flex-wrap" id="tabButtons">
            <button class="tab-btn active px-4 py-2 rounded-lg text-sm font-medium border border-[#262a33]" onclick="showTab('projects', this)">Projets</button>
            <button class="tab-btn px-4 py-2 rounded-lg text-sm font-medium border border-[#262a33] text-gray-400" onclick="showTab('progress', this)">Progressions</button>
            <button class="tab-btn px-4 py-2 rounded-lg text-sm font-medium border border-[#262a33] text-gray-400" onclick="showTab('evals', this)">Évaluations</button>
            <button class="tab-btn px-4 py-2 rounded-lg text-sm font-medium border border-[#262a33] text-gray-400" onclick="showTab('users', this)">Utilisateurs</button>
        </div>

        <!-- TAB: PROJECTS -->
        <div id="tab-projects" class="tab-content">
            <div class="card p-5 mb-6">
                <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Nouveau projet</h3>
                <form action="${pageContext.request.contextPath}/admin" method="post" class="grid grid-cols-1 md:grid-cols-3 gap-3">
                    <input type="hidden" name="action" value="createProject" />
                    <input class="input-dark" type="text" name="name" placeholder="Nom du projet" required />
                    <input class="input-dark" type="text" name="slug" placeholder="Slug" required />
                    <input class="input-dark" type="text" name="description" placeholder="Description" />
                    <input class="input-dark" type="number" name="deadlineDays" placeholder="Délai (jours)" value="15" required />
                    <input class="input-dark" type="number" name="maxRetries" placeholder="Tentatives max" value="3" required />
                    <input class="input-dark" type="number" name="minGrade" placeholder="Note minimale" value="60" required />
                    <button type="submit" class="btn-teal md:col-span-3">+ Créer le projet</button>
                </form>
            </div>

            <div class="card p-5">
                <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Projets existants</h3>
                <div class="overflow-x-auto">
                <table class="w-full text-sm">
                    <thead class="text-gray-500 text-left border-b border-[#262a33]">
                        <tr>
                            <th class="pb-2 pr-3">Nom</th><th class="pb-2 pr-3">Slug</th><th class="pb-2 pr-3">Délai</th>
                            <th class="pb-2 pr-3">Tentatives</th><th class="pb-2 pr-3">Note min</th><th class="pb-2 pr-3">Statut</th><th class="pb-2">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="proj" items="${projects}">
                            <tr class="border-b border-[#1f232b]">
                                <form action="${pageContext.request.contextPath}/admin" method="post">
                                <input type="hidden" name="action" value="updateProject" />
                                <input type="hidden" name="projectId" value="${proj.id}" />
                                <td class="py-2 pr-3"><input class="input-dark w-32" name="name" value="${proj.name}" /></td>
                                <td class="py-2 pr-3"><input class="input-dark w-24" name="slug" value="${proj.slug}" /></td>
                                <td class="py-2 pr-3"><input class="input-dark w-16" type="number" name="deadlineDays" value="${proj.deadlineDays}" /></td>
                                <td class="py-2 pr-3"><input class="input-dark w-14" type="number" name="maxRetries" value="${proj.maxRetries}" /></td>
                                <td class="py-2 pr-3"><input class="input-dark w-16" type="number" name="minGrade" value="${proj.minGrade}" /></td>
                                <td class="py-2 pr-3">
                                    <label class="flex items-center gap-1 text-xs">
                                        <input type="checkbox" name="active" ${proj.active ? 'checked' : ''} /> Actif
                                    </label>
                                </td>
                                <td class="py-2 flex gap-2 flex-wrap">
                                    <button type="submit" class="btn-teal">Enregistrer</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Désactiver ce projet ?');">
                                    <input type="hidden" name="action" value="deactivateProject" />
                                    <input type="hidden" name="projectId" value="${proj.id}" />
                                    <button type="submit" class="btn-outline">Désactiver</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Supprimer définitivement ? Impossible si des étudiants y sont inscrits.');">
                                    <input type="hidden" name="action" value="deleteProject" />
                                    <input type="hidden" name="projectId" value="${proj.id}" />
                                    <button type="submit" class="btn-red">Supprimer</button>
                                </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </div>
            </div>
        </div>

        <!-- TAB: PROGRESS -->
        <div id="tab-progress" class="tab-content hidden">
            <div class="card p-5">
                <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Progressions des étudiants</h3>
                <div class="overflow-x-auto">
                <table class="w-full text-sm">
                    <thead class="text-gray-500 text-left border-b border-[#262a33]">
                        <tr>
                            <th class="pb-2 pr-3">Étudiant</th><th class="pb-2 pr-3">Projet</th><th class="pb-2 pr-3">Statut</th>
                            <th class="pb-2 pr-3">Tentative</th><th class="pb-2 pr-3">Note</th><th class="pb-2 pr-3">Pénalité</th>
                            <th class="pb-2 pr-3">Deadline</th><th class="pb-2">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pr" items="${allProgress}">
                            <tr class="border-b border-[#1f232b]">
                                <td class="py-2 pr-3">${pr.username}</td>
                                <td class="py-2 pr-3">${pr.projectName}</td>
                                <td class="py-2 pr-3">
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${pr.status == 'COMPLETED'}">badge-completed</c:when>
                                            <c:when test="${pr.status == 'FAILED'}">badge-failed</c:when>
                                            <c:otherwise>badge-progress</c:otherwise>
                                        </c:choose>">${pr.status}</span>
                                </td>
                                <td class="py-2 pr-3">#${pr.attemptNumber}</td>
                                <td class="py-2 pr-3">${not empty pr.grade ? pr.grade : '—'}</td>
                                <td class="py-2 pr-3">${pr.penaltyPercent}%</td>
                                <td class="py-2 pr-3"><fmt:formatDate value="${pr.deadline}" pattern="dd/MM/yyyy"/></td>
                                <td class="py-2 flex gap-1 flex-wrap">
                                    <form action="${pageContext.request.contextPath}/admin" method="post" class="flex gap-1">
                                        <input type="hidden" name="action" value="completeProject" />
                                        <input type="hidden" name="userId" value="${pr.userId}" />
                                        <input type="hidden" name="projectId" value="${pr.projectId}" />
                                        <input class="input-dark w-14" type="number" name="grade" placeholder="Note" min="0" max="100" />
                                        <button type="submit" class="btn-teal">Valider</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin" method="post" class="flex gap-1">
                                        <input type="hidden" name="action" value="markFailed" />
                                        <input type="hidden" name="userId" value="${pr.userId}" />
                                        <input type="hidden" name="projectId" value="${pr.projectId}" />
                                        <input class="input-dark w-14" type="number" name="penaltyPercent" placeholder="Pénal.%" value="0" />
                                        <button type="submit" class="btn-red">Échec</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Accorder une nouvelle tentative ?');">
                                        <input type="hidden" name="action" value="retryProject" />
                                        <input type="hidden" name="userId" value="${pr.userId}" />
                                        <input type="hidden" name="projectId" value="${pr.projectId}" />
                                        <button type="submit" class="btn-outline">Retry</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Retirer cette inscription ?');">
                                        <input type="hidden" name="action" value="removeEnrollment" />
                                        <input type="hidden" name="userId" value="${pr.userId}" />
                                        <input type="hidden" name="projectId" value="${pr.projectId}" />
                                        <button type="submit" class="btn-red">Retirer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </div>
            </div>
        </div>

        <!-- TAB: EVALUATIONS -->
        <div id="tab-evals" class="tab-content hidden">
            <div class="card p-5 mb-6">
                <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Planifier une évaluation</h3>
                <form action="${pageContext.request.contextPath}/admin" method="post" class="grid grid-cols-1 md:grid-cols-4 gap-3">
                    <input type="hidden" name="action" value="scheduleEvaluation" />
                    <select name="userId" class="input-dark" required>
                        <option value="">Étudiant...</option>
                        <c:forEach var="u" items="${allUsers}"><option value="${u.id}">${u.username}</option></c:forEach>
                    </select>
                    <select name="projectId" class="input-dark" required>
                        <option value="">Projet...</option>
                        <c:forEach var="p" items="${projects}"><option value="${p.id}">${p.name}</option></c:forEach>
                    </select>
                    <select name="evaluatorId" class="input-dark">
                        <option value="">Évaluateur (optionnel)...</option>
                        <c:forEach var="u" items="${allUsers}"><option value="${u.id}">${u.username}</option></c:forEach>
                    </select>
                    <input class="input-dark" type="datetime-local" name="scheduledDate" required />
                    <button type="submit" class="btn-teal md:col-span-4">+ Planifier</button>
                </form>
            </div>

            <div class="card p-5">
                <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Toutes les évaluations</h3>
                <div class="overflow-x-auto">
                <table class="w-full text-sm">
                    <thead class="text-gray-500 text-left border-b border-[#262a33]">
                        <tr><th class="pb-2 pr-3">Projet</th><th class="pb-2 pr-3">Étudiant</th><th class="pb-2 pr-3">Date</th>
                            <th class="pb-2 pr-3">Statut</th><th class="pb-2 pr-3">Note finale</th><th class="pb-2">Actions</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ev" items="${allEvals}">
                            <tr class="border-b border-[#1f232b]">
                                <td class="py-2 pr-3">#${ev.projectId}</td>
                                <td class="py-2 pr-3">#${ev.userId}</td>
                                <td class="py-2 pr-3"><fmt:formatDate value="${ev.scheduledDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td class="py-2 pr-3">
                                    <span class="badge ${ev.status == 'COMPLETED' ? 'badge-completed' : 'badge-pending'}">${ev.status}</span>
                                </td>
                                <td class="py-2 pr-3">${not empty ev.finalGrade ? ev.finalGrade : '—'}</td>
                                <td class="py-2 flex gap-1 flex-wrap">
                                    <c:if test="${ev.status != 'COMPLETED'}">
                                        <form action="${pageContext.request.contextPath}/admin" method="post" class="flex gap-1 items-center">
                                            <input type="hidden" name="action" value="submitEvaluationResult" />
                                            <input type="hidden" name="evalId" value="${ev.id}" />
                                            <input class="input-dark w-14" type="number" name="finalGrade" placeholder="Note" min="0" max="100" required />
                                            <label class="text-xs flex items-center gap-1"><input type="checkbox" name="passed" /> Réussi</label>
                                            <input class="input-dark w-28" type="text" name="comment" placeholder="Commentaire" />
                                            <button type="submit" class="btn-teal">Soumettre</button>
                                        </form>
                                    </c:if>
                                    <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Supprimer cette évaluation ?');">
                                        <input type="hidden" name="action" value="deleteEvaluation" />
                                        <input type="hidden" name="evalId" value="${ev.id}" />
                                        <button type="submit" class="btn-red">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </div>
            </div>
        </div>

        <!-- TAB: USERS -->
        <div id="tab-users" class="tab-content hidden">
            <div class="card p-5">
                <h3 class="text-sm uppercase tracking-wider text-gray-400 mb-4">Utilisateurs</h3>
                <div class="overflow-x-auto">
                <table class="w-full text-sm">
                    <thead class="text-gray-500 text-left border-b border-[#262a33]">
                        <tr><th class="pb-2 pr-3">Nom</th><th class="pb-2 pr-3">Email</th><th class="pb-2 pr-3">Niveau</th>
                            <th class="pb-2 pr-3">Campus</th><th class="pb-2 pr-3">Rôle</th><th class="pb-2">Actions</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${allUsers}">
                            <tr class="border-b border-[#1f232b]">
                                <td class="py-2 pr-3">${u.username}</td>
                                <td class="py-2 pr-3">${u.email}</td>
                                <td class="py-2 pr-3">
                                    <form action="${pageContext.request.contextPath}/admin" method="post" class="flex gap-1">
                                        <input type="hidden" name="action" value="updateUserLevel" />
                                        <input type="hidden" name="userId" value="${u.id}" />
                                        <input class="input-dark w-14" type="number" name="level" value="${u.currentLevel}" />
                                        <button type="submit" class="btn-outline">OK</button>
                                    </form>
                                </td>
                                <td class="py-2 pr-3">${u.campusLocation}</td>
                                <td class="py-2 pr-3">
                                    <form action="${pageContext.request.contextPath}/admin" method="post" class="flex gap-1">
                                        <input type="hidden" name="action" value="updateUserRole" />
                                        <input type="hidden" name="userId" value="${u.id}" />
                                        <select name="role" class="input-dark">
                                            <option value="USER" ${u.role == 'USER' ? 'selected' : ''}>USER</option>
                                            <option value="ADMIN" ${u.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                                        </select>
                                        <button type="submit" class="btn-outline">OK</button>
                                    </form>
                                </td>
                                <td class="py-2">
                                    <form action="${pageContext.request.contextPath}/admin" method="post" onsubmit="return confirm('Supprimer cet utilisateur ?');">
                                        <input type="hidden" name="action" value="deleteUser" />
                                        <input type="hidden" name="userId" value="${u.id}" />
                                        <button type="submit" class="btn-red">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
    </main>

    <script>
        function showTab(name, btn) {
            document.querySelectorAll('.tab-content').forEach(el => el.classList.add('hidden'));
            document.getElementById('tab-' + name).classList.remove('hidden');
            document.querySelectorAll('#tabButtons .tab-btn').forEach(b => { b.classList.remove('active'); b.classList.add('text-gray-400'); });
            btn.classList.add('active'); btn.classList.remove('text-gray-400');
        }
    </script>
</body>
</html>