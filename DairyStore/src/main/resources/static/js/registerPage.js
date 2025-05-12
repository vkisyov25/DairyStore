function toggleCompanyFields() {
    const role = document.getElementById("role").value;
    const companyFields = document.getElementById("companyFields");
    const isCompanyForm = document.getElementById("isCompanyForm");

    if (role === "seller") {
        companyFields.style.display = "block";
        isCompanyForm.style.display = "none"
    } else if (role === "buyer") {
        isCompanyForm.style.display = "block"
        if (document.getElementById("isCompany").checked) {
            companyFields.style.display = "block";
        } else {
            companyFields.style.display = "none";
        }
    } else {
        companyFields.style.display = "none";
    }
}

function saveUser() {
    let obj = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        authorities: document.getElementById("role").value,
        email: document.getElementById("email").value,
        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value,
        companyName: document.getElementById("companyName").value,
        companyEIK: document.getElementById("companyEIK").value
    }

    fetch("/test/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(obj)
    }).then(response => {
        return response.json()
    }).then(data => {
        console.log(data.errors)
        if (data.errors) {
            displayErrors(data.errors);
        } else {
            alert(data.message);
            window.location.href = "/html/login.html";
        }
    }).catch(error => {
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
                ? errors[field][0]
                : errors[field];
            errorSpan.style.color = "red";
        }
    });
}
