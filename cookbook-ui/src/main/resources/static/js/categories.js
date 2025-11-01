const API_BASE_URL = '/api/v1';

// Загрузка категорий при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
});

// Загрузка всех категорий
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        if (!response.ok) throw new Error('Ошибка загрузки категорий');
        
        const categories = await response.json();
        displayCategories(categories);
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('categories-list').innerHTML = 
            '<div class="empty-state">Ошибка загрузки категорий</div>';
    }
}

// Отображение категорий
function displayCategories(categories) {
    const container = document.getElementById('categories-list');
    
    if (categories.length === 0) {
        container.innerHTML = '<div class="empty-state">Категорий пока нет. Создайте первую категорию!</div>';
        return;
    }
    
    container.innerHTML = categories.map(category => `
        <div class="category-card">
            <h3>${escapeHtml(category.name)}</h3>
            ${category.description ? `<p>${escapeHtml(category.description)}</p>` : ''}
        </div>
    `).join('');
}

// Показать модальное окно создания категории
function showCreateCategoryModal() {
    document.getElementById('createCategoryModal').classList.add('show');
}

// Закрыть модальное окно создания категории
function closeCreateCategoryModal() {
    document.getElementById('createCategoryModal').classList.remove('show');
    document.getElementById('createCategoryForm').reset();
}

// Обработка создания категории
async function handleCreateCategory(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const data = {
        name: formData.get('name'),
        description: formData.get('description') || null
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/categories`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) throw new Error('Ошибка создания категории');
        
        closeCreateCategoryModal();
        
        // Перезагрузить список категорий
        await loadCategories();
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось создать категорию. Попробуйте еще раз.');
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
    const modal = document.getElementById('createCategoryModal');
    if (event.target === modal) {
        closeCreateCategoryModal();
    }
}
