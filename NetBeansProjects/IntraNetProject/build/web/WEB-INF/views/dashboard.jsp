<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Intranet - Tableau de bord</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 font-sans">

    <nav class="bg-gray-800 text-white p-4 shadow-md">
        <div class="max-w-7xl mx-auto flex justify-between items-center">
            <h1 class="text-xl font-bold">Intranet 42</h1>
            <div class="flex items-center gap-4">
                <span>Bienvenue, <strong class="text-blue-400">${sessionScope.loggedUser.username}</strong></span>
                <a href="${pageContext.request.contextPath}/logout" class="bg-red-600 hover:bg-red-700 px-4 py-2 rounded text-sm transition">
                    Déconnexion
                </a>
            </div>
        </div>
    </nav>

    <main class="max-w-7xl mx-auto mt-8 p-4">
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Votre espace personnel</h2>
            <p class="text-gray-600">Ceci est la page d'accueil de l'intranet. Seuls les utilisateurs connectés peuvent voir cette page.</p>
            
            <div class="mt-6">
                <p><strong>Votre Email :</strong> ${sessionScope.loggedUser.email}</p>
                <p><strong>Votre ID :</strong> ${sessionScope.loggedUser.id}</p>
            </div>
        </div>
    </main>

</body>
</html>
