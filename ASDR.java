import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

public class ASDR implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<TipoToken> primeroEXPRESSION= new ArrayList<>(); //Se declara una lista en la que se incluiran los tokens que forman el conjunto primero de EXPRESSION
    private List<TipoToken> primeroSTATEMENT= new ArrayList<>();
    private List<TipoToken> primeroEXPR_STMT= new ArrayList<>();
    private List<TipoToken> primeroFOR_STMT= new ArrayList<>();
    private List<TipoToken> primeroIF_STMT= new ArrayList<>();
    private List<TipoToken> primeroPRINT_STMT= new ArrayList<>();
    private List<TipoToken> primeroRETURN_STMT= new ArrayList<>();
    private List<TipoToken> primeroWHILE_STMT= new ArrayList<>();
    private List<TipoToken> primeroBLOCK_STMT= new ArrayList<>();
    private List<TipoToken> primeroVAR_DECL= new ArrayList<>();
    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);

        //Se agregan los elementos del conjunto primero de EXPRESSION
        primeroEXPRESSION.add(TipoToken.BANG);
        primeroEXPRESSION.add(TipoToken.MINUS);
        primeroEXPRESSION.add(TipoToken.TRUE);
        primeroEXPRESSION.add(TipoToken.FALSE);
        primeroEXPRESSION.add(TipoToken.NULL);
        primeroEXPRESSION.add(TipoToken.NUMBER);
        primeroEXPRESSION.add(TipoToken.STRING);
        primeroEXPRESSION.add(TipoToken.IDENTIFIER);
        primeroEXPRESSION.add(TipoToken.LEFT_PAREN);

        //Se agregan los elementos del conjunto primero de STATEMENT
        primeroSTATEMENT.add(TipoToken.BANG);
        primeroSTATEMENT.add(TipoToken.MINUS);
        primeroSTATEMENT.add(TipoToken.TRUE);
        primeroSTATEMENT.add(TipoToken.FALSE);
        primeroSTATEMENT.add(TipoToken.NULL);
        primeroSTATEMENT.add(TipoToken.NUMBER);
        primeroSTATEMENT.add(TipoToken.STRING);
        primeroSTATEMENT.add(TipoToken.IDENTIFIER);
        primeroSTATEMENT.add(TipoToken.LEFT_PAREN);
        primeroSTATEMENT.add(TipoToken.FOR);
        primeroSTATEMENT.add(TipoToken.IF);
        primeroSTATEMENT.add(TipoToken.PRINT);
        primeroSTATEMENT.add(TipoToken.RETURN);
        primeroSTATEMENT.add(TipoToken.WHILE);
        primeroSTATEMENT.add(TipoToken.LEFT_BRACE);
        //Se agregan los elementos del conjunto primero de EXPR_STMT
        primeroEXPR_STMT.add(TipoToken.BANG);
        primeroEXPR_STMT.add(TipoToken.MINUS);
        primeroEXPR_STMT.add(TipoToken.TRUE);
        primeroEXPR_STMT.add(TipoToken.FALSE);
        primeroEXPR_STMT.add(TipoToken.NULL);
        primeroEXPR_STMT.add(TipoToken.NUMBER);
        primeroEXPR_STMT.add(TipoToken.STRING);
        primeroEXPR_STMT.add(TipoToken.IDENTIFIER);
        primeroEXPR_STMT.add(TipoToken.LEFT_PAREN);
        //Se agregan los elemntos del conjunto primero de FOR_STMT
        primeroFOR_STMT.add(TipoToken.FOR);
        //Se agregan los elementos del conjunto primero de IF_STMT
        primeroIF_STMT.add(TipoToken.IF);
        //Se agregan los elementos del conjunto primero de PRINT_STAMENT
        primeroPRINT_STMT.add(TipoToken.PRINT);
        //Se agregan los elementos del conjunto primero de RETURN_STAMENT
        primeroRETURN_STMT.add(TipoToken.RETURN);
        //Se agregan los elementos del conjunto primero de WHILE_STAMENT
        primeroWHILE_STMT.add(TipoToken.WHILE);
        //Se agregan los elementos del conjunto primero de BLOCK_STAMENT
        primeroBLOCK_STMT.add(TipoToken.LEFT_PAREN);
        //Se agregan los elementos del conjunto primero de VAR_DECL
        primeroVAR_DECL.add(TipoToken.VAR);
     
    }

    @Override
    public boolean parse() {
        PROGRAM();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("La sintaxis es correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    // PROGRAM -> DECLARATION
    private void PROGRAM(){
            DECLARATION();
    }
    
    /************************************************************************
     *                            Declaraciones
    ************************************************************************/

    private void DECLARATION(){
        if(hayErrores) //Termina el 
            return;

        /*
         * Dentro de cada if se compara con los elementos del conjunto primero 
         * de cada uno de los no terminales
         */

        // DECLARATION -> FUN_DECL DECLARATION
        else if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            DECLARATION();
        }
        // DECLARATION -> VAR_DECL DECLARATION
        else if (preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
            DECLARATION();
        }
        // DECLARATION -> STATEMENT DECLARATION
        else if (primeroSTATEMENT.contains(preanalisis.tipo)){
            STATEMENT();
            DECLARATION();
        }

        /*
        * La producción DECLARATION -> Ɛ no se incluye en codigo, debido a esto, 
        * si el tipo de preanalisis no coincide con ninguno de los tipos de los if
        * No se conciderará como error.
        */

    }
    
    // FUN_DECL -> fun FUNCTION
    private void FUN_DECL(){
        if(hayErrores)
            return;
        
        /*
         * En esta función, a diferencia de la funcion anterior, primero 
         * se compara que el elemento del preanalisis sea FUN, ya que si 
         * no coincide se trata de un error. Las producciones de este 
         * no terminal no contemplan Ɛ, por lo que si se recibe un terminal
         * distinto será un error.
         */
        
        match(TipoToken.FUN);
        if(hayErrores){
            System.out.println("Se esperaba una FUNCION");
        }
        FUNCTION();
    }

    // VAR_DECL -> var id VAR_INIT ;
    private void VAR_DECL(){
        if(hayErrores)
            return;
        
        /*
         * En esta función primero se compara que el elemento del preanalisis 
         * sea VAR, ya que si no coincide se trata de un error. Las producciones 
         * de este no terminal no contemplan Ɛ, por lo que si se recibe un terminal
         * distinto será un error; es por ello que EL PRIMER IF-ELSE ES NECESARIO.
         * 
         * El if-else dentro del primero se usa para detallar el error. No es 
         * necesario seguir esta estructura, en seguida se muestra otra forma 
         * que se puede usar para detallar el error.
         */
        
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            if(preanalisis.tipo == TipoToken.IDENTIFIER){
                match(TipoToken.IDENTIFIER);
                VAR_INIT();

                /*
                 * Esta es otra forma de manejar los errores.
                 * Despues de usar la función match() se verifica si hay errores, si
                 * los hay se muestra el mensaje de error y se usa "return".
                 * Usar "return" es necesario para evitar que  
                 */
                match(TipoToken.SEMICOLON);
                if(hayErrores){
                    System.out.println("Se esperaba ';'");
                    return;
                }
            }
            else{
                hayErrores = true;
                System.out.println("Se esperaba un IDENTIFICADOR luego de VAR.");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba una VARIABLE");
        }
    }

    // VAR_INIT -> = EXPRESSION | Ɛ
    private void VAR_INIT(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }

        /*
        * La producción DECLARATION -> Ɛ no se incluye en codigo, debido a esto, 
        * si el tipo de preanalisis no coincide con ninguno de los tipos de los if
        * No se conciderará como error.
        */
        
    }
    
    /************************************************************************
     *                              Sentencias
    ************************************************************************/     
    //SATAMENT -> EXPR_STMT
             // -> FOR_STMT
             // -> IF_STMT
             // -> PRINT_STMT
             // -> RETURN_STMT
             // -> WHILE_STMT
             // -> BLOCK
    private void STATEMENT(){
        if (hayErrores) {
            return; 
        }
        if (primeroEXPR_STMT.contains(preanalisis.tipo)) {
            EXPR_STMT();
        } 
        else if (primeroFOR_STMT.contains(preanalisis.tipo)) {
            FOR_STMT();
        } 
        else if (primeroIF_STMT.contains(preanalisis.tipo)){
            IF_STMT();
        }
        else if (primeroPRINT_STMT.contains(preanalisis.tipo)) {
            PRINT_STMT();
        } 
        else if (primeroRETURN_STMT.contains(preanalisis.tipo)){
            RETURN_STMT();
        }
        else if (primeroWHILE_STMT.contains(preanalisis.tipo)) {
            WHILE_STMT();
        } 
        else if (primeroBLOCK_STMT.contains(preanalisis.tipo)){
            BLOCK();
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba una SENTENCIA");
        }
    }
    //EXPR_STMT -> EXPRESSION ;
    private void EXPR_STMT(){
        if (hayErrores) {
            return;
        }
        else if (primeroEXPR_STMT.contains(preanalisis.tipo)) {
            EXPRESSION();
            if (preanalisis.tipo==TipoToken.SEMICOLON) {
                match(TipoToken.SEMICOLON);
            } else {
                hayErrores=true;
                System.out.println("Se esperaba un ;");
            }
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba un EXPRESION");
        }
        
    }
    //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private void FOR_STMT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.FOR) {
            match(TipoToken.FOR);
            if (preanalisis.tipo==TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                FOR_STMT_1();
                FOR_STMT_2();
                FOR_STMT_3();
                if (preanalisis.tipo==TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                }
                else{
                    hayErrores=true;
                    System.out.println("Se esperaba un RIGHT PAREN");
                }
            }
            else{
                hayErrores=true;
                System.out.println("Se esperaba un LEFT PAREN");
            }
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un FOR");
        }
    }
    // FOR_STMT_1 -> VAR_DECL
    //            -> EXPR_STMT
    //            -> ;
    private void FOR_STMT_1(){
        if (hayErrores) {
            return;
        }
        if (primeroVAR_DECL.contains(preanalisis.tipo)) {
            VAR_DECL();
        }
        else if (primeroEXPR_STMT.contains(preanalisis.tipo)) {
            EXPR_STMT();
        }
        else if(preanalisis.tipo==TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba una PRIMERA SENTENCIA FOR");
        }
    }
