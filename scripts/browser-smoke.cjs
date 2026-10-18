/* Run against a disposable database: registers test accounts and deletes its own trip. */
const { chromium } = require("../frontend/node_modules/playwright");
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");
const baseURL = process.env.BASE_URL || "http://127.0.0.1:4200";
const artifacts = process.env.ARTIFACT_DIR || "/tmp/tripboard-browser-tests";
const suffix = Date.now().toString();
const username = "owner" + suffix;
const memberName = "member" + suffix;
const password = "Browser-test-" + suffix;

(async () => {
  fs.mkdirSync(artifacts, { recursive: true });
  const browser = await chromium.launch({
    headless: true,
    ...(process.env.CHROMIUM_PATH
      ? { executablePath: process.env.CHROMIUM_PATH }
      : {}),
    args: ["--no-sandbox"],
  });
  const page = await browser.newPage({
    viewport: { width: 1440, height: 1000 },
  });
  page.setDefaultTimeout(12000);
  const browserErrors = [];
  if (process.env.DEBUG_E2E) {
    page.on("framenavigated", (frame) =>
      console.log("Navigation:", frame.url()),
    );
    page.on("response", (response) => {
      if (response.status() >= 400)
        console.log("HTTP:", response.status(), response.url());
    });
  }
  page.on("pageerror", (e) => browserErrors.push(e.message));
  const dialog = () => page.locator("dialog");
  const section = (name) =>
    page
      .getByRole("navigation")
      .getByRole("link", { name, exact: true })
      .click();
  async function saved(name) {
    await dialog().getByRole("button", { name, exact: true }).click();
    await dialog().waitFor({ state: "detached" });
  }
  async function newIdea(title) {
    await page.getByRole("button", { name: "Suggest an idea" }).click();
    await dialog().getByLabel("What’s the idea?").fill(title);
    await dialog().getByLabel("Location", { exact: true }).fill("Reykjavik");
    await dialog()
      .getByLabel(/Estimated cost/)
      .fill("12.00");
    await dialog()
      .getByLabel("Make the case")
      .fill("A great stop for the whole crew.");
    await saved("Save idea");
    await page.locator(".idea-card").filter({ hasText: title }).waitFor();
  }
  try {
    await page.goto(baseURL + "/trips");
    await page.waitForURL("**/login");
    await page.getByRole("link", { name: "Create an account" }).click();
    await page.getByLabel("Username").fill(username);
    await page
      .getByLabel("Email", { exact: true })
      .fill(username + "@example.com");
    await page.getByLabel("Password", { exact: false }).fill(password);
    await page.getByRole("button", { name: "Create account" }).click();
    await page.waitForURL("**/trips");
    const memberResponse = await page.request.post(
      baseURL + "/api/auth/register",
      {
        data: {
          username: memberName,
          email: memberName + "@example.com",
          password,
        },
      },
    );
    assert.equal(memberResponse.status(), 201);
    const member = await memberResponse.json();

    await page.getByRole("button", { name: "Create a trip" }).click();
    await dialog()
      .getByLabel("Trip name")
      .fill("Browser adventure " + suffix);
    await dialog().getByLabel("Destination", { exact: true }).fill("Iceland");
    await dialog().getByLabel("Start date").fill("2027-07-12");
    await dialog().getByLabel("End date").fill("2027-07-18");
    await dialog()
      .getByLabel("A little about this trip")
      .fill("A complete browser verification journey.");
    await saved("Save trip");
    await page
      .getByRole("heading", { name: "Browser adventure " + suffix })
      .click();
    await page.getByText("Total shared expenses", { exact: true }).waitFor();
    const tripId = page.url().split("/").pop();
    assert.equal(
      (
        await page.request.get(baseURL + "/api/trips/" + tripId, {
          headers: { Authorization: "Bearer " + member.token },
        })
      ).status(),
      403,
    );
    await page.getByRole("button", { name: "Edit trip", exact: true }).click();
    await dialog()
      .getByLabel("Destination", { exact: true })
      .fill("South Iceland");
    await saved("Save trip");
    await page.getByText("South Iceland", { exact: true }).waitFor();
    await section("Members");
    await page.getByLabel("Add a registered traveler").fill(memberName);
    await page.getByRole("button", { name: "Add member" }).click();
    await page.locator(".member-row").filter({ hasText: memberName }).waitFor();

    await section("Itinerary");
    await page.getByRole("button", { name: "Add activity" }).click();
    await dialog().getByLabel("Activity name").fill("Cliff walk");
    await dialog().getByLabel("Location", { exact: true }).fill("Vik");
    await dialog()
      .getByLabel(/Estimated cost/)
      .fill("0");
    await saved("Save activity");
    let activity = page
      .locator(".activity-card")
      .filter({ hasText: "Cliff walk" });
    await activity.getByRole("button", { name: "View", exact: true }).click();
    await dialog().getByRole("heading", { name: "Cliff walk" }).waitFor();
    await dialog().getByRole("button", { name: "Close dialog" }).click();
    await activity.getByRole("button", { name: "Edit", exact: true }).click();
    await dialog().getByLabel("Activity name").fill("Cliff walk updated");
    await saved("Save activity");

    await section("Expenses");
    await page.getByRole("button", { name: "Add expense" }).click();
    await dialog()
      .getByLabel("Description", { exact: true })
      .fill("Shared dinner");
    await dialog()
      .getByLabel(/Amount/)
      .fill("10.01");
    await saved("Save expense");
    const expense = page
      .locator(".expense-row")
      .filter({ hasText: "Shared dinner" });
    await expense.getByRole("button", { name: "View", exact: true }).click();
    assert.match(await dialog().innerText(), /5\.01/);
    assert.match(await dialog().innerText(), /5\.00/);
    await dialog().getByRole("button", { name: "Close dialog" }).click();
    await expense.getByRole("button", { name: "Edit", exact: true }).click();
    await dialog()
      .getByLabel(/Amount/)
      .fill("12.00");
    await saved("Save expense");
    await page
      .locator(".transfer-row")
      .getByText("€6.00", { exact: true })
      .waitFor();
    await page
      .locator(".transfer-row")
      .getByRole("button", { name: "Record", exact: true })
      .click();
    await saved("Record payment");
    await page.getByRole("heading", { name: "All square" }).waitFor();
    await page
      .locator(".settlements")
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    await saved("Delete");
    await page
      .locator(".transfer-row")
      .getByText("€6.00", { exact: true })
      .waitFor();

    await section("Ideas");
    await newIdea("Museum stop");
    let idea = page.locator(".idea-card").filter({ hasText: "Museum stop" });
    await idea.getByRole("button", { name: /Up$/ }).click();
    await idea.locator('button[aria-pressed="true"]').waitFor();
    await idea.getByRole("button", { name: /Down$/ }).click();
    await page.waitForFunction(() =>
      document
        .querySelector('.vote-bar button[aria-pressed="true"]')
        ?.textContent.includes("Down"),
    );
    await idea.getByRole("button", { name: "Edit", exact: true }).click();
    await dialog().getByLabel("What’s the idea?").fill("Museum visit");
    await saved("Save idea");
    idea = page.locator(".idea-card").filter({ hasText: "Museum visit" });
    await idea.getByRole("button", { name: "Accept", exact: true }).click();
    await saved("Accept idea");
    await idea.getByRole("button", { name: /Add to itinerary/ }).click();
    await saved("Add to itinerary");
    await idea.getByText("ON THE ITINERARY", { exact: true }).waitFor();
    await newIdea("A second option");
    await page
      .locator(".idea-card")
      .filter({ hasText: "A second option" })
      .getByRole("button", { name: "Reject", exact: true })
      .click();
    await saved("Reject idea");
    await newIdea("Temporary idea");
    await page
      .locator(".idea-card")
      .filter({ hasText: "Temporary idea" })
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    await saved("Delete");

    await page.setViewportSize({ width: 390, height: 844 });
    for (const name of [
      "Overview",
      "Itinerary",
      "Expenses",
      "Ideas",
      "Members",
    ]) {
      await section(name);
      await page
        .locator(".section-heading, .overview-welcome")
        .first()
        .waitFor();
      assert.equal(
        await page.evaluate(
          () => document.documentElement.scrollWidth > innerWidth,
        ),
        false,
        name + " mobile overflow",
      );
    }
    await page.screenshot({
      path: path.join(artifacts, "members-mobile.png"),
      fullPage: true,
    });
    await page.setViewportSize({ width: 1440, height: 1000 });
    await page
      .locator(".member-row")
      .filter({ hasText: memberName })
      .getByRole("button", { name: "Remove" })
      .click();
    await saved("Delete");
    await page
      .getByRole("alert")
      .filter({ hasText: "financial records" })
      .waitFor();
    await section("Expenses");
    await page
      .locator(".expense-row")
      .filter({ hasText: "Shared dinner" })
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    await saved("Delete");
    await page
      .getByRole("heading", { name: "Nothing to split just yet." })
      .waitFor();
    await section("Members");
    await page
      .locator(".member-row")
      .filter({ hasText: memberName })
      .getByRole("button", { name: "Remove" })
      .click();
    await saved("Delete");
    await page
      .locator(".member-row")
      .filter({ hasText: memberName })
      .waitFor({ state: "detached" });
    assert.equal(
      (
        await page.request.get(
          baseURL + "/api/trips/" + tripId + "/dashboard",
          {
            headers: { Authorization: "Bearer " + member.token },
          },
        )
      ).status(),
      403,
    );
    await section("Itinerary");
    await page
      .locator(".activity-card")
      .filter({ hasText: "Cliff walk updated" })
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    await saved("Delete");
    await page
      .locator(".activity-card")
      .filter({ hasText: "Museum visit" })
      .waitFor();
    await section("Overview");
    await page.screenshot({
      path: path.join(artifacts, "overview-desktop.png"),
      fullPage: true,
    });
    await page
      .locator(".trip-hero")
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    await saved("Delete");
    await page.waitForURL("**/trips");
    await page.getByRole("button", { name: "Sign out" }).click();
    await page.waitForURL("**/login");
    await page.waitForFunction(() =>
      document.querySelector("form")?.classList.contains("ng-pristine"),
    );
    await page
      .getByLabel("Email", { exact: true })
      .fill(username + "@example.com");
    await page.getByLabel("Password", { exact: true }).fill(password);
    if (process.env.DEBUG_E2E)
      console.log(
        "Login input lengths:",
        await page
          .locator("input")
          .evaluateAll((inputs) =>
            inputs.map((i) => ({
              name: i.name,
              length: i.value.length,
              valid: i.validity.valid,
              control: i.getAttribute("formcontrolname"),
            })),
          ),
      );
    await page.getByRole("button", { name: "Sign in" }).click();
    await page.waitForURL("**/trips");
    assert.deepEqual(browserErrors, []);
    console.log(
      "PASS: registration/login, route protection, trip/activity/expense/idea CRUD, membership, voting, conversion, settlements, desktop/mobile layouts and account preservation.",
    );
  } catch (error) {
    await page.screenshot({
      path: path.join(artifacts, "failure.png"),
      fullPage: true,
    });
    console.error(
      "Visible errors:",
      await page.locator(".error").allTextContents(),
    );
    throw error;
  } finally {
    await browser.close();
  }
})().catch((e) => {
  console.error(e);
  process.exitCode = 1;
});
