const $ = (id) => document.getElementById(id);

const claimInput = $("claimInput");
const characterCount = $("characterCount");
const analyzeButton = $("analyzeButton");
const loadingSection = $("loadingSection");
const resultSection = $("resultSection");
const historyContainer = $("historyContainer");
const errorMessage = $("errorMessage");

const demoResults = [
  {
    match: ["earth", "round"],
    verdict: "SUPPORTED",
    confidence: 94,
    claimType: "FACTUAL",
    explanation: "The claim is consistent with established scientific evidence and the accepted shape of Earth.",
    decomposedClaims: ["Earth has an approximately spherical shape.", "Earth is not a flat plane."],
    evidence: [
      ["Scientific observation", "Measurements, satellite imagery, and observations consistently support a spherical Earth."],
      ["Astronomical evidence", "Earth's curved shadow and observations from space provide supporting evidence."]
    ]
  },
  {
    match: ["sun", "revolves", "earth"],
    verdict: "CONTRADICTED",
    confidence: 96,
    claimType: "FACTUAL",
    explanation: "The statement conflicts with the modern heliocentric model of the Solar System.",
    decomposedClaims: ["The Sun is at the center of the Solar System.", "Earth orbits the Sun."],
    evidence: [
      ["Astronomy", "Planetary observations and orbital measurements support Earth's orbit around the Sun."],
      ["Scientific model", "The heliocentric model accurately describes the observed motions of planets."]
    ]
  },
  {
    match: ["coffee", "smarter"],
    verdict: "INSUFFICIENT EVIDENCE",
    confidence: 63,
    claimType: "GENERAL",
    explanation: "The statement is broad and does not provide enough detail to establish a universal conclusion.",
    decomposedClaims: ["Coffee consumption may affect alertness.", "That does not establish that coffee makes everyone smarter."],
    evidence: [
      ["Research context", "Effects of caffeine can vary between individuals and depend on dose and context."]
    ]
  },
  {
    match: ["climate", "humans"],
    verdict: "MIXED",
    confidence: 78,
    claimType: "SCIENTIFIC",
    explanation: "Human activity is a major driver of recent climate change, while climate systems also contain natural influences.",
    decomposedClaims: ["Human activities affect climate.", "Natural factors also influence climate variability."],
    evidence: [
      ["Climate science", "Multiple lines of evidence connect greenhouse-gas emissions with recent warming."],
      ["Natural variability", "Natural processes can influence climate on different timescales."]
    ]
  }
];

function findDemoResult(statement) {
  const text = statement.toLowerCase();
  return demoResults.find(item => item.match.every(word => text.includes(word))) || {
    verdict: "INSUFFICIENT EVIDENCE",
    confidence: 61,
    claimType: "GENERAL",
    explanation: "This standalone demo does not have a matching evidence scenario for the submitted statement.",
    decomposedClaims: [statement, "Additional evidence would be needed for a stronger conclusion."],
    evidence: [["Demo evidence", "Use one of the example claims to see the full verification presentation."]]
  };
}

function updateCounter() {
  characterCount.textContent = `${claimInput.value.length}/500`;
}
claimInput.addEventListener("input", updateCounter);

document.querySelectorAll(".example-chip").forEach(btn => {
  btn.addEventListener("click", () => {
    claimInput.value = btn.dataset.claim;
    updateCounter();
    claimInput.focus();
  });
});

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

function applyVerdictStyle(verdict) {
  const el = $("verdict");
  el.className = "verdict-badge";
  const cls = verdict.toLowerCase().replaceAll(" ", "-");
  el.classList.add(`verdict-${cls}`);
  el.textContent = verdict;
}

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

function renderClaims(claims) {
  $("decomposedClaims").innerHTML = claims.map((claim, i) => `
    <div class="decomposed-claim" style="animation-delay:${i * 70}ms">
      <span class="claim-number">${String(i + 1).padStart(2, "0")}</span>
      <span>${escapeHTML(claim)}</span>
    </div>
  `).join("");
}

function renderEvidence(evidence) {
  $("evidenceContainer").innerHTML = evidence.map((item, i) => `
    <div class="evidence-card" style="animation-delay:${i * 80}ms">
      <div class="evidence-icon">◈</div>
      <div>
        <strong>${escapeHTML(item[0])}</strong>
        <p>${escapeHTML(item[1])}</p>
      </div>
    </div>
  `).join("");
}

function renderSources() {
  $("sourcesContainer").innerHTML = `
    <div class="source-card">
      <div class="source-icon">◎</div>
      <div class="source-top">
        <strong>ClaimCheck demo evidence set</strong>
        <p>Local demonstration data used for the standalone frontend.</p>
      </div>
      <span class="source-status">REFERENCE</span>
    </div>
    <div class="source-card">
      <div class="source-icon">✦</div>
      <div class="source-top">
        <strong>Explainable verification layer</strong>
        <p>Shows evidence context, classification, and confidence without backend connection.</p>
      </div>
      <span class="source-status">DEMO</span>
    </div>
  `;
}

