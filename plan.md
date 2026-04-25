# Plan: Replace the AngularJS User Interface

## Goal

Replace the current AngularJS 1.x user interface while preserving the current
operational model: one Spring Boot application built and deployed as a single
JAR file. The replacement must fit naturally with the existing Thymeleaf shell,
static resources in `src/main/resources/static`, and REST API controllers under
`/api`.

The current UI is a small hash-routed AngularJS app:

- `src/main/resources/templates/index.html` provides the main Thymeleaf shell.
- `src/main/resources/static/scripts/app.js` configures hash routes.
- `src/main/resources/static/views/*.html` contains route-level partials.
- AngularJS services call existing REST endpoints such as `/api/category`,
  `/api/package`, `/api/search`, `/api/os`, `/api/license`, and package rating
  endpoints.
- UI behavior is mostly list rendering, search, pagination, package browsing by
  category/license/OS/architecture, privacy content, and package rating.

## Constraints

- Deployment should remain a single Spring Boot JAR.
- The backend should remain Java 17 and Spring Boot.
- Existing `/api` endpoints should remain stable for external clients such as
  `mport-manager`.
- The UI should not require a separate Node/JavaScript runtime in production.
- Build-time Node use is acceptable only if Maven or CI owns it deterministically.
- Any option should avoid editing already-applied Flyway migrations.

## Options Summary

| Option | Single-JAR compatible | Build complexity | Runtime complexity | Best fit |
| --- | --- | --- | --- | --- |
| Thymeleaf-only server-rendered UI | Yes | Low | Low | Small public app, SEO, low JavaScript needs |
| React frontend bundled into static resources | Yes | Medium | Medium | Richer client-side UX with broad ecosystem |
| Modern Angular frontend bundled into static resources | Yes | High | Medium | Teams wanting Angular conventions and full framework structure |

## Option 1: Thymeleaf-Only Server-Rendered UI

This replaces AngularJS with regular Spring MVC controllers and Thymeleaf
templates. Pages are rendered on the server and link to normal URLs instead of
hash routes.

Possible route mapping:

- `/` renders the home page.
- `/category/{id}` renders packages in a category.
- `/license/{license}` renders packages for a license.
- `/os/{os}/arch/{arch}` renders packages for an OS/architecture pair.
- `/search?term=...&page=...&size=...` renders search results.
- `/privacy` renders the privacy page.
- `POST /package/{name}/rating` submits ratings from a regular form or small
  JavaScript enhancement.

The existing REST API can remain for external consumers. The UI can either call
services directly from MVC controllers or use the same service layer behind the
REST controllers.

### Pros

- Simplest way to preserve single-JAR deployment.
- No Node build chain is required.
- Removes AngularJS, Angular UI Bootstrap, and most browser-side framework
  dependencies.
- Server-rendered pages improve crawlability and direct-link behavior compared
  with hash routes.
- Easier to test with Spring MVC tests because page state is generated on the
  server.
- Fits the current app size. The existing UI has a small number of pages and
  limited interactivity.
- Lower long-term maintenance burden for a Java/Spring-focused project.

### Cons

- More page reloads unless progressive enhancement is added.
- Pagination, search, and rating interactions become less fluid than an SPA
  unless implemented with small JavaScript enhancements.
- Requires new MVC controller methods and Thymeleaf templates for each page.
- Some duplicated presentation mapping may be needed if REST DTOs are not ideal
  for rendered pages.
- Complex future client-side interactions would be harder than with React or
  Angular.

### Integration Approach

1. Add MVC page controllers separate from `/api` controllers.
2. Create a shared Thymeleaf layout for header, navigation, footer, analytics,
   and common assets.
3. Convert `static/views/main.html`, `category.html`, `license.html`, `os.html`,
   `search.html`, and `privacy.html` into Thymeleaf templates.
4. Replace hash links such as `#!/search` with normal links such as `/search`.
5. Replace Angular pagination with server-generated pagination links.
6. Implement rating as either a standard POST form or a small fetch-based
   progressive enhancement.
