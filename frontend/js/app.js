// DOM elements

const $ = (id) => document.getElementById(id);

const claimInput = $("claimInput");
const characterCount = $("characterCount");
const analyzeButton = $("analyzeButton");
const loadingSection = $("loadingSection");
const resultSection = $("resultSection");
const historyContainer = $("historyContainer");
const errorMessage = $("errorMessage");

// ACCOUNT / PROFILE 

const accountMenu = document.getElementById("accountMenu");
const loginButton = document.getElementById("loginButton");
const profileButton = document.getElementById("profileButton");
const profileDropdown = document.getElementById("profileDropdown");

const profileAvatar = document.getElementById("profileAvatar");
const profileUserName = document.getElementById("profileUserName");

const profileLargeAvatar =
    document.getElementById("profileLargeAvatar");

const profileDropdownName =
    document.getElementById("profileDropdownName");

const profileDropdownEmail =
    document.getElementById("profileDropdownEmail");

const logoutButton =
    document.getElementById("logoutButton");

const profileHistoryButton =
    document.getElementById("profileHistoryButton");

const accountButton = document.getElementById("accountButton");
const accountDetailsPanel = document.getElementById("accountDetailsPanel");
const accountCloseButton = document.getElementById("accountCloseButton");

const accountDetailsAvatar = document.getElementById("accountDetailsAvatar");
const accountDetailsName = document.getElementById("accountDetailsName");
const accountDetailsEmail = document.getElementById("accountDetailsEmail");
const accountDetailsUserId = document.getElementById("accountDetailsUserId");


// Show logged-in user

function updateAccountUI() {

    const isLoggedIn =
        localStorage.getItem("claimcheckLoggedIn") === "true";

    const userName =
        localStorage.getItem("claimcheckUserName") || "User";

    const userEmail =
        localStorage.getItem("claimcheckUser") || "";

    if (!isLoggedIn) {

        loginButton.hidden = false;
        profileButton.hidden = true;
        profileDropdown.hidden = true;

        return;
    }

    loginButton.hidden = true;
    profileButton.hidden = false;

    profileUserName.textContent = userName;
    profileDropdownName.textContent = userName;
    profileDropdownEmail.textContent = userEmail;

    const firstLetter =
        userName.trim().charAt(0).toUpperCase() || "U";

    profileAvatar.textContent = firstLetter;
    profileLargeAvatar.textContent = firstLetter;
}


// Open / close profile dropdown

profileButton.addEventListener("click", () => {

    const isOpen =
        !profileDropdown.hidden;

    profileDropdown.hidden = isOpen;

    profileButton.setAttribute(
        "aria-expanded",
        String(!isOpen)
    );

});


// Close dropdown when clicking outside 

document.addEventListener("click", event => {

    if (
        accountMenu &&
        !accountMenu.contains(event.target)
    ) {

        profileDropdown.hidden = true;

        profileButton.setAttribute(
            "aria-expanded",
            "false"
        );

    }

});


// Logout 

logoutButton.addEventListener("click", () => {

    localStorage.removeItem(
        "claimcheckLoggedIn"
    );

    localStorage.removeItem(
        "claimcheckUser"
    );

    localStorage.removeItem(
        "claimcheckUserName"
    );

    localStorage.removeItem(
        "claimcheckUserId"
    );

    window.location.href = "login.html";

});


// Open verification history 

profileHistoryButton.addEventListener(
    "click",
    () => {

        profileDropdown.hidden = true;

        document
            .getElementById("history")
            .scrollIntoView({
                behavior: "smooth"
            });

    }
);

accountButton.addEventListener("click", () => {
  const userName = localStorage.getItem("claimcheckUserName") || "User";
  const userEmail = localStorage.getItem("claimcheckUser") || "Not available";
  const userId = localStorage.getItem("claimcheckUserId") || "Not available";

  const firstLetter = userName.trim().charAt(0).toUpperCase() || "U";

  accountDetailsAvatar.textContent = firstLetter;
  accountDetailsName.textContent = userName;
  accountDetailsEmail.textContent = userEmail;
  accountDetailsEmailCard.textContent = userEmail;
  accountDetailsUserId.textContent = userId;

  profileDropdown.hidden = true;
  accountDetailsPanel.hidden = false;

  document.body.classList.add("account-modal-open");
});

