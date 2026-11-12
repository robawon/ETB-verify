---
name: EthioShield System
colors:
  surface: '#f8f9ff'
  surface-dim: '#d0dbed'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e6eeff'
  surface-container-high: '#dee9fc'
  surface-container-highest: '#d9e3f6'
  on-surface: '#121c2a'
  on-surface-variant: '#3e4a3d'
  inverse-surface: '#27313f'
  inverse-on-surface: '#eaf1ff'
  outline: '#6e7a6b'
  outline-variant: '#becab9'
  surface-tint: '#006e24'
  primary: '#006d23'
  on-primary: '#ffffff'
  primary-container: '#078930'
  on-primary-container: '#ffffff'
  inverse-primary: '#6fdd79'
  secondary: '#6c5e00'
  on-secondary: '#ffffff'
  secondary-container: '#fedf10'
  on-secondary-container: '#716200'
  tertiary: '#be0010'
  on-tertiary: '#ffffff'
  tertiary-container: '#e72022'
  on-tertiary-container: '#ffffff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#8bfb92'
  primary-fixed-dim: '#6fdd79'
  on-primary-fixed: '#002106'
  on-primary-fixed-variant: '#005319'
  secondary-fixed: '#ffe33f'
  secondary-fixed-dim: '#e2c600'
  on-secondary-fixed: '#211b00'
  on-secondary-fixed-variant: '#524700'
  tertiary-fixed: '#ffdad6'
  tertiary-fixed-dim: '#ffb4ab'
  on-tertiary-fixed: '#410002'
  on-tertiary-fixed-variant: '#93000a'
  background: '#f8f9ff'
  on-background: '#121c2a'
  surface-variant: '#d9e3f6'
typography:
  display-lg:
    fontFamily: Roboto Flex
    fontSize: 57px
    fontWeight: '700'
    lineHeight: 64px
    letterSpacing: -0.25px
  display-md:
    fontFamily: Roboto Flex
    fontSize: 45px
    fontWeight: '600'
    lineHeight: 52px
  headline-lg:
    fontFamily: Roboto Flex
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Roboto Flex
    fontSize: 26px
    fontWeight: '600'
    lineHeight: 32px
  headline-md:
    fontFamily: Roboto Flex
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 30px
  title-lg:
    fontFamily: Roboto Flex
    fontSize: 20px
    fontWeight: '500'
    lineHeight: 26px
  title-md:
    fontFamily: Roboto Flex
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 22px
  body-lg:
    fontFamily: Roboto Flex
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Roboto Flex
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Roboto Flex
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-md:
    fontFamily: Roboto Flex
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
  label-sm:
    fontFamily: Roboto Flex
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.5px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-sm: 0.5rem
  margin: 1rem
  margin-tablet: 1.5rem
  margin-desktop: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
---

## Brand & Style

This design system serves as the institutional foundation for national currency verification, fraud intelligence, and forensic banknote inspection. It projects authority, absolute cryptographic trust, and clarity under high-pressure verification environments (such as retail cash counters, banking tellers, and border checkpoints).

### Design Movement
The visual philosophy implements a strict **Corporate / Modern Government Utility** language grounded in Material Design 3 and Google Security guidelines. It emphasizes high-legibility state communication, federal reliability, structured modular surface containers, and strict visual hierarchy.

### Core Values & Emotional Resonance
- **Sovereign Trust:** Authoritative, clear visual architecture without ornamental distractions.
- **Immediate Legibility:** Unambiguous status indicators (Authentic, Suspect, Counterfeit) identifiable instantly in diverse lighting.
- **Accessible Utility:** High-contrast touch targets, standard federal layouts, and clean typographic rhythms built for high-throughput mobile interactions.

## Colors

The palette balances sovereign national identity with standard digital security semantics. 

- **Primary (`#078930`)**: Represents verified authenticity, sovereign institutional presence, and confirmed integrity. Used for primary security badges, active validation states, and primary CTAs.
- **Secondary (`#FCDD09`)**: Functions as the caution and audit-flag color. Applied to intermediate alerts, manual inspection requests, and currency wear-and-tear warnings.
- **Tertiary (`#DA121A`)**: Dedicated strictly to fraud warnings, counterfeit alerts, critical integrity failures, and security revocations.
- **Neutral Dark (`#1F2937` / `#111827`)**: The grounding foundation for primary text, deep system structural elements, and high-contrast tactile framing.

### Surface Tonal Hierarchy (MD3 Model)
- **Surface (`#F9FAFB`)**: Base canvas color providing maximum contrast for text and camera viewports.
- **Surface Container Lowest (`#FFFFFF`)**: Pure white cards and elevated verification sheets.
- **Surface Container Low (`#F3F4F6`)**: Secondary panels, contextual scanner view backgrounds, and input fields.
- **Surface Container High (`#E5E7EB`)**: Structural dividers, active hover/focus states, and unselected component chips.
- **On-Primary / On-Tertiary (`#FFFFFF`)**: Pure white text and glyphs over solid status containers to guarantee federal AA/AAA compliance.
- **On-Secondary (`#1F2937`)**: Deep neutral text on gold surfaces to preserve strict readability.

## Typography

Typographic execution relies entirely on `Roboto Flex` to align with the core Android ecosystem, federal terminal hardware, and Google Material Design 3 guidelines.

