import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* 
 * Analizador Lexico
 * Materia: Compiladores
 * Profesor: Gabriel de Jesus Rodriguez Jordan
 * Integrantes:
 *  - Díaz Ortiz Brandon Aldair 
 *  - Nava Izquierdo César
 *  - Rojas Vazquez Diego
 *  
 * Secciones que aporto cada integrante:
 *  Díaz Ortiz Brandon Aldair:
 *      - Tokens de uno o dos caracteres
 *      - Manejo de cadenas
 *      - Identificación de la posición de los errores
 *  
 *  Nava Izquierdo César
 *      - Manejo de comentarios de una linea y multilinea
 *      - Tokens de un caracter
 *  Rojas vazquez Diego
 *      - Numeros enteros, de punto decimal y en notación cientefica
 *      - Simbolos que no pertenecen al lenguaje   
 *      - Identificación de la linea de los errores
*/
public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source){
        this.source = source + " ";
    }
    
    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        int linea = 1;
        int posicion = 0;
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);
            posicion++;
            if(c == '\n'){
                lexema = "";
                linea++;
                posicion = 0;
            }
            switch (estado){
                case 0:
                    //Palabras reservadas e identificadores
                    if(Character.isLetter(c)){
                        estado = 9;
                        lexema += c;
                    }
                    //Numeros enteros, de punto decimal y en notación cientefica.
                    else if(Character.isDigit(c)){
                        estado = 11;
                        lexema += c;
                    } 
                    //Tokens de uno o dos caracteres
                    else if(c == '>'){
                        estado = 1;
                        lexema += c;
                    }
                    else if(c == '<'){
                        estado = 2;
                        lexema += c;
                    }
                    else if(c == '='){
                        estado = 7;
                        lexema += c;
                    }
                    else if(c == '!'){
                        estado = 10;
                        lexema += c;
                    }
                    //Manejo de cadenas
                    else if(c =='"'){
                        estado = 16;
                        lexema += c;
                    }
                    //Manejo de comentarios de una linea y multilinea
                    else if(c == '/'){
                        estado = 17;
                        lexema += c;
                    }
                    //Tokens de un solo caracter
                    else if(c == '+'){
                        estado = 21;
                        lexema += c;
                    }
                    else if(c == '-'){
                        estado = 22;
                        lexema += c;
                    }
                    else if(c == '*'){
                        estado = 23;
                        lexema += c;
                    }
                    else if(c == '{'){
                        estado = 24;
                        lexema += c;
                    }
                    else if(c == '}'){
                        estado = 25;
                        lexema += c;
                    }
                    else if(c == '('){
                        estado = 26;
                        lexema += c;
                    }
                    else if(c == ')'){
                        estado = 27;
                        lexema += c;
                    }
                    else if(c == ','){
                        estado = 28;
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado = 29;
                        lexema += c;
                    }
                    else if(c == ';'){
                        estado = 30;
                        lexema += c;
                    }
                    //Simbolos que no pertenecen al lenguaje
                    else if(!Character.isLetterOrDigit(c) && c!='\n' && c!=' ' && c!='\t' && c!='\r'){
                        estado = 31;
                        i--;
                        posicion--;
                        lexema = "";
                    }
                    if(i == source.length()-1){
                        lexema = "";
                        Token t = new Token(TipoToken.EOF, lexema);
                        tokens.add(t);
                    }
                break;
                //--------------------------------------------------------------------------------------------------
                //                              Tokens de uno o dos caracteres
                //--------------------------------------------------------------------------------------------------
                case 1:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);
                        i--;
                        posicion--;
                    }
                    estado = 0;
                    lexema = "";
                break;
                case 2:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                    }
                    estado = 0;
                    lexema = "";
                break;
                case 7:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                    }
                    estado = 0;
                    lexema = "";
                break;
                case 10:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                    }
                    estado = 0;
                    lexema = "";
                break;
                //--------------------------------------------------------------------------------------------------
                //                              Palabras reservadas e identificadores
                //--------------------------------------------------------------------------------------------------
                case 9:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        estado = 9;
                        lexema += c;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }
                        estado = 0;
                        lexema = "";
                        i--; 
                        posicion--;
                    }
                break;
                //--------------------------------------------------------------------------------------------------
                //                   Numeros enteros, de punto decimal y en notación cientefica.
                //--------------------------------------------------------------------------------------------------
                case 11:
                    if(Character.isDigit(c)){
                        estado = 11;
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado = 12;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 14;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--; 
                        posicion--;
                    }
                break;
                case 12:
                    if(Character.isDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else{
                        lexema = lexema.substring(0, lexema.length() - 1);
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i-=2;
                        posicion -= 2;
                    }
                break;
                case 13:
                    if(Character.isDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 14;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.parseFloat(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--; 
                        posicion--;
                    }
                break;
                case 14:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '+' || c == '-'){
                        estado = 151;
                        lexema += c;
                    }
                    else{
                        if(lexema.contains(".")){
                            lexema = lexema.substring(0, lexema.length() - 1);
                            Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                            tokens.add(t);
                        }
                        else{
                            lexema = lexema.substring(0, lexema.length() - 1);
                            Token t = new Token(TipoToken.NUMBER, lexema, Float.parseFloat(lexema));
                            tokens.add(t);
                        }
                        i-=2;
                        posicion -= 2;
                        lexema = "";
                        estado = 0;
                    }
                break;
                case 15:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--; 
                        posicion--;
                    }
                break;
                case 151:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else{
                        System.out.println("Hola");
                        lexema = lexema.substring(0, lexema.length() - 2);
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.parseFloat(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i-=3;
                        posicion -= 3;
                    }
                break;
    //--------------------------------------------------------------------------------------------------
    //                                          Manejo de cadenas
    //--------------------------------------------------------------------------------------------------
                case 16:
                    if(c == '\n'){
                        do {
                            if(c == '\n'){
                                Interprete.error(linea, ""+posicion,"No se permiten saltos de linea dentro de una cadena.");
                                posicion = 0;
                            }
                            if(i != (source.length() - 1)){
                                i++;
                                c = source.charAt(i);
                            }
                            else{
                                Interprete.error(linea,""+posicion, "Se esperaba '\"'. La cadena no se cerro corectamente.");
                                break;
                            }
                        } while (c != '"');
                        estado = 0;
                        lexema = "";
                    }
                    else if(c != '"'){
                        estado = 16;
                        lexema += c;
                        if(i == (source.length() - 1)){
                            Interprete.error(linea,""+posicion, "Se esperaba '\"'. La cadena no se cerro corectamente.");
                        }
                    }
                    else{
                        lexema += c;
                        Token t = new Token(TipoToken.STRING, lexema, lexema.substring(1, lexema.length() - 1));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                break;
                //--------------------------------------------------------------------------------------------------
                //                              Comentarios de una linea y multilinea
                //--------------------------------------------------------------------------------------------------
                case 17:
                    if(c == '/'){
                        estado = 20;
                        lexema += c;
                    }
                    else if(c == '*'){
                        estado = 18;
                        lexema = "";
                    }
                    else{
                        Token t = new Token(TipoToken.SLASH, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                    }
                break;
                case 18:
                    if(c != '*'){
                        estado = 18;
                        if(i == (source.length() - 1)){
                            Interprete.error(linea,""+posicion, "Se esperaba '*/'. El comentario no cerro corectamente.");
                        }
                    }
                    else{
                        estado = 19;
                    }
                break;
                case 19:
                    if(c == '*'){
                        estado = 19;
                    }
                    else if(c == '/'){
                        estado = 0;
                    }
                    else{
                        estado = 18;
                        if(i == (source.length() - 1)){
                            Interprete.error(linea,""+posicion, "Se esperaba '*/'. El comentario no cerro corectamente.");
                        }
                    }
                break;
                case 20:
                    if(c != '\n'){
                        estado = 20;
                        lexema += c;
                    }
                    else{
                        estado = 0;
                        lexema = "";
                    }
                break;
                //--------------------------------------------------------------------------------------------------
                //                                      Tokens de un caracter
                //--------------------------------------------------------------------------------------------------
                case 21:{
                    Token t = new Token(TipoToken.PLUS, lexema);
                    tokens.add(t);
                    i--; 
                    posicion--;
                    lexema = "";
                    estado = 0;
                break;}
                case 22:{
                        Token t = new Token(TipoToken.MINUS, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 23:{
                        Token t = new Token(TipoToken.STAR, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 24:{
                        Token t = new Token(TipoToken.LEFT_BRACE, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 25:{
                        Token t = new Token(TipoToken.RIGHT_BRACE, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 26:{
                        Token t = new Token(TipoToken.LEFT_PAREN, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 27:{
                        Token t = new Token(TipoToken.RIGHT_PAREN, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 28:{
                        Token t = new Token(TipoToken.COMMA, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 29:{
                        Token t = new Token(TipoToken.DOT, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                case 30:{
                        Token t = new Token(TipoToken.SEMICOLON, lexema);
                        tokens.add(t);
                        i--; 
                        posicion--;
                        lexema = "";
                        estado = 0;
                break;}
                //--------------------------------------------------------------------------------------------------
                //                              Simbolos que no pertenecen al lenguaje
                //--------------------------------------------------------------------------------------------------
                case 31:
                    Interprete.error(linea,""+posicion, "Caracter invalido.");
                    estado = 0;
                break;
            }
        }
        return tokens;
    }
}
