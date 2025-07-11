<%@ page import="java.util.List" %>
<%@ page import="models.Quiz" %>
<%@ page import="models.Question" %>
<%@ page import="questions.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    @SuppressWarnings("unchecked")
    List<Question> questions = (List<Question>) request.getAttribute("questions");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Take Quiz - <%= quiz != null ? quiz.getTitle() : "Quiz" %></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body class="overflow-hidden bg-gray-50">
<div class="flex h-screen">
    <!-- Sidebar -->
    <div class="w-80 bg-white border-r border-gray-200 p-6 flex flex-col">
        <!-- Logo -->
        <div class="mb-8 cursor-pointer" onclick="window.location.href='<%= request.getContextPath() %>/'">
            <h1 class="text-2xl font-bold text-indigo-600 hover:text-indigo-700 transition-colors">QuizMaster</h1>
            <p class="text-slate-500 text-sm">Your Learning Adventure</p>
        </div>

        <!-- Simple Navigation (trimmed) -->
        <nav class="space-y-1 flex-1">
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='index.jsp'">
                <div class="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l9-9 9 9M4 10v10a1 1 0 001 1h3m10-11l9 9V21a1 1 0 01-1 1h-3m-3-11H7"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Home</span>
            </div>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col bg-gray-50">
        <!-- Top Bar -->
        <div class="bg-white border-b border-gray-200 p-6 flex items-center justify-between">
            <h2 class="text-xl font-semibold text-slate-700">QuizMaster</h2>
            <div>
                <a href="profile" class="px-4 py-2 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">Profile</a>
            </div>
        </div>

        <!-- Quiz Content -->
        <div class="flex-1 overflow-y-auto p-8">
            <div class="max-w-3xl mx-auto bg-white p-8 rounded-2xl border border-gray-200">
                <h1 class="text-3xl font-bold text-slate-700 mb-6 text-center">Take Quiz: <%= quiz != null ? quiz.getTitle() : "" %></h1>

                <form action="take-quiz?quizId=<%= quiz != null ? quiz.getId() : 0 %>" method="post" class="space-y-8">
                    <% if (questions != null) {
                           int idx = 1;
                           for (Question q : questions) {
                    %>
                    <div class="space-y-4">
                        <h2 class="text-xl font-semibold text-slate-800"><%= idx %>. <%= q.getQuestionText() %></h2>

                        <% if (q.getQuestionType().equals(Question.MULTIPLE_CHOICE)) { %>
                            <% MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q; %>
                            <div class="space-y-2">
                                <% for (String option : mcq.getOptions()) { %>
                                    <label class="flex items-center space-x-2">
                                        <input type="radio" name="answer_<%= q.getId() %>" value="<%= option %>" class="h-4 w-4 text-indigo-600 border-gray-300 focus:ring-indigo-500">
                                        <span><%= option %></span>
                                    </label>
                                <% } %>
                            </div>
                        <% } else if (q.getQuestionType().equals(Question.MULTIPLE_CHOICE_MULTIPLE_ANSWERS)) { %>
                            <% MultipleChoiceMultipleAnswersQuestion mmq = (MultipleChoiceMultipleAnswersQuestion) q; %>
                            <div class="space-y-2">
                                <% for (String option : mmq.getOptions()) { %>
                                    <label class="flex items-center space-x-2">
                                        <input type="checkbox" name="answer_<%= q.getId() %>" value="<%= option %>" class="h-4 w-4 text-indigo-600 border-gray-300 focus:ring-indigo-500">
                                        <span><%= option %></span>
                                    </label>
                                <% } %>
                                <p class="text-sm text-slate-500">Select all that apply.</p>
                            </div>
                        <% } else if (q.getQuestionType().equals(Question.FILL_IN_BLANK) || q.getQuestionType().equals(Question.PICTURE_RESPONSE)) { %>
                            <input type="text" name="answer_<%= q.getId() %>" class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-400" placeholder="Your answer...">
                        <% } else if (q.getQuestionType().equals(Question.QUESTION_RESPONSE)) { %>
                            <textarea name="answer_<%= q.getId() %>" rows="3" class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-400" placeholder="Your answer..."></textarea>
                        <% } else if (q.getQuestionType().equals(Question.MULTI_ANSWER)) { %>
                            <textarea name="answer_<%= q.getId() %>" rows="4" class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-400" placeholder="Enter each answer on a new line or separate by commas"></textarea>
                        <% } %>
                    </div>
                    <% idx++; } } %>

                    <div class="text-center">
                        <button type="submit" class="px-8 py-3 bg-indigo-600 text-white font-semibold rounded-xl hover:bg-indigo-700 transition-colors">Submit Quiz</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html> 