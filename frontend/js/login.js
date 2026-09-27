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


// Backend login

loginForm.addEventListener("submit", async event => {

  event.preventDefault();

  const email = emailInput.value.trim();
  const password = passwordInput.value.trim();

  if (!email || !password) {

    loginMessage.textContent =
      "Please enter your email and password.";

    loginMessage.style.color = "#d94b67";

    return;
  }

  loginMessage.textContent =
    "Signing in...";

  loginMessage.style.color =
    "#665be8";

  const loginButton =
    loginForm.querySelector(".login-submit");

  loginButton.disabled = true;

  try {

    const response = await fetch(
      "http://localhost:8080/api/auth/login",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          email: email,
          password: password
        })
      }
    );

    const result = await response.json();

    if (!response.ok) {

      throw new Error(
        result.message ||
        "Invalid email or password."
      );
    }

    /*
     * Save basic login information
     * for the current frontend session.
     */
    localStorage.setItem(
      "claimcheckLoggedIn",
      "true"
    );

    localStorage.setItem(
      "claimcheckUser",
      result.email
    );

    localStorage.setItem(
      "claimcheckUserName",
      result.name
    );

    localStorage.setItem(
      "claimcheckUserId",
      result.userId
    );

    loginMessage.textContent =
      "Login successful. Opening ClaimCheck...";

    loginMessage.style.color =
      "#4c9a70";

    setTimeout(() => {

      window.location.href =
        "index.html";

    }, 700);

  } catch (error) {

    console.error(
      "Login failed:",
      error
    );

    loginMessage.textContent =
      error.message ||
      "Unable to login. Please try again.";

    loginMessage.style.color =
      "#d94b67";

  } finally {

    loginButton.disabled = false;

  }

});


// Forgot password demo

forgotPassword.addEventListener("click", event => {

  event.preventDefault();

  loginMessage.textContent =
    "Password recovery is available in the standalone demo.";

  loginMessage.style.color =
    "#665be8";

});