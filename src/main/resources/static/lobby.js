const token = localStorage.getItem('pokerKittensToken');
if (!token) window.location.replace('/');

const playerName = localStorage.getItem('pokerKittensUsername') || 'Игрок';
const cards = document.querySelectorAll('.table-card');
const tableName = document.querySelector('#table-name');
const tableStakes = document.querySelector('#table-stakes');
const tablePlayers = document.querySelector('#table-players');
const tableBuyin = document.querySelector('#table-buyin');
const joinButton = document.querySelector('#join-table');
const seatMessage = document.querySelector('#seat-message');
document.querySelector('#player-name').textContent = playerName;

function selectTable(card) {
  cards.forEach((item) => item.classList.toggle('selected', item === card));
  tableName.textContent = card.dataset.table;
  tableStakes.textContent = card.dataset.stakes;
  tablePlayers.textContent = card.dataset.players;
  tableBuyin.textContent = card.dataset.buyin;
  const closed = card.dataset.closed === 'true';
  joinButton.disabled = closed;
  joinButton.textContent = closed ? 'Нет свободных мест' : 'Занять место';
  seatMessage.textContent = '';
}

cards.forEach((card) => {
  card.tabIndex = 0;
  card.addEventListener('click', () => selectTable(card));
  card.addEventListener('keydown', (event) => {
    if (event.key === 'Enter' || event.key === ' ') { event.preventDefault(); selectTable(card); }
  });
});

joinButton.addEventListener('click', () => { seatMessage.textContent = `Место за столом ${tableName.textContent} выбрано.`; });
document.querySelector('#logout').addEventListener('click', () => {
  localStorage.removeItem('pokerKittensToken');
  localStorage.removeItem('pokerKittensTokenType');
  localStorage.removeItem('pokerKittensUsername');
  window.location.replace('/');
});
