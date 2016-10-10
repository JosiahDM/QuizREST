
var quiz = (function() {

    var userAnswers = [];
    var onlogin;

    var buildTable = function(data) {
        var tableDiv = document.getElementById('mainTable');
        var table = document.createElement('table');
        var tableHeader = document.createElement('thead');
        var headRow = document.createElement('tr');
        var tableBody = document.createElement('tbody');
        table.id = 'table';

        var cols = ['Quiz Name', 'View', 'Take'];

        for (var i = 0; i < cols.length; i++) {
            var th = document.createElement('th');
            th.textContent = cols[i];
            headRow.appendChild(th);
        }
        tableHeader.appendChild(headRow);
        for (var p in data) {
            var tr = document.createElement('tr');
            tr.id = data[p].id;
            var td = document.createElement('td');
            td.textContent = data[p].name;
            tr.appendChild(td);
            var viewTd = document.createElement('td');
            var takeTd = document.createElement('td');
            var editTd = document.createElement('td');
            var delTd = document.createElement('td');
            var view = document.createElement('button');
            var take = document.createElement('button');
            var edit = document.createElement('button');
            var del = document.createElement('button');
            edit.textContent = 'Edit';
            del.textContent = 'Delete';
            view.textContent = 'View';
            take.textContent = 'Take';
            viewTd.appendChild(view);
            takeTd.appendChild(take);
            delTd.appendChild(del);
            editTd.appendChild(edit);
            tr.appendChild(viewTd);
            tr.appendChild(takeTd);
            tr.appendChild(delTd);
            tr.appendChild(editTd);
            tableBody.appendChild(tr);
        }
        table.appendChild(tableHeader);
        table.appendChild(tableBody);
        tableDiv.appendChild(table);
    };

    var makeButtonListeners = function() {
        var btns = document.getElementsByTagName('button');
        for (var i = 0; i < btns.length; i++) {
            btns[i].addEventListener('click', function(e) {
                var row = e.target.parentElement.parentElement;
                var id = row.id;
                removeLoginError(e);
                switch (e.target.textContent) {
                    case 'View': request('GET', 'api/quiz/'+id, showQuiz);
                        break;
                    case 'Take':
                        module.ajax('GET', 'api/quiz/'+id, takeQuizButtonEvent);
                        break;
                    case 'Edit':
                        createEditButtonEvent(row);
                        createSubmitButtonEvent(row);
                        break;
                    case 'Delete':
                        if (confirm("Do you really want to delete this?")){
                            module.ajax('DELETE', 'api/quiz/'+id, rebuildTable);
                        }
                        break;
                    default:
                        break;
                }
            });
        }
    };

    var removeLoginError = function(e) {
        var error = document.getElementById('loginError');
        if (e.target.id !== 'login' && error) {
            error.parentElement.removeChild(error);
        }
    };

    var takeQuizButtonEvent = function(xhr) {
        if (xhr.status < 400 &&  xhr.readyState === 4) {
            clearDiv('mainTable');
            clearDiv('formInput');
            var quiz = JSON.parse(xhr.responseText);
            displayHeader(quiz.name);
            displayQuestion(quiz, 0);
        }
    };
    var displayHeader = function(name) {
        clearDiv('headerDiv');
        var h1 = document.createElement('h1');
        h1.textContent = name;
        var header = document.getElementById('headerDiv');
        header.appendChild(h1);
    };

    var makeQuizDiv = function(quiz) {
        var quizDiv = document.getElementById('quizDiv');
        if (!quizDiv) {
            quizDiv = document.createElement('div');
            quizDiv.id = 'quizDiv';
        }
        var questionDiv = document.createElement('div');
        questionDiv.id = 'questionDiv';
        var answers = document.createElement('ul');
        answers.id = 'answers';
        quizDiv.appendChild(questionDiv);
        quizDiv.appendChild(answers);
        document.body.appendChild(quizDiv);
        return quizDiv;
    };

    var displayQuestion = function(quiz, index) {
        if (index < quiz.questions.length) {
            var quizDiv = makeQuizDiv(quiz);
            var qDiv = quizDiv.firstChild;
            var currentQuestion = quiz.questions[index];
            qDiv.textContent = currentQuestion.questionText;
            displayAnswers(currentQuestion, quizDiv.lastChild, quiz, index);
        } else {
            clearDiv('quizDiv');
            displayHeader("You've finished the quiz!");
            var score = displayScore();
            var user = getUserFromCookie();
            if (user) {
                saveScore(quiz, user.id, score);
            } else {
                userAnswers = {'quiz':quiz,'score':score};
                promptToSave();
            }
        }
    };

    var promptToSave = function() {
        var ptag = document.createElement('p');
        ptag.textContent="You're not logged in. Log in above to save your score!";
        var quizDiv = document.getElementById('quizDiv');
        quizDiv.appendChild(ptag);
        onlogin = new Event('loggedIn');
        document.addEventListener('loggedIn', function(e) {
            saveScore(userAnswers['quiz'], getUserFromCookie().id, userAnswers['score']);

        });
    };

    var getUserFromCookie = function() {
        if (document.cookie) {
            var tokens = document.cookie.split(' ');
            var name = tokens[0].split('=')[1];
            var id = tokens[1].split('=')[1];
            var user = { 'id' : id, 'username' : name };
            return user;
        }
    };

    var saveScore = function(quiz, userId, score) {
        var requestBody = '{ "user": { "id": '+userId+' }, "value": '+score+' }';
        module.ajax('POST', 'api/user/'+userId+'/scores/'+quiz.id, displaySaved, requestBody);
        };

    var displaySaved = function(xhr) {
        if (xhr.status < 400 && xhr.readyState === 4) {
            var pTag = document.createElement('p');
            pTag.textContent = "Your score has been successfully saved."
            document.getElementById('quizDiv').appendChild(pTag);
        }
    };

    var displayScore = function() {
        var quizDiv = document.getElementById('quizDiv');
        var results = document.createElement('p');
        var score = 0;
        userAnswers.forEach(function(val) {
            if (val) {
                score++;
            }
        });
        score = ((score/userAnswers.length)*100).toFixed(1);
        results.textContent = "Your score: " + score + "%";
        quizDiv.appendChild(results);
        return score;
    };

    var displayAnswers = function(question, ansDiv, quiz, questionsIndex) {
        question.answers.forEach(function(val, index, arr) {
            var li = document.createElement('li');
            li.textContent = val.answerText;
            li.id = val.id;
            li.addEventListener('click', function() {
                userAnswers.push(val.correct);
                clearDiv('quizDiv');
                displayQuestion(quiz, ++questionsIndex);
            });
            ansDiv.appendChild(li);
        });
    };

    var createSubmitButtonEvent = function(row) {
        var submit = document.getElementById('submit'+row.id);
        if (submit) {
            var input = document.getElementById('edit'+row.id);
            submit.addEventListener('click', function() {
                if (input.value.trim().length > 3) {
                    var requestBody = '{ "name" : "'+input.value.trim()+'"}';
                    module.ajax('PUT', 'api/quiz/'+row.id, rebuildTable, requestBody);
                } else {
                    if (!document.getElementById('error'+row.id)) {
                        var td = document.createElement('td');
                        td.id = 'error'+row.id;
                        td.textContent = 'Quiz name must be 4 or more characters!';
                        input.parentElement.parentElement.appendChild(td);
                    }
                }
            });
        }
    };

    var createEditButtonEvent = function(row) {
        if (!document.getElementById('edit'+row.id)) {
            var td = document.createElement('td');
            var input = document.createElement('input');
            input.id = 'edit'+row.id;
            input.setAttribute('type', 'text');
            input.value = row.firstChild.textContent;
            var submit = document.createElement('input');
            submit.id = 'submit'+row.id;
            submit.setAttribute('type', 'submit');
            td.appendChild(input);
            td.appendChild(submit);
            row.appendChild(td);
        } else {
            row.removeChild(document.getElementById('edit'+row.id).parentElement);
            var error = document.getElementById('error'+row.id);
            if (error) {
                row.removeChild(error);
            }
        }
    };

    var showQuiz = function(data) {
        document.getElementById('mainTable').removeChild(table);
        displayHeader(data.name)
        clearDiv('formInput');
        module.ajax('GET', 'api/quiz/'+data.id  +'/scores', getScores);
    };

    var clearDiv = function(containerDivId) {
        var div = document.getElementById(containerDivId);
        if (!div) {
            return;
        }
        while (div.firstChild) {
            div.removeChild(div.firstChild);
        }
    };

    var getScores = function(xhr) {
        if(xhr.status < 400 && xhr.readyState === 4) {
            var scores = JSON.parse(xhr.responseText)
            // module.tableBuilder(scores, 'mainTable');
            var tableDiv = document.getElementById('mainTable');
            var table = document.createElement('table');
            var thead = document.createElement('thead');
            var th1 = document.createElement('th');
            var th2 = document.createElement('th');
            th1.textContent = 'Username';
            th2.textContent = 'Score';
            var tbody = document.createElement('tbody');
            scores.forEach(function(val, index, arr) {
                var tr = document.createElement('tr');
                var userTd = document.createElement('td');
                userTd.textContent = val['username'];
                var scoreTd = document.createElement('td');
                scoreTd.textContent = val['value'];
                tr.appendChild(userTd);
                tr.appendChild(scoreTd);
                tbody.appendChild(tr);
            });
            thead.appendChild(th1);
            thead.appendChild(th2);
            table.appendChild(thead);
            table.appendChild(tbody);
            tableDiv.appendChild(table);
        }
    };

    // button to pop up a form to create a quiz
    var addMakeQuizButton = function(data) {
        var formDiv = document.getElementById('formInput');
        var button = document.createElement('button');
        button.id = 'makeQuiz';
        button.textContent = 'Make Quiz'
        button.addEventListener('click', makeQuizButtonListeners);
        formDiv.appendChild(button);
    };
    // button listeners to create a quiz
    var makeQuizButtonListeners = function(e) {
        var formDiv = document.getElementById('formInput');
        removeLoginError(e);
        if (!document.getElementById('quizInput')) {
            var form = document.createElement('form');
            form.id = 'quizInput';
            var input = document.createElement('input');
            input.setAttribute('type', 'text');
            input.setAttribute('placeholder', 'Quiz Name');
            form.appendChild(input);
            var submit = document.createElement('input');
            submit.setAttribute('type', 'submit');
            submit.setAttribute('value', 'Submit');
            submit.addEventListener('click', submitNewQuiz);
            input.id = 'makeQuiz';
            form.appendChild(input);
            form.appendChild(submit);
            formDiv.appendChild(form);
        }
    };

    var submitNewQuiz = function(e) {
        e.preventDefault();
        var name = e.target.previousSibling.value;

        if (name) {
            var quiz = '{"name" : "'+name+'"}';
            module.ajax('POST', 'api/quiz', rebuildTable, quiz);
        };
    };

    var rebuildTable = function(data) {
        var tableDiv = document.getElementById('mainTable');

        if (data.readyState === 4 && data.status < 400) {
            tableDiv.removeChild(tableDiv.firstChild);
            request('GET', 'api/quiz', buildTable);
            request('GET', 'api/quiz', makeButtonListeners);

        }
    };

    var addLoginForm = function() {
        var navDiv = document.getElementById('nav');
        var navForm = document.createElement('form');
        var login = document.createElement('input');
        var pw = document.createElement('input');
        var submit = document.createElement('input');
        navForm.id = 'navForm';
        login.id = 'username';
        pw.id = 'pw';
        submit.value = 'Login';
        submit.id = 'login';
        pw.setAttribute('type', 'password');
        login.setAttribute('placeholder', 'Username')
        pw.setAttribute('placeholder', 'Password');
        login.setAttribute('type', 'text');
        submit.setAttribute('type', 'submit');
        navDiv.appendChild(navForm);
        navForm.appendChild(login);
        navForm.appendChild(pw);
        navForm.appendChild(submit);
        loginEvent(submit);
    };

    var loginEvent = function(login) {
        login.addEventListener('click', function(e) {
            e.preventDefault();
            var password = login.previousSibling.value;
            var username = login.parentElement.firstChild.value;
            var requestBody = '{ "username" : "'+username+'", "password" : "'+password+'" }';
            module.ajax('POST', 'api/auth', userLogin, requestBody);
        });
    };

    var userLogin = function(xhr) {
        if (xhr.readyState === 4 && xhr.status < 400) {
            var user = JSON.parse(xhr.responseText);
            replaceLoginForm(user);
            setCookie(user);
            if (onlogin) {
                document.dispatchEvent(onlogin);
            }
        } else if (xhr.readyState === 4 && xhr.status >= 400) {
            loginError();
        }
    };

    var replaceLoginForm = function(user) {
        clearDiv('navForm');
        var navForm = document.getElementById('navForm');
        if (!navForm) {
            navForm = document.createElement('navForm');
            navForm.id = 'navForm';
            document.getElementById('nav').appendChild(navForm);
        };
        var h3 = document.createElement('h3');
        h3.textContent = 'Logged in as ' + user.username;
        var button = document.createElement('button');
        button.value = 'logout'
        button.textContent = 'Logout';
        button.addEventListener('click', function(e) {
            e.preventDefault();
            deleteCookie(user);
            clearDiv('nav');
            addLoginForm();
        });
        navForm.appendChild(h3);
        h3.appendChild(button);
    };

    var setCookie = function(user) {
        if (!document.cookie) {
            document.cookie = "name="+user.username + " id=" +user.id + ";";
        }
    };

    var deleteCookie = function(user) {
        if (document.cookie) {
            document.cookie = 'name='+user.username+' id='+user.id+'; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        }
    };

    var loginError = function() {
        if (!document.getElementById('loginError')) {
            var error = document.createElement('div');
            error.textContent = 'Invalid username/password.';
            error.id = 'loginError';
            document.getElementById('nav').appendChild(error);
        }
    };

    addEventListener('load', function() {
        if (document.cookie) {
            replaceLoginForm(getUserFromCookie());
        } else {
            addLoginForm();
        }
        request('GET', 'api/quiz', buildTable);
        request('GET', 'api/quiz', makeButtonListeners);
        request('GET', 'api/quiz', addMakeQuizButton);
    });

    var request = function(type, uri, callback) {
        var xhr = new XMLHttpRequest();
        xhr.open(type, uri);
        xhr.onreadystatechange = function() {
            if (xhr.status < 400 && xhr.readyState === 4) {
                callback(JSON.parse(xhr.responseText));
            }
            else if (xhr.status > 400 && xhr.readyState === 4) {
                console.log('ERROR');
            }
        };
        xhr.send();
    };
})();