accountCloseButton.addEventListener("click", () => {
  accountDetailsPanel.hidden = true;
  document.body.classList.remove("account-modal-open");
});

accountDoneButton.addEventListener("click", () => {
  accountDetailsPanel.hidden = true;
  document.body.classList.remove("account-modal-open");
});


// Initialize account UI 

updateAccountUI();

// Character counter

function updateCounter() {
  characterCount.textContent = `${claimInput.value.length}/500`;
}

claimInput.addEventListener("input", updateCounter);


// Example claim buttons

document.querySelectorAll(".example-chip").forEach(btn => {
  btn.addEventListener("click", () => {
    claimInput.value = btn.dataset.claim;
    updateCounter();
    claimInput.focus();
  });
});


// Loading and scanning UI

function createScanningUI() {
  loadingSection.hidden = false;
  loadingSection.innerHTML = `
    <div class="verification-loader">
      <div class="scanner-orb">
        <div class="scanner-ring ring-one"></div>
        <div class="scanner-ring ring-two"></div>
        <div class="scanner-core">✦</div>
      </div>
      <div class="scan-copy">
        <span class="scan-kicker">CLAIMCHECK AI</span>
        <h2 id="scanTitle">Analyzing claim</h2>
        <p id="scanDescription">Understanding the statement and preparing evidence checks.</p>
      </div>
      <div class="scan-progress">
        <div class="scan-progress-track"><span id="scanProgressBar"></span></div>
        <span id="scanProgressValue">0%</span>
      </div>
      <div class="scan-steps">
        <div class="scan-step active">01 <span>Analyze</span></div>
        <div class="scan-step">02 <span>Decompose</span></div>
        <div class="scan-step">03 <span>Evidence</span></div>
        <div class="scan-step">04 <span>Verify</span></div>
      </div>
    </div>
  `;
}


// Verification animation

function runVerificationAnimation() {
  const stages = [
    ["Analyzing claim", "Understanding the statement and identifying its key meaning.", 20],
    ["Decomposing claim", "Breaking the statement into smaller checkable points.", 43],
    ["Searching evidence", "Comparing the claim with available evidence scenarios.", 67],
    ["Cross-checking", "Reviewing evidence consistency and confidence.", 88],
    ["Verification complete", "Preparing the explainable result.", 100]
  ];

  return new Promise(resolve => {
    let i = 0;
    const title = () => $("scanTitle");
    const desc = () => $("scanDescription");
    const bar = () => $("scanProgressBar");
    const value = () => $("scanProgressValue");

    const timer = setInterval(() => {
      const [t, d, p] = stages[i];
      if (title()) title().textContent = t;
      if (desc()) desc().textContent = d;
      if (bar()) bar().style.width = `${p}%`;
      if (value()) value().textContent = `${p}%`;

      document.querySelectorAll(".scan-step").forEach((step, idx) => {
        step.classList.toggle("active", idx <= Math.min(i, 3));
      });

      i++;

      if (i >= stages.length) {
        clearInterval(timer);
        setTimeout(resolve, 250);
      }
    }, 650);
  });
}


// Verdict styling

function applyVerdictStyle(verdict) {
  const el = $("verdict");
  el.className = "verdict-badge";
  const cls = verdict.toLowerCase().replaceAll(" ", "-");
  el.classList.add(`verdict-${cls}`);
  el.textContent = verdict;
}


// Confidence animation

function animateConfidence(target) {
  const valueEl = $("confidenceValue");
  const progress = $("confidenceProgress");
  let current = 0;

  return new Promise(resolve => {
    const timer = setInterval(() => {
      current += 2;
      if (current >= target) {
        current = target;
        clearInterval(timer);
        resolve();
      }

      valueEl.textContent = `${current}%`;
      progress.style.width = `${current}%`;
    }, 15);
  });
}


// Render decomposed claims

function renderClaims(claims) {
  $("decomposedClaims").innerHTML = claims.map((claim, i) => `
    <div class="decomposed-claim" style="animation-delay:${i * 70}ms">
      <span class="claim-number">${String(i + 1).padStart(2, "0")}</span>
      <span>${escapeHTML(claim.statement)}</span>
    </div>
  `).join("");
}


// Render evidence

