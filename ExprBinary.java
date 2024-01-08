public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        try {
            Object leftValue = left.solve(tablaSimbolos);
            Object rightValue = right.solve(tablaSimbolos);

            if (leftValue instanceof Number && rightValue instanceof Number) {
                double leftDouble = ((Number) leftValue).doubleValue();
                double rightDouble = ((Number) rightValue).doubleValue();
                int leftInteger = ((Number) leftValue).intValue();
                int rightInteger = ((Number) rightValue).intValue();

                switch (operator.tipo) {
                    case MINUS:
                        if(leftValue instanceof Integer)
                            return leftInteger - rightInteger;
                        else
                            return leftDouble - rightDouble;
                    case PLUS:
                        if(leftValue instanceof Integer)
                            return leftInteger + rightInteger;
                        else
                            return leftDouble + rightDouble;
                    case STAR:
                        if(leftValue instanceof Integer)
                            return leftInteger * rightInteger;
                        else
                            return leftDouble * rightDouble;
                    case SLASH:
                        if (rightDouble != 0) {
                            if(leftValue instanceof Integer)
                                return leftInteger / rightInteger;
                            else
                                return leftDouble / rightDouble;
                        } else 
                            throw new RuntimeException("Error: División por cero");
                    case BANG_EQUAL:
                        return (leftValue != rightValue);
                    case EQUAL_EQUAL:
                        return (leftValue == rightValue);
                    case GREATER:
                        return leftDouble > rightDouble;
                    case GREATER_EQUAL:
                        return leftDouble >= rightDouble;
                    case LESS:
                        return leftDouble < rightDouble;
                    case LESS_EQUAL:
                        return leftDouble <= rightDouble;
                    default:
                        break;
                }
            } else if(leftValue instanceof String || rightValue instanceof String){
                if(operator.tipo == TipoToken.PLUS)
                    return leftValue.toString() + rightValue.toString();
            } else {
                throw new RuntimeException("Error: Operandos no numéricos");
            }
        } catch (Exception e) {
            throw new RuntimeException("");
        }
        return null;
    }
}
