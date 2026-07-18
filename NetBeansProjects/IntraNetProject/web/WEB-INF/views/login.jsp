<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Intranet - Connexion</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen font-sans">

    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        
        <div class="text-center mb-8">
            <h1 class="text-3xl font-bold text-gray-800">Intranet</h1>
            <p class="text-gray-500 text-sm mt-2">Connectez-vous à votre espace</p>
        </div>

        <%-- Gestion des alertes (Erreur ou Succès d'inscription) --%>
        <c:if test="${not empty error}">
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded" role="alert">
                <p>${error}</p>
            </div>
        </c:if>
        <c:if test="${param.success == 'true'}">
            <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6 rounded" role="alert">
                <p>Inscription réussie ! Vous pouvez maintenant vous connecter.</p>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST" class="space-y-6">
            
            <div>
                <label for="username" class="block text-sm font-medium text-gray-700">Nom d'utilisateur</label>
                <input type="text" id="username" name="username" required 
                       class="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-sm shadow-sm placeholder-gray-400
                              focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
            </div>

            <div>
                <label for="password" class="block text-sm font-medium text-gray-700">Mot de passe</label>
                <input type="password" id="password" name="password" required 
                       class="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-sm shadow-sm placeholder-gray-400
                              focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
            </div>

            <div>
                <button type="submit" 
                        class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transition duration-150 ease-in-out">
                    Se connecter
                </button>
            </div>
        </form>

        <div class="mt-6 text-center">
            <p class="text-sm text-gray-600">
                Pas encore de compte ? 
                <a href="${pageContext.request.contextPath}/register" class="font-medium text-gray-900 hover:text-blue-600 transition duration-150 ease-in-out">
                    Créer un compte
                </a>
            </p>
        </div>
    </div>

</body>
</html>