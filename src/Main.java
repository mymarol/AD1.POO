import java.util.Scanner;

class Produto {
    String produto; // nome do produto
    double preco;
    int codigo; // identificador exclusivo
    static int incremento = 1; // usado para AUTO_INCREMENT no codiog de id

    /**
     * Inicializa um produto
     * @param String produto nome do produto
     * @param double preco preço do produto
     */
    public Produto(String produto, double preco){
        this.produto = produto;
        this.preco = preco;
        this.codigo = incremento++;
    }
    public Produto(){
        this.codigo = incremento++;
    }

    // contrato para garantir polimorfismo
    // entre Produto e ProdutoComposto
    public void add(Produto p) {}
    public void remove(Produto p) {}

    /**
     * Representação em string do produto
     * nome preco id:
     */
    public String toString(){
        return String.format("%s $%.2f id:%d", produto, preco, codigo);
    }
}

class ProdutoComposto extends Produto{
    Produto[] produtos; // lista de produtos
    // aponta para a posição do último vetor. Útil para evitar
    // percorrer posições com null do vetor de produtos
    int ultimo = -1;

    /**
     * Inicializa ProdutoComposto. Calcula o seu preço
     * como a soma dos preços dos produtos que o compõem.
     * @param String produto nome do produto
     * @param Produto[] produtos vetor inicializado de produtos
     */
    public ProdutoComposto(String produto, Produto[] produtos){
        this.produto = produto;
        this.codigo = incremento++;
        this.produtos = produtos;
        double preco = 0;
        for(Produto p: produtos){
            preco += p.preco;
        }
        this.preco = preco;
        this.ultimo = produtos.length - 1;
    }

    /**
     * Inicializa ProdutoComposto com um vetor de produtos vazio
     * de tamanhoVetor posições
     * @param String produto nome do produto
     * @param int tamanhoVetor tamanho do vetor
     */
    public ProdutoComposto(String produto, int tamanhoVetor) {
        this.produto = produto;
        this.produtos = new Produto[tamanhoVetor];
    }

    /**
     * Adiciona um produto ao vetor de itens de produto,
     * soma o preço do produto ao preço atual do produto composto.
     * @param Produto p produto a ser adicionado ao vetor
     */
    public void add(Produto p) {
        ultimo++; // atualiza o índice do último elemento
        produtos[ultimo] = p;
        preco += p.preco;
    }

    /**
     * Remove um produto, caso exista, do vetor de produtos.
     * Também decrementa o preço do produto composto.
     * @param Produto p produto a ser removido
     */
    public void remove(Produto p) {
        // busca índice do produto p no vetor
        int posicao = buscar(p);
        // se o produto existir no vetor, então remova-o
        if(posicao != -1){
            for(int i = posicao; i < ultimo; i++){
                produtos[i] = produtos[i + 1];
            }
            produtos[ultimo] = null;
            ultimo--; // atualiza o índice do último elemento
            preco -= p.preco; // decrementa o preço
        }

    }

    /**
     * Busca o índice do elemento no vetor de produtos.
     * @param Produto p produto procurado.
     * @return int índice do elemento ou -1, caso não seja encontrado.
     */
    public int buscar(Produto p) {
        int index = -1; // produto não existe

        for(int i = 0; i <= ultimo; i++){
            if (produtos[i] == p){
                return i;
            }
        }
        return index;
    }

    /**
     * Exibe informações sobre o produto composto como um todo,
     * bem como sobre cada um dos produtos que o compõem.
     * @return String Representação do ProdutoComposto
     */
    @Override
    public String toString(){
        String saida = super.toString();
        for(Produto p: produtos){
            if (p != null) {
                saida += "\n-" + p.toString();
            }
        }
        return saida;
    }
}

/**
 class abstrata que define um comportamento comum às classes
 Estoque e Carrinho, no que diz respeito ao gerenciamento de
 produtos (adicionar, remover, alterar)
 */
abstract class GerenciadorProdutos {
    Produto[] itens;
    int ultimo = -1; // vetor inicialmente vazio. útlimo = -1

    /**
     * Inicializa o Gerenciador de produtos informando
     * o tamanho do vetor de produtos
     */
    public GerenciadorProdutos(int tamanho) {
        itens = new Produto[tamanho];
    }

