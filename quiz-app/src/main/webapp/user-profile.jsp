<%@ page import="models.User" %>
<%@ page import="DAO.NotificationDAO" %>
<%@ page import="DAO.UserDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - QuizMaster</title>
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

        .profile-picture {
            transition: all 0.3s ease;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }

        .profile-picture:hover {
            transform: scale(1.05);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
        }

        /* Stats Cards */
        .stat-card-1 {
            background: linear-gradient(135deg, #8387C3 0%, #676a9c 100%) !important;
            color: #000;
            position: relative;
            overflow: hidden;
            border: 2px solid #222;
            z-index: 1;
        }

        .stat-card-2 {
            background: linear-gradient(135deg, #CEB5D4 0%, #a68eab 100%) !important;
            color: #000;
            position: relative;
            overflow: hidden;
            border: 2px solid #222;
            z-index: 1;
        }

        .stat-card-3 {
            background: linear-gradient(135deg, #537E72 0%, #406358 100%) !important;
            color: #000;
            position: relative;
            overflow: hidden;
            border: 2px solid #222;
            z-index: 1;
        }

        .stat-card-4 {
            background: linear-gradient(135deg, #7D9FC0 0%, #5f7b96 100%) !important;
            color: #000;
            position: relative;
            overflow: hidden;
            border: 2px solid #222;
            z-index: 1;
        }

        .friend-button {
            transition: all 0.3s ease;
        }

        .friend-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body class="overflow-hidden bg-gray-50">
    <%
    // Get the profile user and current user
    User profileUser = (User) request.getAttribute("profileUser");
    User currentUser = (User) session.getAttribute("user");
    Boolean areFriends = (Boolean) request.getAttribute("areFriends");
    Boolean isPendingRequest = (Boolean) request.getAttribute("isPendingRequest");

    if (profileUser == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

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
        </nav>
    </div>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col bg-gray-50">
        <!-- Top Bar -->
        <div class="bg-white border-b border-gray-200 p-6">
            <div class="flex items-center justify-between">
                <!-- Page Title -->
                <div class="flex-1">
                    <h1 class="text-2xl font-bold text-slate-700"><%= profileUser.getName() %>'s Profile</h1>
                    <p class="text-slate-500 text-sm">View user profile and connect</p>
                </div>

                <!-- User Actions -->
                <div class="flex items-center space-x-4 ml-6">
                    <!-- Back to My Profile -->
                    <a href="profile" class="px-4 py-3 bg-gray-100 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">
                        <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                        </svg>
                        My Profile
                    </a>

                    <!-- Notification Icon -->
                    <a href="notifications" class="relative p-3 bg-gray-50 rounded-xl border border-gray-200 hover:bg-gray-100 transition-colors">
                        <svg class="w-5 h-5 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.73 21a2 2 0 0 1-3.46 0"/>
                        </svg>
                        <% if (currentUser != null) {
                            NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
                            int notificationCount = notificationDAO.getNotificationCount(currentUser.getId());
                            if (notificationCount > 0) {
                        %>
                        <span class="absolute -top-1 -right-1 bg-red-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center notification-pulse"><%= notificationCount %></span>
                        <% } } %>
                    </a>

                    <!-- Logout Button -->
                    <a href="signout" class="px-4 py-3 bg-slate-600 rounded-xl text-white font-medium hover:bg-slate-700 transition-colors">
                        <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                        </svg>
                        Logout
                    </a>
                </div>
            </div>
        </div>

        <!-- Main Content Area -->
        <div class="flex-1 p-6 overflow-y-auto">
            <!-- Profile Header -->
            <div class="mb-8 p-8 bg-white rounded-2xl border border-gray-200 card-hover">
                <div class="flex items-center justify-between">
                    <div class="flex items-center space-x-6">
                        <!-- Profile Picture -->
                        <div class="relative">
                            <div class="w-32 h-32 bg-gradient-to-br from-blue-400 to-purple-500 rounded-full flex items-center justify-center profile-picture">
                                <span class="text-white text-4xl font-bold">
                                    <%= profileUser.getName().substring(0, 1).toUpperCase() %>
                                </span>
                            </div>
                        </div>

                        <!-- Profile Info -->
                        <div class="flex-1">
                            <h2 class="text-3xl font-bold text-slate-700 mb-2"><%= profileUser.getName() %></h2>
                            <p class="text-slate-500 text-lg mb-4"><%= profileUser.getEmail() %></p>
                            <div class="flex items-center space-x-4 text-sm text-slate-600">
                                <div class="flex items-center">
                                    <svg class="w-4 h-4 mr-1 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                    </svg>
                                    Member since <%= request.getAttribute("userCreatedAt") != null ? request.getAttribute("userCreatedAt") : "Unknown" %>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Friendship Actions -->
                    <div class="flex flex-col space-y-3">
                        <% if (currentUser != null && currentUser.getId() != profileUser.getId()) { %>
                        <% if (areFriends != null && areFriends) { %>
                        <!-- Already Friends -->
                        <div class="flex items-center space-x-2">
                            <button class="friend-button px-6 py-3 bg-green-100 text-green-700 rounded-xl font-medium border-2 border-green-300">
                                <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                                Friends!
                            </button>
                            <form method="post" action="friend-action" style="display: inline;">
                                <input type="hidden" name="action" value="unfriend">
                                <input type="hidden" name="userId" value="<%= profileUser.getId() %>">
                                <button type="submit" class="friend-button px-4 py-3 bg-red-100 text-red-700 rounded-xl font-medium border-2 border-red-300 hover:bg-red-200 transition-colors">
                                    <svg class="w-4 h-4 inline mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                    </svg>
                                    Unfriend
                                </button>
                            </form>
                        </div>
                        <% } else if (isPendingRequest != null && isPendingRequest) { %>
                        <!-- Pending Friend Request -->
                        <form method="post" action="friend-action" style="display: inline;">
                            <input type="hidden" name="action" value="cancel">
                            <input type="hidden" name="userId" value="<%= profileUser.getId() %>">
                            <button type="submit" class="friend-button px-6 py-3 bg-yellow-100 text-yellow-700 rounded-xl font-medium border-2 border-yellow-300 hover:bg-yellow-200 transition-colors">
                                <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                                Request Pending
                            </button>
                        </form>
                        <% } else { %>
                        <!-- Not Friends - Show Add Friend Button -->
                        <form method="post" action="friend-action" style="display: inline;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="userId" value="<%= profileUser.getId() %>">
                            <button type="submit" class="friend-button px-6 py-3 bg-blue-100 text-blue-700 rounded-xl font-medium border-2 border-blue-300 hover:bg-blue-200 transition-colors">
                                <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                                </svg>
                                Add Friend
                            </button>
                        </form>
                        <% } %>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- Stats Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                <!-- Quizzes Made -->
                <div class="stat-card-1 rounded-2xl p-6 card-hover">
                    <div class="flex items-center justify-between mb-4">
                        <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                            <svg class="w-6 h-6 text-black" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                            </svg>
                        </div>
                        <div class="text-right">
                            <div class="text-3xl font-bold text-black"><%= request.getAttribute("quizzesCreated") != null ? request.getAttribute("quizzesCreated") : 0 %></div>
                            <div class="text-black text-sm">Quizzes Made</div>
                        </div>
                    </div>
                </div>

                <!-- Quizzes Taken -->
                <div class="stat-card-2 rounded-2xl p-6 card-hover">
                    <div class="flex items-center justify-between mb-4">
                        <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                            <svg class="w-6 h-6 text-black" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                            </svg>
                        </div>
                        <div class="text-right">
                            <div class="text-3xl font-bold text-black"><%= request.getAttribute("quizzesTaken") != null ? request.getAttribute("quizzesTaken") : 0 %></div>
                            <div class="text-black text-sm">Quizzes Taken</div>
                        </div>
                    </div>
                </div>

                <!-- Friends Count -->
                <div class="stat-card-3 rounded-2xl p-6 card-hover">
                    <div class="flex items-center justify-between mb-4">
                        <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                            <svg class="w-6 h-6 text-black" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                            </svg>
                        </div>
                        <div class="text-right">
                            <div class="text-3xl font-bold text-black">
                                <% java.util.List friendsList = (java.util.List) request.getAttribute("friendsList"); %>
                                <%= friendsList != null ? friendsList.size() : 0 %>
                            </div>
                            <div class="text-black text-sm">Friends</div>
                        </div>
                    </div>
                </div>

                <!-- Achievements -->
                <div class="stat-card-4 rounded-2xl p-6 card-hover">
                    <div class="flex items-center justify-between mb-4">
                        <div class="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center">
                            <svg class="w-6 h-6 text-black" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                        </div>
                        <div class="text-right">
                            <div class="text-3xl font-bold text-black">
                                <% java.util.List achievementsList = (java.util.List) request.getAttribute("achievementsList"); %>
                                <%= achievementsList != null ? achievementsList.size() : 0 %>
                            </div>
                            <div class="text-black text-sm">Achievements</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Profile Details -->
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <!-- Recent Activity -->
                <div class="bg-white rounded-2xl border border-gray-200 p-6 card-hover">
                    <h3 class="text-xl font-bold text-slate-700 mb-4">Recent Activity</h3>
                    <div class="space-y-4">
                            <%
                        java.util.List recentActivities = (java.util.List) request.getAttribute("recentActivities");
                        if (recentActivities != null && !recentActivities.isEmpty()) {
                            for (Object obj : recentActivities) {
                                models.QuizHistory qh = (models.QuizHistory) obj;
                        %>
                        <div class="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
                            <div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                                <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4"/>
                                </svg>
                            </div>
                            <div class="flex-1">
                                <p class="text-slate-700 font-medium">Completed quiz ID: <%= qh.getQuizId() %></p>
                                <p class="text-slate-500 text-sm">Score: <%= qh.getScore() %>% • <%= qh.getCompletedDate() %></p>
                            </div>
                        </div>
                            <%   }
                        } else { %>
                        <div class="text-slate-500 text-center py-8">No recent activity found.</div<% } %>
                    </div>
                </div>

                <!-- Recent Achievements -->
                <div class="bg-white rounded-2xl border border-gray-200 p-6 card-hover lg:col-span-2">
                    <h3 class="text-xl font-bold text-slate-700 mb-4">Achievements</h3>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <%
                            Integer quizzesCreated = (Integer) request.getAttribute("quizzesCreated");
                            Integer quizzesTaken = (Integer) request.getAttribute("quizzesTaken");
                            Integer bestScores = (Integer) request.getAttribute("bestScores");
                            Integer practiceQuizzes = (Integer) request.getAttribute("practiceQuizzes");

                            boolean hasAchievements = false;

                            if (quizzesCreated != null && quizzesCreated >= 1) {
                                hasAchievements = true;
                        %>
                        <div class="flex items-center space-x-3 p-4 bg-gradient-to-r from-yellow-50 to-orange-50 rounded-lg border border-yellow-200">
                            <div class="w-12 h-12 bg-yellow-100 rounded-full flex items-center justify-center">
                                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                                </svg>
                            </div>
                            <div>
                                <p class="text-slate-700 font-medium">First Quiz Created</p>
                                <p class="text-slate-500 text-sm">Created your first quiz</p>
                            </div>
                        </div>
                        <%
                            }

                            if (quizzesCreated != null && quizzesCreated >= 5) {
                                hasAchievements = true;
                        %>
                        <div class="flex items-center space-x-3 p-4 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg border border-blue-200">
                            <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"/>
                                </svg>
                            </div>
                            <div>
                                <p class="text-slate-700 font-medium">Quiz Creator</p>
                                <p class="text-slate-500 text-sm">Created 5 or more quizzes</p>
                            </div>
                        </div>
                        <%
                            }

                            if (quizzesTaken != null && quizzesTaken >= 1) {
                                hasAchievements = true;
                        %>
                        <div class="flex items-center space-x-3 p-4 bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg border border-green-200">
                            <div class="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                            </div>
                            <div>
                                <p class="text-slate-700 font-medium">First Quiz Taken</p>
                                <p class="text-slate-500 text-sm">Completed your first quiz</p>
                            </div>
                        </div>
                        <%
                            }

                            if (quizzesTaken != null && quizzesTaken >= 10) {
                                hasAchievements = true;
                        %>
                        <div class="flex items-center space-x-3 p-4 bg-gradient-to-r from-purple-50 to-pink-50 rounded-lg border border-purple-200">
                            <div class="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
                                <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                                </svg>
                            </div>
                            <div>
                                <p class="text-slate-700 font-medium">Quiz Enthusiast</p>
                                <p class="text-slate-500 text-sm">Completed 10 or more quizzes</p>
                            </div>
                        </div>
                        <%
                            }

                            if (bestScores != null && bestScores >= 1) {
                                hasAchievements = true;
                        %>
                        <div class="flex items-center space-x-3 p-4 bg-gradient-to-r from-orange-50 to-red-50 rounded-lg border border-orange-200">
                            <div class="w-12 h-12 bg-orange-100 rounded-full flex items-center justify-center">
                                <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"/>
                                </svg>
                            </div>
                            <div>
                                <p class="text-slate-700 font-medium">Top Scorer</p>
                                <p class="text-slate-500 text-sm">Achieved highest score in a quiz</p>
                            </div>
                        </div>
                        <%
                            }

                            if (practiceQuizzes != null && practiceQuizzes >= 1) {
                                hasAchievements = true;
                        %>
                        <div class="flex items-center space-x-3 p-4 bg-gradient-to-r from-cyan-50 to-blue-50 rounded-lg border border-cyan-200">
                            <div class="w-12 h-12 bg-cyan-100 rounded-full flex items-center justify-center">
                                <svg class="w-6 h-6 text-cyan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                                </svg>
                            </div>
                            <div>
                                <p class="text-slate-700 font-medium">Practice Makes Perfect</p>
                                <p class="text-slate-500 text-sm">Completed practice mode quizzes</p>
                            </div>
                        </div>
                        <%
                            }
                            if (!hasAchievements) {
                        %>
                        <div class="text-slate-500 text-center py-8 md:col-span-2">
                            <svg class="w-12 h-12 text-slate-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                            No achievements yet. Start creating and taking quizzes to earn achievements!
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

