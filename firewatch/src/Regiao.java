import java.util.Calendar;
import java.util.List;
import java.util.Arrays;

public class Regiao {
    private String nome;
    private Calendar ultFogo;
    private char [][] ambiente;
    
    public Regiao (String nome, Calendar ultFogo, int largura, int altura, List<Par<Integer, Integer>> casas, List<Par<Integer, Integer>> estradas, List<Par<Integer, Integer>> agua) {
        this.nome = nome;
        this.ultFogo = ultFogo;
        this.ambiente = new char[largura][altura];
        
        // Definição do terreno inicial
        for (char[] linha: ambiente) {
            Arrays.fill(linha, '.');
        }

        // Definição das casas
        for (Par p: casas) {
            this.ambiente[(int) p.primeiro()][(int) p.segundo()] = 'H';
        }

        // Definição das estradas
        for (Par p: estradas) {
            this.ambiente[(int) p.primeiro()][(int) p.segundo()] = '=';
        }

        // Definição das aguas
        for (Par p: agua) {
            this.ambiente[(int) p.primeiro()][(int) p.segundo()] = '~';
        }

        
    }
    

    public String nome() {
        return this.nome;
    }

    public int ardiveis() {
        // Devolve número de casas + terrenos (únicos elementos ardíveis) ainda não ardidos
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


    // Podemos fazer mais métodos que estes, desde que sejam privados

}


public static void main(String[] args) {
    
}