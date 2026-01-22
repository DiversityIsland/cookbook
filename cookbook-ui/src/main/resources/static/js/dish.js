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
    
    const imageHtml = dish.imageId 
        ? `<img src="${API_BASE_URL}/images/${dish.imageId}/data" alt="${escapeHtml(dish.name)}" class="dish-image-img">`
        : `<div class="dish-image-placeholder">🍽️</div>`;
    
    const imageButtonsHtml = dish.imageId
        ? `
            <button class="btn btn-secondary btn-small" onclick="showUploadImageModal()">Заменить фото</button>
            <button class="btn btn-danger btn-small" onclick="removeImage()">Удалить фото</button>
          `
        : `<button class="btn btn-primary btn-small" onclick="showUploadImageModal()">Добавить фото</button>`;
    
    container.innerHTML = `
        <div class="dish-header">
            <div class="dish-header-content">
                <div class="dish-image-container">
                    ${imageHtml}
                    <div class="dish-image-actions">
                        ${imageButtonsHtml}
                    </div>
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
        <div class="recipe-card" onclick="openRecipe('${recipe.id}')">
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

// Открыть страницу рецепта
function openRecipe(id) {
    window.location.href = `/recipe.html?id=${id}`;
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
function formatDuration(durationSeconds) {
    // Duration приходит как число секунд (например, 5400.000000000)
    const totalMinutes = Math.floor(durationSeconds / 60);
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;
    
    if (hours > 0) {
        return minutes > 0 ? `${hours} ч ${minutes} мин` : `${hours} ч`;
    }
    return `${minutes} мин`;
}

// Вспомогательная функция для экранирования HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Показать модальное окно загрузки изображения
function showUploadImageModal() {
    document.getElementById('uploadImageModal').classList.add('show');
}

// Закрыть модальное окно загрузки изображения
function closeUploadImageModal() {
    document.getElementById('uploadImageModal').classList.remove('show');
    document.getElementById('uploadImageForm').reset();
}

// Обработка загрузки изображения
async function handleUploadImage(event) {
    event.preventDefault();
    
    const fileInput = document.getElementById('imageFile');
    const file = fileInput.files[0];
    
    if (!file) {
        alert('Пожалуйста, выберите файл');
        return;
    }
    
    // Проверка типа файла
    if (!file.type.startsWith('image/')) {
        alert('Пожалуйста, выберите изображение');
        return;
    }
    
    try {
        // Загрузить изображение
        const formData = new FormData();
        formData.append('file', file);
        
        const uploadResponse = await fetch(`${API_BASE_URL}/images`, {
            method: 'POST',
            body: formData
        });
        
        if (!uploadResponse.ok) {
            throw new Error(`Ошибка загрузки изображения: ${uploadResponse.status}`);
        }
        
        const imageId = (await uploadResponse.text()).trim().replace(/^"|"$/g, '');
        
        // Установить изображение для блюда
        const setImageResponse = await fetch(`${API_BASE_URL}/dishes/${currentDishId}/image/${imageId}`, {
            method: 'PUT'
        });
        
        if (!setImageResponse.ok) {
            throw new Error(`Ошибка установки изображения: ${setImageResponse.status}`);
        }
        
        closeUploadImageModal();
        
        // Перезагрузить детали блюда
        await loadDishDetails();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось загрузить изображение. Попробуйте еще раз.');
    }
}

// Удаление изображения
async function removeImage() {
    if (!confirm('Вы уверены, что хотите удалить фото?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/dishes/${currentDishId}/image`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Ошибка удаления изображения');
        
        // Перезагрузить детали блюда
        await loadDishDetails();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось удалить изображение. Попробуйте еще раз.');
    }
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const createRecipeModal = document.getElementById('createRecipeModal');
    const uploadImageModal = document.getElementById('uploadImageModal');
    
    if (event.target === createRecipeModal) {
        closeCreateRecipeModal();
    }
    if (event.target === uploadImageModal) {
        closeUploadImageModal();
    }
}
