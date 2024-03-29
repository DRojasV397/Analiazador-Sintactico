import java.util.ArrayList;
import java.util.List;


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
    private List<Statement> sentenciasProg = new ArrayList<>();

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
        primeroBLOCK_STMT.add(TipoToken.LEFT_BRACE);
        //Se agregan los elementos del conjunto primero de VAR_DECL
        primeroVAR_DECL.add(TipoToken.VAR);
    
    }

    @Override
    public boolean parse() {
        PROGRAM();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("La sintaxis es correcta");
            return true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    public List<Statement> obtenerSentencias(){
        return sentenciasProg;
    }

    // PROGRAM -> DECLARATION
    private void PROGRAM(){
            DECLARATION(sentenciasProg);
    }
    
    /************************************************************************
     *                            Declaraciones
    ************************************************************************/

    private void DECLARATION(List<Statement> sentencias){
        Statement sentencia;

        // DECLARATION -> FUN_DECL DECLARATION
        if(preanalisis.tipo == TipoToken.FUN){
            sentencia = FUN_DECL();
            sentencias.add(sentencia);
            DECLARATION(sentencias);
        }
        // DECLARATION -> VAR_DECL DECLARATION
        else if (preanalisis.tipo == TipoToken.VAR){
            sentencia = VAR_DECL();
            sentencias.add(sentencia);
            DECLARATION(sentencias);
        }
        // DECLARATION -> STATEMENT DECLARATION
        else if (primeroSTATEMENT.contains(preanalisis.tipo)){
            sentencia = STATEMENT();
            sentencias.add(sentencia);
            DECLARATION(sentencias);
        }
    }
    
    // FUN_DECL -> fun FUNCTION
    private StmtFunction FUN_DECL(){
        match(TipoToken.FUN);
        buscarErrores("Se esperaba una FUNCION");
        return FUNCTION();
    }

    // VAR_DECL -> var id VAR_INIT ;
    private StmtVar VAR_DECL(){
        match(TipoToken.VAR);
        buscarErrores("Se esperaba VAR.");
        match(TipoToken.IDENTIFIER);
        buscarErrores("Se esperaba un IDENTIFICADOR luego de VAR.");
        Token identificador = previous();
        Expression inicializacion = VAR_INIT();
        match(TipoToken.SEMICOLON);
        buscarErrores("Se esperaba ';'");
        return new StmtVar(identificador, inicializacion);        
    }

    // VAR_INIT -> = EXPRESSION | Ɛ
    private Expression VAR_INIT(){
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            return EXPRESSION();
        }
        return null;
    }
    
    /************************************************************************
     *                              Sentencias
    ************************************************************************/     
    //STATEMENT -> EXPR_STMT
             // -> FOR_STMT
             // -> IF_STMT
             // -> PRINT_STMT
             // -> RETURN_STMT
             // -> WHILE_STMT
             // -> BLOCK
    private Statement STATEMENT(){
        if (primeroEXPR_STMT.contains(preanalisis.tipo))
            return EXPR_STMT();
        else if (primeroFOR_STMT.contains(preanalisis.tipo)) 
            return FOR_STMT();
        else if (primeroIF_STMT.contains(preanalisis.tipo))
            return IF_STMT();
        else if (primeroPRINT_STMT.contains(preanalisis.tipo)) 
            return PRINT_STMT();
        else if (primeroRETURN_STMT.contains(preanalisis.tipo))
            return RETURN_STMT();
        else if (primeroWHILE_STMT.contains(preanalisis.tipo))
            return WHILE_STMT();
        else if (primeroBLOCK_STMT.contains(preanalisis.tipo))
            return BLOCK();
        else{
            hayErrores = true;
            buscarErrores("Se esperaba una SENTENCIA");
            return null;
        }
    }

    //EXPR_STMT -> EXPRESSION ;
    private StmtExpression EXPR_STMT(){
        StmtExpression expresion = new StmtExpression(EXPRESSION());
        match(TipoToken.SEMICOLON);
        buscarErrores("Se esperaba un ;");
        return expresion;
    }


    //FOR_STMT_1
    // while (FOR_STMT_2){
        // STATEMENT
        // FOR_STMT_3
    //}

    //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT(){
        match(TipoToken.FOR);
        buscarErrores("Se esperaba un FOR");

        match(TipoToken.LEFT_PAREN);
        buscarErrores("Se esperaba un LEFT PAREN");

        Statement for1 = FOR_STMT_1();
        StmtExpression for2 = FOR_STMT_2();
        StmtExpression for3 = FOR_STMT_3();

        match(TipoToken.RIGHT_PAREN);
        buscarErrores("Se esperaba un RIGTH PAREN");

        Statement stm = STATEMENT();

        List<Statement> body= new ArrayList<Statement>();
        List<Statement> incializacion= new ArrayList<Statement>();
        body.add(stm);
        if(for3 != null) body.add(for3);
        StmtBlock bodyBlock = new StmtBlock(body);
        StmtLoop stm_while;
        stm_while = (for2 == null) ? new StmtLoop(new ExprLiteral(true), bodyBlock) :  new StmtLoop(for2.expression, bodyBlock);
        if(for1 != null) incializacion.add(for1);
        incializacion.add(stm_while);
        StmtBlock stm_for = new StmtBlock(incializacion);
        return stm_for;
    }

    // FOR_STMT_1 -> VAR_DECL
    //            -> EXPR_STMT
    //            -> ;
    private Statement FOR_STMT_1(){
        if (primeroVAR_DECL.contains(preanalisis.tipo)) {
            StmtVar declaracion = VAR_DECL();
            return declaracion;
        }
        else if (primeroEXPR_STMT.contains(preanalisis.tipo)) {
            StmtExpression exp = EXPR_STMT();
            return exp;
        }
        else if(preanalisis.tipo==TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return null;
        }
        else {
            hayErrores=true;
            buscarErrores("Se esperaba una PRIMERA SENTENCIA FOR");
            return null;
        }
    }

    //FOR_STMT_2 -> EXPRESSION;
    //           -> ;
    private StmtExpression FOR_STMT_2(){
        if (primeroEXPRESSION.contains(preanalisis.tipo)) {
            StmtExpression exp = new StmtExpression(EXPRESSION());
            match(TipoToken.SEMICOLON);
            buscarErrores("Se esperaba un ;");
            return exp;
        }
        else if(preanalisis.tipo==TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return null;
        }
        else {
            hayErrores=true;
            buscarErrores("Se esperaba una SEGUNDA SENTENCIA FOR");
            return null;
        }
    }

    // FOR_STMT_3 -> EXPRESSION
    //            -> Ɛ
    private StmtExpression FOR_STMT_3(){
        if (primeroEXPRESSION.contains(preanalisis.tipo)) {
            StmtExpression exp = new StmtExpression(EXPRESSION());
            return exp;
        }
        return null;
    }

    //IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMENT
    private StmtIf IF_STMT(){
        match(TipoToken.IF);
        buscarErrores("Se esperaba un IF");
        match(TipoToken.LEFT_PAREN);
        buscarErrores("Se esperaba un LEFT PAREN");
        StmtExpression condition= new StmtExpression(EXPRESSION());
        match(TipoToken.RIGHT_PAREN);
        buscarErrores("Se esperaba un RIGHT PAREN");
        Statement thenBranch = STATEMENT();
        Statement elseBranch = ELSE_STATEMENT();
        return new StmtIf(condition.expression,thenBranch,elseBranch);
    }

    // ELSE_STATEMENT -> else STATEMENT
    //                -> Ɛ
    private Statement ELSE_STATEMENT(){
        if (hayErrores) {
            return null;
        }
        if (preanalisis.tipo==TipoToken.ELSE) {
            match(TipoToken.ELSE);
            Statement stmnt = STATEMENT();
            return stmnt;
        }
        return null;
    }

    //PRINT_STMT -> print EXPRESSION ;      
    private Statement PRINT_STMT(){
        match(TipoToken.PRINT);
        buscarErrores("Se esperaba un PRINT");
        StmtExpression expression = new StmtExpression(EXPRESSION());
        match(TipoToken.SEMICOLON);
        buscarErrores("Se esperaba un ;");
        return new StmtPrint(expression.expression);
    }

    //RETURN_STMT -> return RETURN_EXP_OPC ;
    private Statement RETURN_STMT(){
        match(TipoToken.RETURN);
        buscarErrores("Se esperaba un RETURN");
        Statement opcional = RETURN_EXP_OPC();
        match(TipoToken.SEMICOLON);
        buscarErrores("Se esperaba un ;");
        return opcional;
    }

    //RETURN_EXP_OPC -> EXPRESSION
    //               -> Ɛ
    private Statement RETURN_EXP_OPC(){
        if (primeroEXPRESSION.contains(preanalisis.tipo)) {
            StmtExpression value = new StmtExpression(EXPRESSION());
            return new StmtReturn(value.expression);
        }
        return null;
    }
    
    //WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private Statement WHILE_STMT(){
        match(TipoToken.WHILE);
        buscarErrores("Se esperaba un WHILE");
        match(TipoToken.LEFT_PAREN);
        buscarErrores("Se esperaba un LEFT PAREN");
        Expression condicon = EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        buscarErrores("Se esperaba un RIGHT PAREN");
        Statement body = STATEMENT();
        return new StmtLoop(condicon, body);            
    }

    //BLOCK -> { DECLARATION }
    private StmtBlock BLOCK(){
        match(TipoToken.LEFT_BRACE);
        buscarErrores("Se esperaba un LEFT BRACE");
        List<Statement> statements = new ArrayList<Statement>();
        DECLARATION(statements);
        match(TipoToken.RIGHT_BRACE);
        buscarErrores("Se esperaba un RIGHT BRACE");
        return new StmtBlock(statements);
    }



    /************************************************************************
     *                              Expresiones
    ************************************************************************/
    // EXPRESSION -> ASSIGNMENT
    private Expression EXPRESSION(){
        Expression expresion = ASSIGNMENT();
        return expresion;
    }

    // ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private Expression ASSIGNMENT(){
        Expression expresion = LOGIC_OR();
        Expression aux = ASSIGNMENT_OPC(expresion);
        return aux;
    }

    // ASSIGNMENT_OPC -> = EXPRESSION | Ɛ
    private Expression ASSIGNMENT_OPC(Expression expresion){
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Expression valor = EXPRESSION(); 
            Token aux1 = ((ExprVariable) expresion).name;
            Expression aux2 = new ExprAssign(aux1, valor);
            return aux2;
        }
        else   
            return expresion;
    }

    // LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private Expression LOGIC_OR(){
        Expression expresion = LOGIC_AND();
        Expression aux = LOGIC_OR_2(expresion);
        return aux;
    }
    
    // LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 | Ɛ
    private Expression LOGIC_OR_2(Expression expresion1){
        if(preanalisis.tipo == TipoToken.OR){
            match(TipoToken.OR);
            Token operador = previous();
            Expression expresion2 = LOGIC_AND();
            Expression aux1 = new ExprLogical(expresion1, operador, expresion2);
            Expression aux2 = LOGIC_OR_2(aux1);
            return aux2;
        }
        else
            return expresion1;
    }

    // LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND(){
        Expression expresion = EQUALITY();
        Expression aux = LOGIC_AND_2(expresion);
        return aux;
    }

    // LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | Ɛ
    private Expression LOGIC_AND_2(Expression expresion1){
        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            Token operador = previous();
            Expression expresion2 = EQUALITY();
            Expression aux1 = new ExprLogical(expresion1, operador, expresion2);
            Expression aux2 = LOGIC_AND_2(aux1);
            return aux2;
        }
        else
            return expresion1;
    }

    // EQUALITY -> COMPARISON EQUALITY_2
    private Expression EQUALITY(){
        Expression expresion = COMPARISON();
        Expression aux = EQUALITY_2(expresion);
        return aux;
    }

    // EQUALITY_2 -> != COMPARISON EQUALITY_2  |  == COMPARISON EQUALITY_2  |  Ɛ
    private Expression EQUALITY_2(Expression expresion1){
        if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            Token operador_1 = previous();
            Expression expresion2_1 = COMPARISON();
            Expression aux1_1 = new ExprBinary(expresion1, operador_1, expresion2_1);
            Expression aux2_1 = EQUALITY_2(aux1_1);
            return aux2_1;
        }
        else if(preanalisis.tipo == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            Token operador_2 = previous();
            Expression expresion2_2 = COMPARISON();
            Expression aux1_2 = new ExprBinary(expresion1, operador_2, expresion2_2);
            Expression aux2_2 = EQUALITY_2(aux1_2);
            return aux2_2;
        }
        return expresion1;
    }

    // COMPARISON -> TERM COMPARISON_2
    private Expression COMPARISON(){
        Expression expresion = TERM();
        Expression aux = COMPARISON_2(expresion);
        return aux;
    }

    // COMPARISON_2 -> > TERM COMPARISON_2
    //              -> >= TERM COMPARISON_2
    //              -> < TERM COMPARISON_2
    //              -> <= TERM COMPARISON_2
    //              -> Ɛ
    private Expression COMPARISON_2(Expression expresion1){
        if (preanalisis.tipo == TipoToken.GREATER) {
            match(TipoToken.GREATER);
            Token operador_1 = previous();
            Expression expresion2_1 = TERM();
            Expression aux1_1 = new ExprBinary(expresion1, operador_1, expresion2_1);
            Expression aux2_1 = COMPARISON_2(aux1_1);
            return aux2_1;
        } 
        else 
        if (preanalisis.tipo == TipoToken.GREATER_EQUAL) {
            match(TipoToken.GREATER_EQUAL);
            Token operador_2 = previous();
            Expression expresion2_2 = TERM();
            Expression aux1_2 = new ExprBinary(expresion1, operador_2, expresion2_2);
            Expression aux2_2 = COMPARISON_2(aux1_2);
            return aux2_2;
        }
        else 
        if (preanalisis.tipo == TipoToken.LESS) {
            match(TipoToken.LESS);
            Token operador_3 = previous();
            Expression expresion2_3 = TERM();
            Expression aux1_3 = new ExprBinary(expresion1, operador_3, expresion2_3);
            Expression aux2_3 = COMPARISON_2(aux1_3);
            return aux2_3;
        }
        else 
        if (preanalisis.tipo == TipoToken.LESS_EQUAL) {
            match(TipoToken.LESS_EQUAL);
            Token operador_4 = previous();
            Expression expresion2_4 = TERM();
            Expression aux1_4 = new ExprBinary(expresion1, operador_4, expresion2_4);
            Expression aux2_4 = COMPARISON_2(aux1_4);
            return aux2_4;
        }
        return expresion1;
    }

    // TERM -> FACTOR TERM_2
    private Expression TERM(){
        Expression expresion = FACTOR();
        Expression aux = TERM_2(expresion);
        return aux;
    }

    // TERM_2 -> - FACTOR TERM_2  |  + FACTOR TERM_2  |  Ɛ
    private Expression TERM_2(Expression expresion1){
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token operador = previous();
            Expression expresion2_1 = FACTOR();
            Expression aux1_1 = new ExprBinary(expresion1, operador, expresion2_1);
            Expression aux2_1 = TERM_2(aux1_1);
            return aux2_1;
        }
        else 
        if(preanalisis.tipo == TipoToken.PLUS){
            match(TipoToken.PLUS);
            Token operador = previous();
            Expression expresion2_2 = FACTOR();
            Expression aux1_2 = new ExprBinary(expresion1, operador, expresion2_2);
            Expression aux2_2 = TERM_2(aux1_2);
            return aux2_2;
        }
        else
            return expresion1;
    }

    // FACTOR -> UNARY FACTOR_2
    private Expression FACTOR(){
        Expression expresion = UNARY();
        Expression aux = FACTOR_2(expresion);
        return aux;
    }

    // FACTOR_2 -> / UNARY FACTOR_2  |  * UNARY FACTOR_2  |  Ɛ
    private Expression FACTOR_2(Expression expresion1){
        if(preanalisis.tipo == TipoToken.SLASH){
            match(TipoToken.SLASH);
            Token operador = previous();
            Expression expresion2 = UNARY();
            ExprBinary aux1 = new ExprBinary(expresion1, operador, expresion2);
            Expression aux2 = FACTOR_2(aux1);
            return aux2;
        }
        else 
        if(preanalisis.tipo == TipoToken.STAR){
            match(TipoToken.STAR);
            Token operador1 = previous();
            Expression expresion2_2 = UNARY();
            ExprBinary aux1_2 = new ExprBinary(expresion1, operador1, expresion2_2);
            Expression aux2_2 = FACTOR_2(aux1_2);
            return aux2_2;
        }
        else
            return expresion1;
    }

    // UNARY -> ! UNARY  |  - UNARY  |  CALL
    private Expression UNARY(){
        if(preanalisis.tipo == TipoToken.BANG){
            match(TipoToken.BANG);
            Token operador = previous();
            Expression expresion = UNARY();
            Expression aux = new ExprUnary(operador, expresion);
            return aux;
        }
        else 
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token operador1 = previous();
            Expression expresion1 = UNARY();
            Expression aux1 = new ExprUnary(operador1, expresion1);
            return aux1;
        }
        else{
            Expression aux2 = CALL();
            return aux2;
        }
    }

    // CALL -> PRIMARY CALL_2
    private Expression CALL(){
        Expression expresion = PRIMARY();
        Expression aux = CALL_2(expresion);
        return aux;
    }

    // CALL_2 -> ( ARGUMENTS_OPC ) CALL_2  |  Ɛ
    private Expression CALL_2(Expression expresion){
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            List<Expression> argumentos = ARGUMENTS_OPC();
            match(TipoToken.RIGHT_PAREN);
            buscarErrores("Se esperaba un RIGHT PAREN");
            ExprCallFunction aux = new ExprCallFunction(expresion, argumentos); 
            Expression aux2 = CALL_2(aux);
            return aux2;
        }
        else
            return expresion;
    }

    // PRIMARY -> true
    //         -> false
    //         -> null
    //         -> number
    //         -> string
    //         -> id
    //         -> ( EXPRESSION )
    private Expression PRIMARY(){
        Expression expresion;
        if(preanalisis.tipo == TipoToken.TRUE){
            match(TipoToken.TRUE);
            expresion = new ExprLiteral(true);
            return expresion;
            }
        else 
        if(preanalisis.tipo == TipoToken.FALSE){
            match(TipoToken.FALSE);
            expresion = new ExprLiteral(true);
            return expresion;
            }
        else 
        if(preanalisis.tipo == TipoToken.NULL){
            match(TipoToken.NULL);
            expresion = new ExprLiteral(null);
            return expresion;
            }
        else 
        if(preanalisis.tipo == TipoToken.NUMBER){
            match(TipoToken.NUMBER);
            expresion = new ExprLiteral(previous().literal);
            return expresion;
            }
        else 
        if(preanalisis.tipo == TipoToken.STRING){
            match(TipoToken.STRING);
            expresion = new ExprLiteral(previous().literal);
            return expresion;
            }
        else 
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            expresion = new ExprVariable(previous());
            return expresion;
            }
        else 
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            expresion = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            buscarErrores("Se esperaba RIGHT PAREN");
            return new ExprGrouping(expresion);
        }
        else{
            hayErrores = true;
            buscarErrores("Se esperaba '(', un IDENTIFICADOR, una STRING, 'true', 'false', 'null', o un NUMERO");
            return null;
        }
    }
    
    /************************************************************************
     *                                 Otras
    ************************************************************************/

    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private StmtFunction FUNCTION(){
        match(TipoToken.IDENTIFIER);
        buscarErrores("Se esperaba un IDENTIFICADOR");

        Token identificador = previous();
        match(TipoToken.LEFT_PAREN);
        buscarErrores("Se esperaba '(''");

        List<Token> parametros = PARAMETERS_OPC();

        match(TipoToken.RIGHT_PAREN);
        buscarErrores("Se esperaba ')'");

        StmtBlock bloque = BLOCK();

        return new StmtFunction(identificador, parametros, bloque);
    }

    // PARAMETERS_OPC -> PARAMETERS | Ɛ
    private List<Token> PARAMETERS_OPC(){

        List<Token> parametros = new ArrayList<Token>();
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            parametros = PARAMETERS();
            return parametros;
        }
        return parametros;
    }

    // PARAMETERS -> id PARAMETERS_2
    private List<Token> PARAMETERS(){
        List<Token> parametros = new ArrayList<Token>();
        match(TipoToken.IDENTIFIER);
        buscarErrores("Se esperaba un IDENTIFICADOR");
        Token parametro = previous();
        parametros.add(parametro);
        PARAMETERS_2(parametros);
        return parametros;
    }

    //PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private void PARAMETERS_2(List<Token> parametros){
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            buscarErrores("Se esperaba un IDENTIFICADOR");
            Token parametro = previous();
            parametros.add(parametro);
            PARAMETERS_2(parametros);
        }
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private List<Expression> ARGUMENTS_OPC(){
        List<Expression> expresiones = new ArrayList<Expression>();
        if(primeroEXPRESSION.contains(preanalisis.tipo)){
            Expression expresion = EXPRESSION();
            expresiones.add(expresion);
            ARGUMENTS(expresiones);
            return expresiones;
        }
        else
            return expresiones;
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS(List<Expression> expresiones){
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            Expression expresion = EXPRESSION();
            expresiones.add(expresion);
            ARGUMENTS(expresiones);
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
    

    private void buscarErrores(String mensaje){
        if(hayErrores)
            error(previous().linea, String.valueOf(previous().posicion), mensaje);
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }

    static void error(int linea, String posicion, String mensaje){
        reportar(linea, posicion, mensaje);
    }

    public static void reportar(int linea, String posicion, String mensaje){
        throw new RuntimeException(
            "[linea " + linea + "] Error" + posicion + ": " + mensaje
        );
    }
}