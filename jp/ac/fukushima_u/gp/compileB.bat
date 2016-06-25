//自身を移動先予定に予めコピー
copy compileB.bat %1\compileB.bat

//第一引数のディレクトリに移動
cd %1

//カレントディレクトリ内のファイルをコンパイル
for %%s (*.java) do javac %%s

//カレントディレクトリ内のサブファイルを第一引数として
//順次自身の再帰呼び出しを行う
for /d %%s (*) do cd compileB.bat %%s

//ひと通り作業が終了したら、compileB.batの削除を行い、上位ディレクトリへ
del compileB.bat
cd ..

