package lbs.erasmus.touristanbul;


import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * Created by Jess on 16/03/14.
 */
public class HandlerXML extends DefaultHandler {

    private Double totalResults;
    private StringBuilder cadena = new StringBuilder();

    public Double getTotalResults() {
        return totalResults;
    }

    @Override
    public void startElement(String uri, String nombreLocal, String nombreCualif, Attributes atributos) throws SAXException {
        cadena.setLength(0);
    }

    @Override
    public void characters(char ch[], int comienzo, int lon){
        cadena.append(ch, comienzo, lon);
    }

    @Override
    public void endElement(String uri, String nombreLocal,String nombreCualif) throws SAXException {
        totalResults = Double.parseDouble(cadena.toString());
    }

}
