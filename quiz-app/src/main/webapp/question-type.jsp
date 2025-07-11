<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Select Question Type</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* reuse quick minimal styling */
        .card-hover{transition:all .3s ease}
        .card-hover:hover{transform:translateY(-4px);box-shadow:0 12px 24px rgba(0,0,0,.15)}
    </style>
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-6">
<div class="max-w-3xl w-full bg-white rounded-2xl border border-gray-200 p-8 card-hover">
    <h1 class="text-2xl font-bold text-slate-700 mb-2 text-center">
        Choose type for question <%= request.getAttribute("currentIndex") %> of <%= request.getAttribute("totalQuestions") %>
    </h1>
    <p class="text-slate-600 text-center mb-6">Pick how this question will be presented</p>

    <form action="question-type" method="post" class="space-y-4 max-w-md mx-auto">
        <input type="hidden" name="currentIndex" value="<%= request.getAttribute("currentIndex") %>">

        <!-- drop-down -->
        <label for="questionType" class="block text-sm font-medium text-slate-700 mb-1">Question Type</label>
        <select id="questionType" name="questionType" required
                class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent">
            <option value="MultipleChoice">Multiple Choice (single answer)</option>
            <option value="MultipleChoiceMultipleAnswers">Multiple Choice (multiple answers)</option>
            <option value="FillInBlank">Fill in the Blank</option>
            <option value="QuestionResponse">Question Response</option>
            <option value="PictureResponse">Picture Response</option>
            <option value="MultiAnswer">Multi-Answer</option>
        </select>

        <div class="flex justify-end">
            <button type="submit"
                    class="px-6 py-3 bg-blue-600 text-white rounded-xl font-medium hover:bg-blue-700 transition-colors">
                Next
            </button>
        </div>
    </form>
</div>
</body>
</html> 