//     FOR_STMT_2 -> EXPRESSION;
               // -> ;
    private void FOR_STMT_2(){
        if (hayErrores) {
            return;
        }
        else if (primeroEXPRESSION.contains(preanalisis.tipo)) {
            EXPRESSION();
        }
        else if(preanalisis.tipo==TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba una SEGUNDA SENTENCIA FOR");
        }
    }

    // FOR_STMT_3 -> EXPRESSION
    //            -> Ɛ
    private void FOR_STMT_3(){
        if (hayErrores) {
            return;
        }
        else if (primeroEXPRESSION.contains(preanalisis.tipo)) {
            EXPRESSION();
        }
    }
    //IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMENT
    private void IF_STMT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.IF) {
            match(TipoToken.IF);
            if (preanalisis.tipo==TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();
                if (preanalisis.tipo==TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                    ELSE_STATEMENT();
                }
                else{
                    hayErrores=true;
                    System.out.println("Se esperaba un RIGHT PAREN");
                }
            }
            else{
                hayErrores=true;
                System.out.println("Se esperaba un LEFT PAREN");
            }
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un IF");
        }
    }
    // ELSE_STATEMENT -> else STATEMENT
    //                -> Ɛ
    private void ELSE_STATEMENT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.ELSE) {
            match(TipoToken.ELSE);
            STATEMENT();
        }
    }
    //PRINT_STMT -> print EXPRESSION ;      
    private void PRINT_STMT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.PRINT) {
            match(TipoToken.PRINT);
            EXPRESSION();
            if (preanalisis.tipo==TipoToken.SEMICOLON) {
                match(TipoToken.SEMICOLON);
            } else {
                hayErrores=true;
                System.out.println("Se esperaba un ;");
            }
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un PRINT");
        }
    }
    //RETURN_STMT -> return RETURN_EXP_OPC ;
    private void RETURN_STMT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.RETURN) {
            match(TipoToken.RETURN);
            RETURN_EXP_OPC();
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un PRINT");
        }
    }
    //RETURN_EXP_OPC -> EXPRESSION
    //               -> Ɛ
    private void RETURN_EXP_OPC(){
        if (hayErrores) {
            return;
        }
        if (primeroEXPRESSION.contains(preanalisis.tipo)) {
            EXPRESSION();
        }
    }
    //WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private void WHILE_STMT(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.WHILE) {
            match(TipoToken.WHILE);
            if (preanalisis.tipo==TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();
                if (preanalisis.tipo==TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                }
                else{
                    hayErrores=true;
                    System.out.println("Se esperaba un RIGHT PAREN");
                }
            }
            else{
                hayErrores=true;
                System.out.println("Se esperaba un LEFT PAREN");
            }
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un WHILE");
        }
    }

    //BLOCK -> { DECLARATION }
    private void BLOCK(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo==TipoToken.LEFT_PAREN) {
            match(TipoToken.LEFT_PAREN);
            DECLARATION();
            if (preanalisis.tipo==TipoToken.RIGHT_PAREN) {
                match(TipoToken.RIGHT_PAREN);
            } else {
                hayErrores=true;
                System.out.println("Se esperaba un RIGHT PAREN"); 
            }
        } else {
            hayErrores=true;
            System.out.println("Se esperaba un LEFT PAREN");
        }
    }



    /************************************************************************
     *                              Expresiones
    ************************************************************************/
    // EXPRESSION -> ASSIGNMENT
    private void EXPRESSION(){
        if (hayErrores)
            return;
        ASSIGNMENT();
    }

    // ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private void ASSIGNMENT(){
        if (hayErrores)
            return;
        LOGIC_OR();
        ASSIGNMENT_OPC();
    }

    // ASSIGNMENT_OPC -> = EXPRESSION | Ɛ
    private void ASSIGNMENT_OPC(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
    }

    // LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private void LOGIC_OR(){
        if (hayErrores)
            return;
        LOGIC_AND();
        LOGIC_OR_2();
    }
    // LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 | Ɛ
    private void LOGIC_OR_2(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.OR){
            match(TipoToken.OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    // LOGIC_AND -> EQUALITY LOGIC_AND_2
    private void LOGIC_AND(){
        if (hayErrores)
            return;
        EQUALITY();
        LOGIC_AND_2();
    }

    // LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | Ɛ
    private void LOGIC_AND_2(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    // EQUALITY -> COMPARISON EQUALITY_2
    private void EQUALITY(){
        if (hayErrores)
            return;
        COMPARISON();
        EQUALITY_2();
    }

    // EQUALITY_2 -> != COMPARISON EQUALITY_2  |  == COMPARISON EQUALITY_2  |  Ɛ
    private void EQUALITY_2(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.tipo == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
    }

    // COMPARISON -> TERM COMPARISON_2
    private void COMPARISON(){
        if (hayErrores) {
            return;
        }
        TERM();
        COMPARISON_2();
    }

    // COMPARISON_2 -> > TERM COMPARISON_2
    //              -> >= TERM COMPARISON_2
    //              -> < TERM COMPARISON_2
    //              -> <= TERM COMPARISON_2
    //              -> Ɛ
    private void COMPARISON_2(){
        if (hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.GREATER) {
            match(TipoToken.GREATER);
            TERM();
            COMPARISON_2();
        } 
        else 
        if (preanalisis.tipo == TipoToken.GREATER_EQUAL) {
            match(TipoToken.GREATER_EQUAL);
            TERM();
            COMPARISON_2();
        }
        else 
        if (preanalisis.tipo == TipoToken.LESS) {
            match(TipoToken.LESS);
            TERM();
            COMPARISON_2();
        }
        else 
        if (preanalisis.tipo == TipoToken.LESS_EQUAL) {
            match(TipoToken.LESS_EQUAL);
            TERM();
            COMPARISON_2();
        }
    }

    // TERM -> FACTOR TERM_2
    private void TERM(){
        if (hayErrores)
            return;
        FACTOR();
        TERM_2();
    }

    // TERM_2 -> - FACTOR TERM_2  |  + FACTOR TERM_2  |  Ɛ
    private void TERM_2(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            FACTOR();
            TERM_2();
        }
        else 
        if(preanalisis.tipo == TipoToken.PLUS){
            match(TipoToken.PLUS);
            FACTOR();
            TERM_2();
        }
    }

    // FACTOR -> UNARY FACTOR_2
    private void FACTOR(){
        if (hayErrores) 
            return;
        UNARY();
        FACTOR_2();
    }

    // FACTOR_2 -> / UNARY FACTOR_2  |  * UNARY FACTOR_2  |  Ɛ
    private void FACTOR_2(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.SLASH){
            match(TipoToken.SLASH);
            UNARY();
            FACTOR_2();
        }
        else 
        if(preanalisis.tipo == TipoToken.STAR){
            match(TipoToken.STAR);
            UNARY();
            FACTOR_2();
        }
    }

    // UNARY -> ! UNARY  |  - UNARY  |  CALL
    private void UNARY(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.BANG){
            match(TipoToken.BANG);
            UNARY();
        }
        else 
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            UNARY();
        }
        else
            CALL();
    }

    // CALL -> PRIMARY CALL_2
    private void CALL(){
        if (hayErrores)
            return;
        PRIMARY();
        CALL_2();
    }

    // CALL_2 -> ( ARGUMENTS_OPC ) CALL_2  |  Ɛ
    private void CALL_2(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            ARGUMENTS_OPC();
            if (preanalisis.tipo==TipoToken.RIGHT_PAREN) {
                match(TipoToken.RIGHT_PAREN);
            } else {
                System.out.println("Se esperaba RIGHT PAREN");   
            }
            CALL_2();
        }
    }

    // PRIMARY -> true
    //         -> false
    //         -> null
    //         -> number
    //         -> string
    //         -> id
    //         -> ( EXPRESSION )
    private void PRIMARY(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.TRUE)
            match(TipoToken.TRUE);
        else 
        if(preanalisis.tipo == TipoToken.FALSE)
            match(TipoToken.FALSE);
        else 
        if(preanalisis.tipo == TipoToken.NULL)
            match(TipoToken.NULL);
        else 
        if(preanalisis.tipo == TipoToken.NUMBER)
            match(TipoToken.NUMBER);
        else 
        if(preanalisis.tipo == TipoToken.STRING)
            match(TipoToken.STRING);
        else 
        if(preanalisis.tipo == TipoToken.IDENTIFIER)
            match(TipoToken.IDENTIFIER);
        else 
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            if (preanalisis.tipo==TipoToken.RIGHT_PAREN) {
                match(TipoToken.RIGHT_PAREN);
            } else {
                System.out.println("Se esperaba RIGHT PAREN");   
            }
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '(', un IDENTIFICADOR, una STRING, 'true', 'false', 'null', o un NUMERO");
        }
    }
    
    /************************************************************************
     *                                 Otras
    ************************************************************************/

    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private void FUNCTION(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            if(hayErrores){
                System.out.println("Se esperaba un IDENTIFICADOR");
                return;
            }
            match(TipoToken.LEFT_PAREN);
            if(hayErrores){
                System.out.println("Se esperaba '(''");
                return;
            }
            PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            if (hayErrores) {
                System.out.println("Se esperaba ')''");
                return;
            }
            BLOCK();
        }
    }

    // PARAMETERS_OPC -> PARAMETERS | Ɛ
    private void PARAMETERS_OPC(){
        if(hayErrores)
            return;

        PARAMETERS();
    }

    // PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(){
        if(hayErrores)
            return;

        match(TipoToken.IDENTIFIER);
        if(hayErrores){
            System.out.println("Se esperaba un IDENTIFICADOR");
            return;
        }
        PARAMETERS_2();
    }

    //PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private void PARAMETERS_2(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            if (hayErrores) {
                System.out.println("Se esperaba un IDENTIFICADOR");
                return;
            }
            PARAMETERS_2();
        }
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS_OPC(){
        if(hayErrores)
            return;
        
        /*
        
         * Se verifica si el tipo de preanalisis pertenece al conjunto primero de EXPRESSION, 
         * de lo contrario se tomara como la producción Ɛ.
         * Esto se podria hacer comparando de forma directa dentro del if el tipo de preanalisis 
         * con los elementos del conjunto primero de EXPRESSION pero de esta forma el codigo se 
         * vuelve mas legible.
         */
        if(primeroEXPRESSION.contains(preanalisis.tipo)){
            EXPRESSION();
            ARGUMENTS();
        }
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS(){
        if(hayErrores)
            return;
        
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            EXPRESSION();
            ARGUMENTS();
        }
    }


    /************************************************************************
     *                                  Match
    ************************************************************************/
    private void match(TipoToken tt){
            if(preanalisis.tipo == tt){
                i++;
                preanalisis = tokens.get(i);
            }
            else{
                hayErrores = true;
            }
        }
}