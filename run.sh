clear

echo "Compilation en cours..."

rm -r classes

if javac -d classes src/*.java; then
    echo "Aucune erreur trouvée"
    echo "Exécution du programme en cours..."
    echo ""
    echo "---------------------------";
    echo ""
    java -cp classes main.Main
else
    echo "Erreur lors de la compilation"
fi
