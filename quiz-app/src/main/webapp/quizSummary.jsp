<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Quiz" %>
<%@ page import="models.QuizResult" %>
<%@ page import="models.User" %>
<%@ page import="com.quizmaster.servlet.QuizSummaryServlet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    User creator = (User) request.getAttribute("creator");
    ArrayList<QuizResult> userResults = (ArrayList<QuizResult>) request.getAttribute("userResults");
    List<QuizResult> topPerformersAllTime = (List<QuizResult>) request.getAttribute("topPerformersAllTime");
    List<QuizResult> topPerformersLastDay = (List<QuizResult>) request.getAttribute("topPerformersLastDay");
    List<QuizResult> recentPerformers = (List<QuizResult>) request.getAttribute("recentPerformers");
    QuizSummaryServlet.QuizStats quizStats = (QuizSummaryServlet.QuizStats) request.getAttribute("quizStats");
    Boolean isOwner = (Boolean) request.getAttribute("isOwner");
    User currentUser = (User) request.getAttribute("currentUser");
    String sortBy = (String) request.getAttribute("sortBy");

    DecimalFormat df = new DecimalFormat("#.##");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Summary - <%= quiz.getTitle() %></title>
    <link rel="stylesheet" href="<c:url value='/styles.css'/>">
    <style>
        .quiz-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .stats-card {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1rem;
        }
        .performance-table {
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .action-buttons {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
        }
        .badge-score {
            font-size: 0.9rem;
            padding: 0.5rem 0.8rem;
        }
        .user-link {
            color: #007bff;
            text-decoration: none;
        }
        .user-link:hover {
            text-decoration: underline;
        }
        .sort-controls {
            background: #f8f9fa;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <!-- Quiz Header -->
    <div class="quiz-header">
        <div class="container">
            <div class="row">
                <div class="col-md-8">
                    <h1 class="display-5"><%= quiz.getTitle() %></h1>
                    <p class="lead"><%= quiz.getDescription() %></p>
                    <p class="mb-0">
                        Created by:
                        <a href="ProfileServlet?userId=<%= creator.getId() %>" class="text-white">
                            <strong><%= creator.getName() %></strong>
                        </a>
                    </p>
                </div>
                <div class="col-md-4">
                    <div class="text-end">
                        <div class="action-buttons">
                            <a href="TakeQuizServlet?quizId=<%= quiz.getId() %>"
                               class="btn btn-light btn-lg">
                                <i class="fas fa-play"></i> Take Quiz
                            </a>
                            <% if (quiz.isPracticeMode()) { %>
                            <a href="TakeQuizServlet?quizId=<%= quiz.getId() %>&practice=true"
                               class="btn btn-outline-light">
                                <i class="fas fa-dumbbell"></i> Practice Mode
                            </a>
                            <% } %>
                            <% if (isOwner != null && isOwner) { %>
                            <a href="EditQuizServlet?quizId=<%= quiz.getId() %>"
                               class="btn btn-warning">
                                <i class="fas fa-edit"></i> Edit Quiz
                            </a>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <!-- Left Column - User Performance & Quiz Info -->
            <div class="col-lg-8">
                <% if (currentUser != null && userResults != null && !userResults.isEmpty()) { %>
                <!-- User's Past Performance -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">Your Past Performance</h5>
                        <div class="sort-controls">
                            <label class="form-label me-2">Sort by:</label>
                            <select class="form-select form-select-sm" style="width: auto; display: inline-block;"
                                    onchange="sortResults(this.value)">
                                <option value="date" <%= "date".equals(sortBy) ? "selected" : "" %>>Date</option>
                                <option value="percentage" <%= "percentage".equals(sortBy) ? "selected" : "" %>>Percentage</option>
                                <option value="score" <%= "score".equals(sortBy) ? "selected" : "" %>>Score</option>
                            </select>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th>Attempt</th>
                                    <th>Score</th>
                                    <th>Percentage</th>
                                    <th>Points</th>
                                    <th>Mode</th>
                                </tr>
                                </thead>
                                <tbody>
                                <%
                                    int attemptNum = 1;
                                    for (QuizResult result : userResults) {
                                %>
                                <tr>
                                    <td><strong>#<%= attemptNum++ %></strong></td>
                                    <td>
                                                        <span class="badge badge-score bg-primary">
                                                            <%= result.getScore() %>/<%= result.getTotalQuestions() %>
                                                        </span>
                                    </td>
                                    <td>
                                                        <span class="badge badge-score <%= result.getPercentage() >= 80 ? "bg-success" :
                                                                                             result.getPercentage() >= 60 ? "bg-warning text-dark" : "bg-danger" %>">
                                                            <%= df.format(result.getPercentage()) %>%
                                                        </span>
                                    </td>
                                    <td><%= result.getTotalPoints() %>/<%= result.getMaxPoints() %></td>
                                    <td>
                                                        <span class="badge <%= result.isPracticeMode() ? "bg-info" : "bg-success" %>">
                                                            <%= result.isPracticeMode() ? "Practice" : "Official" %>
                                                        </span>
                                    </td>
                                </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <% } else if (currentUser != null) { %>
                <!-- No attempts yet -->
                <div class="alert alert-info">
                    <h5>No attempts yet</h5>
                    <p>You haven't taken this quiz yet. Click "Take Quiz" to get started!</p>
                </div>
                <% } %>

                <!-- Recent Test Takers -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">Recent Test Takers</h5>
                    </div>
                    <div class="card-body p-0">
                        <% if (recentPerformers != null && !recentPerformers.isEmpty()) { %>
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th>User</th>
                                    <th>Score</th>
                                    <th>Percentage</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% for (QuizResult result : recentPerformers) { %>
                                <tr>
                                    <td>
                                        <a href="ProfileServlet?userId=<%= result.getUserId() %>" class="user-link">
                                            User #<%= result.getUserId() %>
                                        </a>
                                    </td>
                                    <td>
                                                        <span class="badge badge-score bg-primary">
                                                            <%= result.getScore() %>/<%= result.getTotalQuestions() %>
                                                        </span>
                                    </td>
                                    <td>
                                                        <span class="badge badge-score <%= result.getPercentage() >= 80 ? "bg-success" :
                                                                                             result.getPercentage() >= 60 ? "bg-warning text-dark" : "bg-danger" %>">
                                                            <%= df.format(result.getPercentage()) %>%
                                                        </span>
                                    </td>
                                </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% } else { %>
                        <div class="text-center py-4">
                            <p class="text-muted">No recent attempts found.</p>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- Right Column - Leaderboards & Stats -->
            <div class="col-lg-4">
                <!-- Quiz Statistics -->
                <div class="stats-card">
                    <h5 class="mb-3">Quiz Statistics</h5>
                    <div class="row text-center">
                        <div class="col-6">
                            <div class="border-end">
                                <h4 class="text-primary mb-1"><%= quizStats.getTotalAttempts() %></h4>
                                <small class="text-muted">Total Attempts</small>
                            </div>
                        </div>
                        <div class="col-6">
                            <h4 class="text-success mb-1"><%= df.format(quizStats.getAveragePercentage()) %>%</h4>
                            <small class="text-muted">Average Score</small>
                        </div>
                    </div>
                    <hr>
                    <div class="row text-center">
                        <div class="col-4">
                            <div class="border-end">
                                <h6 class="text-success mb-1"><%= df.format(quizStats.getMaxPercentage()) %>%</h6>
                                <small class="text-muted">Highest</small>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="border-end">
                                <h6 class="text-info mb-1"><%= df.format(quizStats.getMedianPercentage()) %>%</h6>
                                <small class="text-muted">Median</small>
                            </div>
                        </div>
                        <div class="col-4">
                            <h6 class="text-danger mb-1"><%= df.format(quizStats.getMinPercentage()) %>%</h6>
                            <small class="text-muted">Lowest</small>
                        </div>
                    </div>
                </div>

                <!-- Top Performers All Time -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">🏆 Top Performers (All Time)</h5>
                    </div>
                    <div class="card-body p-0">
                        <% if (topPerformersAllTime != null && !topPerformersAllTime.isEmpty()) { %>
                        <div class="list-group list-group-flush">
                            <%
                                int rank = 1;
                                for (QuizResult result : topPerformersAllTime) {
                            %>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <div>
                                    <span class="badge bg-gold me-2"><%= rank++ %></span>
                                    <a href="ProfileServlet?userId=<%= result.getUserId() %>" class="user-link">
                                        User #<%= result.getUserId() %>
                                    </a>
                                </div>
                                <div class="text-end">
                                                <span class="badge badge-score bg-success">
                                                    <%= df.format(result.getPercentage()) %>%
                                                </span>
                                    <br>
                                    <small class="text-muted">
                                        <%= result.getScore() %>/<%= result.getTotalQuestions() %>
                                    </small>
                                </div>
                            </div>
                            <% } %>
                        </div>
                        <% } else { %>
                        <div class="text-center py-4">
                            <p class="text-muted">No results available yet.</p>
                        </div>
                        <% } %>
                    </div>
                </div>

                <!-- Top Performers Last Day -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">🔥 Top Performers (Last Day)</h5>
                    </div>
                    <div class="card-body p-0">
                        <% if (topPerformersLastDay != null && !topPerformersLastDay.isEmpty()) { %>
                        <div class="list-group list-group-flush">
                            <%
                                int dailyRank = 1;
                                for (QuizResult result : topPerformersLastDay) {
                            %>
                            <div class="list-group-item d-flex justify-content-between align-items-center">
                                <div>
                                    <span class="badge bg-warning text-dark me-2"><%= dailyRank++ %></span>
                                    <a href="ProfileServlet?userId=<%= result.getUserId() %>" class="user-link">
                                        User #<%= result.getUserId() %>
                                    </a>
                                </div>
                                <div class="text-end">
                                                <span class="badge badge-score bg-success">
                                                    <%= df.format(result.getPercentage()) %>%
                                                </span>
                                    <br>
                                    <small class="text-muted">
                                        <%= result.getScore() %>/<%= result.getTotalQuestions() %>
                                    </small>
                                </div>
                            </div>
                            <% } %>
                        </div>
                        <% } else { %>
                        <div class="text-center py-4">
                            <p class="text-muted">No recent results available.</p>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function sortResults(sortBy) {
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('sortBy', sortBy);
        window.location.search = urlParams.toString();
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/your-fontawesome-kit.js" crossorigin="anonymous"></script>
</body>
</html>