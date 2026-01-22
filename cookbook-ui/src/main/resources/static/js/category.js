const API_BASE_URL = '/api/v1';
let currentCategoryId = null;

// Загрузка данных при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    currentCategoryId = urlParams.get('id');
    
    if (!currentCategoryId) {
        window.location.href = '/categories.html';
        return;
    }
    
    loadCategoryDetails();
    loadCategoryDishes();
});

// Загрузка деталей категории
async function loadCategoryDetails() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories/${currentCategoryId}`);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Категория не найдена');
            }
            throw new Error('Ошибка загрузки категории');
        }
        
        const category = await response.json();
        displayCategoryDetails(category);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('category-details').innerHTML = 
            '<div class="alert alert-error">Ошибка загрузки категории. <a href="/categories.html">Вернуться к списку</a></div>';
    }
}

// Отображение деталей категории
function displayCategoryDetails(category) {
    const container = document.getElementById('category-details');
    
    container.innerHTML = `
        <div class="dish-header">
            <div class="dish-info">
                <h2>${escapeHtml(category.name)}</h2>
                ${category.description ? `<div class="description">${escapeHtml(category.description)}</div>` : ''}
                <div class="dish-meta">
                    <div class="meta-item">🍽️ Блюд: ${category.dishesCount}</div>
                    <div class="meta-item">📅 Создано: ${formatDate(category.createdAt)}</div>
                </div>
                <div class="dish-actions">
                    <button class="btn btn-danger btn-small" onclick="deleteCategory()">Удалить категорию</button>
                </div>
            </div>
        </div>
    `;
}

// Загрузка блюд категории
async function loadCategoryDishes() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories/${currentCategoryId}/dishes`);
        if (!response.ok) throw new Error('Ошибка загрузки блюд');
        
        const dishes = await response.json();
        displayDishes(dishes);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('dishes-grid').innerHTML = 
            '<div class="empty-state">Ошибка загрузки блюд категории</div>';
    }
}

// Отображение блюд
function displayDishes(dishes) {
    const container = document.getElementById('dishes-grid');
    
    if (dishes.length === 0) {
        container.innerHTML = '<div class="empty-state">В этой категории пока нет блюд</div>';
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

// Удаление категории
async function deleteCategory() {
    if (!confirm('Вы уверены, что хотите удалить эту категорию?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/categories/${currentCategoryId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Ошибка удаления категории');
        
        // Вернуться на страницу категорий
        window.location.href = '/categories.html';
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось удалить категорию. Попробуйте еще раз.');
    }
}

// Форматирование даты
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Вспомогательная функция для экранирования HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Показать модальное окно добавления блюда
function showAddDishModal() {
    document.getElementById('addDishModal').classList.add('show');
    loadAvailableDishes();
}

// Закрыть модальное окно добавления блюда
function closeAddDishModal() {
    document.getElementById('addDishModal').classList.remove('show');
}

// Загрузка доступных блюд (которых еще нет в категории)
async function loadAvailableDishes() {
    try {
        const [allDishesResponse, categoryDishesResponse] = await Promise.all([
            fetch(`${API_BASE_URL}/dishes`),
            fetch(`${API_BASE_URL}/categories/${currentCategoryId}/dishes`)
        ]);
        
        if (!allDishesResponse.ok || !categoryDishesResponse.ok) {
            throw new Error('Ошибка загрузки блюд');
        }
        
        const allDishes = await allDishesResponse.json();
        const categoryDishes = await categoryDishesResponse.json();
        
        // Фильтруем блюда, которых еще нет в категории
        const categoryDishIds = new Set(categoryDishes.map(d => d.id));
        const availableDishes = allDishes.filter(d => !categoryDishIds.has(d.id));
        
        displayAvailableDishes(availableDishes);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('available-dishes').innerHTML = 
            '<div class="empty-state">Ошибка загрузки блюд</div>';
    }
}

// Отображение доступных блюд
function displayAvailableDishes(dishes) {
    const container = document.getElementById('available-dishes');
    
    if (dishes.length === 0) {
        container.innerHTML = '<div class="empty-state">Все блюда уже добавлены в эту категорию</div>';
        return;
    }
    
    container.innerHTML = dishes.map(dish => {
        const imageHtml = dish.imageId 
            ? `<img src="${API_BASE_URL}/images/${dish.imageId}/data" alt="${escapeHtml(dish.name)}" class="available-dish-icon-img">`
            : `<div class="available-dish-icon">🍽️</div>`;
        
        return `
            <div class="available-dish-item">
                ${imageHtml}
                <div class="available-dish-name">${escapeHtml(dish.name)}</div>
                <button class="btn btn-primary btn-small" onclick="addDishToCategory('${dish.id}')">
                    Добавить
                </button>
            </div>
        `;
    }).join('');
}

// Добавить блюдо в категорию
async function addDishToCategory(dishId) {
    try {
        const response = await fetch(`${API_BASE_URL}/categories/${currentCategoryId}/dishes/${dishId}`, {
            method: 'POST'
        });
        
        if (!response.ok) throw new Error('Ошибка добавления блюда');
        
        // Обновить список блюд категории
        await loadCategoryDishes();
        await loadCategoryDetails();
        
        // Обновить список доступных блюд в модальном окне
        await loadAvailableDishes();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось добавить блюдо в категорию. Попробуйте еще раз.');
    }
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const addDishModal = document.getElementById('addDishModal');
    if (event.target === addDishModal) {
        closeAddDishModal();
    }
}
