import java.util.Scanner;

class Produto {
    String produto;
    double preco;
    int codigo;
    static int incremento = 1;

    public Produto(String produto, double preco){
        this.produto = produto;
        this.preco = preco;
        this.codigo = incremento++;
    }
    public Produto(){

    }

    public String toString(){
        return String.format("%s %.2f %d", produto, preco, codigo);
    }
}

class ProdutoComposto extends Produto{
    Produto[] produtos;

    public ProdutoComposto(String produto, Produto[] produtos){
        this.produto = produto;
        this.codigo = incremento++;
        this.produtos = produtos;
        double preco = 0;
        for(Produto p: produtos){
            preco += p.preco;
        }
        this.preco = preco;
    }

    @Override
    public String toString(){
        String saida = super.toString();
        for(Produto p: produtos){
            saida += "\n-" + p.toString();
        }
        return saida;
    }
}


public class Main{
    //inicializa o leitor do teclado
    static Scanner entrada = new Scanner(System.in);

    static int qtd = 0;

    static Produto[] Menu, Carrinho;
    //inicializador estático - inicia atributos estáticos
    static{
        Carrinho = new Produto[20];
        Menu = new Produto[11];
        Menu[0] = new Produto("tinta", 80);
        Menu[1] = new Produto("lixa", 10);
        Menu[2] = new Produto("pincel", 45);
        Menu[3] = new Produto("rolo", 30);
        Menu[4] = new Produto("bandeja", 25);
        Menu[5] = new Produto("verniz", 120);
        Menu[6] = new Produto("espatula", 5);
        Menu[7] = new Produto("massa corrida", 90);
        Menu[8] = new Produto("cimento", 60);
        Menu[9] = new ProdutoComposto("Kit pintura", new Produto[]{Menu[0], Menu[1], Menu[2], Menu[3],
                Menu[4]});
        Menu[10] = new ProdutoComposto("Kit Tinta", new Produto[]{Menu[0], Menu[5]});
    };

    public static void exibirMenu(){
        int opcao = -2;
        while(opcao != -1){
            System.out.println("\n-------- Menu Produtos --------");
            for(int i= 0; i < Menu.length; i++){
                System.out.println(i + " = " + Menu[i]);
            }
            System.out.println("-1 = Sair");
            opcao = entrada.nextInt();
            if(opcao == -1){
                continue;
            }
            else{
                System.out.println("\n item escolhido:" + Menu[opcao]);
                Carrinho[qtd] = Menu[opcao];
                qtd++;
            }
        }
    }

    public static void mostrarCarrinho(){
        double total = 0;
        System.out.println("\n-------- Carrinho de Compras --------");
        for(int i = 0; i < qtd; i++){
            System.out.println(i + " = " + Carrinho[i]);
            total += Carrinho[i].preco;
        }
        System.out.printf("\n Total = $%.2f \n", total);
    }

    public static double orcamento(Produto[] nota){
        double total = 0;
        for(Produto p: nota){
            total += p.preco;
        }
        return total;
    }

    public static void main(String args[]){
        /*Produto produto1 = new Produto("capacete", 100);
        Produto[] vetor = {new Produto("pincel", 20), new Produto("brocha", 30),
                new Produto("removedor", 60)};
        ProdutoComposto produto2 = new ProdutoComposto("kit", vetor);
        System.out.println(produto1);
        System.out.println(produto2);
        Produto[] produtos = {produto1, produto2};
        System.out.println(orcamento(produtos));
        */

        int opcao = -1;
        while (opcao != 0){
            System.out.println("\n-------- Menu Principal --------");
            System.out.println("1 - Produtos");
            System.out.println("2 - Orçamento");
            System.out.println("3 - Carrinho");
            System.out.println("0 - Exit");
            opcao = entrada.nextInt();

            switch(opcao){
                case 1:
                    exibirMenu();
                    break;
                case 2:
                    //orcamento();
                    break;
                case 3:
                    mostrarCarrinho();
                    break;
                case 0:
                    continue;
            }
        }
    }
}