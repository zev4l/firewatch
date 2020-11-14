import java.util.Calendar;
import java.util.List;

public class Regiao {
    private String nome;
    private Calendar ultFogo;
    private int [][] ambiente;
    
    public Regiao (String nome, Calendar ultFogo, int largura, int altura, List<Par<Integer, Integer>> casas, List<Par<Integer, Integer>> estradas, List<Par<Integer, Integer>> agua) {
        this.nome = nome;
        this.ultFogo = ultFogo;
        this.ambiente = new int[largura][altura];
        // ... TODO: Falta adicionar o resto dos atributos

        
    }
    

    public String nome() {
        return this.nome;
    }

    public int ardiveis() {
        // Devolve número de casas + terrenos ainda não ardidos
    }

    public void registaFogo(Calendar data, List<Par<Integer, Integer>> sitios) {
        // Regista um novo fogo para a região, acontecido na data data, em que arderam
        // os elementos referidos em sitios
    }

    public static boolean dadosValidos(int largura, int altura, 
                                        List<Par<Integer, Integer>> casas,
                                        List<Par<Integer, Integer>> estradas,
                                        List<Par<Integer, Integer>> agua) {
        // veririfica se os indices dados em casas, estradas e agua condizem com a largura e altura dadas,
        // (ver se para cada List<Par<Integer, Integer>> .primeiro() < largura e .segundo() < altura)
    }

    public EstadoSimulacao[][] alvoSimulacao() {
        // devolve matriz correspondente ao ambiente atual em que os terrenos e casas não ardidos são EstadoSimulacao.LIVRE
        // e a água, estradas e elementos já ardidos são EstadoSimulacao.OBSTACULO
    }

    public NivelPerigo nivelPerigo(Calendar data, int[] tempoLimites) {
        // retorna o nível de perigo, ver forma de cálculo no início da pag.4 do enunciado
    }

    public String toString() {
        // self-explanatory
    }

    // Defini aqui os enums, apesar de sentir que deviam estar
    // no Simulador.java, porque não sei se o podemos editar.

    enum EstadoSimulacao {
        LIVRE,
        AFETADO,
        OBSTACULO
    }

    enum NivelPerigo {
        VERDE,
        AMARELO,
        LARANJA,
        VERMELHO
    }

    // Podemos fazer mais métodos que estes, desde que sejam privados

}
