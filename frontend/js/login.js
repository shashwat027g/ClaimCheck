const loginForm = document.getElementById("loginForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const togglePassword = document.getElementById("togglePassword");
const loginMessage = document.getElementById("loginMessage");
const forgotPassword = document.getElementById("forgotPassword");

// Restore saved theme

const savedTheme =
  localStorage.getItem("claimcheckTheme");

if(savedTheme === "dark"){
  document.body.classList.add("soft-night");
}

// Show / hide password

togglePassword.addEventListener("click", () => {

  const isPassword =
    passwordInput.type === "password";

  passwordInput.type =
    isPassword ? "text" : "password";

  togglePassword.textContent =
    isPassword ? "Hide" : "Show";

});


// Demo login

loginForm.addEventListener("submit", event => {

  event.preventDefault();

  const email = emailInput.value.trim();
  const password = passwordInput.value.trim();

  if(!email || !password){

    loginMessage.textContent =
      "Please enter your email and password.";

    loginMessage.style.color = "#d94b67";

    return;
  }


  // Standalone frontend demo login

  localStorage.setItem(
    "claimcheckLoggedIn",
    "true"
  );

  localStorage.setItem(
    "claimcheckUser",
    email
  );


  loginMessage.textContent =
    "Login successful. Opening ClaimCheck...";

  loginMessage.style.color =
    "#4c9a70";


  setTimeout(() => {

    window.location.href = "index.html";

  }, 700);

});


// Forgot password demo

forgotPassword.addEventListener("click", event => {

  event.preventDefault();

  loginMessage.textContent =
    "Password recovery is available in the standalone demo.";

  loginMessage.style.color =
    "#665be8";

});