clear

echo "Compilation en cours..."

rm -r classes

if javac -d classes src/*.java; then
    java -cp classes main.Main
else
    echo "Erreur lors de la compilation"
fi