function renderEvidence(evidence) {
  if (!evidence || !evidence.length) {
    $("evidenceContainer").innerHTML = `
      <div class="evidence-card">
        <div class="evidence-icon">◈</div>
        <div>
          <strong>No evidence found</strong>
          <p>No external or stored evidence was available for this claim.</p>
        </div>
      </div>
    `;

    return;
  }

  $("evidenceContainer").innerHTML = evidence.map((item, i) => `
    <div class="evidence-card" style="animation-delay:${i * 80}ms">
      <div class="evidence-icon">◈</div>
      <div>
        <strong>${escapeHTML(item.title || item.sourceName || "Evidence")}</strong>
        <p>${escapeHTML(item.content || "")}</p>
        ${
          item.url
            ? `<a href="${escapeHTML(item.url)}" target="_blank" rel="noopener noreferrer">View source</a>`
            : ""
        }
      </div>
    </div>
  `).join("");
}


// Render sources

function renderSources(sources) {
  if (!sources || !sources.length) {
    $("sourcesContainer").innerHTML = `
      <div class="source-card">
        <div class="source-icon">◎</div>
        <div class="source-top">
          <strong>No source information available</strong>
          <p>No source credibility information was returned.</p>
        </div>
        <span class="source-status">N/A</span>
      </div>
    `;

    return;
  }

  const uniqueSources = Array.from(
    new Map(
      sources.map(source => [
        `${source.sourceName}-${source.sourceType}`,
        source
      ])
    ).values()
  );

  $("sourcesContainer").innerHTML = uniqueSources.map(source => `
    <div class="source-card">
      <div class="source-icon">✦</div>

      <div class="source-top">
        <strong>${escapeHTML(source.sourceName || "Unknown source")}</strong>
        <p>
          ${escapeHTML(source.sourceType || "OTHER")}
          · Credibility score:
          ${Math.round(source.credibilityScore * 100)}%
        </p>
      </div>

      <span class="source-status">REFERENCE</span>
    </div>
  `).join("");
}


// Show verification result

async function showResult(result) {

  $("claimType").textContent = result.claimType || "GENERAL_FACTUAL";

  $("claimId").textContent =
    result.claimId
      ? `CC-${result.claimId}`
      : "CC-UNKNOWN";

  $("explanation").textContent =
    result.explanation || "No explanation available.";

  applyVerdictStyle(result.verdict || "INSUFFICIENT_EVIDENCE");

  renderClaims(result.decomposedClaims || []);

  renderEvidence(result.evidence || []);

  renderSources(result.sources || []);

  resultSection.hidden = false;

  const confidencePercent =
    Math.round((result.confidence || 0) * 100);

  await animateConfidence(confidencePercent);

  resultSection.scrollIntoView({
    behavior: "smooth",
    block: "start"
  });
}


// History storage

let backendHistory = [];

async function loadHistory() {

  try {

    const response = await fetch(
      "http://localhost:8080/api/claims/history"
    );

    if (!response.ok) {
      throw new Error("Failed to load claim history.");
    }

    const claims = await response.json();

    /*
     * Get the latest verification result
     * for every claim.
     */
    backendHistory = await Promise.all(
      claims.map(async (claim) => {

        try {

          const historyResponse = await fetch(
            `http://localhost:8080/api/claims/${claim.claimId}/history`
          );

          if (!historyResponse.ok) {
            return {
              ...claim,
              verdict: "INSUFFICIENT_EVIDENCE",
              confidence: 0
            };
          }

          const verificationHistory =
            await historyResponse.json();

          const latest =
            verificationHistory[0];

          if (!latest) {
            return {
              ...claim,
              verdict: "INSUFFICIENT_EVIDENCE",
              confidence: 0
            };
          }

          return {
            ...claim,
            verdict: latest.verdict,
            confidence: Math.round(
              latest.confidence * 100
            ),
            timestamp: latest.verifiedAt
          };

        } catch (error) {

          console.error(
            `Failed to load history for claim ${claim.claimId}:`,
            error
          );

          return {
            ...claim,
            verdict: "INSUFFICIENT_EVIDENCE",
            confidence: 0
          };
        }

      })
    );

    renderHistory();

  } catch (error) {

    console.error(
      "History loading failed:",
      error
    );

    backendHistory = [];

    renderHistory();
  }
}


