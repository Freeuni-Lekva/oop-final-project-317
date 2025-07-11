<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Question</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-6">
<div class="max-w-3xl w-full bg-white rounded-2xl border border-gray-200 p-8">
    <h1 class="text-2xl font-bold text-slate-700 mb-2 text-center">
        Question <%= request.getAttribute("currentIndex") %> / <%= request.getAttribute("totalQuestions") %>
    </h1>
    <p class="text-slate-600 text-center mb-6 flex items-center justify-center gap-4">
        <span>Type: <%= request.getAttribute("questionType") %></span>
        <a href="question-type?index=<%= request.getAttribute("currentIndex") %>"
           class="px-3 py-1 text-sm bg-gray-100 border border-gray-300 rounded-md hover:bg-gray-200 transition-colors">
            Change Type
        </a>
    </p>

    <form action="question-builder" method="post" class="space-y-4">
        <input type="hidden" name="currentIndex" value="<%= request.getAttribute("currentIndex") %>"/>
        <input type="hidden" name="questionType" value="<%= request.getAttribute("questionType") %>"/>

        <!-- Common field -->
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">Question Prompt *</label>
            <textarea name="prompt" rows="3" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3"></textarea>
        </div>

        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">Points *</label>
            <input type="number" min="1" name="points" value="1" required class="w-28 bg-gray-50 border border-gray-200 rounded-xl px-4 py-2" />
        </div>


        <% String type = (String) request.getAttribute("questionType"); %>
        <% if ("MultipleChoice".equals(type)) { %>
            <!-- dynamic options, single correct via numeric index -->
            <div id="optionsContainer" class="space-y-4">
                <% for(int i=1;i<=2;i++){ %>
                <div class="relative p-4 border border-gray-200 rounded-lg">
                    <label class="block text-sm font-medium text-slate-700 mb-1">Option <%= i %></label>
                    <input type="text" name="option<%= i %>" class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" />
                    <button type="button" onclick="removeOption(this)" class="absolute top-2 right-2 text-sm text-red-500 hover:text-red-700">✕</button>
                </div>
                <% } %>
            </div>
            <button type="button" onclick="addOption()" class="px-3 py-2 bg-gray-100 border border-gray-200 rounded-lg text-sm hover:bg-gray-200">+ Add Option</button>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1 mt-4">Correct Option (index)</label>
                <input id="correctIndex" type="number" min="1" name="answer" required class="w-24 bg-gray-50 border border-gray-200 rounded-xl px-4 py-2" />
            </div>
        <% } else if ("MultipleChoiceMultipleAnswers".equals(type)) { %>
            <!-- dynamic options, choose one or more correct answers -->
            <div id="optionsContainer" class="space-y-4">
                <% for(int i=1;i<=2;i++){ %>
                <div class="flex items-center space-x-3 p-4 border border-gray-200 rounded-lg relative">
                    <input type="checkbox" id="opt<%= i %>" name="answers" value="<%= i %>" class="h-4 w-4 text-blue-600 border-gray-300 rounded">
                    <label for="opt<%= i %>" class="block text-sm font-medium text-slate-700">Option <%= i %></label>
                    <input type="text" name="option<%= i %>" class="flex-1 bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" />
                    <button type="button" onclick="removeOption(this)" class="absolute top-2 right-2 text-sm text-red-500 hover:text-red-700">✕</button>
                </div>
                <% } %>
            </div>
            <button type="button" onclick="addOption()" class="px-3 py-2 bg-gray-100 border border-gray-200 rounded-lg text-sm hover:bg-gray-200">+ Add Option</button>
            <p class="text-xs text-slate-500 mt-1">Tick all checkboxes that correspond to the correct answers.</p>
        <% } else if ("FillInBlank".equals(type)) { %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Correct Answer</label>
                <input type="text" name="answer" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 mb-3" />
                <button type="button" onclick="insertBlank()" class="px-4 py-2 bg-gray-100 border border-gray-200 rounded-lg text-sm hover:bg-gray-200">
                    Insert Blank (____) into Prompt
                </button>
                <p class="text-xs text-slate-500 mt-1">Placeholders (____) inside the question prompt will be shown as blanks to the quiz taker.</p>
            </div>
        <% } else if ("QuestionResponse".equals(type)) { %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Expected Answer (free text)</label>
                <textarea name="answer" rows="2" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3"></textarea>
            </div>
        <% } else if ("PictureResponse".equals(type)) { %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Image URL *</label>
                <input type="url" name="imageUrl" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" placeholder="https://example.com/image.png" />
            </div>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Correct Answer</label>
                <input type="text" name="answer" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" />
            </div>
        <% } else if ("MultiAnswer".equals(type)) { %>
            <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Accepted Answers (one per line)</label>
                <textarea name="answers" rows="4" required class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3" placeholder="Answer 1&#10;Answer 2&#10;Answer 3"></textarea>
            </div>
        <% } else { %>
            <!-- Unsupported type placeholder -->
            <p class="text-slate-500">Custom fields for <%= type %> can be added later.</p>
        <% } %>

        <div class="flex justify-end">
            <button type="submit" class="px-6 py-3 bg-blue-600 text-white rounded-xl font-medium hover:bg-blue-700">Save &amp; Continue</button>
        </div>
    </form>
