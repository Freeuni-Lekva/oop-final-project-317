<%@ page import="models.User" %>
<%@ page import="models.Notification" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DAO.NotificationDAO" %>
<%@ page import="notifications.ChallengeNotification" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notifications - QuizMaster</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f2f0e9;
        }
        .sidebar-item {
            transition: all 0.2s ease;
        }
        .sidebar-item:hover {
            background-color: #f8fafc;
            transform: translateX(2px);
        }
    </style>
</head>
<body class="overflow-hidden bg-gray-50">
<div class="flex h-screen">
    <!-- Sidebar -->
    <div class="w-80 bg-white border-r border-gray-200 p-6 flex flex-col">
        <!-- Logo -->
        <div class="mb-8 cursor-pointer" onclick="window.location.href='index.jsp'">
            <h1 class="text-2xl font-bold text-indigo-600 hover:text-indigo-700 transition-colors">QuizMaster</h1>
            <p class="text-slate-500 text-sm">Your Learning Adventure</p>
        </div>

        <!-- Navigation -->
        <nav class="space-y-1 flex-1">
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='QuizHistory.jsp'">
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

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='achievements'">
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
        </nav>
    </div>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col bg-gray-50">
        <!-- Top Bar -->
        <div class="bg-white border-b border-gray-200 p-6">
            <div class="flex items-center justify-between">
                <div class="flex-1">
                    <h1 class="text-2xl font-bold text-slate-700">Notifications</h1>
                    <p class="text-slate-500 text-sm">Stay updated with your quiz activities and friend requests</p>
                </div>
                
                <!-- User Actions -->
                <div class="flex items-center space-x-4 ml-6">
                    <% if (session.getAttribute("user") == null) { %>
                    <a href="login" class="px-4 py-3 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">Login</a>
                    <a href="register" class="px-4 py-3 bg-slate-600 rounded-xl text-white font-medium hover:bg-slate-700 transition-colors">Sign Up</a>
                    <% } else { %>
                    <a href="profile" class="px-4 py-3 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 font-medium hover:bg-gray-200 transition-colors">
                        <svg class="w-4 h-4 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                        </svg>
                        Profile
                    </a>
                    <a href="signout" class="px-4 py-3 bg-slate-600 rounded-xl text-white font-medium hover:bg-slate-700 transition-colors">Sign Out</a>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- Notifications List -->
        <div class="flex-1 p-6 overflow-y-auto">
            <div class="space-y-4">
                <%
                    ArrayList<Notification> notifications = (ArrayList<Notification>) request.getAttribute("notifications");
                    if (notifications != null && !notifications.isEmpty()) {
                        for (Notification notification : notifications) {
                %>
                <div class="bg-white rounded-xl p-4 shadow-sm hover:shadow-md transition-shadow">
                    <div class="flex items-start justify-between">
                        <div>
                            <h3 class="text-lg font-semibold text-slate-700"><%= notification.getTitle() %></h3>
                            <p class="text-slate-600 mt-1"><%= notification.getMessage() %></p>
                            <%
                                NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
                                String senderUsername = notificationDAO.getSenderUsername(notification.getFromUserId());
                            %>
                            <p class="text-sm text-slate-500 mt-2">From: <span class="font-medium"><%= senderUsername %></span></p>
                        </div>
                        <div class="flex items-center space-x-3">
                            <% if (Notification.FRIEND_REQUEST_NOTIFICATION.equals(notification.getQuestionType())) { %>
                                <form action="friendship" method="POST" class="inline">
                                    <input type="hidden" name="action" value="accept">
                                    <input type="hidden" name="fromUserId" value="<%= notification.getFromUserId() %>">
                                    <input type="hidden" name="notificationId" value="<%= notification.getId() %>">
                                    <button type="submit" class="px-4 py-2 bg-[#86A789] text-white rounded-lg hover:bg-[#739072] transition-colors">
                                        Accept
                                    </button>
                                </form>
                            <% } %>
                            <% if (Notification.CHALLENGE_NOTIFICATION.equals(notification.getQuestionType())) {
                                   long quizId = 0;
                                   if (notification instanceof ChallengeNotification) {
                                       quizId = ((ChallengeNotification) notification).getQuizId();
                                   }
                                   String quizLink = quizId > 0 ? ("QuizSummary?quizId=" + quizId) : "#";
                            %>
                                <a href="<%= quizLink %>" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-500 transition-colors">
                                    Take Quiz
                                </a>
                            <% } %>
                            <form action="notifications" method="POST" class="inline">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="notificationId" value="<%= notification.getId() %>">
                                <button type="submit" class="text-red-500 hover:text-red-600">
                                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                                    </svg>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <%
                        }
                    } else {
                %>
                <div class="text-center py-12">
                    <svg class="w-16 h-16 text-slate-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
                    </svg>
                    <h3 class="text-xl font-semibold text-slate-700 mb-2">No notifications</h3>
                    <p class="text-slate-500">You're all caught up! Check back later for new updates.</p>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
</body>
</html> 