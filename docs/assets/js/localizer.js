// This is a very cruel implementation, but I do not want to move this basic page to a framework
// so you have to deal with this code, with some hard-code :smirk:.
function getPath(lang) {
    return "assets/lang/" + lang + ".json";
}

function getUserLanguage() {
    return Intl.DateTimeFormat().resolvedOptions().locale.split("-")[0];
}

function readLanguageData(lang, callback) {
    console.log("Attempting to fetch language data for: " + lang);
    
    var file = getPath(lang);
    var rawFile = new XMLHttpRequest();
    rawFile.overrideMimeType("application/json");
    rawFile.open("GET", file, true);
    rawFile.onreadystatechange = function () {
        if (rawFile.readyState === 4 && rawFile.status == "200") {
            callback(rawFile.responseText);
        }
        if (rawFile.status == "404") {
            console.log("Language " + lang + " not found. Falling back to english.")
            readLanguageData("en", callback);
        }
    }
    rawFile.send(null);
}

readLanguageData(getUserLanguage(), function (text) {
    var json = JSON.parse(text);
    var keys = Object.keys(json);

    keys.forEach(element => {
        var html = document.getElementById(element);
        if (html != undefined && html != null && element !== "typer-start" && element !== "soon-banner") {
            html.innerHTML = json[element].replaceAll("\n", "<br>");
        }
    })

    setupTyper(json['typer-text'], json['typer-start']);

    var soon = json['soon-banner'];
    var array = document.getElementsByClassName("banner");
    for (let element in array) {
        array[element].innerHTML = soon;
    }
})

function setupTyper(texts, start) {
    document.getElementById("typer-start").innerHTML = start.replaceAll('%typer%', '<div class="typed" style="display: inline;">');
    var options = {
        strings: texts,
        typeSpeed: 30,
        backSpeed: 40,
        shuffle: true,
        backDelay: 5000,
        loop: true,
    };
    var typed = new Typed('.typed', options);
}