document.addEventListener('DOMContentLoaded', function () {
  // Find the username element populated by thymeleaf security
  var usernameEl = document.getElementById('username');
  var initialEl = document.getElementById('user-initial');

  if (!usernameEl || !initialEl) return;
  var name = usernameEl.textContent || usernameEl.innerText || '';
  name = name.trim();
  if (name.length === 0) return;
  var initial = name.charAt(0).toUpperCase();
  initialEl.textContent = initial;
});
