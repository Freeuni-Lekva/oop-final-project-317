<%@ page import="models.User" %>
<%@ page import="DAO.NotificationDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - QuizMaster</title>
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

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-indigo-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Friends</span>
            </div>
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
                <!-- Page Title -->
                <div class="flex-1">
                    <h1 class="text-2xl font-bold text-slate-700">My Profile</h1>
                    <p class="text-slate-500 text-sm">Manage your account and preferences</p>
                </div>

                <!-- User Actions -->
                <div class="flex items-center space-x-4 ml-6">
                    <!-- Notification Icon (always visible) -->
                    <a href="notifications" class="relative p-3 bg-gray-50 rounded-xl border border-gray-200 hover:bg-gray-100 transition-colors">
                        <svg class="w-5 h-5 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.73 21a2 2 0 0 1-3.46 0"/>
                        </svg>
                        <% if (session.getAttribute("user") != null) {
                            NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
                            User currentUser = (User) session.getAttribute("user");
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
            <!-- Go to Homepage Button -->
            <div class="mb-6 flex justify-end">
                <a href="index.jsp" class="px-6 py-3 bg-blue-600 rounded-xl text-white font-medium hover:bg-blue-700 transition-colors shadow">
                    Go to Homepage & Make Quizzes
                </a>
            </div>
            <!-- Profile Header -->
            <div class="mb-8 p-8 bg-white rounded-2xl border border-gray-200 card-hover">
                <div class="flex items-center space-x-6">
                    <!-- Profile Picture -->
                    <div class="relative">
                        <div class="w-32 h-32 bg-gradient-to-br from-blue-400 to-purple-500 rounded-full flex items-center justify-center profile-picture">
                            <span class="text-white text-4xl font-bold">
                            </span>
                        </div>
                        <button class="absolute bottom-0 right-0 w-8 h-8 bg-white rounded-full border-2 border-gray-200 flex items-center justify-center hover:bg-gray-50 transition-colors">
                            <svg class="w-4 h-4 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"/>
                            </svg>
                        </button>
                    </div>

                    <!-- Profile Info -->
                    <div class="flex-1">
                        <h2 class="text-3xl font-bold text-slate-700 mb-2">
                            <%= session.getAttribute("user") != null ? ((models.User)session.getAttribute("user")).getName() : "" %>
                        </h2>
                        <p class="text-slate-500 text-lg mb-4"><%= request.getAttribute("userEmail") != null ? request.getAttribute("userEmail") : "" %></p>
                        <div class="flex items-center space-x-4 text-sm text-slate-600">
                            <div class="flex items-center">
                                <svg class="w-4 h-4 mr-1 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                                Member since <%= request.getAttribute("userCreatedAt") != null ? request.getAttribute("userCreatedAt") : "" %>
                            </div>
                        </div>
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

                <!-- Friends -->
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
                    <div class="mt-2">
                        <% if (friendsList != null && !friendsList.isEmpty()) { %>
                            <ul class="list-disc ml-6 text-slate-700">
                            <% for (Object obj : friendsList) { models.User friend = (models.User) obj; %>
                                <li><%= friend.getName() %></li>
                            <% } %>
                            </ul>
                        <% } else { %>
                            <div class="text-slate-500">No friends found.</div>
                        <% } %>
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
                    <div class="mt-2">
                        <% if (achievementsList != null && !achievementsList.isEmpty()) { %>
                            <ul class="list-disc ml-6 text-slate-700">
                            <% for (Object obj : achievementsList) { models.Notification ach = (models.Notification) obj; %>
                                <li><%= ach.getTitle() %> - <%= ach.getMessage() %></li>
                            <% } %>
                            </ul>
                        <% } else { %>
                            <div class="text-slate-500">No achievements found.</div>
                        <% } %>
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
                                <p class="text-slate-500 text-sm">Score: <%= qh.getScore() %>%,   <%= qh.getCompletedDate() %></p>
                            </div>
                        </div>
                        <%   }
                        } else { %>
                        <div class="text-slate-500">No recent activity found.</div>
                        <% } %>
                    </div>
                </div>

                <!-- Settings & Preferences -->
                <div class="bg-white rounded-2xl border border-gray-200 p-6 card-hover">
                    <h3 class="text-xl font-bold text-slate-700 mb-4">Account Settings</h3>
                    <div class="space-y-4">
                        <!-- Change Email Form -->
                        <form method="post" action="profile" class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                            <input type="hidden" name="action" value="changeEmail" />
                            <div class="flex items-center space-x-3">
                                <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                                    <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                                    </svg>
                                </div>
                                <div>
                                    <p class="text-slate-700 font-medium">Change Email</p>
                                    <input type="email" name="newEmail" placeholder="New email" class="border rounded px-2 py-1 text-sm" required />
                                </div>
                            </div>
                            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 font-medium">Change</button>
                        </form>
                        <!-- Change Username Form -->
                        <form method="post" action="profile" class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                            <input type="hidden" name="action" value="changeUsername" />
                            <div class="flex items-center space-x-3">
                                <div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                                    <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                                    </svg>
                                </div>
                                <div>
                                    <p class="text-slate-700 font-medium">Change Username</p>
                                    <input type="text" name="newUsername" placeholder="New username" class="border rounded px-2 py-1 text-sm" required />
                                </div>
                            </div>
                            <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 font-medium">Change</button>
                        </form>
                        <!-- Deactivate Account -->
                        <form method="post" action="profile" onsubmit="return confirm('Are you sure you want to deactivate your account? This cannot be undone.');" class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                            <input type="hidden" name="action" value="deactivateAccount" />
                            <div class="flex items-center space-x-3">
                                <div class="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                                    <svg class="w-5 h-5 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                                    </svg>
                                </div>
                                <div>
                                    <p class="text-slate-700 font-medium">Deactivate Account</p>
                                    <p class="text-slate-500 text-sm">Permanently delete your account</p>
                                </div>
                            </div>
                            <button type="submit" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 font-medium">Deactivate</button>
                        </form>
                        <!-- Take a Quiz Button -->
                        <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                            <div class="flex items-center space-x-3">
                                <div class="w-10 h-10 bg-yellow-100 rounded-full flex items-center justify-center">
                                    <svg class="w-5 h-5 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                                    </svg>
                                </div>
                                <div>
                                    <p class="text-slate-700 font-medium">Take a Quiz</p>
                                    <p class="text-slate-500 text-sm">Go to quiz start page</p>
                                </div>
                            </div>
                            <a href="index.jsp" class="px-4 py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600 font-medium">Go</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>