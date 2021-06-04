%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "lexer.h"

#define MEM(size) ((char *)malloc( (size + 1) * sizeof(char)));
#define INDENT 4


char * get_indent(unsigned indent) {
    char *res = (char *)malloc(indent + 1);
    for (long i = 0; i < indent; i++) {
        res[i] = ' ';
    }
    res[indent] = '\0';
    return res;
}


%}

%define api.pure
%locations
%lex-param {yyscan_t scanner}
%parse-param {yyscan_t scanner}
%parse-param { long env[26]}

%union {
	char* ident;
	long num;
}

%token <ident> IDENT
%token <num> NUMBER
%token ASSIGN DOT
%token SQ_LBRACKET SQ_RBRACKET COMMA SEMICOLON COLON LPAREN RPAREN LBRACE RBRACE
%token FUN_SPEC
%token VAR_SPEC VAL_SPEC
%token CHAR_SPEC SHORT_SPEC INT_SPEC LONG_SPEC FLOAT_SPEC DOUBLE_SPEC STRING_SPEC 
%token PRIVATE_SPEC PROTECTED_SPEC PUBLIC_SPEC INTERNAL_SPEC
%token CLASS_SPEC CONSTRUCTOR_SPEC

%type <ident> start class_decl class_entry field_decl field_mods access_mod type fun_decl fun_params class_params ident_val constructor_decl

%{
int yylex(YYSTYPE * yylval_param, YYLTYPE * yylloc_param , yyscan_t scanner);
void yyerror(YYLTYPE *yylloc, yyscan_t scanner, long env[26], const char *msg);
%}

%%

start:
	class_decl add_indent class_entry remove_indent {
		printf("%s\n%s", $1, $3);
	}
	;

class_decl:
	access_mod CLASS_SPEC ident_val LBRACE {
		char* indent = get_indent(env[0]);
		$$ = MEM(strlen(indent) + strlen($1) + strlen("class") + strlen($3) + 1 + 2);
		sprintf($$, "%s%sclass %s{", indent, $1, $3);
		//free($1);
		free($3);
		free(indent);
	}
	|
	access_mod CLASS_SPEC ident_val LPAREN class_params RPAREN LBRACE {
		char* indent = get_indent(env[0]);
		if (strlen($5) > 0){
			$$ = MEM(2 * strlen(indent) + strlen($1) + strlen("class") + strlen($3) + strlen($5) + strlen("(){") + 2 + strlen("\n"));
			sprintf($$, "%s%sclass %s(\n%s%s) {", indent, $1, $3, $5, indent);
			//free($1);
			free($3);
			free($5);
			free(indent);
		} else {
			$$ = MEM(strlen(indent) + strlen($1) + strlen("class") + strlen($3) + 3 + 2 + 1);
			sprintf($$, "%s%sclass %s() {", indent, $1, $3);
			//free($1);
			free($3);
			free(indent);
		}
	}
	;

class_entry:
	field_decl class_entry {
		char* indent = get_indent(env[0]);
		$$ = MEM(strlen(indent) +strlen($1) + 1 + strlen($2));
		sprintf($$, "%s%s\n%s", indent, $1, $2);
		free($1);
		free($2);
		free(indent);
	}
	|
	constructor_decl class_entry {
		char* indent = get_indent(env[0]);
		$$ = MEM(strlen(indent) +strlen($1) + 1 + strlen($2));
		sprintf($$,"%s%s\n%s",indent,$1,$2);
		free($1);
		free($2);
		free(indent);
	}
	|
	fun_decl class_entry {
		char* indent = get_indent(env[0]);
		$$ = MEM(strlen(indent) +strlen($1) + 1 + strlen($2));
		sprintf($$, "%s%s\n%s", indent, $1, $2);
		free($1);
		free($2);
		free(indent);
	}
	|
	class_decl add_indent class_entry remove_indent class_entry {
		char* indent = get_indent(env[0] - INDENT);
		$$ = MEM(strlen(indent) + strlen($1) + strlen($3) + strlen($5) + 2 * strlen("\n"));
		sprintf($$, "%s%s\n%s\n%s", indent, $1, $3, $5);
		free($1);
		free($3);
		free($5);
		free(indent);
	}
	|
	RBRACE {
		char* indent = get_indent(env[0] - INDENT);
		$$ = MEM(strlen(indent) + strlen("}"));
		sprintf($$, "%s}", indent);
		free(indent);
	}
	;

