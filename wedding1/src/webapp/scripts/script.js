document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");
    const signupForm = document.getElementById("register-form");
    const logoutBtn = document.getElementById("logoutBtn");

    document.querySelectorAll('.role-btn').forEach(button => {
        button.addEventListener('click', function() {
            // Remove 'active' class from all buttons
            document.querySelectorAll('.role-btn').forEach(btn => {
                btn.classList.remove('active');
            });

            // Add 'active' class to the clicked button
            this.classList.add('active');

            // Get the selected role
            const selectedRole = this.getAttribute('data-role');
            console.log(`Selected role: ${selectedRole}`);

            if (selectedRole === 'vendor') {
                console.log("Vendor selected!");
                document.querySelectorAll('.vendor-field').forEach(field => {
                                field.style.display = 'block';
                            });
                // Add specific logic for vendor selection here
            } else if (selectedRole === 'customer') {
                console.log("Customer selected!");
                document.querySelectorAll('.vendor-field').forEach(field => {
                                field.style.display = 'none';
                            });
                // Add specific logic for customer selection here
            }
        });
    });


    // ✅ Signup Event Listener
    if (signupForm) {
        setupSignupPasswordValidation();
        signupForm.addEventListener("submit", function (e) {

        e.preventDefault();

            const name = document.getElementById("name").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            const mobile = document.getElementById("mobile").value;
            const isValidPassword= validatePassword(password);
            const role = document.querySelector(".role-btn.active").getAttribute("data-role");
            let location = null;
            let company = null;
            if(role === "vendor"){
               location = document.getElementById("location").value;
               company = document.getElementById("company").value;
            }

                const reqData = {
                            name: name,
                            email: email,
                            password: password,
                            mobile: mobile,
                            role: role,
                            ...(role === "vendor" && {
                                company: company,
                                location: location
                            })
                        };


            if (!isValidPassword) {
                document.getElementById("signupMessage").innerText = "Please enter a strong password";
                return;
            }else{
             document.getElementById("signupMessage").innerText ="";
            }

            console.log(reqData);

			fetch("http://localhost:8080/wedding1/signup-servlet", {
			    method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(reqData)})
			.then(response => response.json())  // Ensure JSON parsing
			.then(data => {
			    document.getElementById("signupMessage").innerText = data.message;
			    if (data.success) {
			        setTimeout(() => window.location.href = "login.html", 2000);
			    }
			})
			.catch(error => console.error("Signup Error:", error));
        });
    }

    // ✅ Login Event Listener
    if (loginForm) {
        loginForm.addEventListener("submit", function (e) {
            e.preventDefault();
            const loginMessage = document.getElementById("loginMessage");
            loginMessage.innerText = ""; // Clear previous messages

            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
//            const role = document.querySelector(".role-btn.active").getAttribute("data-role");

            // Basic validation
            if (!email || !password) {
                loginMessage.innerText = "Please fill in all fields";
                loginMessage.style.color = "#dc3545";
                return;
            }

            const jsonCredentials = {
                email : email,
                password : password,
            }

            fetch("http://localhost:8080/wedding1/login-servlet", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(jsonCredentials),
                credentials: "include"
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(data => Promise.reject(data));
                    }
                    return response.json();
                })
                .then(data => {
                    loginMessage.style.color = data.success ? "#28a745" : "#dc3545";
                    loginMessage.innerText = data.message;

                    if (data.success && data.user) {
                        // Store user data and redirect
                        sessionStorage.setItem('userEmail', data.user.email);
                        sessionStorage.setItem('userId', data.user.userId);
                        sessionStorage.setItem('userName', data.user.name);
                        window.location.href = (data.user.roleId === 2)? "dashboard-customer.html" : "dashboard-vendor.html";
                    } else if (data.message.includes("verify")) {
                        loginMessage.innerHTML = "Please verify your email before logging in. <br>Check your inbox for the verification link.";
                    }
                })
                .catch(error => {
                    loginMessage.style.color = "#dc3545";
                    if (error.message) {
                        loginMessage.innerText = error.message;
                    } else {
                        loginMessage.innerText = "Login failed. Please try again.";
                        console.error("Login Error:", error);
                    }
                });
        });
    }

    // ✅ Logout Event Listener (Calls LogoutServlet)
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function () {
            fetch("LogoutServlet", { method: "POST", credentials: "include" })
            .then(() => location.reload());
        });
    }

});



// Password configuration object
const passwordConfig = {
    minLength: 8,
    patterns: {
        uppercase: { regex: /[A-Z]/, message: "uppercase letter" },
        lowercase: { regex: /[a-z]/, message: "lowercase letter" },
        number: { regex: /[0-9]/, message: "number" },
        special: { regex: /[!@#$%^&*]/, message: "special character (!@#$%^&*)" }
    }
};

// Password validation method
function validatePassword(password) {
    // Check minimum length
    if (password.length < passwordConfig.minLength) {
        return false;
    }

    // Check each pattern requirement
    for (const [key, pattern] of Object.entries(passwordConfig.patterns)) {
        if (!pattern.regex.test(password)) {
            return false;
        }
    }

    return true;
}


// Method to set up password validation listener
function setupPasswordValidation() {
    const passwordInput = document.getElementById("password");
    const passwordMessage = document.getElementById("loginMessage");

    if (passwordInput && passwordMessage) {
        passwordInput.addEventListener("input", function() {
            const result = validatePassword(this.value);
            passwordMessage.textContent = result.message;
            passwordMessage.style.color = result.isValid ? "#28a745" : "#dc3545";
        });
    }
}

function setupSignupPasswordValidation() {
    const passwordInput = document.getElementById("password");
    const passwordMessage = document.getElementById("passwordMessage");
    const signupButton = document.querySelector("#signupForm button[type='submit']");

    if (passwordInput && passwordMessage) {
        passwordInput.addEventListener("input", function() {
            const result = validatePassword(this.value);

            // Update message and styling
            passwordMessage.textContent = result.message;
            passwordMessage.style.color = result.isValid ? "#28a745" : "#dc3545";

            // Enable/disable signup button based on password validity
            signupButton.disabled = !result.isValid;
            signupButton.style.opacity = result.isValid ? "1" : "0.6";
        });
    }
}
