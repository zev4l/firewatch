import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES
//TODO: COMPLETAR TODOS OS REQUIRES

/**
 * @author Grupo 77 - Augusto Gouveia (55371), José Almeida (55373)
 * @date Novembro 2020
 * 
 * Esta classe contém os métodos requiridos para criar regiões e 
 * gerir a sua existência englobada na instituição.
 * Completa com os métodos necessários para monitorizar o estado
 * das regiões adicionadas a qualquer altura, e afetar as mesmas.
 */
public class Instituicao {

    // Relação entre o número de anos que passaram desde o último fogo e o nível de perigo 
    public static final int[] RISCO_ANOS = {2, 3, 5, 8}; 

    // Relação entre a força do vento e o número de elementos vizinhos no ambiente de uma
    // região que serão afetados
    public static final int[] VENTOS_LIMITES = {0, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21};

    public ArrayList<Regiao> regioes;
    private String designacao;


    public Instituicao(String designacao) {
        this.regioes = new ArrayList<Regiao>();
        this.designacao = designacao;
    }

    
    /** 
     * @param nome - O nome da região a adicionar.
     * @param ultFogo - Data do último fogo a afetar a região.
     * @param largura - Largura do ambiente da região.
     * @param altura - Altura do ambiente da região.
     * @param casas - Lista de posições de casas no ambiente.
     * @param estradas - Lista de posições de estradas no ambiente.
     * @param agua - Lista de posições de água no ambiente.
     * @requires - 
     */
    public void adicionaRegiao(String nome, Calendar ultFogo, int largura, int altura,
                                List<Par<Integer, Integer>> casas,
                                List<Par<Integer, Integer>> estradas,
                                List<Par<Integer, Integer>> agua) {

        // Assumindo que ainda não existe uma região com este nome, 
        //e que os dados são válidos, cria-a com os parametros referidos.
        Regiao novaRegiao = new Regiao(nome, ultFogo, largura, altura, casas, estradas, agua);
        regioes.add(novaRegiao);
    }

    
    /** 
     * @param nome - 
     * @return boolean
     */
    public boolean existeRegiao(String nome) {
        // Devolve true se a região estiver registada nesta instituição
        for(Regiao regiao : this.regioes){
            if (nome.equals(regiao.nome())){
                return true;
            }
        }
        
        return false;
    }

    
    /** 
     * @return List<Par<String, NivelPerigo>>
     */
    public List<Par<String,NivelPerigo>> niveisDePerigo() {
        // Devolve os nomes e níveis de perigo de cara região registada,
        // os níveis devem ser calculados relativamente à data corrente
        // e À constante RISCO_ANOS

        List<Par<String,NivelPerigo>> output = new ArrayList<Par<String,NivelPerigo>>();

        for (Regiao r: regioes) {
            Par<String,NivelPerigo> elemento = new Par<String,NivelPerigo>(r.nome(), r.nivelPerigo(Calendar.getInstance(), RISCO_ANOS));
            output.add(elemento);
        }

        return output;

    }

    
    /** 
     * @return EstadoSimulacao[][]
     */
    public EstadoSimulacao[][] alvoSimulacao() {
        // Devolve alvo da simulação da região de maior nível de perigo
        


        Regiao regiaoMaisPerigosa = this.regioes.get(0);

        
        for (Regiao regiao: this.regioes){

            int nivelPerigoRegiaoAtual = regiao.nivelPerigo(Calendar.getInstance(), RISCO_ANOS).ordinal();
            int nivelPerigoRegiaoMaisPerigosa = regiaoMaisPerigosa.nivelPerigo(Calendar.getInstance(), RISCO_ANOS).ordinal();
        
            if(nivelPerigoRegiaoAtual > nivelPerigoRegiaoMaisPerigosa){
                regiaoMaisPerigosa = regiao;
            }
        }
        return regiaoMaisPerigosa.alvoSimulacao();
    }

    
    /** 
     * @return boolean
     */
    public boolean podeAtuar() {
        // Devolve true se existe pelo menos uma região com elementos ardíveis


        for (Regiao regiao: this.regioes) {
            if (regiao.ardiveis() > 0) {
                return true;
            }
        }
        
        return false;
    }

    
    /** 
     * @param sitios
     */
    public void registaFogo(String regiao, Calendar data, List<Par<Integer, Integer>> sitios) {
        // regista na região referida um fogo acontecido na data referida. 
        // ATENÇÃO: APÓS ISTO PODE ACONTECER QUE A REGIÃO DE MAIOR NIVEL DE PERIGO MUDE.
        
        for (Regiao r: this.regioes) {
            if (r.nome().equals(regiao)) {
                r.registaFogo(data, sitios);
            } 
        }
    }
    
    
    /** 
     * @return String
     */
    public String toString() {
        // Self-explanatory
        StringBuilder output = new StringBuilder();
        output.append(imprimirAsteriscos(27));
        output.append("Designacao: " + this.designacao + "\n");   
        output.append("Regiao maior perigo: ");
        output.append(indiceRegiaoMaiorPerigo() + "\n");
        output.append("-------- REGIOES -------\n");
        for(Regiao regiao : this.regioes){
            output.append("Nivel perigo de fogo: ");
            output.append(regiao.nivelPerigo(Calendar.getInstance(), RISCO_ANOS) + "\n");
            output.append(regiao.toString());
            output.append(imprimirTracos(20));

        }
        output.append(imprimirAsteriscos(27));

        return output.toString();
}

    
    /** 
     * @param number
     * @return String
     */
    // MÉTODOS ADICIONAIS (PARA USO INTERNO)

    private String imprimirAsteriscos(int number){
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < number; i++){
            output.append("*");
        }
        output.append("\n");
        return output.toString();
    }

    
    /** 
     * @param number
     * @return String
     */
    private String imprimirTracos(int number){
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < number; i++){
            output.append("-");
        }
        output.append("\n");
        return output.toString();
    }

    
    /** 
     * @return Par<String, NivelPerigo>
     */
    private Par<String,NivelPerigo> maiorPerigo() {
        List<Par<String,NivelPerigo>> teste = niveisDePerigo();
        Par<String,NivelPerigo> maisPerigosa = teste.get(0);

        for (Par<String,NivelPerigo> r: teste) {
            if (r.segundo().ordinal() > maisPerigosa.segundo().ordinal()) {
                maisPerigosa = r;
            } 
        }

        return maisPerigosa;
    }

    
    /** 
     * @return int
     */
    private int indiceRegiaoMaiorPerigo() {
        int indice = 0;
        for (Regiao regiao : this.regioes) {
            if (regiao.nome().equals(maiorPerigo().primeiro())) {
                indice = this.regioes.indexOf(regiao);
            }
        }
        return indice;
    }
}
