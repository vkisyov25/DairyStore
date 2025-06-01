loadProductsForSale();
function openUserProfile() {
    window.location.href = "/html/userInformationPage.html";
}

let priceWithoutDeliveryFee = 0;


function loadProductsForSale() {
    document.getElementById("order-input").style.display = "none";

    let table = document.getElementById("product-table");
    table.style.display = "none";
    if (table.style.display === "none") {
        table.style.display = "block";
        document.getElementById("analysisResult").style.display = "none";
        document.getElementById("orderList").style.display = "none";
        document.getElementById("shopping-car-div").style.display = "none";
    } else {
        table.style.display = "none";
    }
    /*document.getElementById("cartNotification").style.display = "none";*/
    setTimeout(() => {
        document.getElementById("cartNotification").style.display = "none";
    }, 5000);

    fetch("/products/for-sale")
        .then(response => {
            if (!response.ok) {
                return new Error("Невалидна заявка")
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            const tableBody = document.getElementById("tbody")
            tableBody.innerHTML = "";
            data.forEach(product => {
                const row = document.createElement("tr");
                let quantityInputId = `quantity-${product.id}`;

                let content = `
                        <td>${product.name}</td>
                        <td>${product.type}</td>
                        <td>${product.weight}</td>
                        <td>${product.price}</td>
                        <td>${product.description}</td>
                        <td>${product.availability}</td>
                        <td><input type="number" id="${quantityInputId}" name="quantity" value="1" min="1"></td>
                        `;

                if (product.availability === "В наличност") {
                    content += `
                        <td><button onclick="addToCart(${product.id}, document.getElementById('${quantityInputId}').value)">Добави в количка</button></td>
                        `;
                } else {
                    content += `
                        <td><button disabled>Добави в количка</button></td>
                        `;
                }

                row.innerHTML = content;
                tableBody.appendChild(row);

            });
        })
        .catch(error => {
            console.error('Error:', error)
        });
}

function addToCart(id, quantity) {
    console.log(quantity);
    // URL на контролера, където е "/add"
    const url = '/cart/add';

    // Данни, които ще изпратим
    const params = new URLSearchParams();
    params.append('productId', id);
    params.append('quantity', quantity);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',  // Content-Type за параметрите
        },
        body: params.toString()  // Преобразуваме обектите в string формат
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            return response.text();
        })
        .then(data => {
            loadProductsForSale()
            document.getElementById("cartNotification").style.display = "block";
        })
        .catch(error => {
            console.error('Грешка при изпращане на заявката:', error);
            alert(error);
        });
}

function viewShoppingCart() {

    priceWithoutDeliveryFee = 0;
    document.getElementById("cart_table").style.display = "block";
    const shoppingCart = document.getElementById("shopping-car-div");
    if (shoppingCart.style.display === "none") {
        shoppingCart.style.display = "block";
        document.getElementById("analysisResult").style.display = "none";
        document.getElementById("orderList").style.display = "none";
        document.getElementById("product-table").style.display = "none";
    }

    document.getElementById("cartNotification").style.display = "none";
    fetch("/cart/view")
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            const tableBody = document.getElementById("tb");
            tableBody.innerHTML = "";
            let totalPrice = 0;
            data.forEach(cartProduct => {
                const row = document.createElement("tr");
                totalPrice += cartProduct.totalPricePerProduct;
                row.innerHTML = `
                        <td>${cartProduct.name}</td>
                        <td>${cartProduct.type}</td>
                        <td>${cartProduct.weight}</td>
                        <td>${cartProduct.price}</td>
                        <td>${cartProduct.quantity}</td>
                        <td>${cartProduct.discount}</td>
                        <td>${cartProduct.totalPricePerProduct}</td>
                        <td><button type="submit" onclick="deleteProductFromCart(${cartProduct.id})">Изтрий</button></td>
                    `;
                tableBody.appendChild(row);
            })
            console.log("Обща сума на покупката", totalPrice);
            document.getElementById("total-price").textContent = "Дължима сума без вкючена доставка: " + totalPrice.toFixed(2)+" лв";
            priceWithoutDeliveryFee = totalPrice;


            if (priceWithoutDeliveryFee === 0) {
                document.getElementById("inputOrderInfoBtn").style.display = "none";
            } else {
                document.getElementById("inputOrderInfoBtn").style.display = "block";
            }


        })
        .catch(error => {
            alert(error);
        })
}

