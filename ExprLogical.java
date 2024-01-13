public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        try {
            if(operator.tipo == TipoToken.OR)
                return (boolean) left.solve(tablaSimbolos) || (boolean) right.solve(tablaSimbolos);
            else
            return (boolean) left.solve(tablaSimbolos) && (boolean) right.solve(tablaSimbolos);
        } catch (Exception e) {
            throw new RuntimeException("Error: Los operadores deben ser booleanos");
        }
    }
}

