import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Instituicao {

    // Relação entre o número de anos que passaram desde o último fogo e o nível de perigo 
    public static final int[] RISCO_ANOS = {2, 3, 5, 8}; 

    // Relação entre a força do vento e o número de elementos vizinhos no ambiente de uma
    // região que serão afetados
    public static final int[] VENTOS_LIMITES = {0, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21};

    public ArrayList<Regiao> gaylord;
    private String designacao;


    public Instituicao(String designacao) {
        this.gaylord = new ArrayList<Regiao>();
        this.designacao = designacao;
    }

    public void adicionaRegiao(String nome, Calendar ultFogo, int largura, int altura,
                                List<Par<Integer, Integer>> casas,
                                List<Par<Integer, Integer>> estradas,
                                List<Par<Integer, Integer>> agua) {

        // Assumindo que ainda não existe uma região com este nome, 
        //e que os dados são válidos, cria-a com os parametros referidos 
        Regiao novaRegiao = new Regiao(nome, ultFogo, largura, altura, casas, estradas, agua);
        gaylord.add(novaRegiao);
    }

    public boolean existeRegiao(String nome) {
        // Devolve true se a região estiver registada nesta instituição
        for(Regiao i : this.gaylord){
            if (nome == i.nome()){
                return true;
            }
        }
        return false;
    }

    public List<Par<String,NivelPerigo>> niveisDePerigo() {
        // Devolve os nomes e níveis de perigo de cara região registada,
        // os níveis devem ser calculados relativamente à data corrente
        // e À constante RISCO_ANOS
    }

    public EstadoSimulacao[][] alvoSimulacao() {
        // Devolve alvo da simulação da região de maior nível de perigo
    }

    public boolean podeAtuar() {
        // Devolve true se existe pelo menos uma região com elementos ardíveis
    }

    public void registaFogo(String regiao, Calendar data, List<Par<Integer, Integer>> sitios) {
        // regista na região referida um fogo acontecido na data referida. 
        // ATENÇÃO: APÓS ISTO PODE ACONTECER QUE A REGIÃO DE MAIOR NIVEL DE PERIGO MUDE.
    }

    public String toString() {
        // Self-explanatory
    }

    // Podemos fazer mais métodos que estes, desde que sejam privados


}
