package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprSet extends Expression{
    final Expression object;
    final Token name;
    final Expression value;

    ExprSet(Expression object, Token name, Expression value) {
        this.object = object;
        this.name = name;
        this.value = value;
    }
}
