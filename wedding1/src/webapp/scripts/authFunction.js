function checkAuthentication() {
    document.addEventListener("DOMContentLoaded", function () {
        let token = getCookie("auth_token");
        // const logoutBtn = document.getElementById("logoutBtn");
        // logoutBtn.style.display = 'block';
        checkLoginStatus();
    });
}

function getCookie(name) {
    let cookies = document.cookie.split("; ");
    for (let i = 0; i < cookies.length; i++) {
        let parts = cookies[i].split("=");
        if (parts[0] === name) return parts[1];
    }
    return null;
}

function checkLoginStatus() {
    fetch("http://localhost:8080/wedding1/auth-servlet?_="+new Date().getTime(), {
        method: "GET",
        credentials: "include"
    })
        .then(response => response.json())
        .then(data => {
            if (data.loggedIn) {
                // Update session storage with user data
                sessionStorage.setItem('userEmail', data.email);
                sessionStorage.setItem('userId', data.userId);
                sessionStorage.setItem('userName', data.name);

                // Redirect to dashboard only if on login/index page
                const currentPage = window.location.pathname;
                if (currentPage.includes('login.html') ||currentPage.includes('signup.html') || currentPage === '/' || currentPage.endsWith('/')) {
                    window.location.href = (data.isVendor) ? 'dashboard-vendor.html' : 'dashboard-customer.html';
                }
            } else {
                // Clear session storage if not logged in
                sessionStorage.clear();

                // Show logout button only if it exists
//                if (logoutBtn) {
//                    logoutBtn.style.display = "none";
//                }

//                // Redirect to index if not already there
//                if (window.location.pathname.includes('login.html')) {
//                    window.location.href = 'login.html';
//                }
//                if (window.location.pathname.includes('signup.html')) {
//                    window.location.href = 'signup.html';
//                }
            }
        })
        .catch(error => {
            console.error('Auth check failed:', error);
            sessionStorage.clear();
            window.location.href = 'login.html';
        });
}


// Call the function when the page loads
checkAuthentication();
