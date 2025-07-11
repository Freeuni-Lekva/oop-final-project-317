<%@ page import="java.util.List" %>
<%@ page import="models.Quiz" %>
<%@ page import="models.Question" %>
<%@ page import="questions.*" %>
<%@ page import="models.User" %>
<%@ page import="DAO.NotificationDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    @SuppressWarnings("unchecked")
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    User currentUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Take Quiz - <%= quiz != null ? quiz.getTitle() : "Quiz" %></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

        body {
            font-family: 'Inter', sans-serif;
            background-color: #f2f0e9;
            min-height: 100vh;
        }

        .card-hover {
            transition: all 0.3s ease;
        }

        .card-hover:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 24px rgba(73, 89, 107, 0.15);
        }

        .sidebar-item {
            transition: all 0.2s ease;
        }

        .sidebar-item:hover {
            background-color: #f8fafc;
            transform: translateX(2px);
        }

        .notification-pulse {
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.7; }
        }
    </style>
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

        <!-- Navigation -->
        <nav class="space-y-1 flex-1">
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='quiz-history'">
                <div class="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Quiz History</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='my-creations'">
                <div class="w-8 h-8 bg-green-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Creations</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='<%= request.getContextPath() %>/achievements'">
                <div class="w-8 h-8 bg-yellow-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Achievements</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='popular-quizzes'">
                <div class="w-8 h-8 bg-red-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Popular Quizzes</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='recents'">
                <div class="w-8 h-8 bg-purple-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Recent Quizzes</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='friends'">
                <div class="w-8 h-8 bg-indigo-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Friends</span>
            </div>

            <% if (currentUser != null && currentUser.getIfAdmin()) { %>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3 bg-indigo-50" onclick="window.location.href='admin'">
                <div class="w-8 h-8 bg-indigo-100 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Statistics</span>
            </div>
            <% } %>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col bg-gray-50">
        <!-- Top Bar -->
        <div class="bg-white border-b border-gray-200 p-6">
            <div class="flex items-center justify-between">
                <!-- Search Bar -->
                <div class="flex-1 max-w-2xl">
                    <div class="relative">
                        <form action="search" method="GET" class="w-full">
                            <input type="text" name="q" id="searchInput" placeholder="Search for quizzes, topics, or friends..."
                                   class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent">
                        </form>
                        <div class="absolute right-3 top-3 cursor-pointer" onclick="document.getElementById('searchInput').closest('form').submit();">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- User Actions -->
                <div class="flex items-center space-x-4 ml-6">
                    <% if (currentUser == null) { %>
                    <!-- Login and Register Buttons (when NOT logged in) -->
                    <a href="login" class="px-4 py-3 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">
                        Login
                    </a>
                    <a href="register" class="px-4 py-3 bg-slate-600 rounded-xl text-white font-medium hover:bg-slate-700 transition-colors">
                        Sign Up
                    </a>
                    <% } else { %>
                    <!-- Profile and Sign Out Buttons (when logged in) -->
                    <a href="profile" class="px-4 py-3 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">
                        <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                        </svg>
                        Profile
                    </a>
                    <a href="signout" class="px-4 py-3 bg-slate-600 rounded-xl text-white font-medium hover:bg-slate-700 transition-colors">
                        Sign Out
                    </a>
                    <% } %>

                    <!-- Notification Icon (always visible) -->
                    <a href="notifications" class="relative p-3 bg-gray-50 rounded-xl border border-gray-200 hover:bg-gray-100 transition-colors">
                        <svg class="w-5 h-5 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.73 21a2 2 0 0 1-3.46 0"/>
                        </svg>
                        <% if (currentUser != null) {
                            NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
                            if (notificationDAO != null) {
                                int notificationCount = notificationDAO.getNotificationCount(currentUser.getId());
                                if (notificationCount > 0) {
                        %>
                        <span class="absolute -top-1 -right-1 bg-red-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center notification-pulse"><%= notificationCount %></span>
                        <% } } } %>
                    </a>
                </div>
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