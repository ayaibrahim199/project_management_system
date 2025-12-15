React + Tailwind frontend (minimal scaffold)

Overview
- This folder contains a lightweight Vite + React + Tailwind scaffold for the Teams UI.

Quick start (dev)
1. From `/frontend` run:

```bash
npm install
npm run dev
```

2. Open the dev server at the URL printed by Vite (usually http://localhost:5173).

Build for production
1. From `/frontend` run:

```bash
npm run build
```

2. The `build` script copies the `dist` output into Spring Boot static resources at `src/main/resources/static/frontend` so you can browse the built app at `http://localhost:8080/frontend/` once your Spring Boot server is running and serving static files.

Integration notes
- The React app is independent from the Spring Boot server. For a full integration you should:
  - Create REST endpoints (e.g., `/api/teams`) returning JSON used by the React app.
  - Update React components to fetch data from those endpoints instead of sample data.
  - Remove or adapt server-side Thymeleaf templates once the React frontend fully replaces them.

Tailwind in templates
- `tailwind.config.cjs` includes `../src/main/resources/templates/**/*.html` in its `content` globs so you can adopt Tailwind classes in Thymeleaf templates as well. After adding classes in templates, run `npm run build` to pick up used utilities for production.

Next steps
- Convert server-side data endpoints and wire React to fetch live data.
- Migrate one page at a time (Teams -> Projects -> Tasks) and progressively remove duplicate styling in `static/css`.

Troubleshooting / install tips
- If `npm install` fails with peer dependency errors, use the `bootstrap` script that runs install with legacy peer deps:

```bash
cd frontend
npm run bootstrap
```

- If `npm run dev` reports `vite: command not found`, the `dev` script now uses `npx vite` so you can run:

```bash
cd frontend
npm run dev
```

- If you still have problems, try cleaning the npm cache and reinstalling:

```bash
rm -rf node_modules package-lock.json
npm cache clean --force
npm run bootstrap
```

- Alternative: install `pnpm` and use it instead (pnpm often resolves peer deps more reliably):

```bash
# install pnpm globally once
npm i -g pnpm
pnpm install
pnpm run dev
```

If you want, I can update `package.json` to pin exact versions or prepare a `pnpm-lock.yaml` to reduce install issues.
