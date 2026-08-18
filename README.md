# MiniBank — Object Oriented Programming (CEUE203) Lab Project

Across this semester you will build **MiniBank**, a console banking application, one lab at a time. This repository is your workspace. It runs in **GitHub Codespaces**, so you get the same Java environment from any computer — no installation needed.

The environment is pinned to **JDK 17** (see `.devcontainer/devcontainer.json`), so modern features like `record` and `switch` expressions work for everyone.

---

## First-time setup

1. **Create a GitHub account** if you don't have one. (Optional but recommended: claim the free Student Developer Pack at `education.github.com/pack` to raise your Codespaces hours.)

2. **Make your own copy of this project.** On the template repository, click **Use this template → Create a new repository**. Name it **`oop-minibank-<your-rollno>`** and set it to **Private**. Create it.

3. **Add your instructor as a collaborator.** In *your* new repo: **Settings → Collaborators → Add people →** type **`<siddharthshah-cse>`** and send the invite. (Your instructor will accept it.)

4. **Open a Codespace.** Click the green **Code** button → **Codespaces** tab → **Create codespace on main**. Wait for it to build — it opens VS Code in your browser with Java ready.

5. **Check it works.** In the terminal at the bottom, run:
   ```bash
   java src/Welcome.java
   ```
   You should see "MiniBank environment is ready." Then delete `Welcome.java` and start Lab 1.

---

## Submitting each lab

Work on your code, then at the end of every lab **commit and tag** it so your instructor can find that exact version:

```bash
git add .
git commit -m "Lab 1: menu shell"
git tag lab-01
git push origin main --tags
```

Use `lab-02`, `lab-03`, … for later labs. Commit as you finish each class, not all at the end.

---

## Saving your free hours

- Your Codespace **stops automatically after 30 minutes** of inactivity, but it's good to stop it yourself when you finish (top-left menu → **Stop Current Codespace**).
- A **stopped codespace still uses storage**. After a lab is graded, **delete** old codespaces at `github.com/codespaces` to stay within your free 15 GB.
- Always **commit and push before closing** — an unused codespace can be cleaned up automatically.

---

## Folder structure

```
.
├── .devcontainer/
│   └── devcontainer.json   # pins JDK 17 + Java tools (do not edit)
├── .gitignore              # ignores compiled files and runtime data
└── src/                    # put all your .java files here
```

From Lab 6 onward you will organise `src/` into packages (`com/minibank/...`).
