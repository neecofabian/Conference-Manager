//package backend.data.entity;
//
//import com.conference.backend.messenger.entities.Message;
//import static org.junit.Assert.*;
//import org.junit.Test;
//
//import java.time.LocalDateTime;
//
//public class MessageIT {
//
//    @Test
//    public void testGetText() {
//        Message m1 = new Message("Hello", LocalDateTime.now(), "John");
//
//        assertTrue(m1.getText().equals("Hello"));
//    }
//
//    @Test
//    public void testGetDate() {
//        Message m1 = new Message("Hello", LocalDateTime.of(2020,
//                11, 3, 18, 51, 30), "John");
//
//        assertTrue(m1.getDate().equals(LocalDateTime.of(2020,
//                11, 3, 18, 51, 30)));
//    }
//
//    @Test
//    public void testToString() {
//        Message m1 = new Message("Hello", LocalDateTime.of(2020,
//                11, 3, 18, 51, 30), "John");
//
//        assertTrue(m1.toString().equals("Message [" + "text=" + m1.getText()
//                + ", date=" + m1.getDate().toString() + "]"));
//
//    }
//
//    @Test
//    public void testCopyConstructorEquals() {
//        Message m1 = new Message("Hello", LocalDateTime.of(2020,
//                11, 3, 18, 51, 30), "John");
//        m1.setId("1");
//        Message m2 = new Message(m1);
//        m2.setId(m1.getId());
//        assertTrue(m1.equals(m2));
//    }
//}
