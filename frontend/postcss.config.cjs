let plugins = {};

try {
  // Try to load Tailwind + Autoprefixer if present. If not installed,
  // fall back to an empty plugins object so Vite doesn't crash.
  // This allows using the Tailwind CDN during development without
  // requiring local PostCSS/Tailwind dependencies.
  // eslint-disable-next-line global-require
  const tailwindcss = require('tailwindcss');
  // eslint-disable-next-line global-require
  const autoprefixer = require('autoprefixer');
  plugins = {
    tailwindcss: tailwindcss(),
    autoprefixer: autoprefixer(),
  };
} catch (e) {
  // tailwindcss or autoprefixer not installed — continue with no plugins.
  plugins = {};
}

module.exports = { plugins };
