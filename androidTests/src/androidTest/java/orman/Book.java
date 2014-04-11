package orman;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Book extends Model<Book> {
    @PrimaryKey(autoIncrement = true)
    public long id;

    public String name;

    @ManyToOne
    public Author author;
}
