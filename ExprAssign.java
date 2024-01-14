public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        if(tablaSimbolos.existeIdentificador(name.lexema))
            tablaSimbolos.asignar(name.lexema, value.solve(tablaSimbolos));
        else
            ASDR.error(name.linea, String.valueOf(name.posicion), "Variable no declarada (" + name.lexema + ").");
        return null;
    }
    
}
