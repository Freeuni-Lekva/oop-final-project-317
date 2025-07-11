<%@ page import="java.util.*, models.Quiz, models.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Creations - QuizMaster</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .card-hover{transition:all .3s ease}
        .card-hover:hover{transform:translateY(-4px);box-shadow:0 12px 24px rgba(0,0,0,.15)}
        .quiz-card-1{background:radial-gradient(circle at center,#8387C3 90%,#676a9c 100%)}
        .quiz-card-2{background:radial-gradient(circle at center,#CEB5D4 90%,#a68eab 100%)}
        .quiz-card-3{background:radial-gradient(circle at center,#537E72 90%,#406358 100%)}
        .quiz-card-4{background:radial-gradient(circle at center,#7D9FC0 90%,#5f7b96 100%)}
    </style>
</head>
<body class="overflow-hidden bg-gray-50">
<div class="flex h-screen">
    <!-- Sidebar (simple back link)-->
    <div class="w-72 bg-white border-r border-gray-200 p-6 flex flex-col">
        <h1 class="text-2xl font-bold text-slate-700 mb-6 cursor-pointer" onclick="window.location.href='index.jsp'">&larr; Back</h1>
        <h2 class="text-xl font-semibold text-slate-700">My Created Quizzes</h2>
    </div>

    <!-- Main Content -->
    <div class="flex-1 p-8 overflow-y-auto">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <%
                List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
                if (quizzes == null || quizzes.isEmpty()) {
            %>
            <div class="col-span-full text-center text-slate-600">You have not created any quizzes yet.</div>
            <%
                } else {
                    int idx = 0;
                    String[] cardClasses = {"quiz-card-1", "quiz-card-2", "quiz-card-3", "quiz-card-4"};
                    for (Quiz q : quizzes) {
                        String cardClass = cardClasses[idx % cardClasses.length];
                        idx++;
            %>
            <div class="<%= cardClass %> rounded-2xl p-6 card-hover cursor-pointer" onclick="window.location.href='QuizSummary?quizId=<%= q.getId() %>'">
                <h3 class="text-xl font-bold text-white mb-2"><%= q.getTitle() %></h3>
                <p class="text-white text-sm opacity-90 mb-4"><%= q.getDescription() == null ? "" : q.getDescription() %></p>
                <div class="flex items-center space-x-3 text-white/90 text-xs">
                    <span>Created: <%= q.getCreatedDate().toLocalDate() %></span>
                </div>
            </div>
            <%
                    }
                }
            %>
        </div>
    </div>
</div>
</body>
</html> 