    /**
     * Adiciona um produto na próxima posição livre.
     * @param Produto item produto a ser adicionado
     */
    public void add(Produto item){
        ultimo++;
        itens[ultimo] = item;
    }

    /**
     * Remove um produto na dada posição e realoca o vetor de itens.
     * @param int pos posição do item a ser removido.
     */
    public void remover(int pos){
        // produto a ser removido de produtos compostos também
        Produto prodRemover = itens[pos];

        // removendo o produto da lista do estoque
        for(int i = pos; i < ultimo; i++){
            itens[i] = itens[i + 1];
        }
        itens[ultimo] = null;
        ultimo--;

        // removendo o produto de todos os outros produtos
        // que são compostos por esse.
        for(int i = 0; i <= ultimo; i++) {
            itens[i].remove(prodRemover);
        }
    }

    /**
     * Sobreescreve uma posição do vetor de itens
     * @param int pos posição do produto a ser atualizado
     * @param String nome novo nome do produto
     * @param double preco novo preço do produto
     */
    public void alterar(int pos, String nome, double preco) {
        itens[pos].produto = nome;
        itens[pos].preco = preco;
    }

    /**
     * Representação da lista de itens
     * @return String
     */
    public String toString(){
        String saida = "";

        for(int i = 0; i <= ultimo; i++){
            saida += i + " = " + itens[i] + "\n";
        }
        return saida;
    }
}

/**
 * Esta classe representa a coleção de todos os produtos
 * que podem existir no sistema.
 */
class Estoque extends GerenciadorProdutos {

    /**
     * Inicializa o estoque com alguns produtos.
     * @param int tamanho tamanho do vetor de Produtos
     */
    public Estoque(int tamanho) {
        // primeira linha deve ser a chamada ao super construtor.
        super(tamanho);
        // alguns itens de Produto para interagirmos com o programa.
        itens[0] = new Produto("tinta", 80);
        itens[1] = new Produto("lixa", 10);
        itens[2] = new Produto("pincel", 45);
        itens[3] = new Produto("rolo", 30);
        itens[4] = new Produto("bandeja", 25);
        itens[5] = new Produto("verniz", 120);
        itens[6] = new Produto("espatula", 5);
        itens[7] = new Produto("massa corrida", 90);
        itens[8] = new Produto("cimento", 60);
        //itens[9] = new ProdutoComposto("Kit pintura", new Produto[]{itens[0], itens[1], itens[2], itens[3], itens[4]});
        //itens[10] = new ProdutoComposto("Kit Tinta", new Produto[]{itens[0], itens[5]});
        itens[9] = new ProdutoComposto("Kit pintura", 4);
        itens[9].add(itens[0]);
        itens[9].add(itens[1]);
        itens[9].add(itens[2]);
        itens[9].add(itens[3]);

        itens[10] = new ProdutoComposto("Kit Tinta", 2);
        itens[10].add(itens[0]);
        itens[10].add(itens[5]);
        ultimo = 10;

    }

}

/**
 * Representa a unidade que vai manter os Produtos
 * escolhidos pelo usuário do programa.
 */
class Carrinho extends GerenciadorProdutos {

    /**
     * Inicializa o Carrinho com um vetor de produtos de
     * tamanho posições.
     * @param int tamanho tamanho máximo do vetor de produtos do carrinho
     */
    public Carrinho(int tamanho) {
        super(tamanho);
    }

    /**
     * Exibe a mensagem de compra efetuada
     */
    public void comprar(){
        System.out.println("Compra efetuada com sucesso!");
    }

    /**
     * Calcula o valor total dos Produtos adicionados ao Carrinho.
     * @return double somatório dos preços dos produtos.
     */
    public double total() {
        double total = 0;
        for (int i = 0; i <= ultimo; i++) {
            total += itens[i].preco;
        }
        return total;
    }
}

/**
 * Programa principal. Instanciação das classes modeladas e
 * interação com o usuário.
 * 1) Produtos
 *  a) Ver produtos
 *  b) Alterar produtos
 *  c) Remover produtos
 *  d) Criar produtos
 * 2) orçamento
 * 3) Carrinho
 *  a) Ver carrinho
 *  b) Remover itens do carrinho
 *  c) Adicionar itens ao carrinho
 */
