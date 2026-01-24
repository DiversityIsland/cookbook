const API_BASE_URL = '/api/v1';
let currentRecipeId = null;

// Загрузка данных при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    currentRecipeId = urlParams.get('id');
    
    if (!currentRecipeId) {
        window.location.href = '/';
        return;
    }
    
    loadRecipeDetails();
});

// Загрузка деталей рецепта
async function loadRecipeDetails() {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/${currentRecipeId}/full`);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Рецепт не найден');
            }
            throw new Error('Ошибка загрузки рецепта');
        }
        
        const recipe = await response.json();
        displayRecipeDetails(recipe);
        displayIngredients(recipe.ingredients || []);
        displaySteps(recipe.steps || []);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('recipe-details').innerHTML = 
            '<div class="alert alert-error">Ошибка загрузки рецепта. <a href="/">Вернуться к списку</a></div>';
    }
}

// Отображение деталей рецепта
function displayRecipeDetails(recipe) {
    const container = document.getElementById('recipe-details');
    
    container.innerHTML = `
        <div class="dish-header">
            <div class="dish-info">
                <h2>${escapeHtml(recipe.name)}</h2>
                <div class="description">${escapeHtml(recipe.description)}</div>
                <div class="dish-meta">
                    <div class="meta-item">⏱️ ${formatDuration(recipe.cookingTime)}</div>
                    <div class="meta-item">👥 ${recipe.servings} порций</div>
                    <div class="meta-item">📅 Создано: ${formatDate(recipe.createdAt)}</div>
                </div>
                <div class="dish-actions">
                    <button class="btn btn-secondary btn-small" onclick="goToDish('${recipe.dishId}')">← К блюду</button>
                    <button class="btn btn-danger btn-small" onclick="deleteRecipe()">Удалить рецепт</button>
                </div>
            </div>
        </div>
    `;
}

// Отображение ингредиентов
function displayIngredients(ingredients) {
    const container = document.getElementById('ingredients-list');
    
    if (ingredients.length === 0) {
        container.innerHTML = '<div class="empty-state">Ингредиенты пока не добавлены</div>';
        return;
    }
    
    container.innerHTML = `
        <ul class="ingredients">
            ${ingredients.map(ing => `
                <li class="ingredient-item">
                    <span class="ingredient-name">${escapeHtml(ing.name)}</span>
                    <span class="ingredient-amount">${ing.amount} ${formatUnit(ing.unit)}</span>
                    ${ing.isOptional ? '<span class="ingredient-optional">(опционально)</span>' : ''}
                </li>
            `).join('')}
        </ul>
    `;
}

// Отображение шагов приготовления
function displaySteps(steps) {
    const container = document.getElementById('steps-list');
    
    if (steps.length === 0) {
        container.innerHTML = '<div class="empty-state">Шаги приготовления пока не добавлены. Добавьте первый шаг!</div>';
        return;
    }
    
    // Сортируем шаги по номеру
    const sortedSteps = [...steps].sort((a, b) => a.stepNumber - b.stepNumber);
    
    container.innerHTML = sortedSteps.map(step => `
        <div class="step-card">
            <div class="step-number">Шаг ${step.stepNumber}</div>
            <div class="step-content">
                <p>${escapeHtml(step.description)}</p>
                ${step.estimatedTime ? `<div class="step-time">⏱️ ${formatDuration(step.estimatedTime)}</div>` : ''}
            </div>
        </div>
    `).join('');
}

// Показать модальное окно создания шага
function showCreateStepModal() {
    document.getElementById('createStepModal').classList.add('show');
}

// Закрыть модальное окно создания шага
function closeCreateStepModal() {
    document.getElementById('createStepModal').classList.remove('show');
    document.getElementById('createStepForm').reset();
}

// Обработка создания шага
async function handleCreateStep(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const stepNumber = parseInt(formData.get('stepNumber'));
    const description = formData.get('description').trim();
    const estimatedTimeMinutes = formData.get('estimatedTime');
    
    if (!stepNumber || stepNumber <= 0) {
        alert('Номер шага должен быть больше 0');
        return;
    }
    
    if (!description) {
        alert('Описание шага не может быть пустым');
        return;
    }
    
    const data = {
        stepNumber: stepNumber,
        description: description,
        estimatedTime: estimatedTimeMinutes ? `PT${estimatedTimeMinutes}M` : null
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/${currentRecipeId}/steps`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) throw new Error('Ошибка создания шага');
        
        closeCreateStepModal();
        
        // Перезагрузить рецепт
        await loadRecipeDetails();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось создать шаг. Попробуйте еще раз.');
    }
}

// Перейти к блюду
function goToDish(dishId) {
    window.location.href = `/dish.html?id=${dishId}`;
}

