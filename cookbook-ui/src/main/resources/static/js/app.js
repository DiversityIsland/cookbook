const API_BASE_URL = '/api/v1';

// Загрузка блюд при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    loadDishes();
});

// Загрузка всех блюд
async function loadDishes() {
    try {
        const response = await fetch(`${API_BASE_URL}/dishes`);
        if (!response.ok) throw new Error('Ошибка загрузки блюд');
        
        const dishes = await response.json();
        displayDishes(dishes);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('dishes-grid').innerHTML = 
            '<div class="empty-state">Ошибка загрузки блюд</div>';
    }
}

// Отображение блюд
function displayDishes(dishes) {
    const container = document.getElementById('dishes-grid');
    
    if (dishes.length === 0) {
        container.innerHTML = '<div class="empty-state">Блюд пока нет. Создайте первое блюдо!</div>';
        return;
    }
    
    container.innerHTML = dishes.map(dish => {
        const imageHtml = dish.imageId 
            ? `<img src="${API_BASE_URL}/images/${dish.imageId}/data" alt="${escapeHtml(dish.name)}" class="dish-card-img">`
            : `<div class="dish-card-placeholder">🍽️</div>`;
        
        return `
            <div class="dish-card" onclick="openDish('${dish.id}')">
                ${imageHtml}
                <div class="dish-card-content">
                    <h3>${escapeHtml(dish.name)}</h3>
                </div>
            </div>
        `;
    }).join('');
}

// Открыть страницу блюда
function openDish(id) {
    window.location.href = `/dish.html?id=${id}`;
}

// Показать модальное окно создания блюда
function showCreateDishModal() {
    document.getElementById('createDishModal').classList.add('show');
}

// Закрыть модальное окно создания блюда
function closeCreateDishModal() {
    document.getElementById('createDishModal').classList.remove('show');
    document.getElementById('createDishForm').reset();
}

// Обработка создания блюда
async function handleCreateDish(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const name = formData.get('name').trim();
    const description = formData.get('description').trim();
    
    if (!name) {
        alert('Название блюда не может быть пустым');
        return;
    }
    
    if (!description) {
        alert('Описание не может быть пустым');
        return;
    }
    
    const data = {
        name: name,
        description: description
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/dishes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) throw new Error('Ошибка создания блюда');
        
        const dishId = await response.text();
        closeCreateDishModal();
        
        // Перезагрузить список блюд
        await loadDishes();
        
        // Перейти на страницу нового блюда
        window.location.href = `/dish.html?id=${dishId.replace(/"/g, '')}`;
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось создать блюдо. Попробуйте еще раз.');
    }
}

// Вспомогательная функция для экранирования HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const modal = document.getElementById('createDishModal');
    if (event.target === modal) {
        closeCreateDishModal();
    }
}
