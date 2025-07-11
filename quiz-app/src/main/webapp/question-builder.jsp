<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Question</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-6">
<div class="max-w-3xl w-full bg-white rounded-2xl border border-gray-200 p-8">
    <h1 class="text-2xl font-bold text-slate-700 mb-2 text-center">
        Question <%= request.getAttribute("currentIndex") %> / <%= request.getAttribute("totalQuestions") %>
    </h1>
    <p class="text-slate-600 text-center mb-6">Type: <%= request.getAttribute("questionType") %></p>

    <form action="question-builder" method="post" class="space-y-4">
        <input type="hidden" name="currentIndex" value="<%= request.getAttribute("currentIndex") %>"/>
        <input type="hidden" name="questionType" value="<%= request.getAttribute("questionType") %>"/>

        <!-- Common field -->
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">Question Prompt *</label>
            <textarea name="prompt" rows="3" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3"></textarea>
        </div>

        <% String type = (String) request.getAttribute("questionType"); %>
        <% if ("MultipleChoice".equals(type)) { %>
            <!-- four options, radio correct -->
            <% for(int i=1;i<=4;i++){ %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Option <%= i %></label>
                <input type="text" name="option<%= i %>" class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" />
            </div>
            <% } %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Correct Option (1-4)</label>
                <input type="number" min="1" max="4" name="answer" required class="w-24 bg-gray-50 border border-gray-200 rounded-xl px-4 py-2" />
            </div>
        <% } else if ("FillInBlank".equals(type)) { %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Correct Answer</label>
                <input type="text" name="answer" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" />
            </div>
        <% } else { %>
            <!-- Placeholder for other types -->
            <p class="text-slate-500">Custom fields for <%= type %> can be added later.</p>
        <% } %>

        <div class="flex justify-end">
            <button type="submit" class="px-6 py-3 bg-blue-600 text-white rounded-xl font-medium hover:bg-blue-700">Save &amp; Continue</button>
        </div>
    </form>
</div>
</body>
</html> 