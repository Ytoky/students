
<!-- ABOUT THE PROJECT -->
## About The Project
Мобильное приложение "Библиотека". Позволяет Хранить список книг, изменить конкретную книгу, удалять книгу либо все книги.

Использована многопоточность для разделения UI - потока от работы с БД.
Имеется валидация на количество символов автора, названия книги, максимальная длина страниц, пустые поля, пустой пробел вместо текста.
Приложение не имеет root - доступа к операционной системе.

### Built With
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)

### Дополнительно использованные библиотеки
1. Lombok
2. android.material
3. apache.commons:commons-lang3

<!-- GETTING STARTED -->
## Getting Started
Для запуска приложения на телефоне скачайте из репозитория файл library.apk
### Предварительная установка
Для локально запуска приложения в Android Studio нужно установить плагин Lombok для корректной работы.

1. Перейдите в папку где находиться Android Studio: C:\Program Files\Android\Android Studio\plugins
2. Скачайте плагин с сайта https://plugins.jetbrains.com/plugin/6317-lombok/versions
3. Перенесите папку lombok-plugin в папку из пункта 1.
4. Запускайте приложение

### Установка

1. Клонируйте репозиторий
   ```
   git clone https://github.com/R1con/BookLibrary.git
   ```

<!-- USAGE EXAMPLES -->
## Usage
Пример из приложения где используется многопоточность.
```java
private class AddBookTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try (BookDataBaseHelper bookDataBaseHelper = new BookDataBaseHelper(AddBookActivity.this)) {
                bookDataBaseHelper.addBook(titleInput.getText().toString().trim(),
                        authorInput.getText().toString().trim(),
                        Integer.parseInt(pagesInput.getText().toString().trim()));
            } catch (Exception e) {
                Log.d("error on connection to database", "");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(AddBookActivity.this, "Книга успешно добавлена!", Toast.LENGTH_SHORT).show();
        }
    }
```