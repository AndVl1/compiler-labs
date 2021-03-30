rm lex.yy.c 
rm lexerref
flex lab6ref.l
gcc -o lexerref lex.yy.c
valgrind -q ./lexerref testref.txt