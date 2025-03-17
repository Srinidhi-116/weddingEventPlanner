
document.addEventListener("DOMContentLoaded", function () {
//        setTimeout(()=>{
        checkRevAuthentication();
//        },2000)
});



function checkRevAuthentication() {

        let token = getCookie("auth_token");
        console.log("auth-token : "+token);
        if (token) {
            // Validate token with server
            fetch("auth-servlet", {
                method: "GET",
                credentials: "include"
            })
            .then(response => response.json())
            .then(data => {
            console.log(data.loggedIn)
            console.log(data.isVendor)
                if (data.loggedIn) {
                    window.location.href = (data.isVendor) ? "dashboard-vendor.html": "dashboard-customer.html";
                }
            })
            .catch(error => {
                console.error('Auth validation failed:', error);
                // Call LogoutServlet instead of manually deleting cookie
                fetch("logout-servlet", {
                    method: "POST",
                    credentials: "include"
                });
            });
        }
//          else{
//              window.location.href = "login.html";
//              }



}
    function getCookie(name) {

        let cookies = document.cookie.split("; ");
        for (let i = 0; i < cookies.length; i++) {
            let parts = cookies[i].split("=");
            console.log(parts[1]);
            if (parts[0] === name) return parts[1];

        }
        return null;
    }


    function deleteCookie(name) {
        document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    }