// Format history time

function formatTime(timestamp) {
  const seconds = Math.floor((Date.now() - timestamp) / 1000);
  if (seconds < 60) return "Just now";
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return `${minutes} min ago`;
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return `${hours} hr ago`;
  return `${Math.floor(hours / 24)} day ago`;
}


// History status

function historyStatus(verdict) {
  if (verdict === "SUPPORTED") return ["supported", "✓"];
  if (verdict === "CONTRADICTED") return ["contradicted", "×"];
  if (verdict === "MIXED") return ["mixed", "−"];
  return ["insufficient", "−"];
}


// Render history

function renderHistory() {

  const history = backendHistory;

  if (!history.length) {

    historyContainer.innerHTML = `
      <div class="history-empty">
        <div class="empty-icon">◷</div>
        <strong>No verifications yet</strong>
        <span>Analyze your first claim and it will appear here.</span>
      </div>
    `;

    return;
  }

  historyContainer.innerHTML = history.map((item, i) => {

    const [statusClass, icon] =
      historyStatus(item.verdict);

    return `
      <div
        class="history-row"
        style="animation-delay:${i * 70}ms"
        onclick="openHistoryClaim(${item.claimId})"
        role="button"
        tabindex="0"
      >

        <div class="history-status ${statusClass}">
          ${icon}
        </div>

        <div class="history-main">

          <div class="history-verdict">
            ${escapeHTML(item.verdict)}
          </div>

          <div
            class="history-claim"
            title="${escapeHTML(item.statement)}"
          >
            ${escapeHTML(item.statement)}
          </div>

          <div class="history-time">
            ${item.timestamp
              ? formatTime(new Date(item.timestamp).getTime())
              : "Saved in ClaimCheck"
            }
          </div>

        </div>

        <div class="history-confidence">

          <strong>
            ${item.confidence}%
          </strong>

          <span>Confidence</span>

          <div class="mini-progress">
            <span
              style="width:${item.confidence}%"
            ></span>
          </div>

        </div>

        <div class="history-arrow">›</div>

      </div>
    `;

  }).join("");
}

async function openHistoryClaim(claimId) {

  try {

    resultSection.hidden = true;

    loadingSection.hidden = false;

    const response = await fetch(
      `http://localhost:8080/api/claims/${claimId}/analyze`
    );

    if (!response.ok) {
      throw new Error(
        `Unable to load claim (${response.status}).`
      );
    }

    const result = await response.json();

    loadingSection.hidden = true;

    await showResult(result);

    resultSection.scrollIntoView({
      behavior: "smooth",
      block: "start"
    });

  } catch (error) {

    console.error(
      "Failed to open history claim:",
      error
    );

    loadingSection.hidden = true;

    errorMessage.textContent =
      error.message ||
      "Unable to load this verification.";

  }
}

// Clear history

$("clearHistoryButton").addEventListener("click", () => {
  localStorage.removeItem("claimcheckHistory");
  renderHistory();
});


// New claim button

$("newClaimButton").addEventListener("click", () => {
  resultSection.hidden = true;
  claimInput.focus();
  window.scrollTo({ top: 0, behavior: "smooth" });
});


// Analyze claim

