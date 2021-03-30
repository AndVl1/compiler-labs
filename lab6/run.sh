rm lex.yy.c 
rm lexer
flex lab6.l
gcc -o lexer lex.yy.c
# valgrind -q 
./lexer test.txt