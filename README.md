# AuraBank

Banco didático com interface gráfica desktop em Java Swing, sem dependências externas. Visual em roxo e azul escuro, telas de login e cadastro, painel de saldo, operações e histórico.

## Executar

Com um JDK 8 ou superior instalado, execute na pasta do projeto:

```powershell
javac -encoding UTF-8 -d build src/*.java
java -cp build Main
```

No IntelliJ IDEA, execute o método `main` da classe `Main`.

Para usar a versão de console: `java -cp build Main --console`.

## Funcionalidades

- Cadastro com campos obrigatórios, CPF com 11 dígitos e bloqueio de CPF duplicado.
- Número de conta automático e login por CPF e senha.
- Consulta de saldo e dados, depósito, saque e saída da conta.
- Solicitações de amizade pelo número da conta, com aceite ou recusa pelo destinatário e lista de amigos.
- Transferências somente entre amigos, com débito e crédito dos saldos e histórico nas duas contas na interface gráfica.
- Valores com vírgula ou ponto decimal e até duas casas, sem separador de milhar.
- Recusa de saques acima do saldo e valores zero, negativos ou inválidos.
- Saldo calculado com `BigDecimal`, evitando imprecisão de ponto flutuante.
- Histórico das últimas quatro movimentações de cada conta e botão para ocultar valores.
- Interface com rolagem, navegação pelo teclado e confirmação com Enter nos campos de senha e valor.

Cadastros e saldos ficam em memória e são perdidos ao encerrar o programa. O CPF é validado apenas pelo formato, sem cálculo dos dígitos verificadores. As senhas ficam em memória, sem persistência ou criptografia. O projeto é um exercício de console.

## Testar

```powershell
javac -encoding UTF-8 -d build src/*.java tests/*.java
java -cp build ContaTest
java -cp build AmizadeTransferenciaTest
java '-Djava.awt.headless=true' -cp build InterfaceTest
```

Para transferir, cadastre duas contas na mesma execução. Envie uma solicitação pelo número da outra conta (ex.: `0002`), entre na conta destinatária e aceite. Depois, na conta remetente, selecione o amigo, informe o valor e confirme a transferência. Amizades e solicitações também ficam em memória e são perdidas ao encerrar o programa.