field_decl:
	field_mods ident_val COLON type {
		$$ = MEM(strlen($1) + strlen($2) + strlen($4) + 1 + 1);
		sprintf($$, "%s%s: %s", $1, $2, $4);
		free($1);
		free($2);
		//free($4);
	}
	;

fun_decl:
	access_mod FUN_SPEC ident_val LPAREN fun_params RPAREN LBRACE RBRACE {
		if (strlen($5) > 0){
			$$ = MEM(strlen($1) + strlen("fun") + strlen($3) + strlen("(") + strlen($5) + strlen(")") + strlen("{") + strlen("}") + 1);
			sprintf($$, "%sfun %s(%s){}", $1, $3, $5);
			//free($1);
			free($3);
			free($5);
		} else {
			$$ = MEM(strlen($1) + strlen("fun") + strlen($3) + strlen("(") + strlen(")") + strlen("{") + strlen("}") + 1);
			sprintf($$, "%sfun %s(%s){}", $1, $3, $5);
			//free($1);
			free($3);
		}
	}
	;

constructor_decl:
	access_mod CONSTRUCTOR_SPEC LBRACE RBRACE {
		$$ = MEM(strlen($1) + strlen("constructor") + strlen("{") + strlen("}") + 1);
		sprintf($$, "%s constructor{}", $1);
		//free($1);
	}
	|
	access_mod CONSTRUCTOR_SPEC LPAREN fun_params RPAREN LBRACE RBRACE {
		if (strlen($4) > 0) {
			$$ = MEM(strlen($1) + strlen("constructor") + strlen("(") + strlen($4) + strlen(")") + strlen("{") + strlen("}") + 1);
			sprintf($$, "%s constructor(%s){}", $1, $4);
			//free($1);
			free($4);
		} else {
			$$ = MEM(strlen($1) + strlen("constructor") + strlen("(") + strlen(")") + strlen("{") + strlen("}") + 1);
			sprintf($$, "%s constructor(){}", $1);
			//free($1);
			//free($4);
		}
	}
	;

field_mods:
	access_mod VAL_SPEC {
		if (strlen($1) > 0) {
			$$ = MEM(strlen($1) + strlen("val") + 2);
			sprintf($$, "%sval ", $1);
			//free($1);
		} else {
			$$ = MEM(strlen("val "));
			sprintf($$, "val ");
		}
	}
	|
	access_mod VAR_SPEC {
		if (strlen($1) > 0) {
			$$ = MEM(strlen($1) + strlen("var") + 2);
			sprintf($$, "%svar ", $1);
			//free($1);
		} else {
			$$ = MEM(strlen("var "));
			sprintf($$, "var ");
		}
	}
	;

class_params:
	field_mods ident_val COLON type COMMA class_params {
		char* indent = get_indent(env[0] + INDENT);

		$$ = MEM(strlen(indent) + strlen($1) + strlen($2) + strlen($4) + strlen($6) + 2 + 1 + 2);
		sprintf($$, "%s%s %s: %s,\n%s", indent, $1, $2, $4, $6);
		free($1);
		free($2);
		//free($4);
		free($6);
	}
	//|
	//field_mods ident_val COLON type COMMA {
	//	$$ = MEM(strlen($1) + strlen($2) + strlen($4) + 2 + 1 + 1);
	//	sprintf($$, "%s %s: %s,", $1, $2, $4);
	//}
	|
	field_mods ident_val COLON type {
		char* indent = get_indent(env[0] + INDENT);
		$$ = MEM(strlen(indent) + strlen($1) + strlen($2) + strlen($4) + 2 + 1+1);
		sprintf($$, "%s%s %s: %s\n", indent, $1, $2, $4);
		free($1);
		free($2);
		//free($4);
	}
	|
	ident_val COLON type COMMA class_params {
		char* indent = get_indent(env[0] + INDENT);
		$$ = MEM(strlen(indent) + strlen($1) + strlen($3) + strlen($5) + 1 + 1 + 1 + 1);
		sprintf($$, "%s%s: %s,\n%s", indent, $1, $3, $5);
		free($1);
		//free($3);
		free($5);
	}
	//|
	//ident_val COLON type COMMA {
	//	$$ = MEM(strlen($1) + strlen($3) + 1 + 1 + 1);
	//	sprintf($$, "%s: %s,", $1, $3);
	//}
	| 
	ident_val COLON type {
		char* indent = get_indent(env[0] + INDENT);
		$$ = MEM(strlen(indent) + strlen($1) + strlen($3) + 1 + 1+1);
		sprintf($$, "%s%s: %s\n", indent, $1, $3);
		free($1);
		//free($3);
	}
	|
	%empty {
		$$ = "";
	}
	;
	