7. Remove AngularJS WebJar dependencies and obsolete static scripts after parity
   is reached.

## Option 2: React Bundled Into Spring Boot Static Resources

This replaces AngularJS with a React app built at compile time and copied into
`src/main/resources/static` or `target/classes/static`. Spring Boot still serves
the final assets from the JAR.

Typical layout:

- Add a frontend directory such as `src/main/frontend`.
- Use Vite or another React build tool to generate static assets.
- Configure Maven to install/use Node during the build and copy the built files
  into Spring Boot static resources.
- Keep Thymeleaf as a lightweight shell that loads the bundled JavaScript and
  CSS, or serve a static `index.html` for the app shell.
- Keep existing `/api` endpoints as the data layer.

### Pros

- Single-JAR deployment remains possible because built assets are packaged into
  the Spring Boot JAR.
- React is a good fit for gradually replacing individual AngularJS views.
- Component model is straightforward for package cards, search forms,
  pagination, rating stars, and shared layout pieces.
- Smaller framework surface than modern Angular.
- Strong ecosystem for UI testing, routing, state management, and component
  libraries.
- Can preserve a SPA-style user experience where that matters.

### Cons

- Adds a Node-based build pipeline unless built assets are committed, which is
  usually worse for maintainability.
- Requires CI and local developer environments to support deterministic frontend
  builds.
- Client-side routing needs Spring fallback routes so direct links work.
- More moving parts than the current Java/Maven-only workflow.
- SEO and first-render performance need attention if content is rendered only in
  the browser.
- The team must maintain Java and JavaScript dependency update processes.

### Integration Approach

1. Add `src/main/frontend` with React, routing, tests, and asset build scripts.
2. Add Maven frontend build integration so `mvn package` builds the frontend and
   places assets under `target/classes/static/app` or an equivalent location.
3. Keep a Thymeleaf shell for global metadata, analytics, and asset references.
4. Implement routes for home, category, license, OS/architecture, search, and
   privacy using the existing REST endpoints.
5. Add a Spring MVC fallback for React routes if using browser history routing.
   Alternatively, keep hash routing temporarily to reduce backend routing work.
6. Replace AngularJS templates and controllers route by route.
7. Remove AngularJS WebJars and legacy scripts after all routes are migrated.

## Option 3: Modern Angular Bundled Into Spring Boot Static Resources

This replaces AngularJS with current Angular, built at compile time and packaged
as static assets inside the Spring Boot JAR. This is not an in-place upgrade from
AngularJS; it is effectively a new frontend application.

Typical layout:

- Add an Angular workspace under `src/main/frontend` or `frontend`.
- Configure Maven to run the Angular build.
- Copy build output into Spring Boot static resources before packaging.
- Keep the Spring Boot REST API as the backend data source.
- Use Thymeleaf only as a shell if needed for metadata and deployment-specific
  values.

### Pros

- Single-JAR deployment is possible after the Angular build output is packaged
  into the JAR.
- Provides a complete opinionated framework: routing, forms, dependency
  injection, HTTP client, testing conventions, and build tooling.
- Strong fit if the project expects a larger frontend with many forms, complex
  state, or strict frontend architecture.
- Conceptually familiar to teams that already know Angular-style templates,
  services, and routing.

### Cons

- Highest migration cost of the three options.
- AngularJS templates/controllers cannot be reused directly; most UI code must
  be rewritten.
- Adds the largest frontend toolchain and dependency surface.
- Requires developers and CI to support the Angular CLI build process.
- More architecture than the current UI appears to need.
- Browser history routing still requires Spring fallback configuration.
- Long-term maintenance requires tracking both Spring Boot and Angular release
  cycles.

### Integration Approach

1. Add a new Angular workspace under a frontend directory.
2. Configure Maven to run the Angular production build and copy output into the
   JAR.
3. Implement Angular services for the existing `/api` endpoints.
4. Implement Angular components for home, category, license, OS/architecture,
   search, privacy, pagination, package cards, and rating.
