<%@ page import="models.*,java.util.*,java.text.DecimalFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    QuizResult result = (QuizResult) request.getAttribute("quizResult");
    List<QuizResult> past = (List<QuizResult>) request.getAttribute("userPastResults");
    List<QuizResult> top = (List<QuizResult>) request.getAttribute("topResults");
    User currentUser = (User) request.getAttribute("currentUser");
    Map<Long, User> userMap = (Map<Long, User>) request.getAttribute("userMap");
    DecimalFormat df = new DecimalFormat("#.#");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Result - <%= quiz.getTitle() %></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body class="overflow-hidden bg-gray-50">
<div class="flex h-screen">
    <!-- Sidebar -->
    <div class="w-80 bg-white border-r border-gray-200 p-6 flex flex-col">
        <div class="mb-8 cursor-pointer" onclick="window.location.href='<%= request.getContextPath() %>/'">
            <h1 class="text-2xl font-bold text-indigo-600 hover:text-indigo-700 transition-colors">QuizMaster</h1>
            <p class="text-slate-500 text-sm">Your Learning Adventure</p>
        </div>

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

            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3">
                <div class="w-8 h-8 bg-indigo-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Friends</span>
            </div>

            <% if (session.getAttribute("user") != null) { %>
            <%
                models.User user = (models.User) session.getAttribute("user");
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
            %>
            <% } %>
        </nav>
    </div>

    <div class="flex-1 flex flex-col">
        <div class="bg-white border-b border-gray-200 p-6 flex items-center justify-between">
            <h2 class="text-lg font-semibold text-slate-700">Quiz Result</h2>
            <a href="QuizSummary?quizId=<%= quiz.getId() %>" class="px-4 py-2 bg-gray-100 rounded-xl border border-gray-200 hover:bg-gray-200">Back to Summary</a>
        </div>

        <div class="flex-1 overflow-y-auto p-8 max-w-4xl mx-auto space-y-10">
            <!-- Your Score -->
            <section class="bg-white rounded-2xl border border-gray-200 p-8 text-center">
                <h1 class="text-3xl font-bold text-slate-700 mb-4"><%= quiz.getTitle() %></h1>
                <p class="text-xl text-slate-600 mb-6">You scored <span class="font-bold text-indigo-600"><%= result.getScore() %></span> out of <span class="font-bold"><%= result.getTotalQuestions() %></span> questions (<span class="font-bold text-indigo-600"><%= df.format(result.getPercentage()) %>%</span>).</p>
                <p class="text-slate-500">Points: <span class="font-semibold"><%= result.getTotalPoints() %></span>/<%= result.getMaxPoints() %></p>
            </section>

            <!-- Quiz Settings -->
            <section class="bg-white rounded-2xl border border-gray-200 p-6">
                <h2 class="text-lg font-semibold text-slate-700 mb-4">Quiz Settings</h2>
                <ul class="grid grid-cols-2 gap-2 text-sm text-slate-600">
                    <li>Randomize Questions: <span class="font-medium"><%= quiz.isRandomizeQuestions() ? "Yes" : "No" %></span></li>
                    <li>One Page: <span class="font-medium"><%= quiz.isOnePage() ? "Yes" : "No" %></span></li>
                    <li>Immediate Correction: <span class="font-medium"><%= quiz.isImmediateCorrection() ? "Yes" : "No" %></span></li>
                    <li>Practice Mode Enabled: <span class="font-medium"><%= quiz.isPracticeMode() ? "Yes" : "No" %></span></li>
                </ul>
            </section>

            <!-- Past Performance -->
            <section>
                <h2 class="text-xl font-semibold text-slate-700 mb-3">Your Past Attempts</h2>
                <% if(past!=null && !past.isEmpty()) { %>
                <div class="bg-white rounded-xl border border-gray-200 overflow-x-auto">
                    <table class="min-w-full text-sm divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                        <tr><th class="px-4 py-3 text-left">#</th><th class="px-4 py-3 text-left">Score</th><th class="px-4 py-3 text-left">%</th></tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100">
                        <% int idx=1; for(QuizResult r: past){ boolean isCurrent = r.getId()==result.getId(); %>
                        <tr class="<%= isCurrent?"bg-indigo-50 font-semibold":"hover:bg-gray-50" %>"><td class="px-4 py-3"><%= idx++ %></td><td class="px-4 py-3"><%= r.getScore() %>/<%= r.getTotalQuestions() %></td><td class="px-4 py-3"><%= df.format(r.getPercentage()) %>%</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <p class="text-slate-500">This was your first attempt.</p>
                <% } %>
            </section>

            <!-- Top Performers -->
            <section>
                <h2 class="text-xl font-semibold text-slate-700 mb-3">Top Performers</h2>
                <% if(top!=null && !top.isEmpty()) { %>
                <div class="bg-white rounded-xl border border-gray-200 overflow-x-auto">
                    <table class="min-w-full text-sm divide-y divide-gray-200">
                        <thead class="bg-gray-50"><tr><th class="px-4 py-3 text-left">Rank</th><th class="px-4 py-3 text-left">User</th><th class="px-4 py-3 text-left">%</th></tr></thead>
                        <tbody class="divide-y divide-gray-100">
                        <% int rank=1; for(QuizResult r: top){ 
                            boolean me = (currentUser!=null && currentUser.getId()==r.getUserId());
                            User user = userMap.get(r.getUserId());
                            String userName = user != null ? user.getName() : "Unknown User";
                        %>
                        <tr class="<%= me?"bg-indigo-50 font-semibold":"hover:bg-gray-50" %>">
                            <td class="px-4 py-3"><%= rank++ %></td>
                            <td class="px-4 py-3"><%= userName %><%= me ? " (You)" : "" %></td>
                            <td class="px-4 py-3"><%= df.format(r.getPercentage()) %>%</td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <p class="text-slate-500">No data yet.</p>
                <% } %>
            </section>
        </div>
    </div>
</div>
</body>
</html> 