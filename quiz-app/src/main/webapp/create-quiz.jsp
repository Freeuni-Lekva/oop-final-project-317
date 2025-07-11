<!DOCTYPE html>
<%@ page import="models.User" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Quiz - QuizMaster</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body class="overflow-hidden bg-gray-50">
<div class="flex h-screen">
    <!-- Sidebar -->
    <div class="w-80 bg-white border-r border-gray-200 p-6 flex flex-col">
        <!-- Logo -->
        <div class="mb-8">
            <h1 class="text-2xl font-bold text-slate-700">QuizMaster</h1>
            <p class="text-slate-500 text-sm">Your Learning Adventure</p>
        </div>

        <!-- Navigation -->
        <nav class="space-y-1 flex-1">
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Quiz History</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-green-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Creations</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-yellow-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Achievements</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-red-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Popular Quizzes</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-purple-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Recent Quizzes</span>
            </div>

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-indigo-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Friends</span>
            </div>

            <% if (session.getAttribute("user") != null) { %>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='profile'">
                <div class="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Profile</span>
            </div>
            <% } %>
            <% 
                if (session.getAttribute("user") != null) {
                    User user = (User) session.getAttribute("user");
                    boolean isAdmin = user.getIfAdmin();
                    if (isAdmin) {
            %>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3 bg-indigo-50" onclick="window.location.href='admin'">
                <div class="w-8 h-8 bg-indigo-100 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Statistics</span>
            </div>
            <% 
                    }
                }
            %>
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
                        <input type="text" placeholder="Search for quizzes, topics, or friends..."
                               class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent">
                        <div class="absolute right-3 top-3">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- User Actions -->
                <div class="flex items-center space-x-4 ml-6">
                    <% if (session.getAttribute("user") == null) { %>
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
                    <button class="relative p-3 bg-gray-50 rounded-xl border border-gray-200 hover:bg-gray-100 transition-colors">
                        <svg class="w-5 h-5 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.73 21a2 2 0 0 1-3.46 0"/>
                        </svg>
                        <span class="absolute -top-1 -right-1 bg-red-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center notification-pulse">5</span>
                    </button>
                </div>
            </div>
        </div>

        <!-- Main Content Area -->
        <div class="flex-1 p-6 overflow-y-auto">
            <!-- Page Header -->
            <div class="mb-8">
                <h1 class="text-3xl font-bold text-slate-700 mb-2">Create New Quiz</h1>
                <p class="text-slate-600">Design your own quiz and challenge others!</p>
            </div>

            <!-- Create Quiz Form -->
            <div class="bg-white rounded-2xl border border-gray-200 p-8">
                <% if (request.getAttribute("error") != null) { %>
                <div class="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                    <p class="text-red-700"><%= request.getAttribute("error") %></p>
                </div>
                <% } %>
                <form action="create-quiz" method="post" class="space-y-6">
                    <!-- Basic Information -->
                    <div class="space-y-4">
                        <h3 class="text-xl font-semibold text-slate-700 mb-4">Basic Information</h3>

                        <div>
                            <label for="title" class="block text-sm font-medium text-slate-700 mb-2">Quiz Title *</label>
                            <input type="text" id="title" name="title" required
                                   class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent"
                                   placeholder="Enter quiz title"
                                   value="<%= request.getParameter("title") != null ? request.getParameter("title") : "" %>">
                        </div>

                        <div>
                            <label for="description" class="block text-sm font-medium text-slate-700 mb-2">Description</label>
                            <textarea id="description" name="description" rows="4"
                                      class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent"
                                      placeholder="Describe your quiz..."><%= request.getParameter("description") != null ? request.getParameter("description") : "" %></textarea>
                        </div>

                        <div>
                            <label for="numQuestions" class="block text-sm font-medium text-slate-700 mb-2">Number of Questions *</label>
                            <input type="number" id="numQuestions" name="numQuestions" min="1" max="100" required
                                   class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent"
                                   placeholder="Enter number of questions"
                                   value="<%= request.getParameter("numQuestions") != null ? request.getParameter("numQuestions") : "" %>">
                        </div>
                    </div>

                    <!-- Quiz Settings -->
                    <div class="space-y-4">
                        <h3 class="text-xl font-semibold text-slate-700 mb-4">Quiz Settings</h3>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="flex items-center space-x-3">
                                <input type="checkbox" id="randomizeQuestions" name="randomizeQuestions" value="true"
                                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                                    <%= "true".equals(request.getParameter("randomizeQuestions")) ? "checked" : "" %>>
                                <label for="randomizeQuestions" class="text-sm font-medium text-slate-700">Randomize Questions</label>
                            </div>

                            <div class="flex items-center space-x-3">
                                <input type="checkbox" id="onePage" name="onePage" value="true"
                                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                                    <%= "true".equals(request.getParameter("onePage")) ? "checked" : "" %>>
                                <label for="onePage" class="text-sm font-medium text-slate-700">One Page Display</label>
                            </div>

                            <div class="flex items-center space-x-3">
                                <input type="checkbox" id="immediateCorrection" name="immediateCorrection" value="true"
                                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                                    <%= "true".equals(request.getParameter("immediateCorrection")) ? "checked" : "" %>>
                                <label for="immediateCorrection" class="text-sm font-medium text-slate-700">Immediate Correction</label>
                            </div>

                            <div class="flex items-center space-x-3">
                                <input type="checkbox" id="practiceMode" name="practiceMode" value="true"
                                       class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                                    <%= "true".equals(request.getParameter("practiceMode")) ? "checked" : "" %>>
                                <label for="practiceMode" class="text-sm font-medium text-slate-700">Practice Mode</label>
                            </div>
                        </div>
                    </div>

                    <!-- Form Actions -->
                    <div class="flex justify-end space-x-4 pt-6 border-t border-gray-200">
                        <a href="index.jsp" class="px-6 py-3 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">
                            Cancel
                        </a>
                        <button type="submit" class="px-6 py-3 bg-blue-600 rounded-xl text-white font-medium hover:bg-blue-700 transition-colors">
                            Create Quiz
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>