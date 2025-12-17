#!/bin/bash

echo "=== TEST RESULTS ==="
echo ""

# Test 1: Create valid current account
echo "Test 1: Create valid current account"
cat > test_tmp.txt << 'EOF'
2
PT01-1234-12345678
Pedro
1000
500
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "sucesso\|criada" | head -1
echo ""

# Test 2: Create account with invalid number
echo "Test 2: Create account with invalid number"
cat > test_tmp.txt << 'EOF'
2
ABC123
João
1000
500
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "erro\|exception" | head -1
echo ""

# Test 3: Create account with empty holder
echo "Test 3: Create account with empty holder"
cat > test_tmp.txt << 'EOF'
2
PT01-1234-12345678

X
1000
500
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "erro\|vazio" | head -1
echo ""

# Test 4: Deposit valid amount
echo "Test 4: Deposit valid amount"
cat > test_tmp.txt << 'EOF'
2
PT01-1234-12345678
Pedro
1000
500
4
PT01-1234-12345678
500
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "depositado\|realizado" | head -1
echo ""

# Test 5: Deposit negative amount
echo "Test 5: Deposit negative amount"
cat > test_tmp.txt << 'EOF'
2
PT01-1234-12345678
Pedro
1000
500
4
PT01-1234-12345678
-10
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "erro" | head -1
echo ""

# Test 6: Withdraw with sufficient balance
echo "Test 6: Withdraw with sufficient balance"
cat > test_tmp.txt << 'EOF'
2
PT01-1234-12345678
Pedro
1000
500
5
PT01-1234-12345678
300
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "levantamento\|realizado" | head -1
echo ""

# Test 7: Create valid savings account
echo "Test 7: Create valid savings account"
cat > test_tmp.txt << 'EOF'
3
PT02-5678-87654321
Ana
1000
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "criada" | head -1
echo ""

# Test 8: Withdraw insufficient balance
echo "Test 8: Withdraw insufficient balance"
cat > test_tmp.txt << 'EOF'
2
PT02-5678-87654321
Ana
500
0
5
PT02-5678-87654321
2000
1
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -i "saldo insuficiente\|erro" | head -1
echo ""

# Test 11: List polymorphic accounts
echo "Test 11: List polymorphic accounts"
cat > test_tmp.txt << 'EOF'
2
PT01-1234-12345678
Pedro
1000
500
3
PT02-5678-87654321
Ana
1000
1
PT01-1234-12345678
0
EOF
java -cp out pt.escnaval.exercicios.MenuBanco < test_tmp.txt 2>&1 | grep -E "PT0[12]" | head -2

rm -f test_tmp.txt