function deleteProductFromCart(productId) {

    fetch(`/cart/${productId}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка")
            }
            return response.text();
        })

        .then(() => {
            viewShoppingCart();
        })
        .catch(error => {
            alert(error);
        })

}


function inputOrderInfo() {

    document.getElementById("inputOrderInfoBtn").style.display = "none";
    document.getElementById("order-input").style.display = "block";

    fetch("/order/delivery-companies")

        .then(response => {
            if (!response.ok) {
                throw new Error("Невалидна заявка");
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            let orderInputDiv = document.getElementById("order-input");
            orderInputDiv.innerHTML = `
                 <label for="deliveryAddress">Адрес на доставка</label>
                 <input type="text" id="deliveryAddress" required><br>
                 <label for="deliveryCompany">Фирма доставчик</label>
                 <select id="deliveryCompany" required>
                    <option value="">-- Избери --</option>
                 </select><br>

                 <label for="paymentMethod">Начин на плащане</label>
                 <select id="paymentMethod" required>
                    <option value="">-- Избери --</option>
                    <option value="CARD">С карта</option>
                    <option value="CASH">Кеш</option>
                 </select><br>
                 <p>Такса за доставка: <span id="deliveryFee">-</span> лв.</p><br>
                 <p>Цена с включена доставка: <span id="totalPrice">-</span> лв.</p>
                 <form id="payment-form" style="display:none;">
                    <div id="paymentInfo" >
                        <label for="card-element">Номер на карта</label>
                        <div id="card-element"></div> <!-- Това е мястото, където ще се показва Stripe картовия елемент -->
                        <button type="submit">Плащане</button>
                    </div>
                    <div id="payment-message"></div>
                </form>

                `;

            const selectElement = document.getElementById("deliveryCompany");
            const feeElement = document.getElementById("deliveryFee");

            const paymentMethod = document.getElementById("paymentMethod");


            // Добавяме опциите в селекта
            data.forEach(service => {
                let option = document.createElement("option");
                option.value = service.id;
                option.textContent = service.name;
                selectElement.appendChild(option);
            });

            // Добавяме event listener, който сменя таксата при избор
            selectElement.addEventListener("change", function () {
                const selectedId = Number(this.value);
                const selectedService = data.find(service => service.id === selectedId);

                // Ако има валидна фирма, показваме таксата, иначе оставяме "-"
                feeElement.textContent = selectedService ? `${selectedService.deliveryFee}` : "-";


                let deliveryFee = parseFloat(document.getElementById("deliveryFee").textContent) || 0;
                let totalWithDeliveryFee = priceWithoutDeliveryFee + deliveryFee;

                document.getElementById("totalPrice").textContent = totalWithDeliveryFee.toFixed(2) + "";
            });


            paymentMethod.addEventListener("change", function () {
                if (paymentMethod.value === "CARD") {
                    document.getElementById("payment-form").style.display = "block";
                } else {
                    document.getElementById("payment-form").style.display = "none";
                }

                // Публичен ключ от Stripe
                const stripe = Stripe('pk_test_51R3g8hGrjmQKYSJDcmIDwEYyYx02QBYFMfIclgzZRychxoy4Qz8eW6XxErK72O6ZubFHPfRe4J2DE1grM1RbNCLn0052nCYOaD');  // Заменете с вашия публичен ключ
                const elements = stripe.elements();

                // Създаване на елемент за карта
                const cardElement = elements.create('card');
                cardElement.mount('#card-element');

                // Обработване на формата при изпращане
                const form = document.getElementById('payment-form');
                form.addEventListener('submit', async (event) => {
                    event.preventDefault();


                    // Проверка дали е избран метод за плащане
                    if (paymentMethod.value !== 'CARD') {
                        document.getElementById('payment-message').innerText = 'Моля, изберете метод за плащане!';
                        return;
                    }

                    let address = document.getElementById("deliveryAddress").value;
                    let company = document.getElementById("deliveryCompany").value;
                    if (!address || !company) {
                        alert("Моля, попълнете всички полета!");
                        return;
                    }

                    const isAvailable = await checkAvailability();
                    if (isAvailable === false) {
                        return;
                    }

                    // Създаване на PaymentMethod
                    const {paymentMethod: stripePaymentMethod, error} = await stripe.createPaymentMethod({
                        type: 'card',
                        card: cardElement,
                        billing_details: {
                            name: 'Потребител'  // Може да добавиш име на потребителя
                        }
                    });

                    if (error) {
                        document.getElementById('payment-message').innerText = 'Грешка при създаване на платежен метод: ' + error.message;
                        return;
                    }

                    // Изпращане на заявка към бекенда за създаване на PaymentIntent с новия PaymentMethod
                    const response = await fetch('/api/payments/create-payment-intent', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({
                            amount: priceWithoutDeliveryFee * 100,  // Примерна сума в стотинки
                            payment_method: stripePaymentMethod.id // Изпращате paymentMethod ID към сървъра
                        })
                    });

                    const data = await response.json();

                    if (!data.clientSecret) {
                        document.getElementById('payment-message').innerText = 'Грешка при създаване на плащане!';
                        return;
                    }

                    // Потвърждаване на плащането с Stripe
                    const {
                        paymentIntent,
                        error: confirmError
                    } = await stripe.confirmCardPayment(data.clientSecret, {
                        payment_method: stripePaymentMethod.id
                    });

                    if (paymentIntent.status === "succeeded") {
                        document.getElementById('payment-message').innerText = '✅ Плащането е успешно!';
                        document.getElementById('payment-message').style.color = 'green';
                        let paymentMethodValue = paymentMethod.value;
                        await makeOrder(address, company, paymentMethodValue, stripePaymentMethod.id);
                        loadProductsForSale();
                    }
                    4
                });
            });


        })
        .catch(error => {
            console.log(error);
        })
}

async function checkAvailability() {
    fetch("/order/check-availability")
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage)
                })
            }
            return response.text();
        })
        .then(() => {
            return true
        })
        .catch(error => {
            alert(error);
            return false;
        })
}


async function makeOrder(address, company, paymentMethod, stripePaymentMethodId) {
    let obj = {
        deliveryAddress: address,
        deliveryCompanyName: company,
        paymentMethod: paymentMethod,  // Стойността на метода за плащане, който сте избрали
        paymentIntentId: stripePaymentMethodId  // ID на плащането
    }

    fetch("/order/make", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(obj)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage)
                })
            }
            return response.text();
        })
        .then(date => {
            alert(date);
        })
        .catch(error => {
            /*alert(error);*/
            console.log(error);
        })
}


function viewAnalysis() {
    let analysisResult = document.getElementById("analysisResult");

    if (analysisResult.style.display === "none") {
        analysisResult.style.display = "block";
        document.getElementById("product-table").style.display = "none";
        document.getElementById("orderList").style.display = "none";
        document.getElementById("shopping-car-div").style.display = "none";
    } else {
        analysisResult.style.display = "none";
    }

    fetch("/analysis/buyer")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            analysisResult.innerHTML = `
                <h3>Анализ на купувача</h3>
                <p><strong>Обща сума:</strong> <span>${data.totalPurchasePrice.toFixed(2)} лв.</span></p>
                <p><strong>Средна цена на покупка:</strong> <span>${data.averagePurchasePrice.toFixed(2)} лв.</span></p>
                <p><strong>Най-купуван тип продукт:</strong> <span>${data.mostPurchasedType}</span></p>
            `;


        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            document.getElementById("analysisResult").innerHTML = "<p style='color: red;'>Error loading data!</p>";
            document.getElementById("analysisResult").style.display = "block";
        });
}

function viewOrders() {
    const container = document.getElementById("orderList");

    if (container.style.display === "none") {
        container.style.display = "block";
        document.getElementById("product-table").style.display = "none";
        document.getElementById("orderList").style.display = "none";
        document.getElementById("analysisResult").style.display = "none";
        document.getElementById("order-input").style.display = "none";
        document.getElementById("shopping-car-div").style.display = "none";
    } else {
        container.style.display = "none";
    }

    fetch("/order/viewAll")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            return response.json();

        })
        .then(date => {
            const container = document.getElementById("orderList");
            container.innerHTML = "";

            date.forEach(order => {
                console.log("Обработва се поръчка:", order);
                // Обработка на списъка с продукти
                let productsHtml = "<ol>";
                if (order.orderProductDtoList && order.orderProductDtoList.length > 0) {
                    order.orderProductDtoList.forEach(product => {
                        console.log("Продукт:", product);  // Виж продукта
                        productsHtml += `
                        <h3>-----------------------------------------------</h3>
                        <ol>
                            <strong>Име:</strong> ${product.productName}
                            <br>
                            <strong>Тип:</strong> ${product.productType}
                            <br>
                            <strong>Количество:</strong> ${product.quantity} броя
                        </ol>
                        <h3>-----------------------------------------------</h3>
                    `;
                    });
                } else {
                    productsHtml += "<li>Няма продукти в тази поръчка.</li>";
                }

                productsHtml += "</ul>";

                const divElement = document.createElement("div");
                divElement.innerHTML = `

                    <p><strong>Дата на поръчката:</strong><span>${new Date(order.orderDate).toLocaleString("bg-BG", {
                    day: '2-digit',
                    month: 'long',
                    year: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                    second: '2-digit'
                     })}
                    </span></p>
                    <p><strong>Адрес на доставка:</strong><span>${order.addressToDelivery}</span></p>
                    <p><strong>Начин на плащане:</strong><span>${order.paymentMethod}</span></p>
                    <p><strong>Фирма доставчик:</strong><span>${order.deliveryCompany}</span></p>
                    <p><strong>Доставка:</strong><span>${order.deliveryFee.toFixed(2)}</span> лв</p>
                    <p><strong>Цена с включена доставка:</strong><span>${order.priceWithDeliveryFee.toFixed(2)}</span> лв</p>
                    <br>
                    <p>${productsHtml}</p>
                    <br>
                    `;

                container.appendChild(divElement);
            })
            container.style.display = "block";
        })

        .catch(err => console.error(err));
}