fun_params:
	ident_val COLON type COMMA fun_params {
		$$ = MEM(strlen($1) + strlen($3) + strlen($5) + 1 + 1 + 1 + 1);
		sprintf($$, "%s: %s,%s\n", $1, $3, $5);
		free($1);
		//free($3);
		free($5);
	}
	|
	//ident_val COLON type COMMA {
	//	$$ = MEM(strlen($1) + strlen($3) + 1 + 1 + 1);
	//	sprintf($$, "%s: %s,", $1, $3);
	//}
	//| 
	ident_val COLON type {
		$$ = MEM(strlen($1) + strlen($3) + 1 + 1);
		sprintf($$, "%s: %s", $1, $3);
		free($1);
		//free($3);
	}
	|
	%empty {
		$$ = "";
	}
	;

type:
	CHAR_SPEC{
		$$ = MEM(strlen("Char"));
		sprintf($$, "Char");
	}
	|
	SHORT_SPEC {
		$$ = MEM(strlen("Short"));
		sprintf($$, "Short");
	}
	|
	INT_SPEC {
		$$ = MEM(strlen("Int"));
		sprintf($$, "Int");
	}
	|
	LONG_SPEC {
		$$ = MEM(strlen("Long"));
		sprintf($$, "Long");
	}
	|
	FLOAT_SPEC {
		$$ = MEM(strlen("Float"));
		sprintf($$, "Float");
	}
	|
	DOUBLE_SPEC {
		$$ = MEM(strlen("Double"));
		sprintf($$, "Double");
	}
	|
	STRING_SPEC {
		$$ = MEM(strlen("String"));
		sprintf($$, "String");
	}
	;

access_mod:
	%empty {
		$$ = "";
	}
	|
	PRIVATE_SPEC {
		$$ = MEM(strlen("private "));
		sprintf($$, "private ");
	}
	|
	PROTECTED_SPEC {
		$$ = MEM(strlen("protected "));
		sprintf($$, "protected ");
	}
	|
	INTERNAL_SPEC {
		$$ = MEM(strlen("internal "));
		sprintf($$, "internal ");
	}
	|
	PUBLIC_SPEC {
		$$ = MEM(strlen("public "));
		sprintf($$, "public ");
	}
	;

ident_val:
	IDENT {
		$$ = MEM(strlen(yylval.ident));
		sprintf($$, "%s", yylval.ident);
	}
	;

add_indent:
	%empty {
		env[0] += INDENT;
	}
	;

remove_indent:
	%empty {
		env[0] -= INDENT;
	}
	;
%%


int main()
{
	yyscan_t scanner;
	struct Extra extra;
	long env[26];
    env[0] = 0;
    char * buffer = 0;
    long length;
    FILE * f = fopen ("test.kt", "rb");

    if (f) {
    	fseek (f, 0, SEEK_END);
    	length = ftell (f);
    	fseek (f, 0, SEEK_SET);
    	buffer = malloc (length+1);
    	if (buffer)
    		fread (buffer, 1, length, f);
        fclose (f);
        buffer[length] = '\0';
   	}
	init_scanner(buffer, &scanner, &extra);
	yyparse(scanner, env);
	destroy_scanner(scanner);
    free(buffer);
	return 0;
}
