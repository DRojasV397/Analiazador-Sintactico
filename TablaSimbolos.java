import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {

    private final Map<String, Object> values = new HashMap<>();
    final TablaSimbolos anterior;

    TablaSimbolos(TablaSimbolos anterior){
        this.anterior = anterior;
    }

    boolean existeIdentificador(String identificador){
        if(values.containsKey(identificador))
            return true;
        else 
            if(anterior == null)
                    return false;
            else
                return anterior.existeIdentificador(identificador);
    }

    Object obtener(String identificador) {
        if (values.containsKey(identificador)) 
            return values.get(identificador);
        else
            if(anterior == null)
                throw new RuntimeException("Variable no declarada (" + identificador + ")");
            else
                return anterior.obtener(identificador);
    }

    void asignar(String identificador, Object valor){
        if(values.containsKey(identificador))
            values.put(identificador, valor);
        else
            if(anterior == null)
                if(identificador == "return")
                    throw new RuntimeException("Error: Uso invalido de return");
                else
                    throw new RuntimeException("Variable no declarada (" + identificador + ")");
            else
                anterior.asignar(identificador, valor);
    }

    void declarar(String identificador, Object valor){
        values.put(identificador, valor);
    }
}
