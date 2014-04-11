package orman;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Author extends Model<Author> {
    @PrimaryKey(autoIncrement = true)
    public long id;
    public String name;

    @OneToMany(toType = Book.class, onField = "author")
    public EntityList<Author, Book> books = new EntityList<Author, Book>(Author.class, Book.class, this);
}
