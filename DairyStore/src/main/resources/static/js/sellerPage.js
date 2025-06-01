createProduct();

function openUserProfile() {
    window.location.href = "/html/userInformationPage.html";
}

function createProduct() {
    clearForm();
    const createProductDiv = document.getElementById("createProduct");
    const table = document.getElementById("product-table");
    if (createProductDiv.style.display === "none") {
        createProductDiv.style.display = "block";
        table.style.display = "none";
        document.getElementById("editProduct").style.display = "none"
        document.getElementById("sellerAnalysis").style.display = "none";
    } else {
        createProductDiv.style.display = "none";
    }
}

function saveProduct() {
    const createProductDiv = document.getElementById("createProduct");
    let obj = {
        name: document.getElementById("name").value,
        type: document.getElementById("type").value,
        weight: document.getElementById("weight").value,
        price: document.getElementById("price").value,
        description: document.getElementById("description").value,
        discount: document.getElementById("discount").value,
        quantity: document.getElementById("quantity").value
    }
    console.log(obj);

    fetch("/products/create", {
        method: "POST", headers: {
            "Content-Type": "application/json"
        }, body: JSON.stringify(obj)
    }).then(response => {
        return response.json();
    }).then(data => {
        console.log(data);
        if (data.errors) {
            displayErrors(data.errors);
        } else {
            alert(data.message);
            createProductDiv.style.display = "none"; // Скрива формата
            clearForm(); // Изчиства стойностите във формата
            /*createProduct()*/
        }
    }).catch(error => {
        console.error("Грешка:", error);
        alert("Възникна грешка при изпълнението на заявката.");
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
            errorSpan.textContent = Array.isArray(errors[field]) ? errors[field][0] // Вземаме само първата грешка за всяко поле
                : errors[field];
            errorSpan.style.color = "red";
        }
    });


}

function clearForm() {
    document.querySelectorAll("#createProduct input").forEach(input => {
        input.value = "";
    });
    document.getElementById("type").value = ""; // Нулиране на select-а
    document.getElementById("description").value = ""; // Нулиране на select-а

    document.querySelectorAll(".error-message").forEach(span => {
        span.textContent = "";
    });
}

function viewYourProducts() {
    const table = document.getElementById("product-table");
    const createProductDiv = document.getElementById("createProduct");
    if (table.style.display === "none") {
        table.style.display = "block";
        createProductDiv.style.display = "none";
        document.getElementById("editProduct").style.display = "none";
        document.getElementById("sellerAnalysis").style.display = "none";
    } else {
        table.style.display = "none";
    }

    fetch("/products/all-for-current-user")
        .then(response => {
            if (!response.ok) {
                return new Error("Response is not okay")
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
                    <td><button id="editProductBtn" onclick="editProduct(${product.id})">Редактирай</button></td>
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
            viewYourProducts();
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

function editProduct(id) {
    let editProductDiv = document.getElementById("editProduct");
    if (editProductDiv.style.display === "none") {
        editProductDiv.style.display = "block"
        document.getElementById("product-table").style.display = "none";
        document.getElementById("sellerAnalysis").style.display = "none";
    }
    fetch(`/products/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Неуспешна заявка")
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            editProductDiv.innerHTML = `

             <h2>Продукт - редактиране</h2>
             <label><strong>Име:</strong></label><br>
             <input type="text" id="productName" value="${data.name}" required><br>
             <span class="error-message" id="name-error"></span><br>
             <label><strong>Тип:</strong></label><br>
             <select id="productType">
                <option value="${data.type}">${data.type}</option>
                <option value="кашкавал">кашкавал</option>
                 <option value="сирене">сирене</option>
                <option value="масло">масло</option>
                <option value="прясно мляко">прясно мляко</option>
                <option value="кисело мляко">кисело мляко</option>
                <option value="извара">извара</option>
                <option value="айрян">айрян</option>
            </select>
            <span class="error-message" id="type-error"></span><br>
            <br>
             <label><strong>Тегло(кг):</strong></label><br>
             <input type="number" id="productWeight" value="${data.weight}" required><br>
             <span class="error-message" id="weight-error"></span><br>
             <label><strong>Цена(лв):</strong></label><br>
             <input type="number" id="productPrice" value="${data.price}" required><br>
             <span class="error-message" id="price-error"></span><br>
             <label><strong>Описание:</strong></label><br>
             <input type="text" id="productDescription"value="${data.description}" required><br>
             <span class="error-message" id="description-error"></span><br>
             <label><strong>Отстъпка (%):</strong></label><br>
             <input type="number" id="productDiscount" value="${data.discount}"><br>
             <span class="error-message" id="discount-error"></span><br>
             <label><strong>Количество:</strong></label><br>
             <input type="number" id="productQuantity" value="${data.quantity}"><br>
             <span class="error-message" id="quantity-error"></span><br>
             <button type="submit" onclick="saveEditedProduct(${data.id})">Запази</button>
             <button type="submit" onclick="viewYourProducts()">Отказ</button>

         `;
        })
        .catch(error => {
            console.error(error)
            alert("Грешка в заявката")
        })


}

function saveEditedProduct(id) {
    if (!confirm("Сигурни ли сте, че искате да запазите промените")) {
        return;
    }

    let obj = {
        id: id,
        name: document.getElementById("productName").value,
        type: document.getElementById("productType").value,
        weight: document.getElementById("productWeight").value,
        price: document.getElementById("productPrice").value,
        description: document.getElementById("productDescription").value,
        discount: document.getElementById("productDiscount").value,
        quantity: document.getElementById("productQuantity").value
    }

    console.log(obj);

    fetch("/products/edit", {
        method: "PUT", headers: {
            "Content-Type": "application/json"
        }, body: JSON.stringify(obj)
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            console.log(data);
            console.log(data.errors);
            if (data.errors) {
                editProductDisplayErrors(data.errors)
            } else {
                viewYourProducts();
                alert(data.message);
            }
        })
        .catch(error => {
            console.log(error);
            alert(error);
        })
}

function editProductDisplayErrors(errors) {
    // Почистваме всички предишни грешки
    const errorSpans = document.querySelectorAll('.error-message');
    errorSpans.forEach(span => span.textContent = '');

    // Итериране през грешките и показване на грешката в съответния span
    for (const [field, message] of Object.entries(errors)) {
        const errorElement = document.getElementById(`${field}-error`); // Вземаме span с ID, което съвпада с полето
        if (errorElement) {
            errorElement.textContent = message; // Поставяме съобщението за грешка в span
        }
    }
}


function viewSellerAnalysis() {
    let sellerAnalysis = document.getElementById("sellerAnalysis");
    if (sellerAnalysis.style.display === "none") {
        sellerAnalysis.style.display = "block";
        document.getElementById("createProduct").style.display = "none";
        document.getElementById("product-table").style.display = "none";
        document.getElementById("editProduct").style.display = "none";
    } else {
        sellerAnalysis.style.display = "none";
    }

    fetch("/analysis/seller")
        .then(response => {
            console.log(response);
            if (!response.ok) {
                throw new Error("Невалидна заявка");
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            sellerAnalysis.innerHTML = `
                <h3>Анализ на продавача</h3>
                <p><strong>Оборот:</strong> <span>${data.totalEarnings.toFixed(2)} лв.</span></p>
                <p><strong>Най-продаваният продукт:</strong> <span>${data.topSellingProduct}</span></p>
                <p><strong>Брой купувачи, който са купили от продуктите ви:</strong> <span>${data.buyerCount}</span></p>
                `;
        })
        .catch(error => {
            alert(error);
        })
}