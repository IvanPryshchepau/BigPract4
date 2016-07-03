public class _Runner {

    /*команды записываются с файл command.xls в корне проекта
    для выполнения негативного теста в начале команды дописать ! (например !pageContainsText)
    список команд:
    pageContainsText {page} {text} -- страница содержит текст
    pageContainsLinkText {page} {text} -- страница содержит ссылку с искомым названием
    registration {login} {email} {password} -- регистрация пользователя
    autorize {login} {password} -- авторизация
    search {text} {count} -- поиск с проверкой количества совпадений
    addTopic {forum} {topic} -- добавить тему в указанный форум
    addComment {forum} {topic} {comment} -- добавить сообщение в указанную тему
    editComment {comment} -- редактировать любое сообщение пользователя на указанное
    isOnline {user} -- проверка указания пользователя в списке Who is online
    statisticChange {type} -- изменение статистики (posts, topics, members)

    результат записывается в 7 столбец документа
    результат "!" при негативном тестировании - нет примечания потому, что тест прошел по первоначальному сценарию верно
    */

    public static void main(String[] args) {

        TestFramework tf = new TestFramework();
        tf.commandRun();

    }

}
