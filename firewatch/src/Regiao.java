import java.util.Calendar;
import java.util.List;
import java.util.Arrays;



/**
 * @author Grupo 77 - Augusto Gouveia (55371), Jose Almeida (55373)
 * @date Novembro 2020
 * 
 * Esta classe contem os metodos requiridos para criar uma regiao 
 * e gerir a sua existencia.
 */
public class Regiao {
    private String nome;
    private Calendar ultFogo;
    private char [][] ambiente;
    
    public Regiao (String nome, Calendar ultFogo, int largura, int altura, List<Par<Integer, Integer>> casas, List<Par<Integer, Integer>> estradas, List<Par<Integer, Integer>> agua) {
        this.nome = nome;
        this.ultFogo = ultFogo;
        this.ambiente = new char[largura][altura];
        
        // Definicao do terreno inicial
        for (char[] linha: ambiente) {
            Arrays.fill(linha, '.');
        }

        // Definicao das casas
        for (Par<Integer,Integer> p: casas) {
            this.ambiente[p.primeiro()][p.segundo()] = 'H';
        }

        // Definicao das estradas
        for (Par<Integer,Integer> p: estradas) {
            this.ambiente[p.primeiro()][p.segundo()] = '=';
        }

        // Definicao das aguas
        for (Par<Integer,Integer> p: agua) {
            this.ambiente[p.primeiro()][p.segundo()] = '~';
        }

        
    }
    

    
    /** 
     * Retornar o nome da regiao
     * 
     * @return String representando o nome da regiao
     */
    public String nome() {
        return this.nome;
    }

    
    /**
     * Percorrer o array a procura de quantos terrenos podem arder
     *   
     * @return Um número inteiro representando os terrenos ardiveis
     */
    public int ardiveis() {

        int contador = 0; 
        
        // Percorre o array 
        for (char[] linha : this.ambiente) {
            for (char entrada: linha) {
                // Se o elemento do array e um terreno ou uma casa incrementamos um valor
                // no contador
                if (entrada == '.' | entrada == 'H') {
                    contador ++;
                }
            }
        }

        return contador;
    }

    
    /**
     * Regista um novo fogo na regiao 
     * 
     * @param data Data do fogo
     * @param sitios Lista de sitios onde registar um fogo
     * @requires data != null && sitios != null
     */
    public void registaFogo(Calendar data, List<Par<Integer, Integer>> sitios) {
        // Regista um novo fogo para a regiao, acontecido na data data, em que arderam
        // os elementos referidos em sitios

        for (Par<Integer,Integer> p: sitios) {
            if (this.ambiente[p.primeiro()][p.segundo()] == 'H' | this.ambiente[p.primeiro()][p.segundo()] == '.') {
                this.ambiente[p.primeiro()][p.segundo()] = '!';
            }

            if (data.get(Calendar.YEAR) > this.ultFogo.get(Calendar.YEAR)) {
                this.ultFogo = data;
            }
        }
    }

    
    /** 
     * Verifica se os indices dados em casas, estradas e agua condizem com a largura e altura dadas.
     * 
     * @param largura A largura do terreno
     * @param altura A altura do terreno
     * @param casas Coordenadas dedicadas a casas
     * @param estradas Coordenadas dedicadas a estradas
     * @param agua Coordenadas dedicadas a elementos de agua
     * @requires largura > 0 && altura > 0 && casas != null 
     * && estradas != null && agua != null
     * @return boolean indicando se os dados sao validos
     */
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

    
    /** 
     * Criar uma matriz correspondente ao ambiente atual
     * 
     * @return Uma matriz tipo EstadoSimulacao[][] em que os terrenos e casas nao ardidos sao EstadoSimulacao.LIVRE
     * e a agua, estradas e elementos ja ardidos sao EstadoSimulacao.OBSTACULO
     */
    public EstadoSimulacao[][] alvoSimulacao() {

        EstadoSimulacao[][] alvo = new EstadoSimulacao[this.ambiente.length][this.ambiente[0].length];

        // percorremos a matriz
        for (int linha = 0; linha < this.ambiente.length; linha ++) {
            for (int entrada = 0; entrada < this.ambiente[linha].length; entrada ++) {
                // se o elemento em que nos encontramos representar agua, estrada ou terreno ardido, passa a representar
                // um OBSTACULO na matriz
                if (this.ambiente[linha][entrada] == '~' 
                 || this.ambiente[linha][entrada] == '=' 
                 || this.ambiente[linha][entrada] == '!') {
                    alvo[linha][entrada] = EstadoSimulacao.OBSTACULO;
                }
                // se o elemento em que nos encontramos representar uma casa ou um terreno, passa a representar um 
                // terreno LIVRE na nova matriz
                else if (this.ambiente[linha][entrada] == 'H' | this.ambiente[linha][entrada] == '.') {
                    alvo[linha][entrada] = EstadoSimulacao.LIVRE;
                }
            }
        }

        return alvo;

    }

    
    /** 
     * Retornar a cor correspondente ao nivel de perigo desta regiao
     * 
     * @param data A data atual
     * @param tempoLimites Tabela para calcular o nivel de perigo
     * @requires data != null && tempoLimites != null
     * @return Enumerado NivelPerigo representando o nivel de perigo desta regiao
     */
    public NivelPerigo nivelPerigo(Calendar data, int[] tempoLimites) {
        // retorna o nível de perigo, ver forma de cálculo no início da pag.4 do enunciado
        int diferencaAnos = data.get(Calendar.YEAR) - this.ultFogo.get(Calendar.YEAR);
        NivelPerigo nivelPerigoCor;

        double nivelPerigo = calculaNivelPerigo(tempoLimites, diferencaAnos);

        if (Math.round(nivelPerigo) >= NivelPerigo.values().length) {
            nivelPerigoCor = NivelPerigo.values()[NivelPerigo.values().length - 1]; 
        } else {
            nivelPerigoCor = NivelPerigo.values()[(int) Math.round(nivelPerigo)];
        }

        return nivelPerigoCor;

    }

    
    /** 
     * Retornar uma representacao legivel e bem estruturada da regiao, completa
     * com informacao sobre a mesma.
     * 
     * @return String que representa a regiao, incluindo informacao sobre a mesma.
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Nome: " + this.nome() + "  Data ult. fogo: " + this.ultFogo.get(Calendar.YEAR) + "/"
                               + this.ultFogo.get(Calendar.MONTH) + "/"
                               + this.ultFogo.get(Calendar.DAY_OF_MONTH) + "\n");
        for (char[] linha : this.ambiente){
            for(char coluna : linha){
                output.append(coluna);
            }  
            output.append("\n");
        }
        return output.toString();
    }


    // METODOS ADICIONAIS (APENAS PARA USO INTERNO)


    /**
     * Calcular o indice de perigo desta regiao na tabela de limites
     *  
     * @param perigoLimites Tabela para calcular o limite de perigo desta regiao
     * @param diferencaAnos Diferenca de anos entre a data atual e a data do ultimo fogo 
     * @requires perigoLimites != null && diferencaAnos >= 0 
     * @return int representando o indice de perigo desta regiao na tabela de limites
     */
    private static int calculaIndiceLimitePerigo(int[] perigoLimites, int diferencaAnos){

        Arrays.sort(perigoLimites);
        int indicePerigo = 0;
        for(int indice = 0; indice < perigoLimites.length; indice++) {
            if (diferencaAnos <= perigoLimites[indice]) {
                return indice;
            }
        }
        return indicePerigo;
    }
   

    /** 
     * Calcular o nivel de perigo desta regiao com precisao
     * 
     * @param perigoLimites Tabela para calcular o nivel de perigo desta regiao
     * @param diferencaAnos Diferenca de anos entre a data atual e a data do ultimo fogo
     * @requires perigoLimites != null && diferencaAnos >= 0
     * @return double representando o nivel de perigo desta regiao com precisao
     */
    private double calculaNivelPerigo(int[] perigoLimites, int diferencaAnos) {

        double nivelPerigo = (double) calculaIndiceLimitePerigo(perigoLimites, diferencaAnos);
        int ardiveis = this.ardiveis();
        int totalDeElementos = this.ambiente.length * this.ambiente[0].length;
        int obstaculos = totalDeElementos - ardiveis;

        double racio = ((double)(ardiveis - obstaculos) / totalDeElementos);

        nivelPerigo *= 1 + racio;

        return nivelPerigo;
    }
    
}
