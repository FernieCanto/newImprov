package improviso;

/**
 *
 * @author fernando
 */
public class GrupoSequencia extends GrupoRepeticao {
    protected int indiceAtual = 0;
    
    GrupoSequencia() {
        super();
    }
    
    @Override
    public boolean selecionaProximoGrupo() {
        grupoSelecionado = filhos.get(indiceAtual);
        indiceGrupoSelecionado = indiceAtual;

        indiceAtual++;
        if(indiceAtual == filhos.size())
            indiceAtual = 0;
        return true;
    }
}
