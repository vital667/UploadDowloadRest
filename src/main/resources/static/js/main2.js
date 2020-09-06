'use strict';

var readAndCountUploadForm = document.querySelector('#readAndCountUploadForm');
var readAndCountFileUploadInput = document.querySelector('#readAndCountFileUploadInput');
var readAndCountFileUploadError = document.querySelector('#readAndCountFileUploadError');
var readAndCountUploadSuccess = document.querySelector('#readAndCountUploadSuccess');

var replaceEverySecondUploadForm = document.querySelector('#replaceEverySecondUploadForm');
var replaceEverySecondFileUploadInput = document.querySelector('#replaceEverySecondFileUploadInput');
var replaceEverySecondUploadError = document.querySelector('#replaceEverySecondUploadError');
var replaceEverySecondUploadSuccess = document.querySelector('#replaceEverySecondUploadSuccess');

var showListFile = document.querySelector('#showListFile');

function uploadReadAndCountFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/readAndCount");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            readAndCountFileUploadError.style.display = "none";
            readAndCountUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            readAndCountUploadSuccess.style.display = "block";
        } else {
            readAndCountUploadSuccess.style.display = "none";
            readAndCountFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
        if(xhr.status == 403) {
            readAndCountFileUploadError.innerHTML = "<p style=color:red;><b>Error! 403</b></p><p>Forbidden Type: "+response.fileType+"</p>";
            readAndCountFileUploadError.style.display = "block";
        }
    }

    xhr.send(formData);
}

function uploadReplaceEverySecondFiles(file) {
    var formData = new FormData();

        formData.append("file", file);


    var xhr = new XMLHttpRequest();
    xhr.open("POST", "api/replaceEverySecond");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            replaceEverySecondUploadError.style.display = "none";
            // var content = "<p>File Uploaded Successfully.</p>";
            //
            //     content += "<p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";

            replaceEverySecondUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            replaceEverySecondUploadSuccess.style.display = "block";
        } else {
            replaceEverySecondUploadSuccess.style.display = "none";
            replaceEverySecondUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
        if(xhr.status == 403) {
            replaceEverySecondUploadError.innerHTML = "<p style=color:red;><b>Error! 403</b></p>" +
                "<p>Forbidden Type: "+response.fileType+"</p>";
            replaceEverySecondUploadError.style.display = "block";
        }
    }

    xhr.send(formData);
}


readAndCountUploadForm.addEventListener('submit', function(event){
    var files = readAndCountFileUploadInput.files;
    if(files.length === 0) {
        readAndCountFileUploadError.innerHTML = "Please select a file";
        readAndCountFileUploadError.style.display = "block";
    }
    uploadReadAndCountFile(files[0]);
    event.preventDefault();
}, true);


replaceEverySecondUploadForm.addEventListener('submit', function(event){
    var files = replaceEverySecondFileUploadInput.files;
    if(files.length === 0) {
        replaceEverySecondUploadError.innerHTML = "Please select a file";
        replaceEverySecondUploadError.style.display = "block";
    }
    uploadReplaceEverySecondFiles(files[0]);
    event.preventDefault();
}, true);