</div>
</body>
<script>
    function insertBlank() {
        const ta = document.querySelector('textarea[name="prompt"]');
        if (!ta) return;
        const start = ta.selectionStart || 0;
        const end = ta.selectionEnd || 0;
        const before = ta.value.substring(0, start);
        const after = ta.value.substring(end);
        ta.value = before + '____' + after;
        const pos = start + 4;
        ta.focus();
        ta.setSelectionRange(pos, pos);
    }

    function renumberOptions() {
        const container = document.getElementById('optionsContainer');
        if (!container) return;
        const qType = document.querySelector('input[name="questionType"]')?.value || '';
        const rows = Array.from(container.children);
        rows.forEach((row, idx) => {
            const index = idx + 1; // 1-based
            if (qType === 'MultipleChoice') {
                row.querySelector('label').textContent = 'Option ' + index;
                row.querySelector('input[type="text"]').name = 'option' + index;
            } else if (qType === 'MultipleChoiceMultipleAnswers') {
                const cb = row.querySelector('input[type="checkbox"]');
                cb.id = 'opt' + index;
                cb.value = index;
                const lbl = row.querySelector('label');
                lbl.setAttribute('for', 'opt' + index);
                lbl.textContent = 'Option ' + index;
                row.querySelector('input[type="text"]').name = 'option' + index;
            }
        });
        const correctInput = document.getElementById('correctIndex');
        if (correctInput) {
            correctInput.max = rows.length;
        }
    }

    function addOption() {
        const container = document.getElementById('optionsContainer');
        if (!container) return;
        const nextIndex = container.children.length + 1;
        const qType = document.querySelector('input[name="questionType"]')?.value || '';
        let html = '';
        if (qType === 'MultipleChoice') {
            html = `<div class=\"relative p-4 border border-gray-200 rounded-lg\">` +
                   `<label class=\"block text-sm font-medium text-slate-700 mb-1\">Option ${nextIndex}</label>` +
                   `<input type=\"text\" name=\"option${nextIndex}\" class=\"w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3\" />` +
                   `<button type=\"button\" onclick=\"removeOption(this)\" class=\"absolute top-2 right-2 text-sm text-red-500 hover:text-red-700\">✕</button>` +
                   `</div>`;
        } else if (qType === 'MultipleChoiceMultipleAnswers') {
            html = `<div class=\"flex items-center space-x-3 p-4 border border-gray-200 rounded-lg relative\">` +
                   `<input type=\"checkbox\" id=\"opt${nextIndex}\" name=\"answers\" value=\"${nextIndex}\" class=\"h-4 w-4 text-blue-600 border-gray-300 rounded\">` +
                   `<label for=\"opt${nextIndex}\" class=\"block text-sm font-medium text-slate-700\">Option ${nextIndex}</label>` +
                   `<input type=\"text\" name=\"option${nextIndex}\" class=\"flex-1 bg-gray-50 border border-gray-200 rounded-xl px-4 py-3\" />` +
                   `<button type=\"button\" onclick=\"removeOption(this)\" class=\"absolute top-2 right-2 text-sm text-red-500 hover:text-red-700\">✕</button>` +
                   `</div>`;
        }
        if (html) {
            const wrapper = document.createElement('div');
            wrapper.innerHTML = html;
            container.appendChild(wrapper.firstElementChild);
            renumberOptions();
        }
    }

    function removeOption(btn) {
        const container = document.getElementById('optionsContainer');
        if (!container) return;
        if (container.children.length <= 2) {
            alert('At least two options are required.');
            return;
        }
        const row = btn.closest('div');
        if (row) row.remove();
        renumberOptions();
    }
</script>
</html> 