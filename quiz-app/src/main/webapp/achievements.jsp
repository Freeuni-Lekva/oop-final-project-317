<%@ page import="models.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Achievements - QuizMaster</title>
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

.achievement-card {
    background: white;
    border: 2px solid #e5e7eb;
    border-radius: 1rem;
    padding: 1.5rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-shadow: 0 4px 16px rgba(73, 89, 107, 0.07);
    min-width: 180px;
    min-height: 180px;
    transition: all 0.3s ease;
}

.achievement-card.completed {
    background: linear-gradient(to bottom right, #dcfce7, #ffffff);
    border-color: #86efac;
}

.achievement-card .progress {
    width: 100%;
    height: 6px;
    background-color: #e5e7eb;
    border-radius: 3px;
    margin-top: 1rem;
    overflow: hidden;
}

.achievement-card .progress-bar {
    height: 100%;
    background-color: #22c55e;
    border-radius: 3px;
    transition: width 0.3s ease;
}

.achievement-card .progress-text {
    font-size: 0.875rem;
    color: #6b7280;
    margin-top: 0.5rem;
}

.achievement-card .icon {
    width: 3rem;
    height: 3rem;
    margin-bottom: 1rem;
}

.achievement-card .title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #374151;
    margin-bottom: 0.5rem;
    text-align: center;
}

.achievement-card .desc {
    font-size: 0.95rem;
    color: #6b7280;
    text-align: center;
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
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='QuizHistory.jsp'">
                <div class="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Quiz History</span>
            </div>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='create-quiz.jsp'">
                <div class="w-8 h-8 bg-green-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">My Creations</span>
            </div>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3 bg-yellow-50" onclick="window.location.href='achievements'">
                <div class="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Achievements</span>
            </div>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='index.jsp'">
                <div class="w-8 h-8 bg-red-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Popular Quizzes</span>
            </div>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='index.jsp'">
                <div class="w-8 h-8 bg-purple-50 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
                <span class="text-slate-700 font-medium">Recent Quizzes</span>
            </div>
            <div class="sidebar-item p-3 rounded-lg cursor-pointer flex items-center space-x-3" onclick="window.location.href='index.jsp'">
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
                <div class="flex-1">
                    <h1 class="text-2xl font-bold text-slate-700">Achievements</h1>
                    <p class="text-slate-500 text-sm">Unlock milestones as you learn and play!</p>
                </div>
            </div>
        </div>
        <!-- Achievements Grid -->
        <div class="flex-1 p-6 overflow-y-auto">
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                <!-- Achievement 1: Amateur Author -->
                <%
                    int quizzesCreated = request.getAttribute("quizzesCreated") != null ? (Integer)request.getAttribute("quizzesCreated") : 0;
                    int quizzesTaken = request.getAttribute("quizzesTaken") != null ? (Integer)request.getAttribute("quizzesTaken") : 0;
                    int bestScores = request.getAttribute("bestScores") != null ? (Integer)request.getAttribute("bestScores") : 0;
                    int practiceQuizzes = request.getAttribute("practiceQuizzes") != null ? (Integer)request.getAttribute("practiceQuizzes") : 0;
                %>
                <div class="achievement-card card-hover <%= quizzesCreated >= 1 ? "completed" : "" %>">
                    <div class="icon bg-blue-100 rounded-full flex items-center justify-center">
                        <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                        </svg>
                    </div>
                    <div class="title">Amateur Author</div>
                    <div class="desc">Created 1 quiz</div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <%= Math.min(quizzesCreated * 100, 100) %>%"></div>
                    </div>
                    <div class="progress-text"><%= quizzesCreated %>/1 quizzes</div>
                </div>

                <!-- Achievement 2: Prolific Author -->
                <div class="achievement-card card-hover <%= quizzesCreated >= 5 ? "completed" : "" %>">
                    <div class="icon bg-green-100 rounded-full flex items-center justify-center">
                        <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                        </svg>
                    </div>
                    <div class="title">Prolific Author</div>
                    <div class="desc">Created 5 quizzes</div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <%= Math.min(quizzesCreated * 20, 100) %>%"></div>
                    </div>
                    <div class="progress-text"><%= quizzesCreated %>/5 quizzes</div>
                </div>

                <!-- Achievement 3: Prodigious Author -->
                <div class="achievement-card card-hover <%= quizzesCreated >= 10 ? "completed" : "" %>">
                    <div class="icon bg-purple-100 rounded-full flex items-center justify-center">
                        <svg class="w-8 h-8 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 0V4m0 16v-4"/>
                        </svg>
                    </div>
                    <div class="title">Prodigious Author</div>
                    <div class="desc">Created 10 quizzes</div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <%= Math.min(quizzesCreated * 10, 100) %>%"></div>
                    </div>
                    <div class="progress-text"><%= quizzesCreated %>/10 quizzes</div>
                </div>

                <!-- Achievement 4: Quiz Machine -->
                <div class="achievement-card card-hover <%= quizzesTaken >= 10 ? "completed" : "" %>">
                    <div class="icon bg-yellow-100 rounded-full flex items-center justify-center">
                        <svg class="w-8 h-8 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M9 21V7a3 3 0 016 0v14"/>
                        </svg>
                    </div>
                    <div class="title">Quiz Machine</div>
                    <div class="desc">Took 10 quizzes</div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <%= Math.min(quizzesTaken * 10, 100) %>%"></div>
                    </div>
                    <div class="progress-text"><%= quizzesTaken %>/10 quizzes</div>
                </div>

                <!-- Achievement 5: I am the Greatest -->
                <div class="achievement-card card-hover <%= bestScores >= 1 ? "completed" : "" %>">
                    <div class="icon bg-red-100 rounded-full flex items-center justify-center">
                        <svg class="w-8 h-8 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3"/>
                        </svg>
                    </div>
                    <div class="title">I am the Greatest</div>
                    <div class="desc">Have 1 best score</div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <%= Math.min(bestScores * 100, 100) %>%"></div>
                    </div>
                    <div class="progress-text"><%= bestScores %>/1 best scores</div>
                </div>

                <!-- Achievement 6: Practice Makes Perfect -->
                <div class="achievement-card card-hover <%= practiceQuizzes >= 1 ? "completed" : "" %>">
                    <div class="icon bg-indigo-100 rounded-full flex items-center justify-center">
                        <svg class="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 17l4 4 4-4m0-5a4 4 0 10-8 0 4 4 0 008 0z"/>
                        </svg>
                    </div>
                    <div class="title">Practice Makes Perfect</div>
                    <div class="desc">Took a quiz in practice mode</div>
                    <div class="progress">
                        <div class="progress-bar" style="width: <%= Math.min(practiceQuizzes * 100, 100) %>%"></div>
                    </div>
                    <div class="progress-text"><%= practiceQuizzes %>/1 practice quizzes</div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html> 