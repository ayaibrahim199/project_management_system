document.addEventListener('DOMContentLoaded', function () {
  const toggle = document.querySelector('.nav-toggle');
  const container = document.querySelector('.nav-container');

  if (!toggle || !container) return;

  toggle.addEventListener('click', function () {
    const expanded = this.getAttribute('aria-expanded') === 'true';
    this.setAttribute('aria-expanded', String(!expanded));
    container.classList.toggle('nav-open');
  });

  // Close menu when clicking outside
  document.addEventListener('click', function (e) {
    if (!container.contains(e.target) && container.classList.contains('nav-open')) {
      container.classList.remove('nav-open');
      if (toggle) toggle.setAttribute('aria-expanded', 'false');
    }
  });
});
