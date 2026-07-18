<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr" class="h-full">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Intranet - Connexion</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            background: url('${pageContext.request.contextPath}/assets/img/bkgrnd.jpg'); 
            background-size: cover;
            height: 100vh;
        }
    </style>
</head>
<body class="flex items-center justify-center min-h-screen font-sans">

    <div class="w-full max-w-sm px-6">
        <div class="text-center mb-10">
            <h1 class="text-4xl font-light text-gray-800 tracking-widest"><strong>INTRANET</strong></h1>
        </div>

        <c:if test="${not empty error}">
            <div class="text-red-500 text-center mb-4 text-sm">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST" class="space-y-6">
            <div>
                <input type="text" id="username" name="username" placeholder="Login or email" required 
                       class="w-full bg-transparent border-b border-gray-600 py-2 text-white placeholder-gray-500 focus:outline-none focus:border-teal-500">
            </div>

            <div>
                <input type="password" id="password" name="password" placeholder="Password" required 
                       class="w-full bg-transparent border-b border-gray-600 py-2 text-white placeholder-gray-500 focus:outline-none focus:border-teal-500">
            </div>

            <div class="flex items-center text-gray-500 text-sm">
                <input type="checkbox" id="remember" class="mr-2 bg-transparent border-gray-600">
                <label for="remember">Remember me</label>
            </div>

            <button type="submit" 
                    class="w-full py-3 bg-[#00babc] hover:bg-[#009da0] text-white font-bold uppercase tracking-wider transition duration-150">
                SIGN IN
            </button>
        </form>

        <div class="mt-6 text-center text-sm text-gray-500 space-y-2">
            <p>Forgot or change your password?</p>
            <p>
                Pas encore de compte ? 
                <a href="${pageContext.request.contextPath}/register" class="text-teal-500 hover:underline">Créer un compte</a>
            </p>
        </div>
    </div>
</body>
</html>