---
name: Slate & Mint Finance
colors:
  surface: '#051424'
  surface-dim: '#051424'
  surface-bright: '#2c3a4c'
  surface-container-lowest: '#010f1f'
  surface-container-low: '#0d1c2d'
  surface-container: '#122131'
  surface-container-high: '#1c2b3c'
  surface-container-highest: '#273647'
  on-surface: '#d4e4fa'
  on-surface-variant: '#c6c6cd'
  inverse-surface: '#d4e4fa'
  inverse-on-surface: '#233143'
  outline: '#909097'
  outline-variant: '#45464d'
  surface-tint: '#bec6e0'
  primary: '#bec6e0'
  on-primary: '#283044'
  primary-container: '#0f172a'
  on-primary-container: '#798098'
  inverse-primary: '#565e74'
  secondary: '#bcc7de'
  on-secondary: '#263143'
  secondary-container: '#3e495d'
  on-secondary-container: '#aeb9d0'
  tertiary: '#4edea3'
  on-tertiary: '#003824'
  tertiary-container: '#001c10'
  on-tertiary-container: '#009365'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#dae2fd'
  primary-fixed-dim: '#bec6e0'
  on-primary-fixed: '#131b2e'
  on-primary-fixed-variant: '#3f465c'
  secondary-fixed: '#d8e3fb'
  secondary-fixed-dim: '#bcc7de'
  on-secondary-fixed: '#111c2d'
  on-secondary-fixed-variant: '#3c475a'
  tertiary-fixed: '#6ffbbe'
  tertiary-fixed-dim: '#4edea3'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005236'
  background: '#051424'
  on-background: '#d4e4fa'
  surface-variant: '#273647'
typography:
  display-lg:
    fontFamily: Geist
    fontSize: 48px
    fontWeight: '600'
    lineHeight: '1.1'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Geist
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Geist
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.2'
  headline-md:
    fontFamily: Geist
    fontSize: 20px
    fontWeight: '500'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Geist
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  body-sm:
    fontFamily: Geist
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
  label-caps:
    fontFamily: Geist
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.05em
  data-mono:
    fontFamily: Geist
    fontSize: 16px
    fontWeight: '500'
    lineHeight: '1'
    letterSpacing: -0.01em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 40px
  container-max: 1200px
  gutter: 20px
---

## Brand & Style
The design system is centered on **Utility-Focused Minimalism**. It targets professionals who require immediate clarity regarding their financial health without visual noise or "gamified" distractions. The emotional response is one of calm control and precision.

The style leverages a **Corporate Modern** aesthetic with a high-end, editorial finish. It avoids unnecessary decoration, relying instead on deliberate whitespace and a restrained color palette to guide the user's eye toward critical data points. The interface feels "smooth" through the use of subtle transitions and low-friction interactions.

## Colors
The palette is rooted in a deep "Slate and Charcoal" foundation to reduce eye strain and provide a premium, sophisticated backdrop. 

- **Primary Surfaces**: Use `background` for the main canvas and `surface` for cards and containers.
- **Accents**: Mint green (`income`) is reserved strictly for positive cash flow, growth indicators, and primary "Add" actions. Subtle coral (`expense`) is used for outflows and warnings.
- **Interactions**: Use `secondary_color_hex` for hover states and inactive tab backgrounds.
- **Readability**: Text should primarily use high-contrast off-white for body content and the neutral slate for metadata.

## Typography
This design system utilizes **Geist** for its technical precision and systematic feel. It mimics the clarity of a developer tool while maintaining the elegance of a premium financial suite.

- **Monospaced characteristics**: While Geist is a sans-serif, its tracking and spacing are optimized for numerical alignment. Use `data-mono` for all currency amounts to ensure digits align vertically in lists.
- **Hierarchy**: Use `label-caps` for section headers and table column titles to differentiate structural labels from user data.
- **Weight**: Avoid using "Bold" (700+) to maintain the "smooth" aesthetic; rely on Semi-Bold (600) for primary emphasis and Medium (500) for secondary.

## Layout & Spacing
The layout follows a **Fluid Grid** model with strict adherence to a 4px baseline shift. 

- **Desktop**: A 12-column grid with 24px gutters. Sidebars should be fixed at 280px to maximize the readability of central data tables.
- **Mobile**: Use a single-column flow with 16px side margins.
- **Rhythm**: Use `lg` (24px) spacing between distinct cards and `md` (16px) for internal padding within cards. This generous spacing prevents the data-heavy interface from feeling cramped.

## Elevation & Depth
The design system uses **Tonal Layering** supplemented by **Ambient Shadows** to create a focused hierarchy without visual clutter.

- **Level 0 (Background)**: The darkest slate. Used for the global canvas.
- **Level 1 (Cards/Surface)**: Raised via color (`surface` hex) and a very soft, diffused shadow (Offset: 0, 4px; Blur: 20px; Opacity: 0.2; Color: Black).
- **Level 2 (Modals/Popovers)**: Slightly lighter than the surface color with a thin 1px border using the `border` token. 
- **Interactions**: On hover, interactive elements should not "lift" (no shadow change); instead, they should subtly lighten their background color or increase border opacity.

## Shapes
The shape language is **Soft and Professional**. 

A `roundedness` level of `1` (0.25rem / 4px) is the standard for functional elements like input fields and buttons. For larger layout containers like dashboard cards, use `rounded-lg` (8px). This creates a structured, architectural feel that is more modern than sharp corners but more serious than highly rounded "pill" designs.

## Components
- **Buttons**: Primary buttons use the Mint Green accent with dark slate text. Secondary buttons use a ghost style (transparent background with a 1px `border` token).
- **Input Fields**: Backgrounds should be a shade darker than the card they sit on. Use a 1px border that illuminates to Mint Green on focus.
- **Data Rows**: Lists of transactions should have a subtle bottom border (`1px solid #1E293B`). Avoid alternating row colors; use whitespace and Geist's letter spacing for legibility.
- **Chips**: Use for categories (e.g., "Housing", "Food"). These should be low-contrast: dark slate background with neutral grey text.
- **Progress Bars**: Used for budgets. The "track" is the background color, and the "fill" is Mint Green. If a budget is exceeded, the fill transitions to Subtle Coral.
- **Charts**: Use thin stroke weights (1.5px or 2px) for line charts. Fill areas with a very low-opacity gradient (10% to 0%) of the stroke color.