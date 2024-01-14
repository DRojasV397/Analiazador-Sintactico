import java.util.List;
import java.util.ArrayList;


public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Token paren;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        List<Object> miFuncion = validarFuncion(tablaSimbolos);
        List<Object> argumentos = resolverArgumentos(tablaSimbolos, arguments);
        return ejecutar(tablaSimbolos, argumentos, miFuncion);
    }

    List<Object> validarFuncion(TablaSimbolos tablaSimbolos){
        if(tablaSimbolos.existeIdentificador(callee.toString())){
            try {
                List<Object> param_body = (List<Object>) tablaSimbolos.obtener(callee.toString());
                return param_body;
            } catch (Exception e) {
                throw new RuntimeException("Error: " + callee.toString() + " no es una función.");
            }
        }
        else
            throw new RuntimeException("Error: " + callee.toString() + " ya se declaro.");
    }

    List<Object> resolverArgumentos(TablaSimbolos tablaSimbolos, List<Expression> parametros){
        try {
            List<Object> parametrosResultos = new ArrayList<Object>();
            for (Expression parametro : parametros) {
                parametrosResultos.add(parametro.solve(tablaSimbolos));
            }
            return parametrosResultos;
        } catch (Exception e) {
            throw new RuntimeException("Error: No se pudieron resolver los argumentos para " + callee.toString());
        }
    }

    Object ejecutar(TablaSimbolos tabla, List<Object> argumentos, List<Object> funcion){
        //Verificamos si la cantidad de parametros es igual a la cantidad de argumentos
        if(argumentos.size() != ((List<Token>) funcion.get(0)).size())
            throw new RuntimeException("Error: La cantidad de arguemtos es diferente a la de parametros");
        //Creamos una nueva instancia de la función
        TablaSimbolos instanciaFuncion = new TablaSimbolos(tabla);
        List<Token> parametros = (List<Token>) funcion.get(0);
        for(int i = 0; i < argumentos.size(); i++){
            instanciaFuncion.declarar(parametros.get(i).lexema, argumentos.get(i ));
        }
        instanciaFuncion.declarar("return", null);
        List<Statement> cuerpo = (List<Statement>) funcion.get(1);
        for (Statement statement : cuerpo) {
            statement.exec(instanciaFuncion);
        }
        return instanciaFuncion.obtener("return");
    }
}
