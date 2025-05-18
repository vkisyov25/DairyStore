console.log("Скриптът е зареден!");

document.addEventListener("DOMContentLoaded", function () {
    console.log("Документът е зареден!");

    fetch("/user/allInformation")
        .then(response => {
            if (!response.ok) {
                throw new Error(`Грешка: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Получени данни: ", data);
            let role = data.authorities;
            console.log("Роля:", role);
            const backButtons = document.getElementById("back-buttons");
            if (role === "seller") {
                backButtons.innerHTML = '<a href="/test/seller">Back</a>';
            } else if (role === "buyer") {
                backButtons.innerHTML = '<a href="/test/buyer">Back</a>';
            }

            const container = document.getElementById("data-container");
            let content = `
                        <h2>Моят профил</h2>
                        <p><strong>Име:</strong> <span>${data.name}</span></p>
                        <p><strong>Потребителско име:</strong> <span>${data.username}</span></p>
                        <p><strong>Роля:</strong> <span>${data.authorities}</span></p>
                        <p><strong>Имейл:</strong> <span>${data.email}</span></p>
                        <p><strong>Телефон:</strong> <span>${data.phone}</span></p>
                        <p><strong>Адрес:</strong> <span>${data.address}</span></p>
                    `;

            if (data.companyName && data.companyName.trim() !== "" && data.companyEIK && data.companyEIK.trim() !== "") {
                if (role === "buyer") {
                    content += `
                            <div>
                            <p><strong>Фирма:</strong> <span>${data.companyName}</span></p>
                            <p><strong>ЕИК:</strong> <span>${data.companyEIK}</span></p>
                            <button id="deleteFirmBtn" onclick="deleteCompanyInfo()">Изтрий фирмата и нейния ЕИК</button>
                            </div>
                            <br>
                            <br>
                        `;
                } else {
                    content += `
                            <div>
                            <p><strong>Фирма:</strong> <span>${data.companyName}</span></p>
                            <p><strong>ЕИК:</strong> <span>${data.companyEIK}</span></p>
                            </div>
                            <br>
                            <br>
                        `;
                }

            }

            container.innerHTML = content;
        })
        .catch((error) => {
            console.error("Грешка при зареждане на данните:", error);
            document.getElementById("data-container").textContent = "Грешка при зареждане на данните.";
        });
});

document.getElementById("editBtn").addEventListener("click", function () {
    const container = document.getElementById("data-container");
    container.innerHTML = "";  // Изчистваме предишното съдържание

    fetch("/user/allInformation")
        .then(response => {
            if (!response.ok) {
                throw new Error(`Грешка: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {

            let content = `
                 <h2>Моят профил - Редактиране</h2>
                 <label for="name">Име</label><br>
                 <input type="text" id="name" value="${data.name}"><br>
                 <span class="error-message" id="error-name"></span><br>
                 <label for="email">Имейл</label><br>
                 <input type="email" id="email" value="${data.email}"><br>
                 <span class="error-message" id="error-email"></span><br>
                 <label for="phone">Телефонен номер</label><br>
                 <input type="text" id="phone" value="${data.phone}"><br>
                 <span class="error-message" id="error-phone"></span><br>
                 <label for="address">Адрес</label><br>
                 <input type="text" id="address" value="${data.address}"><br>
                 <span class="error-message" id="error-address"></span><br>
                 <label for="companyName">Име на фирмата</label><br>
                 <input type="text" id="companyName" value="${data.companyName}"><br>
                 <span class="error-message" id="error-companyName" ></span><br>
                 <label for="companyEIK">ЕИК на фирмата</label><br>
                 <input type="text" id="companyEIK" value="${data.companyEIK}"><br>
                 <span class="error-message" id="error-companyEIK"></span><br>
             `;

            container.innerHTML = content;  // Поставяме новото съдържание в контейнера

            // Скриваме бутона "Редактиране" и показваме бутона "Запази"
            document.getElementById("editBtn").style.display = "none";
            document.getElementById("saveBtn").style.display = "inline-block";
        })
        .catch(error => {
            console.error(error);
        })

});

function saveEditedUserInformation() {
    if (!confirm("Сигурни ли сте, че искате да запазите промените")) {
        return;
    }

    let companyName = document.getElementById("companyName") && document.getElementById("companyName").value.trim() !== ""
        ? document.getElementById("companyName").value.trim()
        : null;
    let companyEIK = document.getElementById("companyEIK") && document.getElementById("companyEIK").value.trim() !== ""
        ? document.getElementById("companyEIK").value.trim()
        : null;

    // Проверка: Ако е попълнено само едно от двете полета, показваме грешка и спираме изпращането
    if ((companyName && !companyEIK) || (!companyName && companyEIK)) {
        alert("Ако попълните 'Фирма', трябва да попълните и 'ЕИК', и обратното.");
        return;
    }
    let obj = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value,
        companyName: document.getElementById("companyName").value,
        companyEIK: document.getElementById("companyEIK").value,
    }

    console.log("Sending data:", obj);
    fetch("/user/edit", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(obj)
    })
        .then(response => {
            return response.json();
        })
        .then(result => {
            console.log("Server response:", result);
            if (result.errors) {
                displayErrors(result.errors);
            } else {
                alert(result.message);
                window.location.href = "/html/userInformationPage.html";
            }
        })
        .catch(error => {
            console.error("Error:", error);
        })
}

function displayErrors(errors) {
    // Изчистваме предишните грешки
    document.querySelectorAll(".error-message").forEach(span => {
        span.textContent = "";
    });

    // Обхождаме грешките и ги показваме под съответните полета
    Object.keys(errors).forEach(field => {
        const errorSpan = document.getElementById(`error-${field}`);
        if (errorSpan) {
            errorSpan.textContent = Array.isArray(errors[field])
                ? errors[field][0] // Вземаме само първата грешка за всяко поле
                : errors[field];
            errorSpan.style.color = "red";
        }
    });
}

function deleteCompanyInfo() {
    if (!confirm("Сигурни ли сте, че искате да изтриете фирмата и нейния ЕИК?")) {
        return; // Спира, ако потребителят натисне "Cancel"
    }

    fetch("/user/deleteCompany", {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Грешка при изтриване: ${response.status}`);
            }
            return response.json();
        })
        .then(result => {
            console.log("Server response:", result);
            alert(result.message || "Фирмата беше успешно изтрита!");
            window.location.href = "/html/userInformationPage.html";
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Възникна грешка при изтриването.");
        });
}