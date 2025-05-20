document.getElementById("submitBtn").addEventListener("click", function (event) {
    event.preventDefault(); // Спира презареждането на страницата

    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    let obj = {
        username: username,
        password: password
    }

    fetch("/test/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(obj)
    })
        .then(response => {
            return response.text().then(data => ({status: response.status, body: data}));
        })
        .then(({status, body}) => {
            console.log("Отговор от сървъра:", status, body);

            if (status === 200 && body.startsWith("redirect:")) {
                window.location.href = body.replace("redirect:", ""); // Пренасочване
            } else if (status === 403) {
                alert("Невалидни данни за вход.");
            } else {
                alert(body); // Показва съобщението от сървъра
            }
        })
        .catch(error => {
            console.error("Грешка:", error);
            alert("Възникна грешка при изпълнението на заявката.");
        });

})