async function showResult(statement) {
  const result = findDemoResult(statement);
  $("claimType").textContent = result.claimType;
  $("claimId").textContent = `CC-${Date.now().toString().slice(-6)}`;
  $("explanation").textContent = result.explanation;

  applyVerdictStyle(result.verdict);
  renderClaims(result.decomposedClaims);
  renderEvidence(result.evidence);
  renderSources();

  resultSection.hidden = false;
  await animateConfidence(result.confidence);

  saveHistory(statement, result);
  renderHistory();

  resultSection.scrollIntoView({ behavior: "smooth", block: "start" });
}

function getHistory() {
  try { return JSON.parse(localStorage.getItem("claimcheckHistory") || "[]"); }
  catch { return []; }
}

function saveHistory(statement, result) {
  const history = getHistory();
  history.unshift({
    statement,
    verdict: result.verdict,
    confidence: result.confidence,
    timestamp: Date.now()
  });
  localStorage.setItem("claimcheckHistory", JSON.stringify(history.slice(0, 8)));
}

function formatTime(timestamp) {
  const seconds = Math.floor((Date.now() - timestamp) / 1000);
  if (seconds < 60) return "Just now";
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return `${minutes} min ago`;
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return `${hours} hr ago`;
  return `${Math.floor(hours / 24)} day ago`;
}

function historyStatus(verdict) {
  if (verdict === "SUPPORTED") return ["supported", "✓"];
  if (verdict === "CONTRADICTED") return ["contradicted", "×"];
  if (verdict === "MIXED") return ["mixed", "−"];
  return ["insufficient", "−"];
}

function renderHistory() {
  const history = getHistory();

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
    const [statusClass, icon] = historyStatus(item.verdict);
    return `
      <div class="history-row" style="animation-delay:${i * 70}ms">
        <div class="history-status ${statusClass}">${icon}</div>
        <div class="history-main">
          <div class="history-verdict">${escapeHTML(item.verdict)}</div>
          <div class="history-claim" title="${escapeHTML(item.statement)}">${escapeHTML(item.statement)}</div>
          <div class="history-time">${formatTime(item.timestamp)}</div>
        </div>
        <div class="history-confidence">
          <strong>${item.confidence}%</strong>
          <span>Confidence</span>
          <div class="mini-progress"><span style="width:${item.confidence}%"></span></div>
        </div>
        <div class="history-arrow">›</div>
      </div>
    `;
  }).join("");
}

$("clearHistoryButton").addEventListener("click", () => {
  localStorage.removeItem("claimcheckHistory");
  renderHistory();
});

$("newClaimButton").addEventListener("click", () => {
  resultSection.hidden = true;
  claimInput.focus();
  window.scrollTo({ top: 0, behavior: "smooth" });
});

analyzeButton.addEventListener("click", async () => {
  const statement = claimInput.value.trim();

  if (!statement) {
    errorMessage.textContent = "Please enter a claim before analyzing.";
    claimInput.focus();
    return;
  }

  if (statement.length < 8) {
    errorMessage.textContent = "Please enter a little more detail for the claim.";
    claimInput.focus();
    return;
  }

  errorMessage.textContent = "";
  resultSection.hidden = true;
  analyzeButton.classList.add("is-analyzing");
  analyzeButton.disabled = true;
  analyzeButton.querySelector("span:nth-child(2)").textContent = "Analyzing...";

  createScanningUI();
  loadingSection.scrollIntoView({ behavior: "smooth", block: "center" });

  await runVerificationAnimation();

  loadingSection.hidden = true;
  analyzeButton.classList.remove("is-analyzing");
  analyzeButton.disabled = false;
  analyzeButton.querySelector("span:nth-child(2)").textContent = "Analyze Claim";

  await showResult(statement);
});

claimInput.addEventListener("keydown", event => {
  if (event.ctrlKey && event.key === "Enter") analyzeButton.click();
});

document.querySelectorAll(".side-link,.nav-link").forEach(link => {
  link.addEventListener("click", () => {
    document.querySelectorAll(".side-link,.nav-link").forEach(x => x.classList.remove("active"));
    document.querySelectorAll(`[href="${link.getAttribute("href")}"]`).forEach(x => x.classList.add("active"));
  });
});

$("themeButton").addEventListener("click", () => {
  document.body.classList.toggle("soft-night");
});

function escapeHTML(value) {
  return String(value).replace(/[&<>"']/g, char => ({
    "&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"
  }[char]));
}

updateCounter();
renderHistory();
