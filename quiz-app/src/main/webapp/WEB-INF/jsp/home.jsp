<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Quiz-O-Rama</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: transform 0.3s ease;
        }
        .card:hover {
            transform: translateY(-2px);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0 !important;
            font-weight: 600;
        }
        .quiz-card {
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .quiz-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
        .achievement-badge {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #333;
            border-radius: 20px;
            padding: 0.5rem 1rem;
            margin: 0.25rem;
            display: inline-block;
            font-size: 0.875rem;
            font-weight: 600;
        }
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
        }
        .btn-primary:hover {
            background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
        }
        .message-indicator {
            background: #dc3545;
            color: white;
            border-radius: 50%;
            padding: 0.25rem 0.5rem;
            font-size: 0.75rem;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand fw-bold" href="home">
                <i class="fas fa-brain"></i> Quiz-O-Rama
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="home"><i class="fas fa-home"></i> Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="quiz?action=list"><i class="fas fa-list"></i> Browse Quizzes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="create-quiz"><i class="fas fa-plus"></i> Create Quiz</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="messages">
                            <i class="fas fa-envelope"></i> Messages
                            <c:if test="${unreadCount > 0}">
                                <span class="message-indicator">${unreadCount}</span>
                            </c:if>
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user"></i> ${username}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="profile"><i class="fas fa-user-edit"></i> Profile</a></li>
                            <li><a class="dropdown-item" href="history"><i class="fas fa-history"></i> Quiz History</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <!-- Success/Error Messages -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> ${sessionScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="message" scope="session"/>
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> ${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Welcome Section -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card stats-card">
                    <div class="card-body">
                        <h1 class="card-title mb-0">Welcome back, ${username}! <i class="fas fa-hand-wave"></i></h1>
                        <p class="card-text mt-2 mb-0">Ready to challenge your mind today?</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Main Content -->
            <div class="col-lg-8">
                <!-- Popular Quizzes -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-fire"></i> Popular Quizzes</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty popularQuizzes}">
                                <div class="row">
                                    <c:forEach var="quiz" items="${popularQuizzes}" varStatus="status">
                                        <c:if test="${status.index < 6}">
                                            <div class="col-md-6 mb-3">
                                                <div class="card quiz-card h-100" onclick="location.href='quiz?id=${quiz.quizId}'">
                                                    <div class="card-body">
                                                        <h6 class="card-title">${quiz.title}</h6>
                                                        <p class="card-text text-muted small">${quiz.description}</p>
                                                        <div class="d-flex justify-content-between align-items-center">
                                                            <small class="text-muted">by ${quiz.creatorUsername}</small>
                                                            <span class="badge bg-primary">Take Quiz</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div>
                                <div class="text-center">
                                    <a href="quiz?action=list" class="btn btn-primary">View All Quizzes</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4">
                                    <i class="fas fa-question-circle fa-3x text-muted mb-3"></i>
                                    <p class="text-muted">No quizzes available yet. Be the first to create one!</p>
                                    <a href="create-quiz" class="btn btn-primary">Create Your First Quiz</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- My Quizzes -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-user-edit"></i> My Quizzes</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty userQuizzes}">
                                <div class="row">
                                    <c:forEach var="quiz" items="${userQuizzes}" varStatus="status">
                                        <c:if test="${status.index < 4}">
                                            <div class="col-md-6 mb-3">
                                                <div class="card quiz-card h-100">
                                                    <div class="card-body">
                                                        <h6 class="card-title">${quiz.title}</h6>
                                                        <p class="card-text text-muted small">${quiz.description}</p>
                                                        <div class="d-flex gap-2">
                                                            <a href="quiz?id=${quiz.quizId}" class="btn btn-sm btn-outline-primary">View</a>
                                                            <a href="create-quiz?action=edit&id=${quiz.quizId}" class="btn btn-sm btn-outline-secondary">Edit</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-3">
                                    <p class="text-muted mb-2">You haven't created any quizzes yet.</p>
                                    <a href="create-quiz" class="btn btn-primary btn-sm">Create Your First Quiz</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Sidebar -->
            <div class="col-lg-4">
                <!-- Recent Activity -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h6 class="mb-0"><i class="fas fa-clock"></i> Recent Activity</h6>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty recentActivity}">
                                <c:forEach var="attempt" items="${recentActivity}">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <div>
                                            <div class="fw-bold small">${attempt.quizTitle}</div>
                                            <div class="text-muted small">
                                                Score: ${attempt.score}/${attempt.totalQuestions} 
                                                (<fmt:formatNumber value="${attempt.percentage}" maxFractionDigits="1"/>%)
                                            </div>
                                        </div>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${attempt.attemptDate}" pattern="MMM dd"/>
                                        </small>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small">No recent activity</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Achievements -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h6 class="mb-0"><i class="fas fa-trophy"></i> Achievements</h6>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty achievements}">
                                <c:forEach var="achievement" items="${achievements}">
                                    <div class="achievement-badge" title="${achievement.type.description}">
                                        <i class="${achievement.type.icon}"></i> ${achievement.type.name}
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small">No achievements yet. Start taking quizzes to earn them!</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Friends -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h6 class="mb-0"><i class="fas fa-users"></i> Friends</h6>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty friends}">
                                <c:forEach var="friendship" items="${friends}" varStatus="status">
                                    <c:if test="${status.index < 5}">
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <span class="small">${friendship.getOtherUsername(userId)}</span>
                                            <a href="messages?action=compose&to=${friendship.getOtherUsername(userId)}" 
                                               class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-envelope"></i>
                                            </a>
                                        </div>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${friends.size() > 5}">
                                    <small class="text-muted">and ${friends.size() - 5} more...</small>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small">No friends yet.</p>
                            </c:otherwise>
                        </c:choose>
                        <div class="mt-2">
                            <a href="messages?action=compose" class="btn btn-sm btn-primary">Add Friends</a>
                        </div>
                    </div>
                </div>

                <!-- Friend Requests -->
                <c:if test="${not empty pendingRequests}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h6 class="mb-0"><i class="fas fa-user-plus"></i> Friend Requests</h6>
                        </div>
                        <div class="card-body">
                            <c:forEach var="request" items="${pendingRequests}">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="small">${request.username1}</span>
                                    <div>
                                        <a href="messages?action=respond&messageId=${request.friendshipId}&response=accept" 
                                           class="btn btn-sm btn-success me-1">Accept</a>
                                        <a href="messages?action=respond&messageId=${request.friendshipId}&response=reject" 
                                           class="btn btn-sm btn-outline-secondary">Decline</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 