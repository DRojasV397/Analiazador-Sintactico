package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprSuper extends Expression {
    // final Token keyword;
    final Token method;

    ExprSuper(Token method) {
        // this.keyword = keyword;
        this.method = method;
    }
}