public class Main{
    //inicializa o leitor do teclado
    static Scanner entrada = new Scanner(System.in);
    // estoque de produtos de tamanho máximo 20
    static Estoque estoque = new Estoque(20);
    // carrinho de produtos de tamanho máximo 10
    static Carrinho carrinho = new Carrinho(10);

    /**
     * Exibe o estoque geral do estoque
     */
    public static void exibirMenuEstoque(){
        // Esta parte seria o nosso "estoque" de produtos
        System.out.println("\n--------------- Gerenciamento de produtos ---------------\n");
        System.out.println("0 - Ver Produtos");
        System.out.println("1 - Adicionar Produtos");
        System.out.println("2 - Voltar Menu Principal");
        int opcao = Integer.parseInt(entrada.nextLine());
        switch(opcao){
            case 0:
                exibirProdutos();
                break;
            case 1:
                cadastrarProduto();
                break;
            case 2:
                break;
        }
    }

    /**
     * Exibe a lista de produtos cadastrados.
     * Possibilita alterar e remover os produtos listados.
     */
    public static void exibirProdutos() {
        // loop de interação com o usuário.
        // -2: valor qualquer para que o método inicie entrando no loop
        int opcao = -2;
        // enquanto a pessoa não escolher a opção de saída -1
        while (opcao != -1) {
            System.out.println("\n-------- Produtos Cadastrados --------");
            String listaProdutos = estoque.toString();
            System.out.println(listaProdutos);
            System.out.println("-1 = Sair");
            opcao = Integer.parseInt(entrada.nextLine());

            if (opcao == -1) {
                // volte imediatamente ao início do while e saia do laço porque a condição falhou
                continue;
            } else {
                System.out.println("\n-------------- Ações no produto "+ estoque.itens[opcao] +" -------------\n");
                System.out.println("0 = Alterar Produto");
                System.out.println("1 = Remover Produto");
                System.out.println("-1 = Sair");

                int opcaoProduto = Integer.parseInt(entrada.nextLine());

                switch(opcaoProduto) {
                    case -1: // volta à lista de produtos cadastrados.
                        break;
                    case 0:
                        System.out.println("\n----------Alterar "+ estoque.itens[opcao] +"----------------\n");

                        System.out.print("Novo nome do produto: ");
                        String novoNome = entrada.nextLine();
                        System.out.println("Novo preço do produto: ");
                        double novoPreco = Double.parseDouble(entrada.nextLine());

                        // altera o prodto na posição <opção> do estoque
                        estoque.alterar(opcao, novoNome, novoPreco);

                        System.out.println("\n-------------- Produto alterado -----------------\n");

                        break;
                    case 1:
                        System.out.println("\n--------- Remover "+ estoque.itens[opcao] +"-----------\n");
                        // remove o produto na posição <opção> do estoque. Também remove todas as ocorrências desse como subproduto em produtos compostos.
                        estoque.remover(opcao);

                        break;
                }
            }
        }
    }

