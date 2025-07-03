<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - QuizMaster</title>
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
        .password-strength {
            transition: all 0.3s ease;
        }
        .password-match {
            transition: all 0.3s ease;
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

        <!-- Register Card -->
        <div class="bg-white rounded-2xl p-8 card-hover shadow-xl">
            <!-- Logo Section -->
            <div class="text-center mb-8">
                <h1 class="text-3xl font-bold text-slate-700 mb-2">Join QuizMaster!</h1>
                <p class="text-slate-500">Create your account and start your learning adventure</p>
            </div>

            <!-- Register Form -->
            <form action="${pageContext.request.contextPath}/register" method="post" class="space-y-6" id="registerForm">
                <!-- Username Field -->
                <div>
                    <label for="username" class="block text-sm font-medium text-slate-700 mb-2">
                        Username
                    </label>
                    <div class="relative">
                        <input type="text" 
                               id="username" 
                               name="username" 
                               required
                               value="${param.username}"
                               class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent transition-all"
                               placeholder="Choose a unique username">
                        <div class="absolute right-3 top-3">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                            </svg>
                        </div>
                    </div>
                </div>

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
                               minlength="8"
                               class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent transition-all"
                               placeholder="Create a strong password">
                        <div class="absolute right-3 top-3">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                            </svg>
                        </div>
                    </div>
                    <div class="mt-2">
                        <p class="text-xs text-slate-500 flex items-center">
                            <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                            Password must be at least 8 characters long
                        </p>
                        <div id="passwordStrength" class="mt-1 h-1 bg-gray-200 rounded-full password-strength">
                            <div id="strengthBar" class="h-full rounded-full transition-all duration-300" style="width: 0%;"></div>
                        </div>
                    </div>
                </div>

                <!-- Confirm Password Field -->
                <div>
                    <label for="confirmPassword" class="block text-sm font-medium text-slate-700 mb-2">
                        Confirm Password
                    </label>
                    <div class="relative">
                        <input type="password" 
                               id="confirmPassword" 
                               name="confirmPassword" 
                               required
                               class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent transition-all"
                               placeholder="Confirm your password">
                        <div class="absolute right-3 top-3">
                            <svg class="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                        </div>
                    </div>
                    <div id="passwordMatch" class="mt-2 text-xs password-match" style="display: none;">
                        <p id="matchMessage" class="flex items-center">
                            <svg id="matchIcon" class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                            <span id="matchText">Passwords match!</span>
                        </p>
                    </div>
                </div>

                <!-- Register Button -->
                <button type="submit" 
                        id="submitButton"
                        class="w-full bg-slate-600 text-white py-3 px-4 rounded-xl font-medium hover:bg-slate-700 focus:outline-none focus:ring-2 focus:ring-slate-500 focus:ring-offset-2 transition-all duration-200 transform hover:scale-[1.02] disabled:opacity-50 disabled:cursor-not-allowed">
                    Create Account
                </button>
            </form>

            <!-- Login Link -->
            <div class="mt-8 text-center">
                <div class="relative">
                    <div class="absolute inset-0 flex items-center">
                        <div class="w-full border-t border-gray-200"></div>
                    </div>
                    <div class="relative flex justify-center text-sm">
                        <span class="px-4 bg-white text-slate-500">Already have an account?</span>
                    </div>
                </div>
                <div class="mt-6">
                    <a href="${pageContext.request.contextPath}/login" 
                       class="w-full inline-block bg-gray-100 border border-gray-200 text-slate-700 py-3 px-4 rounded-xl font-medium hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2 transition-all duration-200 text-center">
                        Sign In
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Password strength indicator
        const passwordInput = document.getElementById('password');
        const strengthBar = document.getElementById('strengthBar');
        const confirmPasswordInput = document.getElementById('confirmPassword');
        const passwordMatch = document.getElementById('passwordMatch');
        const matchMessage = document.getElementById('matchMessage');
        const matchIcon = document.getElementById('matchIcon');
        const matchText = document.getElementById('matchText');
        const submitButton = document.getElementById('submitButton');

        passwordInput.addEventListener('input', function() {
            const password = this.value;
            const strength = calculatePasswordStrength(password);
            
            updateStrengthBar(strength);
            checkPasswordMatch();
        });

        confirmPasswordInput.addEventListener('input', checkPasswordMatch);

        function calculatePasswordStrength(password) {
            let strength = 0;
            
            if (password.length >= 8) strength += 25;
            if (password.match(/[a-z]/)) strength += 25;
            if (password.match(/[A-Z]/)) strength += 25;
            if (password.match(/[0-9]/)) strength += 25;
            
            return strength;
        }

        function updateStrengthBar(strength) {
            const strengthBar = document.getElementById('strengthBar');
            strengthBar.style.width = strength + '%';
            
            if (strength < 50) {
                strengthBar.className = 'h-full rounded-full transition-all duration-300 bg-red-400';
            } else if (strength < 75) {
                strengthBar.className = 'h-full rounded-full transition-all duration-300 bg-yellow-400';
            } else {
                strengthBar.className = 'h-full rounded-full transition-all duration-300 bg-green-400';
            }
        }

        function checkPasswordMatch() {
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;
            
            if (confirmPassword.length > 0) {
                passwordMatch.style.display = 'block';
                
                if (password === confirmPassword) {
                    matchMessage.className = 'flex items-center text-green-600';
                    matchIcon.innerHTML = '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>';
                    matchText.textContent = 'Passwords match!';
                    submitButton.disabled = false;
                } else {
                    matchMessage.className = 'flex items-center text-red-600';
                    matchIcon.innerHTML = '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>';
                    matchText.textContent = 'Passwords do not match';
                    submitButton.disabled = true;
                }
            } else {
                passwordMatch.style.display = 'none';
                submitButton.disabled = false;
            }
        }
    </script>
</body>
</html> 