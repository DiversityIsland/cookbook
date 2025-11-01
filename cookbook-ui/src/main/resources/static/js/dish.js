const API_BASE_URL = '/api/v1';
let currentDishId = null;

// Загрузка данных при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    currentDishId = urlParams.get('id');
    
    if (!currentDishId) {
        window.location.href = '/';
        return;
    }
    
    loadDishDetails();
    loadRecipes();
});

// Загрузка деталей блюда
async function loadDishDetails() {
    try {
        const response = await fetch(`${API_BASE_URL}/dishes/${currentDishId}`);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Блюдо не найдено');
            }
            throw new Error('Ошибка загрузки блюда');
        }
        
        const dish = await response.json();
        displayDishDetails(dish);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('dish-details').innerHTML = 
            '<div class="alert alert-error">Ошибка загрузки блюда. <a href="/">Вернуться к списку</a></div>';
    }
}

// Отображение деталей блюда
function displayDishDetails(dish) {
    const container = document.getElementById('dish-details');
    
    container.innerHTML = `
        <div class="dish-header">
            <div class="dish-header-content">
                <div class="dish-image">
                    🍽️
                </div>
                <div class="dish-info">
                    <h2>${escapeHtml(dish.name)}</h2>
                    <div class="description">${escapeHtml(dish.description)}</div>
                    <div class="dish-meta">
                        <div class="meta-item">📝 Рецептов: ${dish.recipesCount}</div>
                        <div class="meta-item">📅 Создано: ${formatDate(dish.createdAt)}</div>
                    </div>
                    <div class="dish-actions">
                        <button class="btn btn-danger btn-small" onclick="deleteDish()">Удалить блюдо</button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Загрузка рецептов блюда
async function loadRecipes() {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/dish/${currentDishId}`);
        if (!response.ok) throw new Error('Ошибка загрузки рецептов');
        
        const recipes = await response.json();
        displayRecipes(recipes);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('recipes-list').innerHTML = 
            '<div class="empty-state">Ошибка загрузки рецептов</div>';
    }
}

// Отображение рецептов
function displayRecipes(recipes) {
    const container = document.getElementById('recipes-list');
    
    if (recipes.length === 0) {
        container.innerHTML = '<div class="empty-state">Рецептов пока нет. Создайте первый рецепт!</div>';
        return;
    }
    
    container.innerHTML = recipes.map(recipe => `
        <div class="recipe-card">
            <h4>${escapeHtml(recipe.name)}</h4>
            <div class="recipe-meta">
                <span>⏱️ ${formatDuration(recipe.cookingTime)}</span>
                <span>👥 ${recipe.servings} порций</span>
                <span>🥕 ${recipe.ingredientsCount} ингредиентов</span>
                <span>📋 ${recipe.stepsCount} шагов</span>
            </div>
        </div>
    `).join('');
}

// Показать модальное окно создания рецепта
function showCreateRecipeModal() {
    document.getElementById('createRecipeModal').classList.add('show');
}

// Закрыть модальное окно создания рецепта
function closeCreateRecipeModal() {
    document.getElementById('createRecipeModal').classList.remove('show');
    document.getElementById('createRecipeForm').reset();
}

// Обработка создания рецепта
async function handleCreateRecipe(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const cookingTimeMinutes = parseInt(formData.get('cookingTime'));
    
    const data = {
        name: formData.get('name'),
        description: formData.get('description'),
        cookingTime: `PT${cookingTimeMinutes}M`, // ISO 8601 Duration format
        servings: parseInt(formData.get('servings'))
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/dish/${currentDishId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) throw new Error('Ошибка создания рецепта');
        
        closeCreateRecipeModal();
        
        // Перезагрузить рецепты и детали блюда
        await loadRecipes();
        await loadDishDetails();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось создать рецепт. Попробуйте еще раз.');
    }
}

// Удаление блюда
async function deleteDish() {
    if (!confirm('Вы уверены, что хотите удалить это блюдо? Все рецепты также будут удалены.')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/dishes/${currentDishId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Ошибка удаления блюда');
        
        // Вернуться на главную страницу
        window.location.href = '/';
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось удалить блюдо. Попробуйте еще раз.');
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

// Форматирование длительности
function formatDuration(duration) {
    // Duration в формате ISO 8601 (например, "PT45M")
    const match = duration.match(/PT(\d+H)?(\d+M)?(\d+S)?/);
    if (!match) return duration;
    
    const hours = match[1] ? parseInt(match[1]) : 0;
    const minutes = match[2] ? parseInt(match[2]) : 0;
    
    if (hours > 0) {
        return `${hours} ч ${minutes} мин`;
    }
    return `${minutes} мин`;
}

// Вспомогательная функция для экранирования HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const modal = document.getElementById('createRecipeModal');
    if (event.target === modal) {
        closeCreateRecipeModal();
    }
}
