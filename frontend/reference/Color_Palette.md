# Color Palette - Extracted from HTML UI

## Material Design Color Tokens (From HTML)

### Primary Colors
```xml
<!-- colors.xml -->
<color name="primary">#bec6e0</color>
<color name="primary_container">#0f172a</color>
<color name="on_primary">#283044</color>
<color name="on_primary_container">#798098</color>
<color name="primary_fixed">#dae2fd</color>
<color name="primary_fixed_dim">#bec6e0</color>
<color name="on_primary_fixed">#131b2e</color>
<color name="on_primary_fixed_variant">#3f465c</color>
```

### Secondary Colors
```xml
<color name="secondary">#bcc7de</color>
<color name="secondary_container">#3e495d</color>
<color name="on_secondary">#263143</color>
<color name="on_secondary_container">#aeb9d0</color>
<color name="secondary_fixed">#d8e3fb</color>
<color name="secondary_fixed_dim">#bcc7de</color>
<color name="on_secondary_fixed">#111c2d</color>
<color name="on_secondary_fixed_variant">#3c475a</color>
```

### Tertiary Colors (Green Accent)
```xml
<color name="tertiary">#4edea3</color>
<color name="tertiary_container">#001c10</color>
<color name="on_tertiary">#003824</color>
<color name="on_tertiary_container">#009365</color>
<color name="tertiary_fixed">#6ffbbe</color>
<color name="tertiary_fixed_dim">#4edea3</color>
<color name="on_tertiary_fixed">#002113</color>
<color name="on_tertiary_fixed_variant">#005236</color>
```

### Error Colors
```xml
<color name="error">#ffb4ab</color>
<color name="error_container">#93000a</color>
<color name="on_error">#690005</color>
<color name="on_error_container">#ffdad6</color>
```

### Surface Colors
```xml
<color name="surface">#051424</color>
<color name="surface_dim">#051424</color>
<color name="surface_bright">#2c3a4c</color>
<color name="surface_container">#122131</color>
<color name="surface_container_low">#0d1c2d</color>
<color name="surface_container_high">#1c2b3c</color>
<color name="surface_container_highest">#273647</color>
<color name="surface_container_lowest">#010f1f</color>
<color name="surface_variant">#273647</color>
<color name="on_surface">#d4e4fa</color>
<color name="on_surface_variant">#c6c6cd</color>
<color name="surface_tint">#bec6e0</color>
```

### Background Colors
```xml
<color name="background">#051424</color>
<color name="on_background">#d4e4fa</color>
<color name="inverse_surface">#d4e4fa</color>
<color name="inverse_on_surface">#233143</color>
<color name="inverse_primary">#565e74</color>
```

### Outline Colors
```xml
<color name="outline">#909097</color>
<color name="outline_variant">#45464d</color>
```

## Simplified Color Palette for Android

### Core Colors (Recommended for Android)
```xml
<!-- Primary App Colors -->
<color name="color_primary">#4edea3</color>        <!-- Tertiary green -->
<color name="color_primary_variant">#009365</color> <!-- Darker green -->
<color name="color_secondary">#bcc7de</color>       <!-- Light blue-gray -->

<!-- Background Colors -->
<color name="color_background">#051424</color>      <!-- Dark blue background -->
<color name="color_surface">#122131</color>         <!-- Card/surface background -->
<color name="color_surface_variant">#1c2b3c</color> <!-- Elevated surfaces -->

<!-- Text Colors -->
<color name="color_on_surface">#d4e4fa</color>      <!-- Primary text (light) -->
<color name="color_on_surface_variant">#c6c6cd</color> <!-- Secondary text -->
<color name="color_on_primary">#002113</color>      <!-- Text on green elements -->

<!-- Semantic Colors -->
<color name="color_income">#4edea3</color>          <!-- Green for income -->
<color name="color_expense">#ffb4ab</color>         <!-- Red for expenses -->
<color name="color_neutral">#bcc7de</color>         <!-- Neutral amounts -->

<!-- Border and Dividers -->
<color name="color_outline">#45464d</color>         <!-- Borders -->
<color name="color_divider">#273647</color>         <!-- Dividers -->
```

## Usage Guidelines

### Financial Data Display
- **Income amounts**: Use `color_income` (#4edea3)
- **Expense amounts**: Use `color_expense` (#ffb4ab)  
- **Balance/Total**: Use `color_on_surface` (#d4e4fa)
- **Secondary info**: Use `color_on_surface_variant` (#c6c6cd)

### UI Components
- **FAB**: `color_primary` background with `color_on_primary` icon
- **Cards**: `color_surface` background with `color_outline` borders
- **Navigation**: `color_surface_variant` background
- **Active states**: `color_primary` with appropriate opacity

### Dark Theme Implementation
This color system is designed for dark theme. For consistency with the HTML UI, implement as the default and primary theme.

```xml
<!-- In themes.xml (night) -->
<style name="Theme.SachsTrack" parent="Theme.Material3.DayNight">
    <item name="colorPrimary">@color/color_primary</item>
    <item name="colorPrimaryVariant">@color/color_primary_variant</item>
    <item name="colorSecondary">@color/color_secondary</item>
    <item name="android:colorBackground">@color/color_background</item>
    <item name="colorSurface">@color/color_surface</item>
    <item name="colorOnSurface">@color/color_on_surface</item>
    <item name="colorOnPrimary">@color/color_on_primary</item>
</style>
```