    /**
     * Cria um produto e o adiciona ao estoque.
     */
    public static void cadastrarProduto(){

        // qualquer valor inicial para entrar no laço de interação
        // com o usuário.
        int opcao = -2;

        while (opcao != -1) {
            System.out.println("\n------------- Cadastro de Produto -------------\n");
            System.out.println("0 - Produto Simples");
            System.out.println("1 - Produto Composto");
            System.out.println("\n-1 - Sair");
            opcao = Integer.parseInt(entrada.nextLine());
            switch(opcao){
                case 0:
                    System.out.print("Nome do Produto: ");
                    String nome = entrada.nextLine();
                    System.out.print("Preço do Produto: ");
                    double preco = Double.parseDouble(entrada.nextLine());

                    // criando produto simples novo
                    Produto produto = new Produto(nome, preco);
                    // adiciona o produto novo ao estoque
                    estoque.add(produto);
                    break;
                case 1:
                    System.out.print("Nome do produto: ");
                    nome = entrada.nextLine();
                    System.out.print("Quantidade de itens: ");
                    // quantidade de itens que formarão o produto composto
                    int quantItens = Integer.parseInt(entrada.nextLine());
                    // produto composto, inicialmente com um vetor de itens com quantItens posições vazias.
                    Produto produto2 = new ProdutoComposto(nome, quantItens);

                    // valor qualquer para entrar em um loop de interação
                    // com o usuário
                    int opcaoProduto = -2;
                    // para evitar que a pessoa tente adicionar mais itens
                    // do que o que foi definido em quantItens
                    int quantAdicionados = 0;

                    // a pessoa pode adicionar até quantItens ao vetor
                    // do ProdutoComposto
                    while (opcaoProduto != -1 && quantAdicionados < quantItens) {
                        System.out.println("\n--------- Adicionar --------\n");
                        System.out.println(estoque);
                        System.out.println("\n-1 = Sair");
                        opcaoProduto = Integer.parseInt(entrada.nextLine());
                        // opção de saída. Volta imediatamente ao while acima
                        // para interromper a adição de itens.
                        if (opcaoProduto == -1) {
                            continue;
                        }
                        // adiciona o produto do estoque à sua composição
                        produto2.add(estoque.itens[opcaoProduto]);
                        quantAdicionados++;
                    }
                    // adiciona o item ao estoque
                    estoque.add(produto2);
                    break;
                case -1:
                    continue; // saída do menu de cadastro de produtos
            }
        }

    }

    /**
     * Lista o menu do carrinho de compra.
     * Possibilita ver os itens do carrinho com o valor total atual,
     * remover e adicionar itens ao carrinho
     */
    public static void menuCarrinho() {

        int opcao = -2;

        while (opcao != -1) {
            System.out.println("\n-------------- Menu Carrinho ------------\n");
            System.out.println("0 = Ver carrinho");
            System.out.println("1 = Adicionar Item");
            System.out.println("\n-1 = Sair");

            opcao = Integer.parseInt(entrada.nextLine());

            switch(opcao) {
                case 0:
                    int opcaoRemover = -2;
                    while (opcaoRemover != -1) {
                        System.out.println("\n------------- Carrinho (Deseja remover um item?) -------------\n");
                        System.out.println(carrinho);
                        double total = orcamento();
                        System.out.println(String.format("Total = $%.2f\n", total));
                        System.out.println("\n-1 = Sair");
                        // posição do produto a ser removido do carrinho.
                        opcaoRemover = Integer.parseInt(entrada.nextLine());
                        if (opcaoRemover == -1) {
                            continue;
                        }
                        // remove um produto na posição opcaoRemover
                        carrinho.remover(opcaoRemover);
                    }

                    break;
                case 1:
                    int opcaoItem = -2;

                    while (opcaoItem != -1) {
                        System.out.println("\n------------- Adicionar -------------\n");
                        System.out.println(estoque);
                        System.out.println("\n-1 = Sair");
                        opcaoItem = Integer.parseInt(entrada.nextLine());
                        if (opcaoItem == -1) {
                            continue;
                        }
                        // adiciona um produto à próxima posição livre do carrinho.
                        carrinho.add(estoque.itens[opcaoItem]);
                    }
                    break;
                case -1:
                    continue; // sai do menu de carrinho
            }
        }
    }

    /**
     * Retorna o valor total dos itens adicionados ao carrinho.
     * @return double somatório do preço dos itens do carrinho.
     */
    public static double orcamento(){
        return carrinho.total();
    }

    /**
     * Método principal. Exibe o menu Inicial, possibilitando entrar
     * no menu de Produtos, Carrinho, além de visualizar o orçamento
     */
    public static void main(String args[]){

        int opcao = -1;
        while (opcao != 0){
            System.out.println("\n-------- Menu Principal --------");
            System.out.println("1 - Produtos");
            System.out.println("2 - Orçamento");
            System.out.println("3 - Carrinho");
            System.out.println("0 - Exit");
            opcao = Integer.parseInt(entrada.nextLine());

            switch(opcao){
                case 1:
                    exibirMenuEstoque();
                    break;
                case 2:
                    System.out.println("\n-------- Orçamento -------\n");
                    double total = orcamento();
                    System.out.println(String.format("Total = $%.2f\n", total));
                    break;
                case 3:
                    menuCarrinho();
                    break;
                case 0:
                    continue; // fim do programa
            }
        }
    }
}