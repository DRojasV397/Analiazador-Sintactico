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
            throw new RuntimeException("Variable no declarada (" + name.lexema + ").");
        return null;
    }
    
}
