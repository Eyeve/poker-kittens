const form = document.querySelector('#auth-form');
const modeButtons = document.querySelectorAll('.mode-button');
const title = document.querySelector('#auth-title');
const subtitle = document.querySelector('#auth-subtitle');
const password = document.querySelector('#password');
const passwordHint = document.querySelector('#password-hint');
const passwordToggle = document.querySelector('#password-toggle');
const message = document.querySelector('#form-message');
const submitButton = document.querySelector('#submit-button');

let mode = 'login';

function setMode(nextMode) {
  mode = nextMode;
  const isLogin = mode === 'login';
  title.textContent = isLogin ? 'Войти в игру' : 'Создать аккаунт';
  subtitle.textContent = isLogin ? 'Введите данные своего аккаунта.' : 'Новый игрок? Займите свое место за столом.';
  submitButton.textContent = isLogin ? 'Войти' : 'Зарегистрироваться';
  password.autocomplete = isLogin ? 'current-password' : 'new-password';
  passwordHint.textContent = isLogin ? '' : 'Минимум 8 символов.';
  message.textContent = '';
  message.className = 'form-message';
  modeButtons.forEach((button) => {
    const active = button.dataset.mode === mode;
    button.classList.toggle('active', active);
    button.setAttribute('aria-selected', String(active));
  });
}

modeButtons.forEach((button) => button.addEventListener('click', () => setMode(button.dataset.mode)));

passwordToggle.addEventListener('click', () => {
  const visible = password.type === 'text';
  password.type = visible ? 'password' : 'text';
  passwordToggle.textContent = visible ? 'Показать' : 'Скрыть';
  passwordToggle.setAttribute('aria-label', visible ? 'Показать пароль' : 'Скрыть пароль');
});

function errorMessage(payload) {
  if (payload?.fields?.password) return 'Пароль должен содержать от 8 до 72 символов.';
  if (payload?.error === 'User already exists') return 'Это имя игрока уже занято.';
  if (payload?.status === 401) return 'Неверное имя игрока или пароль.';
  return payload?.message || 'Не удалось выполнить запрос. Попробуйте еще раз.';
}

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  const username = form.username.value.trim();
  const passwordValue = password.value;
  message.textContent = '';
  message.className = 'form-message';

  if (!username || !passwordValue) {
    message.textContent = 'Заполните имя игрока и пароль.';
    return;
  }
  if (mode === 'register' && passwordValue.length < 8) {
    message.textContent = 'Пароль должен содержать минимум 8 символов.';
    return;
  }

  submitButton.disabled = true;
  submitButton.textContent = 'Проверяем...';
  try {
    const response = await fetch(`/api/auth/${mode}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password: passwordValue })
    });
    const payload = await response.json().catch(() => ({}));
    if (!response.ok) throw payload;

    localStorage.setItem('pokerKittensToken', payload.accessToken);
    localStorage.setItem('pokerKittensTokenType', payload.tokenType || 'Bearer');
    localStorage.setItem('pokerKittensUsername', username);
    window.location.assign('/lobby.html');
  } catch (error) {
    message.textContent = errorMessage(error);
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = mode === 'login' ? 'Войти' : 'Зарегистрироваться';
  }
});
