package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorService {
    
    Date date = new Date();

    public List listOfAuthors() {

        List<Author> authorList = new ArrayList<Author>();

        authorList.add(new Author(1, "Your Mama", date));
        authorList.add(new Author(2, "Your Daddy", date));
        authorList.add(new Author(3, "Your Sister", date));

        return authorList;
    }
}