// Удаление рецепта
async function deleteRecipe() {
    if (!confirm('Вы уверены, что хотите удалить этот рецепт?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/${currentRecipeId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Ошибка удаления рецепта');
        
        // Получить dishId из текущих данных или вернуться на главную
        const recipeResponse = await fetch(`${API_BASE_URL}/recipes/${currentRecipeId}`);
        if (recipeResponse.ok) {
            const recipe = await recipeResponse.json();
            window.location.href = `/dish.html?id=${recipe.dishId}`;
        } else {
            window.location.href = '/';
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось удалить рецепт. Попробуйте еще раз.');
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
    // Duration приходит как число секунд
    const totalMinutes = Math.floor(durationSeconds / 60);
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;
    
    if (hours > 0) {
        return minutes > 0 ? `${hours} ч ${minutes} мин` : `${hours} ч`;
    }
    return `${minutes} мин`;
}

// Форматирование единиц измерения
function formatUnit(unit) {
    const units = {
        'GRAM': 'г',
        'KILOGRAM': 'кг',
        'MILLILITER': 'мл',
        'LITER': 'л',
        'PIECE': 'шт',
        'TABLESPOON': 'ст.л.',
        'TEASPOON': 'ч.л.',
        'CUP': 'стакан',
        'PINCH': 'щепотка',
        'TO_TASTE': 'по вкусу'
    };
    return units[unit] || unit;
}

// Вспомогательная функция для экранирования HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Показать модальное окно управления ингредиентами
function showManageIngredientsModal() {
    document.getElementById('manageIngredientsModal').classList.add('show');
    loadIngredientsForManagement();
}

// Закрыть модальное окно управления ингредиентами
function closeManageIngredientsModal() {
    document.getElementById('manageIngredientsModal').classList.remove('show');
}

// Показать модальное окно добавления ингредиента
function showAddIngredientModal() {
    document.getElementById('addIngredientModal').classList.add('show');
    document.getElementById('addIngredientForm').reset();
}

// Закрыть модальное окно добавления ингредиента
function closeAddIngredientModal() {
    document.getElementById('addIngredientModal').classList.remove('show');
}

// Загрузка ингредиентов для управления
async function loadIngredientsForManagement() {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/${currentRecipeId}/full`);
        if (!response.ok) throw new Error('Ошибка загрузки ингредиентов');
        
        const recipe = await response.json();
        displayIngredientsForManagement(recipe.ingredients || []);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('ingredients-manage-list').innerHTML = 
            '<div class="empty-state">Ошибка загрузки ингредиентов</div>';
    }
}

// Отображение ингредиентов для управления
function displayIngredientsForManagement(ingredients) {
    const container = document.getElementById('ingredients-manage-list');
    
    if (ingredients.length === 0) {
        container.innerHTML = '<div class="empty-state">Ингредиенты пока не добавлены</div>';
        return;
    }
    
    container.innerHTML = ingredients.map(ing => `
        <div class="manage-ingredient-item">
            <div class="manage-ingredient-info">
                <span class="manage-ingredient-name">${escapeHtml(ing.name)}</span>
                <span class="manage-ingredient-amount">${ing.amount} ${formatUnit(ing.unit)}</span>
                ${ing.isOptional ? '<span class="ingredient-optional">(опционально)</span>' : ''}
            </div>
            <button class="btn btn-danger btn-small" onclick="deleteIngredient('${ing.id}')">
                Удалить
            </button>
        </div>
    `).join('');
}

// Добавить ингредиент
async function handleAddIngredient(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const name = formData.get('name').trim();
    const amount = parseFloat(formData.get('amount'));
    
    if (!name) {
        alert('Название ингредиента не может быть пустым');
        return;
    }
    
    if (!amount || amount <= 0) {
        alert('Количество должно быть больше 0');
        return;
    }
    
    const data = {
        name: name,
        amount: amount,
        unit: formData.get('unit'),
        isOptional: formData.get('isOptional') === 'on'
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/${currentRecipeId}/ingredients`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) throw new Error('Ошибка добавления ингредиента');
        
        // Закрыть модальное окно добавления
        closeAddIngredientModal();
        
        // Обновить список в модальном окне управления
        await loadIngredientsForManagement();
        
        // Обновить основной список
        await loadRecipeDetails();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось добавить ингредиент. Попробуйте еще раз.');
    }
}

// Удалить ингредиент
async function deleteIngredient(ingredientId) {
    if (!confirm('Вы уверены, что хотите удалить этот ингредиент?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/ingredients/${ingredientId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Ошибка удаления ингредиента');
        
        // Обновить список в модальном окне
        await loadIngredientsForManagement();
        
        // Обновить основной список
        await loadRecipeDetails();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось удалить ингредиент. Попробуйте еще раз.');
    }
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const createStepModal = document.getElementById('createStepModal');
    const manageIngredientsModal = document.getElementById('manageIngredientsModal');
    const addIngredientModal = document.getElementById('addIngredientModal');
    
    if (event.target === createStepModal) {
        closeCreateStepModal();
    }
    if (event.target === manageIngredientsModal) {
        closeManageIngredientsModal();
    }
    if (event.target === addIngredientModal) {
        closeAddIngredientModal();
    }
}
