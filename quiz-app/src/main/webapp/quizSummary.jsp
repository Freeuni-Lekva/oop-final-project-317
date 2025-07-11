<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Quiz,models.QuizResult,models.User" %>
<%@ page import="com.quizmaster.servlet.QuizSummaryServlet" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    User creator = (User) request.getAttribute("creator");
    List<QuizResult> userResults = (List<QuizResult>) request.getAttribute("userResults");
    List<QuizResult> recentPerformers = (List<QuizResult>) request.getAttribute("recentPerformers");
    QuizSummaryServlet.QuizStats stats = (QuizSummaryServlet.QuizStats) request.getAttribute("quizStats");
    DecimalFormat df = new DecimalFormat("#.#");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= quiz.getTitle() %> - Summary</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="overflow-hidden bg-gray-50">
<div class="flex h-screen">
    <%-- Sidebar copied from index.jsp --%>
    <div class="w-80 bg-white border-r border-gray-200 p-6 flex flex-col">
        <!-- Logo -->
        <div class="mb-8 cursor-pointer" onclick="window.location.href='index.jsp'">
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
                <div class="w-8 h-8 bg-green-50 rounded-lg flex items-center justify-center"><svg class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/></svg></div>
                <span class="text-slate-700 font-medium">My Creations</span>
            </div>
            <!-- Other nav items omitted for brevity -->
        </nav>
    </div>

    <!-- Right side (content) -->
    <div class="flex-1 flex flex-col">
        <!-- Top Bar (simple placeholder) -->
        <div class="bg-white border-b border-gray-200 p-6 flex items-center justify-between">
            <h2 class="text-lg font-semibold text-slate-700">Quiz Summary</h2>
        </div>

        <!-- Content scroll area -->
        <div class="flex-1 overflow-y-auto">
            <!-- Header -->
<header class="bg-gradient-to-r from-indigo-500 to-purple-600 text-white py-12 shadow-md">
    <div class="max-w-5xl mx-auto px-6 flex flex-col md:flex-row md:items-end md:justify-between gap-6">
        <div>
            <h1 class="text-4xl font-bold mb-2"><%= quiz.getTitle() %></h1>
            <p class="opacity-90 mb-4"><%= quiz.getDescription() %></p>
            <p class="text-sm">Created by <a href="profile?userId=<%= creator.getId() %>" class="underline font-semibold"><%= creator.getName() %></a></p>
        </div>
        <!-- Action buttons -->
        <div class="flex flex-wrap gap-3">
            <a href="take-quiz?quizId=<%= quiz.getId() %>" class="px-5 py-3 bg-white/20 rounded-lg font-medium hover:bg-white/30 transition">Take Quiz</a>
            <% if (quiz.isPracticeMode()) { %>
            <a href="take-quiz?quizId=<%= quiz.getId() %>&practice=true" class="px-5 py-3 bg-white/10 rounded-lg font-medium hover:bg-white/20 transition">Practice Mode</a>
            <% } %>
            <% Boolean isOwner=(Boolean)request.getAttribute("isOwner"); if(isOwner!=null && isOwner){ %>
            <a href="edit-quiz?quizId=<%= quiz.getId() %>" class="px-5 py-3 bg-yellow-400 text-slate-800 rounded-lg font-medium hover:bg-yellow-500 transition">Edit Quiz</a>
            <% } %>
        </div>
    </div>
</header>

