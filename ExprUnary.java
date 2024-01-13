public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        if(operator.tipo == TipoToken.MINUS)
            try {
                return (boolean) right.solve(tablaSimbolos);
            } catch (Exception e) {
                throw new RuntimeException("Error: El operando debe ser booleano");
            }
        else
        try {
            return (boolean) right.solve(tablaSimbolos);
        } catch (Exception e) {
            throw new RuntimeException("Error: El operando debe ser booleano");
        }
    }
}
