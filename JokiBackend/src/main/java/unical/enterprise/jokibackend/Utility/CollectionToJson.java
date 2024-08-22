package unical.enterprise.jokibackend.Utility;

import java.util.ArrayList;
import java.util.Collection;


//crea un Json Array con un elemento (element) per ogni item contenuto  nella collection
//è stata dovuta creare questa classe per problemi con Gson su un'unica  classe
public class CollectionToJson {
    public static String covert(Collection<?> c){
        StringBuilder b=new StringBuilder();
        ArrayList<?> a = new ArrayList<>(c);
        b.append("[");
        for (int i=0; i<c.size();i++){
            b.append("{");
            b.append("element:");
            b.append(a.get(i).toString());
            b.append("},");
        }
        b.append("]");
        return b.toString();
    }
}