<!-- Main -->
<main class="flex-1 max-w-5xl mx-auto px-6 py-8 space-y-10">
    <!-- Overall Stats -->
    <section class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-white rounded-xl shadow p-4 text-center">
            <p class="text-sm text-slate-500">Attempts</p>
            <p class="text-2xl font-bold text-indigo-600"><%= stats != null ? stats.getTotalAttempts() : 0 %></p>
        </div>
        <div class="bg-white rounded-xl shadow p-4 text-center">
            <p class="text-sm text-slate-500">Average %</p>
            <p class="text-2xl font-bold text-indigo-600"><%= stats != null ? df.format(stats.getAveragePercentage()) : "0" %>%</p>
        </div>
        <div class="bg-white rounded-xl shadow p-4 text-center">
            <p class="text-sm text-slate-500">Best %</p>
            <p class="text-2xl font-bold text-green-600"><%= stats != null ? df.format(stats.getMaxPercentage()) : "0" %>%</p>
        </div>
        <div class="bg-white rounded-xl shadow p-4 text-center">
            <p class="text-sm text-slate-500">Worst %</p>
            <p class="text-2xl font-bold text-red-600"><%= stats != null ? df.format(stats.getMinPercentage()) : "0" %>%</p>
        </div>
    </section>

    <!-- Your Attempts (if logged in) -->
    <% String sortBy=(String)request.getAttribute("sortBy"); if (userResults != null && !userResults.isEmpty()) { %>
    <section>
        <div class="flex items-center justify-between mb-3">
            <h2 class="text-xl font-semibold text-slate-700">Your Attempts</h2>
            <div class="text-sm flex items-center gap-2">
                <label for="sortSel">Sort by</label>
                <select id="sortSel" class="border rounded px-2 py-1" onchange="location.href='QuizSummary?quizId=<%=quiz.getId()%>&sortBy='+this.value">
                    <option value="date" <%= "date".equals(sortBy)?"selected":"" %>>Date</option>
                    <option value="percentage" <%= "percentage".equals(sortBy)?"selected":"" %>>Percentage</option>
                    <option value="score" <%= "score".equals(sortBy)?"selected":"" %>>Score</option>
                </select>
            </div>
        </div>
        <div class="overflow-x-auto bg-white rounded-xl shadow">
            <table class="min-w-full divide-y divide-gray-200 text-sm">
                <thead class="bg-gray-50">
                <tr>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">#</th>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">Score</th>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">%</th>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">Points</th>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">Mode</th>
                </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                <% int idx=1; for(QuizResult r : userResults){ %>
                <tr class="hover:bg-gray-50">
                    <td class="px-4 py-3"><%= idx++ %></td>
                    <td class="px-4 py-3"><%= r.getScore() %>/<%= r.getTotalQuestions() %></td>
                    <td class="px-4 py-3"><%= df.format(r.getPercentage()) %>%</td>
                    <td class="px-4 py-3"><%= r.getTotalPoints() %>/<%= r.getMaxPoints() %></td>
                    <td class="px-4 py-3"><%= r.isPracticeMode()?"Practice":"Official" %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>
    <% } %>

    <!-- Recent Performers -->
    <section>
        <h2 class="text-xl font-semibold text-slate-700 mb-3">Recent Performers</h2>
        <% if (recentPerformers != null && !recentPerformers.isEmpty()) { %>
        <div class="overflow-x-auto bg-white rounded-xl shadow">
            <table class="min-w-full divide-y divide-gray-200 text-sm">
                <thead class="bg-gray-50">
                <tr>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">User ID</th>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">Score</th>
                    <th class="px-4 py-3 text-left font-medium text-slate-600">%</th>
                </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                <% for(QuizResult r : recentPerformers){ %>
                <tr class="hover:bg-gray-50">
                    <td class="px-4 py-3"><%= r.getUserId() %></td>
                    <td class="px-4 py-3"><%= r.getScore() %>/<%= r.getTotalQuestions() %></td>
                    <td class="px-4 py-3"><%= df.format(r.getPercentage()) %>%</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
        <p class="text-slate-500">No attempts yet.</p>
        <% } %>
    </section>

    <!-- Top performers -->
    <section class="grid md:grid-cols-2 gap-8">
        <!-- All time -->
        <div>
            <h2 class="text-xl font-semibold text-slate-700 mb-3">🏆 Top Performers (All Time)</h2>
            <% List<QuizResult> topAll=(List<QuizResult>)request.getAttribute("topPerformersAllTime"); if(topAll!=null && !topAll.isEmpty()){ %>
            <div class="bg-white rounded-xl shadow overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200 text-sm">
                    <thead class="bg-gray-50"><tr><th class="px-4 py-3 text-left">Rank</th><th class="px-4 py-3 text-left">User</th><th class="px-4 py-3 text-left">%</th></tr></thead>
                    <tbody class="divide-y divide-gray-100">
                    <% int rnk=1; for(QuizResult qr: topAll){ %>
                        <tr class="hover:bg-gray-50"><td class="px-4 py-3"><%= rnk++ %></td><td class="px-4 py-3">User #<%= qr.getUserId() %></td><td class="px-4 py-3"><%= df.format(qr.getPercentage()) %>%</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %><p class="text-slate-500">No data yet.</p><% } %>
        </div>
        <!-- Last day -->
        <div>
            <h2 class="text-xl font-semibold text-slate-700 mb-3">🔥 Top Performers (Last 24h)</h2>
            <% List<QuizResult> topDay=(List<QuizResult>)request.getAttribute("topPerformersLastDay"); if(topDay!=null && !topDay.isEmpty()){ %>
            <div class="bg-white rounded-xl shadow overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200 text-sm">
                    <thead class="bg-gray-50"><tr><th class="px-4 py-3 text-left">Rank</th><th class="px-4 py-3 text-left">User</th><th class="px-4 py-3 text-left">%</th></tr></thead>
                    <tbody class="divide-y divide-gray-100">
                    <% int rnk=1; for(QuizResult qr: topDay){ %>
                        <tr class="hover:bg-gray-50"><td class="px-4 py-3"><%= rnk++ %></td><td class="px-4 py-3">User #<%= qr.getUserId() %></td><td class="px-4 py-3"><%= df.format(qr.getPercentage()) %>%</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %><p class="text-slate-500">No data yet.</p><% } %>
        </div>
    </section>
</main>

</body>
</html>