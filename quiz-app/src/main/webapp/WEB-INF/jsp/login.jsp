<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - QuizMaster</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
    <style>
        body {
            background-color: #f2f0e9;
        }
        .auth-container {
            background: #f2f0e9;
        }
        .back-link {
            color: #64748b;
            transition: all 0.2s ease;
        }
        .back-link:hover {
            color: #475569;
            transform: translateX(-2px);
        }
    </style>
</head>
<body class="auth-container min-h-screen flex items-center justify-center p-4">
    <div class="w-full max-w-md">
        <!-- Back to Home Link -->
        <div class="text-center mb-6">
            <a href="${pageContext.request.contextPath}/" class="inline-flex items-center back-link font-medium">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Home
            </a>
        </div>

        <!-- Login Card -->
        <div class="bg-white rounded-2xl p-8 card-hover shadow-xl">
            <!-- Logo Section -->
            <div class="text-center mb-8">
                <h1 class="text-3xl font-bold text-slate-700 mb-2">Welcome Back!</h1>
                <p class="text-slate-500">Sign in to continue your learning journey</p>
            </div>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="space-y-6">
                <!-- Email Field -->
                <div>
                    <label for="email" class="block text-sm font-medium text-slate-700 mb-2">
                        Email Address
                    </label>
                    <div class="relative">
                        <input type="email" 
                               id="email" 
                               name="email" 
                               required
                               value="${param.email}"
                               class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent transition-all"
                               placeholder="Enter your email address">
                        <div class="absolute right-3 top-3">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207"/>
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- Password Field -->
                <div>
                    <label for="password" class="block text-sm font-medium text-slate-700 mb-2">
                        Password
                    </label>
                    <div class="relative">
                        <input type="password" 
                               id="password" 
                               name="password" 
                               required
                               class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent transition-all"
                               placeholder="Enter your password">
                        <div class="absolute right-3 top-3">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- Login Button -->
                <button type="submit" 
                        class="w-full bg-slate-600 text-white py-3 px-4 rounded-xl font-medium hover:bg-slate-700 focus:outline-none focus:ring-2 focus:ring-slate-500 focus:ring-offset-2 transition-all duration-200 transform hover:scale-[1.02]">
                    Sign In
                </button>
            </form>

            <!-- Register Link -->
            <div class="mt-8 text-center">
                <div class="relative">
                    <div class="absolute inset-0 flex items-center">
                        <div class="w-full border-t border-gray-200"></div>
                    </div>
                    <div class="relative flex justify-center text-sm">
                        <span class="px-4 bg-white text-slate-500">Don't have an account?</span>
                    </div>
                </div>
                <div class="mt-6">
                    <a href="${pageContext.request.contextPath}/register" 
                       class="w-full inline-block bg-gray-100 border border-gray-200 text-slate-700 py-3 px-4 rounded-xl font-medium hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2 transition-all duration-200 text-center">
                        Create New Account
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html> 