- **Legibility in Scanner HUDs**: Titles, serial numbers, and micro-security labels use medium and semi-bold weights (`500` and `600`) to remain sharp over optical scanner overlays and variable lighting conditions.
- **Numbers & Denominations**: Serial readouts, currency values (e.g., 50, 100, 200 ETB), and counterfeit confidence percentages utilize tabular figures with consistent baseline alignments for accurate inspection.

## Layout & Spacing

The layout is built upon an 8dp base grid, using an adaptive fluid grid model scaled for mobile-first handheld use cases.

- **Mobile (default, up to 600dp)**: 4-column layout with `1rem` (16px) outer margins and `1rem` gutters. Touch targets maintain a minimum dimension of 48×48dp.
- **Tablet (600dp to 840dp)**: 8-column layout with `1.5rem` (24px) margins. Used primarily for fixed retail POS integrations and audit consoles.
- **Desktop / Terminal (> 840dp)**: 12-column layout centered with a maximum content width of 1200px and `2rem` (32px) margins.

### Spacing Intent
- `space-xs` (4px) and `space-sm` (8px): Used strictly for badge internal padding, metadata cluster spacing, and icon-to-label offsets.
- `space-md` (16px): Default separation for stacked card containers, form fields, and scanner diagnostic panels.
- `space-lg` (24px) and `space-xl` (32px): Structural spacing delineating separate verification steps, verification history timelines, and institutional report sections.

## Elevation & Depth

Visual hierarchy adopts Material Design 3 tonal elevation combined with low-intensity structural ambient shadows. Surface containers elevate through tinting and subtle occlusion rather than heavy skeletal drop shadows.

- **Level 0 (Flat / Canvas)**: `Surface` background (`#F9FAFB`). Used for default screen canvases and base scanning layouts.
- **Level 1 (Surface Container Lowest)**: Used for resting cards and security module tiles. Border: 1px solid `#E5E7EB`. Shadow: `0px 1px 3px rgba(17, 24, 39, 0.08), 0px 1px 2px rgba(17, 24, 39, 0.04)`.
- **Level 2 (Active Verification & Action Trays)**: Bottom sheets, verification dialogs, and active toolbars. Shadow: `0px 4px 6px -1px rgba(17, 24, 39, 0.1), 0px 2px 4px -1px rgba(17, 24, 39, 0.06)`.
- **Level 3 (Modal Alerts & Overlays)**: Critical counterfeit interception alerts and full-screen camera overlays. Shadow: `0px 10px 15px -3px rgba(17, 24, 39, 0.12), 0px 4px 6px -2px rgba(17, 24, 39, 0.08)`.

## Shapes

The design uses standard **Rounded (Level 2)** shaping. This provides an approachable, highly disciplined look consistent with Android standard components.

- **Base Corner Radius (`rounded-md` / 0.5rem - 8px)**: Standard buttons, text input fields, status chips, and alert cards.
- **Large Corner Radius (`rounded-lg` / 1rem - 16px)**: Currency scan target frames, security module summary containers, and verification summary cards.
- **Extra Large Radius (`rounded-xl` / 1.5rem - 24px)**: Top-edge curvature on bottom sheets, persistent biometric HUDs, and sliding drawers.
- **Pill (`rounded-full`)**: Used exclusively for verification status badges (e.g., "AUTHENTICATED", "INVESTIGATION PENDING") and floating camera trigger buttons.

## Components

### Buttons
- **Filled (Primary Action)**: Solid primary color (`#078930`), On-Primary white text (`#FFFFFF`), 48px height, 8px corner radius (`rounded-md`). Used for "Verify Banknote", "Submit Incident Report", and "Confirm Denomination".
- **Tonal / Secondary Action**: Background `Surface Container Low` with text in neutral dark (`#1F2937`), 48px height. Used for secondary navigation, log history, and cancel actions.
- **Destructive Action**: Filled tertiary (`#DA121A`) button used when flagging active fraud or triggering an emergency counterfeit alert to authorities.

### Security Badges & Chips
- **Authentication Status Pill**: 
  - *Authentic*: Background `#E7F4EB`, border 1px solid `#078930`, text `#078930`, icon: shield-check.
  - *Suspicious*: Background `#FEFBE8`, border 1px solid `#FCDD09`, text `#854D0E`, icon: alert-triangle.
  - *Counterfeit*: Background `#FEE2E2`, border 1px solid `#DA121A`, text `#991B1B`, icon: shield-alert.
- **Filter Chips**: 32px height, 8px radius. Resting state uses `#F3F4F6` background with `#1F2937` label; selected state uses `#078930` background with white label.

### Cards & Container Tiles
- **Inspection Card**: Resting on `Surface Container Lowest`, framed by a 1px solid `#E5E7EB` border. Internal padding: `1rem` (16px).
- **Scanner Viewport Frame**: Overlaid with high-contrast corner reticles (`#FFFFFF` with `#1F2937` bounding stroke) to guide physical note alignment (watermark, security thread, micro-printing zones).

### Form Inputs & Checkboxes
- **Input Fields**: Outlined style with standard 56px height, 8px corner radius. Border color `#D1D5DB`, shifting to 2px solid `#078930` on active focus. Supporting text uses `label-md` in `#4B5563`.
- **Checkboxes & Radios**: Standard 20×20dp square/circular toggles. Checked state fills with `#078930` displaying a crisp white vector checkmark.

### Verification List Items
- Structured with left-aligned currency thumbnail or icon, central denomination and serial number metadata (`title-md` and `body-md`), and right-aligned timestamp and authentication badge. Separated by single-pixel horizontal dividers (`#E5E7EB`).