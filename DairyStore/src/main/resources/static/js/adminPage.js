addDeliveryCompany();

function viewDeliveryCompany() {
    clearForm();
    let table = document.getElementById("deliveryTable")
    if (table.style.display === "none") {
        table.style.display = "block";
        document.getElementById("createDeliveryCompany").style.display = "none";
        document.getElementById("usersTable").style.display = "none";
        document.getElementById("product-table").style.display = "none";
    } else {
        table.style.display = "none";
    }

    fetch("/delivery-company/all")
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            const tableBody = document.querySelector("#deliveryTable tbody");
            tableBody.innerHTML = "";

            data.forEach(company => {
                const row = document.createElement("tr");

                row.innerHTML = `
                        <td>${company.name}</td>
                        <td>${company.deliveryFee.toFixed(2)}</td>
                        <td><button type="submit" onclick="deleteDeliveryFee(${company.id})">Изтрий</button></td>
                    `;
                tableBody.appendChild(row);
            });
        })
        .catch(err => {
            console.error("Грешка при зареждане:", err);
        })
}

function deleteDeliveryFee(companyId) {
    console.log(companyId);
    fetch(`/delivery-company/deleteBy/${companyId}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.text();
        })
        .then(() => {
            document.getElementById("deliveryTable").style.display = "none";
            viewDeliveryCompany()
        })
        .catch(error => {
            alert("Грешка при изтриване: " + error.message);
        })
}

function addDeliveryCompany() {
    clearForm();
    let createdDiv = document.getElementById("createDeliveryCompany")
    if (createdDiv.style.display === "none") {
        createdDiv.style.display = "block";
        document.getElementById("deliveryTable").style.display = "none";
        document.getElementById("usersTable").style.display = "none";
        document.getElementById("product-table").style.display = "none";
    } else {
        createdDiv.style.display = "none";
    }
}

function saveDeliveryCompany() {
    let obj = {
        name: document.getElementById("deliveryCompanyName").value,
        deliveryFee: document.getElementById("deliveryFee").value
    };

    fetch("/delivery-company/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(obj),
        credentials: "include"
    }).then(response => {
        return response.json();
    }).then(data => {
        console.log(data);
        if (data.errors) {
            displayErrors(data.errors);
        } else {
            clearForm();
            alert(data.message);
            document.getElementById("createDeliveryCompany").style.display = "none";
        }
    }).catch(error => {
        console.error("Грешка:", error);
        alert(error);
    });
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

function clearForm() {
    //clear input fields
    document.querySelectorAll("#createDeliveryCompany input").forEach(input => {
        input.value = "";
    });
    //clear error fields
    document.querySelectorAll(".error-message").forEach(span => {
        span.textContent = "";
    });
}

function viewUsers() {
    clearForm();
    let userTable = document.getElementById("usersTable")
    if (userTable.style.display === "none") {
        userTable.style.display = "block";
        document.getElementById("createDeliveryCompany").style.display = "none";
        document.getElementById("deliveryTable").style.display = "none";
        document.getElementById("product-table").style.display = "none";
    } else {
        userTable.style.display = "none";
    }

    fetch("/user/view-all")
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            const tableBody = document.querySelector("#usersTable tbody");
            tableBody.innerHTML = "";

            data.forEach(user => {
                const row = document.createElement("tr");

                row.innerHTML = `
                        <td>${user.username}</td>
                        <td>${user.authorities}</td>
                        <td>${user.email}</td>
                        <td>${user.name}</td>
                        <td>${user.phone}</td>
                        <td>${user.address}</td>
                        <td>${user.companyName}</td>
                        <td>${user.companyEIK}</td>
                        <td><button type="submit" onclick="deleteUserById(${user.id})">Изтрий</button></td>
                    `;
                tableBody.appendChild(row);
            });
        })
        .catch(err => {
            console.error("Грешка при зареждане:", err);
        })
}

function deleteUserById(userId) {
    if (!confirm("Сигурни ли сте, че искате да изтриете потребителя")) {
        return;
    }
    console.log(userId);
    fetch(`/user/deleteBy/${userId}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.text();
        })
        .then(() => {
            document.getElementById("usersTable").style.display = "none";
            viewUsers()
        })
        .catch(error => {
            alert("Грешка при изтриване: " + error.message);
        })
}

function viewProducts() {
    let productTable = document.getElementById("product-table");
    if (productTable.style.display === "none") {
        productTable.style.display = "block";
        document.getElementById("createDeliveryCompany").style.display = "none";
        document.getElementById("deliveryTable").style.display = "none";
        document.getElementById("usersTable").style.display = "none";
    } else {
        productTable.style.display = "none";
    }
    fetch("/products/all")
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            const tableBody = document.getElementById("tbody")
            tableBody.innerHTML = "";
            data.forEach(product => {
                const row = document.createElement("tr")
                row.innerHTML = `
                    <td>${product.name}</td>
                    <td>${product.type}</td>
                    <td>${product.weight}</td>
                    <td>${product.price}</td>
                    <td>${product.description}</td>
                    <td>${product.discount}</td>
                    <td>${product.quantity}</td>
                    <td>${product.username}</td>
                    <td><button id="deleteProductBtn" onclick="deleteProduct(${product.id})">Изтрий</button></td>
                    `;

                tableBody.appendChild(row);

            });
        })
        .catch(error => {
            console.error('Error:', error)
        });
}

function deleteProduct(id) {
    if (!confirm("Сигурни ли сте, че искате да изтриете продукта")) {
        return;
    }
    document.getElementById("product-table").style.display = "none";
    fetch(`/products/${id}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Неуспешна заявка");
            }
            return response.text();
        })
        .then(() => {
            removeProductFromTable(id);
            viewProducts();
            alert("Продуктът е изтрит успешно!");
        })
        .catch(error => {
            console.error("Грешка при изтриване:", error);
            alert("Възникна грешка при изтриването на продукта.");
        });
}

function removeProductFromTable() {
    const row = document.getElementById("product-${id}");
    if (row) {
        row.remove();
    }
}
