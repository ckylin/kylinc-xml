/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import com.kylinc.xml.core.XmlDocument;
import com.kylinc.xml.core.XmlElement;
import com.kylinc.xml.exception.XmlException;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    String xml = "<?xml version='1.1' encoding='gb2312'?>'" +
            "<notes>\n" +
            "    <note>\n" +
            "        <to>George</to>\n" +
            "        <from>John</from>\n" +
            "        <heading id = \"=test\"  name = \"test\">Reminder</heading>\n" +
            "        <body>Don't forget the meeting!</body>\n" +
            "    </note>\n" +
            "    <note>\n" +
            "        <to>George</to>" +
            "        <!-- 这是让哥哥的独家注解 -->\n" +
            "        <from>John</from>\n" +
            "        <heading>Reminder</heading>\n" +
            "        <body id='2323232'>" +
            "           <property name='message'>" +
            "           Don't forget the meeting!" +
            "           </property>" +
            "        </body>\n" +
            "    </note>\n" +
            "</notes>" ;

    @Test public void testSomeLibraryMethod() {
        Library classUnderTest = new Library();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }

    @Test public void testParseXml(){

    }

    @Test public void testFindXmlByXpath(){
        try {
            XmlDocument xmlDocument = XmlDocument.createDocumentByXml(xml);

            XmlElement xmlElement = xmlDocument.xpath("note[1].body.property");

//            assertEquals("2323232",xmlElement.getAttribute("id"));
            assertEquals("Don't forget the meeting!",xmlElement.getText().trim());
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }
}
