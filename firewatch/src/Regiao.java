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
        for (Par<Integer,Integer> p: casas) {
            this.ambiente[p.primeiro()][p.segundo()] = 'H';
        }

        // Definição das estradas
        for (Par<Integer,Integer> p: estradas) {
            this.ambiente[p.primeiro()][p.segundo()] = '=';
        }

        // Definição das aguas
        for (Par<Integer,Integer> p: agua) {
            this.ambiente[p.primeiro()][p.segundo()] = '~';
        }

        
    }
    

    public String nome() {
        return this.nome;
    }

    public int ardiveis() {
        // Devolve número de casas + terrenos (únicos elementos ardíveis) ainda não ardidos

        int contador = 0; 
        
        for (char[] linha : this.ambiente) {
            for (char entrada: linha) {
                if (entrada == '.' | entrada == 'H') {
                    contador ++;
                }
            }
        }

        return contador;
    }

    public void registaFogo(Calendar data, List<Par<Integer, Integer>> sitios) {
        // Regista um novo fogo para a região, acontecido na data data, em que arderam
        // os elementos referidos em sitios
        // TODO: registaFogo()
    }

    public static boolean dadosValidos(int largura, int altura, 
                                        List<Par<Integer, Integer>> casas,
                                        List<Par<Integer, Integer>> estradas,
                                        List<Par<Integer, Integer>> agua) {
        // veririfica se os indices dados em casas, estradas e agua condizem com a largura e altura dadas,
        // (ver se para cada List<Par<Integer, Integer>> .primeiro() < largura e .segundo() < altura)

        boolean validos = true;

        for (int i = 0; i < casas.size() && validos; i++) {
            if (casas.get(i).primeiro() > largura - 1 | casas.get(i).segundo() > altura - 1) {
                validos = false;
            }
        }

        for (int i = 0; i < estradas.size() && validos; i++) {
            if (estradas.get(i).primeiro() > largura - 1| estradas.get(i).segundo() > altura - 1) {
                validos = false;
            }
        }

        for (int i = 0; i < agua.size() && validos; i++) {
            if (agua.get(i).primeiro() > largura - 1 | agua.get(i).segundo() > altura - 1) {
                validos = false;
            }
        }
        
        return validos;

    }

    public EstadoSimulacao[][] alvoSimulacao() {
        // devolve matriz correspondente ao ambiente atual em que os terrenos e casas não ardidos são EstadoSimulacao.LIVRE
        // e a água, estradas e elementos já ardidos são EstadoSimulacao.OBSTACULO

        EstadoSimulacao[][] alvo = new EstadoSimulacao[this.ambiente.length][this.ambiente[0].length];

        for (int linha = 0; linha < this.ambiente.length; linha ++) {
            for (int entrada = 0; entrada < this.ambiente[linha].length; entrada ++) {
                if (this.ambiente[linha][entrada] == '~' | this.ambiente[linha][entrada] == '=' | this.ambiente[linha][entrada] == '!') {
                    alvo[linha][entrada] = EstadoSimulacao.OBSTACULO;
                }
                else if (this.ambiente[linha][entrada] == 'H' | this.ambiente[linha][entrada] == '.') {
                    alvo[linha][entrada] = EstadoSimulacao.LIVRE;
                }
            }
        }

        return alvo;

    }

    public NivelPerigo nivelPerigo(Calendar data, int[] tempoLimites) {
        // retorna o nível de perigo, ver forma de cálculo no início da pag.4 do enunciado
        int diferencaAnos = data.get(Calendar.YEAR) - this.ultFogo.get(Calendar.YEAR);
        NivelPerigo nivelPerigoCor;
        double nivelPerigo = 0.0;
        int racio;
        int ardiveis = this.ardiveis();
        int totalDeElementos = this.ambiente.length * this.ambiente[0].length;
        int obstaculos = totalDeElementos - ardiveis;



        Arrays.sort(tempoLimites);
        for(int celula = 0; celula < tempoLimites.length; celula++){
            if(diferencaAnos <= tempoLimites[celula]){
                nivelPerigo = tempoLimites[celula+1];
            }
        }

        racio = (ardiveis - obstaculos)/totalDeElementos;

        nivelPerigo *= 1+racio;

        if (nivelPerigo >= NivelPerigo.values().length) {
            nivelPerigoCor = NivelPerigo.values()[NivelPerigo.values().length - 1]; 
        } else {
            nivelPerigoCor = NivelPerigo.values()[(int) Math.round(nivelPerigo)];
        }

        return nivelPerigoCor;

    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Nome: " + this.nome() + "  Data ult. fogo: " + this.ultFogo + "\n");
        for (char[] linha : this.ambiente){
            for(char coluna : linha){
                output.append(coluna);
            }  
            output.append("\n");
        }
        return output.toString();
    }


    // Podemos fazer mais métodos que estes, desde que sejam privados

    private double valorPerigo(int diferencaAnos, int[] tempoLimites, int totalDeElementos,){
        double nivelPerigo = 0.0;
        int ratio;
        for(int celula = 0; celula < tempoLimites.length; celula++){
            if(diferencaAnos <= tempoLimites[celula]){
                nivelPerigo = tempoLimites[celula+1];
            }
        }

        racio = (ardiveis - obstaculos)/totalDeElementos;

        nivelPerigo *= 1+racio;
    }

}
