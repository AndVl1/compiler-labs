package main

import (
	"fmt"
	"go/ast"
	"go/parser"
	"go/token"
	"os"
)

var count = 0

// Подсчёт, сколько раз в ходе работы программы вызывались сопрограммы
func main() {
	if len(os.Args) != 2 {
		fmt.Printf("usage: astprint <filename.go>\n")
		return
	}

	// Создаём хранилище данных об исходных файлах
	fset := token.NewFileSet()

	// Вызываем парсер
	if file, err := parser.ParseFile(
		fset,                	// данные об исходниках
		os.Args[1],           	// имя файла с исходником программы
		nil,              	// пусть парсер сам загрузит исходник
		parser.ParseComments, 	// приказываем сохранять комментарии
	); err == nil {
		// Если парсер отработал без ошибок, печатаем дерево
		dynamicInspectAst(file)
		//_ = ast.Fprint(os.Stdout, fset, file, nil)
		fmt.Println(count)
	} else {
		// в противном случае, выводим сообщение об ошибке
		fmt.Printf("Error: %v", err)
	}
}

func dynamicInspectAst(file *ast.File) {

	ast.Inspect(file, func(node ast.Node) bool {
		if blockStmt, ok := node.(*ast.BlockStmt); ok {
			ast.Inspect(blockStmt, func(block ast.Node) bool {
				//block.(*ast.BlockStmt).List
				if goStmt, ok := block.(*ast.GoStmt); ok {
					fmt.Println(goStmt.Go)
					//fmt.Println(goStmt.Call.Fun.End())
					count++
				}
				return true
			})

		}
		return true
	})
}

func staticInspectAst(file *ast.File) {
	count := 0
	ast.Inspect(file, func(node ast.Node) bool {
		if loop, ok := node.(*ast.GoStmt); ok {
			fmt.Println(loop.Go)
			//fmt.Println(goStmt.Call.Fun.End())
			count++
		}
		return true
	})
	fmt.Print(count)
}