analyzeButton.addEventListener("click", async () => {

  const statement = claimInput.value.trim();

  if (!statement) {
    errorMessage.textContent =
      "Please enter a claim before analyzing.";

    claimInput.focus();
    return;
  }

  if (statement.length < 8) {
    errorMessage.textContent =
      "Please enter a little more detail for the claim.";

    claimInput.focus();
    return;
  }

  errorMessage.textContent = "";

  resultSection.hidden = true;

  analyzeButton.classList.add("is-analyzing");
  analyzeButton.disabled = true;

  analyzeButton.querySelector("span:nth-child(2)")
    .textContent = "Analyzing...";

  createScanningUI();

  loadingSection.scrollIntoView({
    behavior: "smooth",
    block: "center"
  });

  try {

    /*
     * Run the existing ClaimCheck scanning animation
     * while the backend processes the claim.
     */
    const animationPromise =
      runVerificationAnimation();

    /*
     * Send the real claim to Spring Boot.
     */
    const response = await fetch(
      "http://localhost:8080/api/claims/analyze",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          statement: statement
        })
      }
    );

    if (!response.ok) {

      let message =
        `Backend request failed (${response.status}).`;

      try {
        const errorData = await response.json();

        if (errorData.message) {
          message = errorData.message;
        }

      } catch {
        // Keep the default error message.
      }

      throw new Error(message);
    }

    const result = await response.json();

    /*
     * Make sure the scanning animation finishes
     * before displaying the result.
     */
    await animationPromise;

    loadingSection.hidden = true;

    analyzeButton.classList.remove("is-analyzing");
    analyzeButton.disabled = false;

    analyzeButton.querySelector("span:nth-child(2)")
      .textContent = "Analyze Claim";

    await showResult(result);

  } catch (error) {

    console.error("Claim analysis failed:", error);

    loadingSection.hidden = true;

    analyzeButton.classList.remove("is-analyzing");
    analyzeButton.disabled = false;

    analyzeButton.querySelector("span:nth-child(2)")
      .textContent = "Analyze Claim";

    errorMessage.textContent =
      error.message ||
      "Unable to analyze the claim. Please make sure the backend is running.";

  }
});


// =========================================
// Enter key behavior
// =========================================

claimInput.addEventListener("keydown", event => {

  // Only handle the Enter key
  if (event.key !== "Enter") {
    return;
  }

  /*
    DESKTOP / LAPTOP
    ----------------
    Enter       → Analyze Claim
    Shift+Enter → New line

    PHONE / TOUCH DEVICE
    --------------------
    Enter       → New line
    Shift+Enter → New line
  */

  const isTouchDevice =
    window.matchMedia("(pointer: coarse)").matches ||
    navigator.maxTouchPoints > 0;

  // On phones/tablets, allow normal Enter behavior.
  if (isTouchDevice) {
    return;
  }

  // On desktop, Shift+Enter creates a new line.
  if (event.shiftKey) {
    return;
  }

  // Desktop Enter → Analyze Claim
  event.preventDefault();
  analyzeButton.click();
});


// Navigation

const mobileMenuButton = $("mobileMenuButton");
const mobileSidebar = $("mobileSidebar");
const mobileSidebarClose = $("mobileSidebarClose");
const sidebarOverlay = $("sidebarOverlay");

function openMobileSidebar(){
  mobileSidebar.classList.add("is-open");
  sidebarOverlay.classList.add("is-visible");

  mobileMenuButton.setAttribute("aria-expanded", "true");
  document.body.classList.add("menu-open");
}

function closeMobileSidebar(){
  mobileSidebar.classList.remove("is-open");
  sidebarOverlay.classList.remove("is-visible");

  mobileMenuButton.setAttribute("aria-expanded", "false");
  document.body.classList.remove("menu-open");
}


// Hamburger button

mobileMenuButton.addEventListener("click", openMobileSidebar);


// Close button

mobileSidebarClose.addEventListener("click", closeMobileSidebar);


// Overlay click

sidebarOverlay.addEventListener("click", closeMobileSidebar);


// Navigation links

document.querySelectorAll(".side-link,.nav-link").forEach(link => {

  link.addEventListener("click", () => {

    document.querySelectorAll(".side-link,.nav-link")
      .forEach(x => x.classList.remove("active"));

    const target = link.getAttribute("href");

    document.querySelectorAll(`[href="${target}"]`)
      .forEach(x => x.classList.add("active"));

    // Close mobile sidebar after selecting a section
    if(window.innerWidth <= 760){
      closeMobileSidebar();
    }
  });

});


// Theme

const savedTheme = localStorage.getItem("claimcheckTheme");

if(savedTheme === "dark"){
  document.body.classList.add("soft-night");
  $("themeButton").textContent = "☀";
}else{
  $("themeButton").textContent = "☾";
}


$("themeButton").addEventListener("click", () => {

  document.body.classList.toggle("soft-night");

  const darkMode = document.body.classList.contains("soft-night");

  localStorage.setItem(
    "claimcheckTheme",
    darkMode ? "dark" : "light"
  );

  $("themeButton").textContent = darkMode ? "☀" : "☾";
});


// HTML escaping

function escapeHTML(value) {
  return String(value).replace(/[&<>"']/g, char => ({
    "&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"
  }[char]));
}


// Initialize application

updateCounter();
loadHistory();