5. Add Spring fallback routing for direct navigation to Angular-managed routes.
6. Remove AngularJS WebJars, `static/scripts`, and `static/views` after the new
   app reaches feature parity.

## Recommended Direction

Use Thymeleaf-only server-rendered pages as the default migration target.

Reasoning:

- The current UI is small and mostly renders server data.
- The project already uses Thymeleaf and Spring MVC.
- The deployment requirement favors minimal runtime and build complexity.
- Removing AngularJS without introducing a large JavaScript toolchain gives the
  largest maintenance reduction.
- Existing REST APIs can stay available for external clients while the web UI
  moves to MVC controllers.

If a richer client-side experience is required later, add targeted progressive
enhancement with small JavaScript modules rather than adopting a full SPA
framework immediately. React is the better SPA fallback if a component framework
becomes necessary. Modern Angular is possible, but it is the least pragmatic fit
for the current application size and migration goal.

## Proposed Migration Plan

### Phase 1: Inventory And Parity Definition

- Document every current route, API call, page state, and user interaction.
- Capture screenshots of the existing pages for visual parity.
- Decide whether URLs should change from hash routes to normal routes.
- Define compatibility redirects from old `#!/...` links where practical.
- Identify which frontend assets are still needed after AngularJS removal.

### Phase 2: Server-Rendered Shell

- Refactor `templates/index.html` into a reusable Thymeleaf layout or fragments.
- Move shared header, navigation, footer, analytics, and stylesheet references
  into common fragments.
- Add normal navigation links for home and search.
- Keep the old AngularJS app available during this phase to reduce migration
  risk.

### Phase 3: Page Controllers And Templates

- Add MVC controllers for home, category, license, OS/architecture, search, and
  privacy pages.
- Reuse existing service classes instead of duplicating business logic.
- Create Thymeleaf templates for each page.
- Add model objects tailored for rendering package cards, package instances,
  licenses, categories, ratings, and pagination.

### Phase 4: Search, Pagination, And Ratings

- Implement search as a GET form using query parameters.
- Implement pagination as regular links with `page` and `size` parameters.
- Implement package ratings as a POST endpoint.
- Add progressive enhancement for rating stars only if the non-JavaScript form
  behavior is too limited.

### Phase 5: Testing And Compatibility

- Add Spring MVC tests for each page controller.
- Keep REST controller tests intact to protect external API compatibility.
- Test direct links, old hash-route entry points, pagination, search, rating
  submission, and empty-result states.
- Verify behavior with local PostgreSQL, Elasticsearch, and Redis where needed.

### Phase 6: Remove AngularJS

- Remove AngularJS WebJar dependencies from `pom.xml`.
- Remove Angular UI Bootstrap dependencies.
- Delete obsolete files under `src/main/resources/static/scripts` and
  `src/main/resources/static/views`.
- Remove Angular-specific attributes from Thymeleaf templates.
- Re-run unit and integration tests and inspect reports because the Maven test
  plugins currently ignore test failures.

## Implementation Notes For A React Or Angular Choice

If React or modern Angular is chosen instead of Thymeleaf-only:

- Add the frontend source under `src/main/frontend`.
- Do not depend on a separate deployed frontend service.
- Configure Maven so `mvn package` produces a complete JAR containing compiled
  frontend assets.
- Keep generated frontend build output out of source control unless there is a
  deliberate release reason to commit it.
- Add a Spring route fallback for client-side routes.
- Keep existing `/api` endpoints stable.
- Add frontend tests to CI so Java and JavaScript failures are both visible.

## Open Decisions

- Whether old `#!/...` URLs need permanent redirect or compatibility handling.
- Whether package rating should work without JavaScript.
- Whether to keep Bootstrap styling or replace the visual system during the UI
  migration.
- Whether search should remain backed by Elasticsearch for all UI paths.
- Whether the privacy page should remain static or become a Thymeleaf template.
