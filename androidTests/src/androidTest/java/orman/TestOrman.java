package orman;

import android.test.AndroidTestCase;

import org.fest.assertions.api.Assertions;
import org.orman.dbms.Database;
import org.orman.dbms.sqliteandroid.SQLiteAndroid;
import org.orman.mapper.MappingSession;
import org.orman.mapper.ModelQuery;

import java.util.List;

public class TestOrman extends AndroidTestCase {
    public static final String SOME_NAME = "SOME NAME";
    public static final String JOHN = "JOHN";
    public static final String FIRST = "First";
    public static final String SECOND = "SECOND";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initOrman();
    }

    private void initOrman() {
        Database db = new SQLiteAndroid(getContext(), "mydb.db");
        MappingSession.registerDatabase(db);
        if (!MappingSession.isSessionStarted()) {
            MappingSession.registerEntity(Author.class);
            MappingSession.registerEntity(Book.class);
            MappingSession.start();
        }
    }

    public void testSave() {
        Author author = new Author();
        author.name = SOME_NAME;
        author.insert();

        List<Author> authors = Author.fetchAll(Author.class);
        Assertions.assertThat(authors.size()).isEqualTo(1);
    }

    public void testSaveRelations() {
        Author author = new Author();
        author.name = JOHN;
        author.insert();

        Book oneBook = new Book();
        oneBook.name = FIRST;
        oneBook.author = author;
        oneBook.insert();

        Book secondBook = new Book();
        secondBook.name = SECOND;
        secondBook.author = author;
        secondBook.insert();

        Author fetchedAuthor = Author.fetchSingle(ModelQuery.select().from(Author.class).getQuery(), Author.class);

        assertEquals(fetchedAuthor.name, JOHN);
        assertEquals(fetchedAuthor.books.size(), 2);
    }

    public void testRelationsDirectly() {
        Author author = new Author();
        author.insert();

        Book someBook = new Book();
        someBook.name = FIRST;
        someBook.insert();

        author.books.add(someBook);
        author.update();

        Author fetchedAuthor = Author.fetchSingle(ModelQuery.select().from(Author.class).getQuery(), Author.class);
        assertEquals(fetchedAuthor.books.size(), 1);
        Book book = fetchedAuthor.books.get(0);
        assertEquals(book.author.name, author.name);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Author.execute(ModelQuery.delete().from(Author.class).getQuery());
        Book.execute(ModelQuery.delete().from(Book.class).getQuery());
    }
}
