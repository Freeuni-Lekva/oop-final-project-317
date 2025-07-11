<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Created</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 flex items-center justify-center min-h-screen p-6">
<div class="bg-white rounded-2xl border border-gray-200 p-10 max-w-lg w-full text-center">
    <h1 class="text-3xl font-bold text-slate-700 mb-4">Quiz successfully created!</h1>
    <p class="text-slate-600 mb-6">You can now share it with others or start taking it yourself.</p>
    <div class="space-x-4">
        <a href="index.jsp" class="px-6 py-3 bg-blue-600 text-white rounded-xl font-medium hover:bg-blue-700">Go Home</a>
        <% Long newQuizId = (Long) session.getAttribute("currentQuizId"); %>
        <a href="<%= request.getContextPath() %>/QuizSummary?quizId=<%= newQuizId %>" class="px-6 py-3 bg-gray-100 border border-gray-200 rounded-xl font-medium text-slate-700 hover:bg-gray-200">View Quiz Page</a>
    </div>
</div>
</body>
</html> 