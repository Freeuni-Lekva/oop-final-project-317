<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Quiz" %>
<%@ page import="models.User" %>
<%@ page import="DAO.NotificationDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results - QuizMaster</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="styles.css">
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

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='friends'">
                <div class="w-8 h-8 bg-indigo-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 515.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 919.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Friends</span>
            </div>

            <% if (session.getAttribute("user") != null) { %>
            <%-- Admin Statistics Button: Only show if user is admin --%>
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
                                   value="<%= request.getAttribute("searchTerm") != null ? request.getAttribute("searchTerm") : "" %>"
                                   class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent">
                        </form>
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
                    <a href="notifications" class="relative p-3 bg-gray-50 rounded-xl border border-gray-200 hover:bg-gray-100 transition-colors">
                        <svg class="w-5 h-5 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.73 21a2 2 0 0 1-3.46 0"/>
                        </svg>
                        <% if (session.getAttribute("user") != null) {
                            NotificationDAO notificationDAO = (NotificationDAO) request.getServletContext().getAttribute("notificationDAO");
                            User currentUser = (User) session.getAttribute("user");
                            if (notificationDAO != null && currentUser != null) {
                                int notificationCount = notificationDAO.getNotificationCount(currentUser.getId());
                                if (notificationCount > 0) {
                        %>
                        <span class="absolute -top-1 -right-1 bg-red-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center notification-pulse"><%= notificationCount %></span>
                        <% } } } %>
                    </a>
                </div>
            </div>
        </div>

        <!-- Main Content Area -->
        <div class="flex-1 p-6 overflow-y-auto">
            <%
                String searchTerm = (String) request.getAttribute("searchTerm");
                String error = (String) request.getAttribute("error");
                ArrayList<Quiz> quizResults = (ArrayList<Quiz>) request.getAttribute("quizResults");
                ArrayList<User> userResults = (ArrayList<User>) request.getAttribute("userResults");
                Integer totalResults = (Integer) request.getAttribute("totalResults");
            %>

            <!-- Search Header -->
            <div class="mb-8">
                <% if (error != null) { %>
                <div class="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                    <div class="flex">
                        <svg class="w-5 h-5 text-red-600 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                        </svg>
                        <p class="text-red-700 text-sm font-medium"><%= error %></p>
                    </div>
                </div>
                <% } %>

                <% if (searchTerm != null) { %>
                <h1 class="text-3xl font-bold text-slate-700 mb-2">Search Results</h1>
                <p class="text-slate-600">
                    Found <%= totalResults != null ? totalResults : 0 %> result(s) for "<span class="font-semibold"><%= searchTerm %></span>"
                </p>
                <% } else { %>
                <h1 class="text-3xl font-bold text-slate-700 mb-2">Search</h1>
                <p class="text-slate-600">Enter a search term to find quizzes and users</p>
                <% } %>
            </div>

            <% if (searchTerm != null && (quizResults != null || userResults != null)) { %>
            
            <!-- Quiz Results Section -->
            <% if (quizResults != null && !quizResults.isEmpty()) { %>
            <div class="mb-8">
                <div class="flex items-center mb-4">
                    <svg class="w-6 h-6 text-blue-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                    </svg>
                    <h2 class="text-2xl font-bold text-slate-700">Quizzes (<%= quizResults.size() %>)</h2>
                </div>
                
                <div class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
                    <%
                        String[] cardClasses = {"quiz-card-1", "quiz-card-2", "quiz-card-3", "quiz-card-4"};
                        for (int i = 0; i < quizResults.size(); i++) {
                            Quiz quiz = quizResults.get(i);
                            String cardClass = cardClasses[i % cardClasses.length];
                    %>
                    <div class="bg-white rounded-2xl p-6 border border-gray-200 card-hover">
                        <div class="flex items-start justify-between mb-4">
                            <div class="bg-blue-50 rounded-full px-3 py-1">
                                <span class="text-blue-600 text-sm font-medium">Quiz</span>
                            </div>
                            <div class="bg-slate-100 rounded-full px-3 py-1">
                                <span class="text-slate-600 text-sm">ID: <%= quiz.getId() %></span>
                            </div>
                        </div>
                        
                        <h3 class="text-xl font-bold text-slate-700 mb-2"><%= quiz.getTitle() %></h3>
                        <p class="text-slate-600 mb-4 text-sm">
                            <%= quiz.getDescription() != null ? (quiz.getDescription().length() > 100 ? quiz.getDescription().substring(0, 100) + "..." : quiz.getDescription()) : "No description available" %>
                        </p>
                        
                        <div class="flex items-center justify-between text-sm text-slate-500 mb-4">
                            <span>Created: <%= quiz.getCreatedDate() != null ? quiz.getCreatedDate().toLocalDate() : "Unknown" %></span>
                            <span><%= quiz.getTimesTaken() %> completions</span>
                        </div>
                        
                        <div class="flex space-x-2">
                            <a href="QuizSummary?quizId=<%= quiz.getId() %>" class="flex-1 text-center bg-blue-600 text-white py-2 px-4 rounded-xl font-medium hover:bg-blue-700 transition-colors">
                                View Details
                            </a>
                            <a href="take-quiz?quizId=<%= quiz.getId() %>" class="flex-1 text-center bg-green-600 text-white py-2 px-4 rounded-xl font-medium hover:bg-green-700 transition-colors">
                                Take Quiz
                            </a>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
            <% } %>

            <!-- User Results Section -->
            <% if (userResults != null && !userResults.isEmpty()) { %>
            <div class="mb-8">
                <div class="flex items-center mb-4">
                    <svg class="w-6 h-6 text-green-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"/>
                    </svg>
                    <h2 class="text-2xl font-bold text-slate-700">Users (<%= userResults.size() %>)</h2>
                </div>
                
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    <% for (User foundUser : userResults) { %>
                    <div class="bg-white rounded-2xl p-6 border border-gray-200 card-hover cursor-pointer" onclick="window.location.href='profile?userId=<%= foundUser.getId() %>'">
                        <div class="text-center">
                            <div class="w-16 h-16 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full mx-auto mb-4 flex items-center justify-center">
                                <span class="text-white text-xl font-bold"><%= foundUser.getName().substring(0, 1).toUpperCase() %></span>
                            </div>
                            
                            <h3 class="text-lg font-bold text-slate-700 mb-1"><%= foundUser.getName() %></h3>
                            <p class="text-slate-500 text-sm mb-3"><%= foundUser.getEmail() %></p>
                            
                            <div class="grid grid-cols-2 gap-2 text-xs text-slate-600 mb-4">
                                <div class="bg-gray-50 rounded-lg p-2">
                                    <div class="font-semibold text-slate-700"><%= foundUser.getQuizCreatedCount() %></div>
                                    <div>Quizzes Created</div>
                                </div>
                                <div class="bg-gray-50 rounded-lg p-2">
                                    <div class="font-semibold text-slate-700"><%= foundUser.getQuizTakenCount() %></div>
                                    <div>Quizzes Taken</div>
                                </div>
                            </div>
                            
                            <% if (foundUser.getIfAdmin()) { %>
                            <span class="inline-block bg-red-100 text-red-600 text-xs px-2 py-1 rounded-full font-medium mb-2">Admin</span>
                            <% } %>
                            
                            <div class="text-center">
                                <span class="text-blue-600 text-sm font-medium hover:underline">View Profile</span>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
            <% } %>

            <!-- No Results Message -->
            <% if (searchTerm != null && ((quizResults == null || quizResults.isEmpty()) && (userResults == null || userResults.isEmpty()))) { %>
            <div class="text-center py-16">
                <div class="w-24 h-24 bg-gray-100 rounded-full mx-auto mb-4 flex items-center justify-center">
                    <svg class="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                    </svg>
                </div>
                <h3 class="text-xl font-semibold text-slate-700 mb-2">No Results Found</h3>
                <p class="text-slate-500 mb-6">We couldn't find any quizzes or users matching "<%= searchTerm %>"</p>
                <div class="space-x-4">
                    <a href="popular-quizzes" class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition-colors">
                        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                        </svg>
                        Browse Popular Quizzes
                    </a>
                    <a href="recents" class="inline-flex items-center px-6 py-3 bg-gray-100 border border-gray-200 rounded-xl text-slate-700 hover:bg-gray-200 transition-colors">
                        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                        </svg>
                        Recent Quizzes
                    </a>
                </div>
            </div>
            <% } %>

            <% } %>
        </div>
    </div>
</div>

<script>
// Auto-focus search input
document.getElementById('searchInput').focus();

// Handle Enter key in search form
document.getElementById('searchInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        if (this.value.trim()) {
            window.location.href = 'search?q=' + encodeURIComponent(this.value.trim());
        }
    }
});
</script>

</body>
</html> 