package UnitTests;

import DomainLayer.Stores.Comment;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    Comment c = new Comment();
    int RegisterdId = 1;
    @Test
    void addComment_okData() {
        c.AddComment(RegisterdId,"Great product");
        LinkedList<String> comments = c.GetComments(RegisterdId);
        assertEquals("Great Product", comments.get(0));
    }
    @Test
    void addComment_TwoComments() {
        c.AddComment(RegisterdId,"Great product");
        c.AddComment(RegisterdId,"I take it back horrible product");
        LinkedList<String> comments = c.GetComments(RegisterdId);
        assertEquals("Great Product", comments.get(0));
        assertEquals("I take it back horrible product", comments.get(1));
    }
    @Test
    void addComment_nullComment() {
        assertThrows(IllegalArgumentException.class,()->c.AddComment(RegisterdId,null));
    }
}