var module = (function() {

    var module = {};

    module.tableBuilder = function(dataArr, location) {
        var table = document.createElement('table');
        table.appendChild(createTableHeader(dataArr[0]));
        table.appendChild(createTableBody(dataArr));
        var containerDiv = document.getElementById(location);
        containerDiv.appendChild(table);
    };

    var createTableBody = function(dataObj) {
        var tbody = document.createElement('tbody');
        dataObj.forEach(function(data, index, arr) {
            var tr = document.createElement('tr');
            tbody.appendChild(tr);
                for (var property in data) {
                    var td = document.createElement('td');
                    td.textContent = data[property];
                    tr.appendChild(td);
                }
                tbody.appendChild(tr);
        });
        return tbody;
    };

    var createTableHeader = function(dataObj) {
        // headers
        var thead = document.createElement('thead');
        var headRow = document.createElement('tr');
        for (var header in dataObj) {
            var th = document.createElement('th');
            th.textContent = header;
            headRow.appendChild(th);
        }
        thead.appendChild(headRow);
        return thead;
    };

    module.ajax = function(method, url, callback, requestBody) {
        var xhr = new XMLHttpRequest();
        xhr.open(method, url);

        if (requestBody) {
            xhr.setRequestHeader('Content-type', 'application/json');
        } else {
            requestBody = null;
        }
        xhr.open(method, url);
        xhr.onreadystatechange = function() {
            callback(xhr);
        };
        xhr.send(requestBody);
    };
    return module;
})();
