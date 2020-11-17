import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        this.regioes = new ArrayList<>();
        this.designacao = designacao;
    }

    
    /** Adiciona uma regiao a  lista de regioes.
     * @param nome - O nome da região a adicionar.
     * @param ultFogo - Data do último fogo a afetar a região.
     * @param largura - Largura do ambiente da região.
     * @param altura - Altura do ambiente da região.
     * @param casas - Lista de posições de casas no ambiente.
     * @param estradas - Lista de posições de estradas no ambiente.
     * @param agua - Lista de posições de água no ambiente.
     * @requires - nome != null && ultFogo != null && largura != null
     * casas, estradas, agua are List<Par<Integer, Integer>>.
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

    
    /** Verifica se a regiao referida esta registada na instituiçao.
     * @param nome - O nome da região a verificar se existe.
     * @return - Valor booleano referente à existência da região na instituição.
     */
    public boolean existeRegiao(String nome) {

        for(Regiao regiao : this.regioes){
            if (nome.equals(regiao.nome())){
                return true;
            }
        }
        
        return false;
    }

    
    /** Constroi e devolve uma lista com informaçao sobre os niveis de perigo de cada regiao na instituiçao.
     * @return - List<Par<String, NivelPerigo>> com objetos Par que incluem o nome das regoes e o respetivo nivel de perigo, 
     * calculado relativamente ah data corrente e ah constante RISCO_ANOS.
     */ 
    public List<Par<String,NivelPerigo>> niveisDePerigo() {

        List<Par<String,NivelPerigo>> output = new ArrayList<>();


        // Para cada regiao registada, adicionar um Par<nome, nivelDePerigo> ao output
        for (Regiao r: regioes) {
            Par<String,NivelPerigo> elemento = new Par<>(r.nome(), r.nivelPerigo(Calendar.getInstance(), RISCO_ANOS));
            output.add(elemento);
        }

        return output;

    }

    
    /** Array bidimensional cujos elementos são representados por elementos do enumerado EstadoSimulacao.
     * @return - Array bidimensional EstadoSimulacao[][] correspondente ao ambiente da regiao com maior nivel de perigo,
     * em que os terrenos e casas nao ardidos sao representados por EstadoSimuacao.LIVRE, e a agua, estradas
     * e elementos ja ardidos sao representados por EstadoSimulacao.OBSTACULO.
     */
    public EstadoSimulacao[][] alvoSimulacao() {
        
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

    
    /** Devolve valor booleano que simboliza se e possivel ou nao atuar sobre alguma das regioes.
     * @return - Valor booleano, true se pelomenos para uma regiao regiao.ardiveis()>0, caso contrario
     * false. 
     */
    public boolean podeAtuar() {

        for (Regiao regiao: this.regioes) {
            if (regiao.ardiveis() > 0) {
                return true;
            }
        }
        
        return false;
    }

    
    /** Regista um fogo na região especificada, com a data referida, afetando os sitios referidos.
     * @param regiao - O nome da região cujo fogo irá afetar.
     * @param data - A data em que o fogo aconteceu.
     * @param sitios - Uma lista com as posições referentes aos sítios ardidos.
     * @requires regiao != null && data != null && sitios != null && data is Calendar && 
     * sitios is List<Par<Integer, Integer>> && regiao is String
     */
    public void registaFogo(String regiao, Calendar data, List<Par<Integer, Integer>> sitios) {
        
        for (Regiao r: this.regioes) {
            if (r.nome().equals(regiao)) {
                r.registaFogo(data, sitios);
            } 
        }
    }
    
    
    /** 
     * Constroi uma representação legível e bem estruturada da instituição, completa
     * com informação sobre esta e todas as suas regiões, devolvendo-a.
     * @return String que representa a instituição, incluindo informação sobre si 
     * própria e todas as regiões registadas.
     */
    public String toString() {

        StringBuilder output = new StringBuilder();

        output.append(imprimirAsteriscos(27));
        output.append("Designacao: ").append(this.designacao).append("\n");
        output.append("Regiao maior perigo: ").append(indiceRegiaoMaiorPerigo()).append("\n");
        output.append("-------- REGIOES -------\n");

        // Calcula nivelPerigo para cada regiao
        for(Regiao regiao : this.regioes){
    
            output.append("Nivel perigo de fogo: ")
                    .append(regiao.nivelPerigo(Calendar.getInstance(), RISCO_ANOS))
                    .append("\n");
            output.append(regiao.toString());
            output.append(imprimirTracos(20));

        }
        output.append(imprimirAsteriscos(27));

        return output.toString();
}

    // MÉTODOS ADICIONAIS (APENAS PARA USO INTERNO)
    
    /** Constroi e devolve barra de asteriscos
     * @param number - Quantidade de asteriscos a imprimir
     * @return String, com number asteriscos seguidos e um newline no fim.
     */
    private String imprimirAsteriscos(int number){

        StringBuilder output = new StringBuilder();

        for(int i = 0; i < number; i++){
            output.append("*");
        }

        output.append("\n");

        return output.toString();
    }

    
    /** Constroi e devolve barra de tracos
     * @param number - Quantidade de asteriscos a imprimir
     * @return String, com number traços seguidos e um newline no fim.
     */
    private String imprimirTracos(int number){

        StringBuilder output = new StringBuilder();

        for(int i = 0; i < number; i++){
            output.append("-");
        }

        output.append("\n");

        return output.toString();
    }


    /** Constroi e devolve objeto Par que inclui informação sobre a região com maior
     * nível de perigo, incluido o seu nome e o seu nível de perigo.
     * @return - Par<String, NivelPerigo> em que String é o nome da região e NivelPerigo
     * é o seu valor de perigo conforme o enumerado NivelPerigo.
     */
    private Par<String,NivelPerigo> maiorPerigo() {

        List<Par<String,NivelPerigo>> niveisDePerigo = niveisDePerigo();
        Par<String,NivelPerigo> maisPerigosa = niveisDePerigo.get(0);

        for (Par<String,NivelPerigo> r: niveisDePerigo) {
            if (r.segundo().ordinal() > maisPerigosa.segundo().ordinal()) {
                maisPerigosa = r;
            } 
        }

        return maisPerigosa;
    }

    /** Devolve indice da região mais perigosa na lista de regioes this.regioes.
     * @return - Inteiro que representa o índice da região de maior perigo, 
     * em this.regioes (lista de todas as regioes na instituição).
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
