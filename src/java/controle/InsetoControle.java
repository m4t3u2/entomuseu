package controle;

import dao.EspecieDao;
import dao.FamiliaDao;
import dao.GeneroDao;
import dao.OrdemDao;
import java.util.ArrayList;
import java.util.List;
import modelo.Especie;
import modelo.Familia;
import modelo.Genero;
import modelo.Ordem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "insetoControle")
@ViewScoped
public class InsetoControle implements Serializable {

    private List<Ordem> ordens;
    private OrdemDao ordemDao;
    private Ordem ordemSelecionada;

    private List<Familia> familias;
    private FamiliaDao familiaDao;
    private Familia familiaSelecionada;

    private List<Genero> generos;
    private GeneroDao generoDao;
    private Genero generoSelecionado;

    private Especie novaEspecie;
    private EspecieDao especieDao;

    private UploadedFile file;

    private Integer ultimoInserido;

    public InsetoControle() {
        ordemDao = new OrdemDao();
        ordens = ordemDao.listarOrdem();
        ordemSelecionada = new Ordem();

        familiaDao = new FamiliaDao();
        familias = new ArrayList<>();
        familiaSelecionada = new Familia();

        generoDao = new GeneroDao();
        generos = new ArrayList<>();
        generoSelecionado = new Genero();

        novaEspecie = new Especie();
        especieDao = new EspecieDao();

        ultimoInserido = 0;
    }

    public void atualizarFamilias() {
        familias = familiaDao.listarPorOrdem(ordemSelecionada);
    }

    public void atualizarGeneros() {
        generos = generoDao.listarPorFamilia(familiaSelecionada);
    }

    public void cadastrar() {
        novaEspecie.setGenero(generoSelecionado);
        ultimoInserido = especieDao.inserir(novaEspecie);
        novaEspecie = new Especie();
        generoSelecionado = new Genero();
        familiaSelecionada = new Familia();
        ordemSelecionada = new Ordem();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inseto Cadastrado, agora envie as imagens.", null));
    }

    public void uploadArquivoHorizontal(FileUploadEvent event) {
        String fileName = event.getFile().getFileName();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String caminho = servletContext.getRealPath("") + File.separator + "resources"
                + File.separator + "pacotes" + File.separator;
        criarPasta(caminho, ultimoInserido, "horizontal");
        String newFileName = servletContext.getRealPath("") + File.separator + "resources"
                + File.separator + "pacotes"
                + File.separator + ultimoInserido
                + File.separator + "horizontal"
                + File.separator + "images"
                + File.separator + fileName;

        if (writeToFile(newFileName, event.getFile().getContents())) {
//            System.out.println("Sucesso no Upload.");
        }
    }

    public void uploadArquivoVertical(FileUploadEvent event) {
        String fileName = event.getFile().getFileName();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String caminho = servletContext.getRealPath("") + File.separator + "resources"
                + File.separator + "pacotes" + File.separator;
        criarPasta(caminho, ultimoInserido, "vertical");
        String newFileName = servletContext.getRealPath("") + File.separator + "resources"
                + File.separator + "pacotes"
                + File.separator + ultimoInserido
                + File.separator + "vertical"
                + File.separator + "images"
                + File.separator + fileName;

        if (writeToFile(newFileName, event.getFile().getContents())) {
//            System.out.println("Sucesso no Upload.");
        }
    }

    public void criarPasta(String caminho, Integer ultimoInserido, String sentido) {
        try {
            File diretorio = new File(caminho + File.separator + ultimoInserido);
            diretorio.mkdir();
                      
            diretorio = new File(caminho + File.separator + ultimoInserido + File.separator + sentido);
            diretorio.mkdir();
                        
            diretorio = new File(caminho + File.separator + ultimoInserido + File.separator + sentido 
                    + File.separator + "images");
            diretorio.mkdir();
                        
            copiarBase(ultimoInserido);
        } catch (Exception ex) {
            System.out.println("Falha ao criar pasta.");
            System.out.println("Erro: " + ex);
        }
    }

    public boolean writeToFile(String path, byte[] content) {
        boolean ok = false;
        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content);
            fos.flush();
            fos.close();
            ok = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public void copiarBase(Integer ultimoInserido) {
        try {
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String caminho = servletContext.getRealPath("") + File.separator + "resources" + File.separator;
            File origem = new File(caminho + "modelo" + File.separator + "jquery");
            File origem2 = new File(caminho + "modelo" + File.separator + "index");
            File destino2 = new File(caminho + "pacotes" + File.separator + ultimoInserido);
            File destinoH = new File(caminho + "pacotes" + File.separator + ultimoInserido + File.separator + "horizontal");
            File destinoV = new File(caminho + "pacotes" + File.separator + ultimoInserido + File.separator + "vertical");
            FileUtils.copyDirectory(origem, destinoH);
            FileUtils.copyDirectory(origem, destinoV);
            FileUtils.copyDirectory(origem2, destino2);
            } catch (Exception e) {
            System.out.println("Falha ao mover base.");
            System.out.println("Erro: " + e);
        }
    }

    public List<Ordem> getOrdens() {
        return ordens;
    }

    public void setOrdens(List<Ordem> ordens) {
        this.ordens = ordens;
    }

    public OrdemDao getOrdemDao() {
        return ordemDao;
    }

    public void setOrdemDao(OrdemDao ordemDao) {
        this.ordemDao = ordemDao;
    }

    public Ordem getOrdemSelecionada() {
        return ordemSelecionada;
    }

    public void setOrdemSelecionada(Ordem ordemSelecionada) {
        this.ordemSelecionada = ordemSelecionada;
    }

    public List<Familia> getFamilias() {
        return familias;
    }

    public void setFamilias(List<Familia> familias) {
        this.familias = familias;
    }

    public FamiliaDao getFamiliaDao() {
        return familiaDao;
    }

    public void setFamiliaDao(FamiliaDao familiaDao) {
        this.familiaDao = familiaDao;
    }

    public Familia getFamiliaSelecionada() {
        return familiaSelecionada;
    }

    public void setFamiliaSelecionada(Familia familiaSelecionada) {
        this.familiaSelecionada = familiaSelecionada;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public GeneroDao getGeneroDao() {
        return generoDao;
    }

    public void setGeneroDao(GeneroDao generoDao) {
        this.generoDao = generoDao;
    }

    public Genero getGeneroSelecionado() {
        return generoSelecionado;
    }

    public void setGeneroSelecionado(Genero generoSelecionado) {
        this.generoSelecionado = generoSelecionado;
    }

    public Especie getNovaEspecie() {
        return novaEspecie;
    }

    public void setNovaEspecie(Especie novaEspecie) {
        this.novaEspecie = novaEspecie;
    }

    public EspecieDao getEspecieDao() {
        return especieDao;
    }

    public void setEspecieDao(EspecieDao especieDao) {
        this.especieDao = especieDao;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Integer getUltimoInserido() {
        return ultimoInserido;
    }

    public void setUltimoInserido(Integer ultimoInserido) {
        this.ultimoInserido = ultimoInserido;
    }

}
