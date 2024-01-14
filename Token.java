public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int linea;
    final int posicion;

    public Token(TipoToken tipo, String lexema, int linea, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.linea = linea;
        this.posicion = posicion; 
    }

    public Token(TipoToken tipo, String lexema, int linea, int posicion, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linea = linea;
        this.posicion = posicion; 
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        if(this.tipo == ((Token)o).tipo){
            return true;
        }

        return false;
    }

    public String toString() {
        return "<" + tipo + " " + lexema + " " + literal + ">";
    }
}
